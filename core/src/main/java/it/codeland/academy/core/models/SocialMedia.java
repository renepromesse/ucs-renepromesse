package it.codeland.academy.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

@Model(adaptables = Resource.class)
public class SocialMedia {

    private static final Logger LOG = LoggerFactory.getLogger(SocialMedia.class);

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @SlingObject
    Resource resource;
    @SlingObject
    private Resource currentResource;
    @SlingObject
    private ResourceResolver resourceResolver;

    @PostConstruct
    protected void init() {

        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        String currentPagePath = Optional.ofNullable(pageManager)
                .map(pm -> pm.getContainingPage(currentResource))
                .map(Page::getPath).orElse("");
    }

    public List<Map<String, String>> getSocialMediaIcons() {
        List<Map<String, String>> listMapNav = new ArrayList<>();
        try {
            Resource navIcons = resource.getChild("navIcons");
            if (navIcons != null) {
                for (Resource navItem : navIcons.getChildren()) {
                    Map<String, String> mapNavItem = new HashMap<>();
                  
                    mapNavItem.put("socialIcon", navItem.getValueMap().get("socialIcon", String.class));
                    mapNavItem.put("socialLink", navItem.getValueMap().get("socialLink", String.class));
                    mapNavItem.put("socialTarget", navItem.getValueMap().get("socialTarget", String.class));
                    listMapNav.add(mapNavItem);
                }
            }
        } catch (Exception e) {

            LOG.info("\n Error from social media Items {} ", e.getMessage());
        }
        return listMapNav;
    }

}