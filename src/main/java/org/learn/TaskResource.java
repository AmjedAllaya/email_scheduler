package org.learn;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/tasks")
public class TaskResource {
    @GET
    public List<Task> listAll() {
        return Task.listAll();
    }
}
