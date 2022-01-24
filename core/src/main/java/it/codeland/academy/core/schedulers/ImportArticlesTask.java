package it.codeland.academy.core.schedulers;



import it.codeland.academy.core.configs.ArticlesConfig;
import it.codeland.academy.core.helpers.ResourceHelper;
import it.codeland.academy.core.services.PageService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.Job;
import org.apache.sling.commons.scheduler.JobContext;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(immediate = true, service = Job.class)
@Designate(ocd = ArticlesConfig.class)
public class ImportArticlesTask implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(ImportArticlesTask.class);
    private ArticlesConfig config;
    
    private int schedulerJobId;

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resourceFactory;

    @Activate
    protected void activate(ArticlesConfig config) {
        this.config = config;
        schedulerJobId = "import articles".hashCode();
        addScheduler(config);
    }

    @Deactivate
    protected void deactivate(ArticlesConfig config) {
        removeSchedulerJob();
    }

    private void removeSchedulerJob() {
        scheduler.unschedule(String.valueOf(schedulerJobId));
    }

    private void addScheduler(ArticlesConfig config) {
        ScheduleOptions in = scheduler.EXPR(config.Expression());
        in.name(String.valueOf(schedulerJobId));
        scheduler.schedule(this, in);
    }

    @Override
    public void execute(JobContext context) {
        LOG.debug("\n\n\n\t\t\t ***** IMPORTING (job running) ****");
        try {
            ResourceResolver resolver = ResourceHelper.getResourceResolver(resourceFactory, "write-user");
            PageService ps = new PageService();
            ps.CreatePage(this.config.FilePath(), resolver);

        } catch (Exception e) {
            LOG.debug("\n\n\n Error: " +ExceptionUtils.getStackTrace(e));
        }
    }
}