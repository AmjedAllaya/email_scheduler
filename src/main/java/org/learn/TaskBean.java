package org.learn;


import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.quartz.*;

import java.util.Date;

@ApplicationScoped
public class TaskBean {
    @Inject
    org.quartz.Scheduler quartz;

    public void scheduleJob(Date startDate) throws SchedulerException {
            String jobName = "myJob";
            String jobGroup = "myGroup";
            String triggerName = "myTrigger";
            String triggerGroup = "myGroup";

            JobKey jobKey = new JobKey(jobName, jobGroup);

            // Check if the job already exists
            if (quartz.checkExists(jobKey)) {
                System.out.println("Job already exists. Updating the trigger.");
                TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
                quartz.unscheduleJob(triggerKey);
            }
            else {
                JobDetail job = JobBuilder.newJob(MyJob.class)
                        .withIdentity(jobKey)
                        .build();

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerName, triggerGroup)
                        .startAt(startDate)
                        .withSchedule(
                                SimpleScheduleBuilder.simpleSchedule()
                                        .withIntervalInSeconds(10)
                                        .repeatForever())
                        .build();

                quartz.scheduleJob(job, trigger);
                System.out.println("Job scheduled successfully to start at: " + startDate);
            }
    }

    @Transactional
    void performTask() {
        System.out.println("Task executed at " + new Date());
        Task task = new Task();
        task.persist();
    }

    public static class MyJob implements Job {

        @Inject
        TaskBean taskBean;

        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Executing job at " + new Date());
            taskBean.performTask();
        }
    }
}

