package app.qontakt.host.quartz;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class ScheduledCheckout implements Job {

    static JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(ScheduledCheckout.class)
                .storeDurably()
                .withIdentity("Quartz_Checkout")
                .withDescription("Invoke Checkout Task")
                .build();
    }

    static Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity("Quartz_Checkout_Trigger")
                .withDescription("Trigger Checkout Task")
                .withSchedule(simpleSchedule().repeatForever().withIntervalInMinutes(1))
                .build();
    }

    private final ScheduledExecutionService scheduledExecutionService;

    public ScheduledCheckout(ScheduledExecutionService scheduledExecutionService) {
        this.scheduledExecutionService = scheduledExecutionService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        this.scheduledExecutionService.checkout();
    }
}
