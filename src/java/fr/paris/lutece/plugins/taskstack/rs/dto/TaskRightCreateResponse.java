package fr.paris.lutece.plugins.taskstack.rs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.paris.lutece.plugins.taskstack.dto.TaskRightDto;
import fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseDto;

import java.util.List;

public class TaskRightCreateResponse extends ResponseDto
{

    @JsonProperty( "task_status" )
    private String taskStatus;

    public String getTaskStatus( )
    {
        return taskStatus;
    }

    public void setTaskStatus( final String taskStatus )
    {
        this.taskStatus = taskStatus;
    }

}
