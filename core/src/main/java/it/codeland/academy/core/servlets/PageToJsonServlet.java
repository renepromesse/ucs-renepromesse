package it.codeland.academy.core.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = { Servlet.class })
@SlingServletResourceTypes(
        resourceTypes="cq:Page",
        methods=HttpConstants.METHOD_GET,
        selectors="export",
        extensions="json")
@ServiceDescription("Export page properties")
public class PageToJsonServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
        final SlingHttpServletResponse resp) throws ServletException, IOException {
        final Resource resource = req.getResource();
        JsonObject simpleObj = new JsonObject();
        
        Resource jcrContent = resource.getChild("jcr:content");

        // convert this value resource jcr:content into a valueMap
        ValueMap thisArticle = jcrContent.adaptTo(ValueMap.class);

        // get properties
        String image = thisArticle.get("image", String.class);
        String text = thisArticle.get("text", String.class);
        String title = thisArticle.get("jcr:title", String.class);
        String tags = thisArticle.get("tags", String.class);
        String date = thisArticle.get("date", String.class);
        
        //simpleObj.addProperty("title", resource.getName());
        simpleObj.addProperty("title", title);
        simpleObj.addProperty("abstract", text);
        simpleObj.addProperty("image", image);
        simpleObj.addProperty("link", resource.getPath().toString() + ".html");
        simpleObj.addProperty("tags", tags);
        simpleObj.addProperty("date", date);

        /**
         * {
            "title": "My article title",
            "abstract": "This is a long article abstract...."
            "image": "/content/dam/whatever.jpg"
            "link": "/content/site/articles/article-1.html"
            "tags": ["tag-1","tag-2"]
            }
        */

        String output = new String();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        output = gson.toJson(simpleObj);
        resp.setContentType("application/json");
        resp.getWriter().write(output);
    }
}
