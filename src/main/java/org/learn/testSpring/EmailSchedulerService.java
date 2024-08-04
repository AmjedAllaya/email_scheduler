package org.learn.testSpring;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.quartz.*;

import java.time.ZoneId;
import java.util.Date;

@ApplicationScoped
public class EmailSchedulerService {

    @Inject
    Scheduler quartz;

    @Inject
    Mailer mailer;

    public void scheduleEmail(Email email) throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("to", email.getTo());
        jobDataMap.put("subject", email.getSubject());
        jobDataMap.put("body", email.getBody());

        JobDetail job = JobBuilder.newJob(EmailJob.class)
                .withIdentity(email.getTo() + "-" + System.currentTimeMillis())
                .usingJobData(jobDataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(email.getTo() + "-trigger-" + System.currentTimeMillis())
                .startAt(Date.from(email.getScheduledTime().atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        quartz.scheduleJob(job, trigger);
    }

    @ApplicationScoped
    public static class EmailJob implements Job {
        @Inject
        Mailer mailer;

        public void execute(JobExecutionContext context) {
            JobDataMap data = context.getMergedJobDataMap();
            String to = data.getString("to");
            String subject = data.getString("subject");
            String body = data.getString("body");

            mailer.send(Mail.withText(to, subject, body));
        }
    }

}
