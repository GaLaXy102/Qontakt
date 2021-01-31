package app.qontakt.host.quartz;

import app.qontakt.host.lokal.LokalService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduledExecutionService {

    private final LokalService lokalService;

    public ScheduledExecutionService(LokalService lokalService, Scheduler scheduler) throws SchedulerException {
        this.lokalService = lokalService;
        scheduler.scheduleJob(ScheduledCheckout.jobDetail(), ScheduledCheckout.trigger(ScheduledCheckout.jobDetail()));
        scheduler.scheduleJob(ScheduledDelete.jobDetail(), ScheduledDelete.trigger(ScheduledDelete.jobDetail()));
    }

    public void delete() {
        LoggerFactory.getLogger(ScheduledExecutionService.class).info("Checking for Visits to delete.");
        this.lokalService.performDeleteVisitsByPolicy();
    }

    public void checkout() {
        LoggerFactory.getLogger(ScheduledExecutionService.class).info("Checking for Visits to checkout.");
        this.lokalService.performCheckoutVisitsByPolicy();
    }

}
