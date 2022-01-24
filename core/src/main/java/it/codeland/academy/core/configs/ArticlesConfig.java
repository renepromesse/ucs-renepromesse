package it.codeland.academy.core.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Articles import Configuration", description = "Articles creation Configuration")
public @interface ArticlesConfig {

    @AttributeDefinition(name = "Cron expression", description = "job cron expression", type = AttributeType.STRING)
    public String Expression() default "*/5 * * * * ?";

    @AttributeDefinition(name = "CSV path", description = "add csv path file", type = AttributeType.STRING)
    String FilePath() default "/content/dam/ucs-exercise-renepromesse/articles.csv";

}
