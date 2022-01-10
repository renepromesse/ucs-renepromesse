package it.codeland.academy.core.models;


import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SliderModel {
    private final List<Resource> slides = new ArrayList<>();
    private final List<Resource> orderedSlides = new ArrayList<>();
    private Iterator<Resource> currentData;

    @SlingObject
    private Resource currentResource;

    @Inject
    private String[] slideOrder;


    @PostConstruct
    protected void init() throws PersistenceException {
       
        Iterator<Resource> children = currentResource.listChildren();
        currentData = children;
        while (children.hasNext()) {
            Resource child = children.next();
            slides.add(child);
        }
        if (slideOrder != null) {
            for (String slide : slideOrder) {
                for (Resource resource : slides) {
                    if (resource.getName().equals(slide)) {
                        orderedSlides.add(resource);
                    }
                }
            }
        } else {
            orderedSlides.addAll(slides);
        }
        
       
    }
    public List<Resource> getSlidesOrder(){
        return orderedSlides;
    }
    public Iterator<Resource> getCurrentData(){
        return currentData;
    }
}
