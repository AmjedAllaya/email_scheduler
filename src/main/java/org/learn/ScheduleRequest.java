package org.learn;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Schedule request object containing job scheduling details")
public class ScheduleRequest {
    @Schema(description = "The start date and time for the job in format dd-MM-yyyy HH:mm:ss", example = "25-12-2024 10:30:00")
    public String startDate;
}
