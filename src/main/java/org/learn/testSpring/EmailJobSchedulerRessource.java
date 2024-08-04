package org.learn.testSpring;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.quartz.*;


@Path("/api/schedule")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmailJobSchedulerRessource {
    @Inject
    EmailSchedulerService schedulerService;
    @POST
    @Path("/email")
    public Response scheduleEmail(Email email) {
        try {
            schedulerService.scheduleEmail(email);
            return Response.ok("Email scheduled successfully").build();
        } catch (SchedulerException e) {
            return Response.serverError().entity("Failed to schedule email: " + e.getMessage()).build();
        }
    }
}
