/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package it.codeland.academy.core.models;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import java.util.*;

@Model(adaptables = Resource.class)
public class Footer_section1 {

    @ValueMapValue(name=PROPERTY_RESOURCE_TYPE, injectionStrategy=InjectionStrategy.OPTIONAL)
    @Default(values="No resourceType")
    protected String resourceType;

    @SlingObject
    private Resource currentResource;

    public List<Map<String, String>> getLinks() {
        List<Map<String, String>> menusList = new ArrayList<>();
        try {
            Resource menus = currentResource.getChild("footer_section_nav");
            if (menus == null) {
                return Collections.emptyList();
            }
            if (menus != null) {
                for (Resource item : menus.getChildren()) {
                    Map<String, String> menuMap = new HashMap<String, String>();
                    menuMap.put("title", item.getValueMap().get("title", String.class));
                    menuMap.put("url", item.getValueMap().get("url", String.class));
                    menuMap.put("type", item.getValueMap().get("type", String.class));
                    menusList.add(menuMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menusList;
    }

    

}
