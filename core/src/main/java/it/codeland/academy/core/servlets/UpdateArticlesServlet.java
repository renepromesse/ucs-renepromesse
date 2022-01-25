package it.codeland.academy.core.servlets;


import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import it.codeland.academy.core.helpers.ResourceHelper;
import it.codeland.academy.core.services.PageService;

import javax.jcr.Node;
import javax.servlet.Servlet;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = { Servlet.class })
@SlingServletPaths(value = { "/bin/articles" })
@ServiceDescription("create articles Servlet")
public class UpdateArticlesServlet extends SlingAllMethodsServlet {

    private final Logger LOG = LoggerFactory.getLogger(UpdateArticlesServlet.class);
    private static final long serialVersionUID = 1L;

    PageManager pageManager;

    @Reference
    protected ResourceResolverFactory resolverFactory;

    public class ImportInfo {
        String total;
        String skipped;
        String created;
        String failedArticle;
        Boolean fileStatus;
        Boolean noChange;
    }

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) {
        try {
            JsonObject simpleObj = new JsonObject();
            simpleObj.addProperty("title", "title");
            simpleObj.addProperty("abstract", "text");
            simpleObj.addProperty("image", "image");

            
            ResourceResolver resolver = ResourceHelper.getResourceResolver(resolverFactory, "write-user");
            PageService ps = new PageService();


            ps.CreatePage("/content/dam/ucs-exercise-renepromesse/articles.csv", resolver);
            
            pageManager = resolver.adaptTo(PageManager.class);
            Page magazine = pageManager.getPage("/content/ucs-renepromesse/magazine/");
           
            Resource magazineContent = magazine.getContentResource();
            Node magazineNode = magazineContent.adaptTo(Node.class);

            // ImportInfo imp = new ImportInfo();
            // imp.total = magazineNode.getProperty("totalArticles").getValue().toString();
            // imp.skipped = magazineNode.getProperty("skippedArticle").getValue().toString();
            // imp.created = magazineNode.getProperty("createdArticle").getValue().toString();
            // imp.failedArticle = magazineNode.getProperty("failedArticle").getValue().toString();
            // imp.fileStatus = magazineNode.getProperty("isCsvFileValid").getBoolean();
            // imp.noChange = magazineNode.getProperty("noChange").getBoolean();

            // String json = new Gson().toJson(imp);

            // resp.setCharacterEncoding("UTF-8");
            // resp.setContentType("application/json");
            // resp.getWriter().write(json);
            
            String output = new String();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            output = gson.toJson(simpleObj);
            resp.setContentType("application/json");
            resp.getWriter().write(output);
        } catch (Exception e) {
            LOG.debug("\n\n\nError: " + ExceptionUtils.getStackTrace(e));
        }
    }

    /*@Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) {
        try {
            ResourceResolver resolver = ResourceHelper.getResourceResolver(resolverFactory, "sys-user");

            pageManager = resolver.adaptTo(PageManager.class);
            Page magazine = pageManager.getPage("/content/ucs-renepromesse/magazine/");
            Resource magazineContent = magazine.getContentResource();
            Node magazineNode = magazineContent.adaptTo(Node.class);

            ImportInfo imp = new ImportInfo();
            imp.total = magazineNode.getProperty("totalArticles").getValue().toString();
            imp.skipped = magazineNode.getProperty("skippedArticle").getValue().toString();
            imp.created = magazineNode.getProperty("createdArticle").getValue().toString();
            imp.failedArticle = magazineNode.getProperty("failedArticle").getValue().toString();
            imp.fileStatus = magazineNode.getProperty("isCsvFileValid").getValue().getBoolean();
            imp.noChange = magazineNode.getProperty("noChange").getBoolean();

            String json = new Gson().toJson(imp);

            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        } catch (Exception e) {
            LOG.debug("\n\n\n Error: " + ExceptionUtils.getStackTrace(e));
        }
    }*/

}
