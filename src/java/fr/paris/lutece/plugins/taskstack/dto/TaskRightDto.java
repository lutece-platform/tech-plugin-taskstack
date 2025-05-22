package fr.paris.lutece.plugins.taskstack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskRightDto
{
    @JsonProperty( "authorized_client_code" )
    private String authorizedClientCode;

    @JsonProperty( "grantee_client_code" )
    private String granteeClientCode;

    @JsonProperty( "task_type" )
    private String taskType;

    public String getAuthorizedClientCode()
    {
        return authorizedClientCode;
    }

    public void setAuthorizedClientCode(String authorizedClientCode)
    {
        this.authorizedClientCode = authorizedClientCode;
    }

    public String getGranteeClientCode()
    {
        return granteeClientCode;
    }

    public void setGranteeClientCode(String granteeClientCode)
    {
        this.granteeClientCode = granteeClientCode;
    }

    public String getTaskType()
    {
        return taskType;
    }

    public void setTaskType(String taskType)
    {
        this.taskType = taskType;
    }
}
