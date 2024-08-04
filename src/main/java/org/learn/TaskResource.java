package org.learn;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
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
    @Operation(summary = "Schedule a new job", description = "Schedules a job to be executed at a specific time.")
    @RequestBody(
            description = "Schedule request with the start date",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ScheduleRequest.class),
                    examples = @ExampleObject(
                            name = "Example request",
                            value = "{\"startDate\": \"25-12-2024 10:30:00\"}"
                    )
            )
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Job scheduled successfully"),
            @APIResponse(responseCode = "400", description = "Invalid date format. Please use dd-MM-yyyy HH:mm:ss"),
            @APIResponse(responseCode = "500", description = "Failed to schedule job")
    })
    public Response scheduleJob(@RequestBody ScheduleRequest request) {
        try {
            // Parse the start date from the request
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
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
