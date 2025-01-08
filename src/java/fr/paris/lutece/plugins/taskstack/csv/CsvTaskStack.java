package fr.paris.lutece.plugins.taskstack.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;
import fr.paris.lutece.plugins.taskstack.util.Constants;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class CsvTaskStack
{
    @CsvBindByName( column = Constants.PARAMETER_CODE )
    @CsvBindByPosition( position = 0 )
    protected String taskCode;

    @CsvBindByName( column = Constants.PARAMETER_ID_RESOURCE )
    @CsvBindByPosition( position = 1 )
    protected String resourceId;

    @CsvBindByName( column = Constants.PARAMETER_TYPE_RESOURCE )
    @CsvBindByPosition( position = 2 )
    protected String resourceType;

    @CsvBindByName( column = Constants.PARAMETER_TYPE )
    @CsvBindByPosition( position = 3 )
    protected String taskType;

    @CsvBindByName( column = Constants.PARAMETER_CREATION_DATE )
    @CsvBindByPosition( position = 4 )
    protected Timestamp creationDate;

    @CsvBindByName( column = Constants.PARAMETER_LAST_UPDATE_DATE )
    @CsvBindByPosition( position = 5 )
    protected Timestamp lastUpdateDate;

    @CsvBindByName( column = Constants.PARAMETER_LAST_UPDATE_CLIENT_CODE )
    @CsvBindByPosition( position = 6 )
    protected String lastUpdateClientCode;

    @CsvBindByName( column = Constants.PARAMETER_STATUS )
    @CsvBindByPosition( position = 7 )
    protected TaskStatusType taskStatus;

    @CsvBindByName( column = Constants.PARAMETER_METADATA )
    @CsvBindByPosition( position = 8 )
    protected Map<String, String> metadata = new HashMap<>( );

    public String getTaskCode()
    {
        return taskCode;
    }

    public void setTaskCode(String taskCode)
    {
        this.taskCode = taskCode;
    }

    public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }

    public String getResourceType()
    {
        return resourceType;
    }

    public void setResourceType(String resourceType)
    {
        this.resourceType = resourceType;
    }

    public String getTaskType()
    {
        return taskType;
    }

    public void setTaskType(String taskType)
    {
        this.taskType = taskType;
    }

    public Timestamp getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate)
    {
        this.creationDate = creationDate;
    }

    public Timestamp getLastUpdateDate()
    {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate)
    {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateClientCode()
    {
        return lastUpdateClientCode;
    }

    public void setLastUpdateClientCode(String lastUpdateClientCode)
    {
        this.lastUpdateClientCode = lastUpdateClientCode;
    }

    public TaskStatusType getTaskStatus()
    {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatusType taskStatus)
    {
        this.taskStatus = taskStatus;
    }

    public Map<String, String> getMetadata()
    {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata)
    {
        this.metadata = metadata;
    }
}
