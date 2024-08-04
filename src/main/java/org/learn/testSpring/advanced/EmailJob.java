package org.learn.testSpring.advanced;


import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;


@ApplicationScoped
public class EmailJob implements Job {
    private static final Logger LOG = Logger.getLogger(EmailJob.class);

    @Inject
    Mailer mailer;

    @ConfigProperty(name = "quarkus.mailer.from")
    String fromEmail;

    public void execute(JobExecutionContext context) {
        LOG.info("Executing Job with key " + context.getJobDetail().getKey());

        var jobDataMap = context.getJobDetail().getJobDataMap();
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String recipientEmail = jobDataMap.getString("email");

        sendMail(recipientEmail, subject, body);
    }

    private void sendMail(String toEmail, String subject, String body) {
        try {
            LOG.info("Sending Email to " + toEmail);

            mailer.send(Mail.withHtml(toEmail, subject, body).setFrom(fromEmail));

            LOG.info("Email sent successfully to " + toEmail);
        } catch (Exception ex) {
            LOG.error("Failed to send email to " + toEmail, ex);
        }
    }
}
