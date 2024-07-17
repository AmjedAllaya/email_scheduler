package org.learn;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.quartz.SchedulerException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("/tasks")
public class TaskResource {
    @Inject
    TaskBean taskBean;

    @POST
    @Path("/schedule")
    public Response scheduleJob(@RequestBody ScheduleRequest request) {
        try {
            // Parse the start date from the request
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = dateFormat.parse(request.startDate);

            // Schedule the job
            taskBean.scheduleJob(startDate);

            return Response.ok("Job scheduled successfully").build();
        } catch (ParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date format. Please use yyyy-MM-dd HH:mm:ss")
                    .build();
        } catch (SchedulerException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to schedule job: " + e.getMessage())
                    .build();
        }
    }
    @GET
    public List<Task> listAll() {
        return Task.listAll();
    }
}
