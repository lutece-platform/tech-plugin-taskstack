package fr.paris.lutece.plugins.taskstack.rs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.paris.lutece.plugins.taskstack.dto.TaskRightDto;

public class TaskRightCreateRequest
{
    @JsonProperty( "task_right" )
    private TaskRightDto taskRight;

    public TaskRightDto getClientTask()
    {
        return taskRight;
    }

    public void setClientTask(TaskRightDto clientTask)
    {
        this.taskRight = clientTask;
    }
}
