package app.qontakt.host.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class ScheduledDelete implements Job {

    static JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(ScheduledDelete.class)
                .storeDurably()
                .withIdentity("Quartz_Delete")
                .withDescription("Invoke Delete Task")
                .build();
    }

    static Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity("Quartz_Delete_Trigger")
                .withDescription("Trigger Delete Task")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(4, 35)) // System Time, most likely UTC
                .build();
    }

    private final ScheduledExecutionService scheduledExecutionService;

    public ScheduledDelete(ScheduledExecutionService scheduledExecutionService) {
        this.scheduledExecutionService = scheduledExecutionService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        this.scheduledExecutionService.delete();
    }
}
