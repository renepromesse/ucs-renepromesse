package it.codeland.academy.core.services;


import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import it.codeland.academy.core.helpers.CsvHelper;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javax.jcr.Node;
import javax.jcr.Session;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.joda.time.DateTime;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PageService {
    private static final Logger LOG = LoggerFactory.getLogger(PageService.class);

    private Session session;
    PageManager pageManager;
    Page newArticlePage;

    int skippedArticle = 0;
    int newArticle = 0;
    int failedArticle = 0;
    int articleCount = 0;

    String pagePath = "/content/ucs-exercise-renepromesse/magazine";
    String templatePath = "/apps/ucs-exercise-renepromesse/templates/article-page";

    String pageTitle = "";
    String pageName = "";
    String image = "";
    String tags= "";
    String articleAbstract = "";

    public void CreatePage(String filePath, ResourceResolver resourceResolver) {
        try {
            DateTime now = new DateTime();
            String date = now.toString();
            session = resourceResolver.adaptTo(Session.class);
            pageManager = resourceResolver.adaptTo(PageManager.class);

            CsvHelper csvFile = new CsvHelper();
            Iterator<String[]> csvData = csvFile.readCsvFile(resourceResolver, filePath);

            if (csvData == null) {
                LOG.debug("\n\n ===>> File not found! \n\n");
                return;
            }
            Page articles = pageManager.getPage("/content/ucs-exercise-renepromesse/magazine/");
            if (articles == null) {
                LOG.debug("\n \n ===>> magazine root page not exist! \n \n");
                return;
            }

            Resource articlesContent = articles.getContentResource();
            Node articlesNode = articlesContent.adaptTo(Node.class);

            Resource csvFileRes = resourceResolver.getResource(filePath);
            Node csvNode = csvFileRes.adaptTo(Node.class);
            DateTime fileCreatedAt = new DateTime(csvNode.getProperty("jcr:created").getValue().toString());
            while (csvData.hasNext()) {
                TimeUnit.SECONDS.sleep(5);
                String[] row = csvData.next();
                if (row.length == 1) {
                    row = row[0].split(",");
                }

                //Saving imported info to the csv file for reference
                articlesNode.setProperty("totalArticles", row.length + 1);

                if (row.length >= 2) {
                    articlesNode.setProperty("isCsvFileValid", true);

                    pageTitle = row[0];
                    pageName = row[1];
                    image = row[2];
                    tags = row[3];
                    articleAbstract = row[4];

                    Page currPage = pageManager.getPage("/content/ucs-exercise-renepromesse/magazine/" + pageName + "/");
                    DateTime currPageCreatedAt = null;
                  
                    // Check if page exist and skip it
                    if (pageManager.getPage("/content/ucs-exercise-renepromesse/magazine/" + pageName + "/") != null) {
                        Resource currPageContent = currPage.getContentResource();
                        Node currPageNode = currPageContent.adaptTo(Node.class);
                        currPageCreatedAt = new DateTime(
                                currPageNode.getProperty("cq:lastModified").getValue().toString());

                        if (!currPage.equals(null) && fileCreatedAt.isBefore(currPageCreatedAt)) {
                            LOG.debug("\n\n ===>> File not changed yet\n \n");
                            articlesNode.setProperty("noChange", true);
                            break;
                        }
                        LOG.debug("\n\n ===>> Article page exist \n \n");
                        skippedArticle++;

                        articlesNode.setProperty("skippedArticle", skippedArticle);
                        continue;
                    }
                    // Create page and set properties in jcr:content node
                    newArticlePage = pageManager.create(pagePath, pageName, templatePath, pageTitle);
                    if (newArticlePage != null) {
                        Node articlePageNode = newArticlePage.adaptTo(Node.class);
                        Node articleJcr = articlePageNode.getNode("jcr:content");
                        articlesNode.setProperty("noChange", false);

                        if (articleJcr != null) {
                            articleJcr.setProperty("jcr:title", pageTitle);
                            articleJcr.setProperty("image", image);
                            articleJcr.setProperty("text", articleAbstract);
                            articleJcr.setProperty("tags", tags);
                            articleJcr.setProperty("date", date);
                        }
                        newArticle++;
                        articlesNode.setProperty("createdArticle", newArticle);   
                    }
                    if (newArticlePage == null){
                        failedArticle++;
                        articlesNode.setProperty("failedArticle", failedArticle);
                    }
                    articleCount++;
                }
                
                LOG.debug("\n \n ===>> Invalid file {}", row.length);
            }

            session.save();

            LOG.debug("\n ---------------------ARTICLES STATS----------------------------------- \n");
            LOG.debug("\n\n ===>>  processed : ",articleCount);
            LOG.debug("\n\n ===>>  created : ",newArticle);
            LOG.debug("\n\n ===>>  failed : ",failedArticle);
            LOG.debug("\n\n ===>>  non processed : ", skippedArticle);
            LOG.debug("\n ------------------------END OF THIS LOG--------------------------------------------------- \n");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("\n\n ===>> Error: {}\n\n", ExceptionUtils.getStackTrace(e));
        }
    }
}