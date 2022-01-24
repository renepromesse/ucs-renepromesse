package it.codeland.academy.core.helpers;

import java.util.HashMap;
import java.util.Map;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

public class ResourceHelper {
    public static ResourceResolver getResourceResolver(ResourceResolverFactory resourceFactory, String sysuser) throws LoginException {
        return resourceFactory.getAdministrativeResourceResolver(null);
        // Map<String,Object> systemUser = new HashMap<String, Object>();
        // systemUser.put(ResourceResolverFactory.SUBSERVICE, sysuser);
        // return resourceFactory.getServiceResourceResolver(systemUser);
    }
}
