package fr.paris.lutece.plugins.taskstack.rs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseDto;
import fr.paris.lutece.plugins.taskstack.dto.TaskRightDto;

import java.util.List;

public class TaskRightSearchResponse extends ResponseDto
{
    @JsonProperty("task_right_list")
    private List<TaskRightDto> taskRightDtoList;

    public List<TaskRightDto> getTaskRightDtoList()
    {
        return taskRightDtoList;
    }

    public void setTaskRightDtoList(List<TaskRightDto> taskRightDtoList)
    {
        this.taskRightDtoList = taskRightDtoList;
    }
}
