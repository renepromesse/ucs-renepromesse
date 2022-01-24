package it.codeland.academy.core.helpers;


import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.text.csv.Csv;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CsvHelper {
    private static final Logger LOG = LoggerFactory.getLogger(CsvHelper.class);
    
    public Iterator<String[]> readCsvFile(ResourceResolver resolver, String filePath) { 
        try {
             Resource res = resolver.getResource(filePath);
             if(res == null) {
                 LOG.debug("\n ===>> File not found ");
                 return null;
             }
             
            Asset asset = res.adaptTo(Asset.class);
            Rendition assetRendition = asset.getOriginal();
            InputStream inputStream = assetRendition.getStream();
            
            Csv csv = new Csv();
            csv.setFieldDelimiter('\"');
            csv.setFieldSeparatorRead(',');
            Iterator<String[]> articlesIter = csv.read(inputStream, null);

            return articlesIter;
         } catch (Exception e) {
            LOG.error("\n ===>> Error: " +ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
            return null;
        }   
     }
}