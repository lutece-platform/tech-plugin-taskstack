package fr.paris.lutece.plugins.taskstack.rs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.paris.lutece.plugins.taskstack.dto.TaskRightDto;

public class TaskRightDeleteRequest
{
    @JsonProperty( "task_right" )
    private TaskRightDto taskRight;

    public TaskRightDto getTaskRight()
    {
        return taskRight;
    }

    public void setTaskRight(TaskRightDto _clientTask)
    {
        this.taskRight = _clientTask;
    }
}
