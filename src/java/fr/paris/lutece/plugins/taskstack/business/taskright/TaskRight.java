package fr.paris.lutece.plugins.taskstack.business.taskright;

public class TaskRight
{
    private String authorizedClientCode;
    private String granteeClientCode;
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
