package org.learn;


import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.quartz.*;

@ApplicationScoped
public class TaskBean {
    @Inject
    org.quartz.Scheduler quartz;

    public void onStart(@Observes StartupEvent event) throws SchedulerException {
        String jobName = "myJob";
        String jobGroup = "myGroup";
        String triggerName = "myTrigger";
        String triggerGroup = "myGroup";

        JobKey jobKey = new JobKey(jobName, jobGroup);

        // Check if the job already exists
        if (quartz.checkExists(jobKey)) {
            System.out.println("Job already exists. Skipping scheduling.");
        } else {
            JobDetail job = JobBuilder.newJob(MyJob.class)
                    .withIdentity(jobKey)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, triggerGroup)
                    .startNow()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(10)
                                    .repeatForever())
                    .build();

            quartz.scheduleJob(job, trigger);
            System.out.println("Job scheduled successfully.");
        }
    }

    @Transactional
    void performTask() {
        Task task = new Task();
        task.persist();
    }

    // A new instance of MyJob is created by Quartz for every job execution
    public static class MyJob implements Job {

        @Inject
        TaskBean taskBean;

        public void execute(JobExecutionContext context) throws JobExecutionException {
            taskBean.performTask();
        }

    }
}
