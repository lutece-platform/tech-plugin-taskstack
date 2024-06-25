/*
 * Copyright (c) 2002-2024, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.taskstack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDto
{
    /**
     * Code of the task, generated at creation time.
     */
    @JsonProperty( "task_code" )
    protected String taskCode;

    /**
     * ID of the resource associated with the task. As the tasks are generic, this ID can be whatever you want but must allow you find the resource in your
     * implementation.
     */
    @JsonProperty( "resource_id" )
    protected String resourceId;

    /**
     * Type of the resource. As the tasks are generic, this type can be whatever you want but must allow you find the resource in your implementation.
     */
    @JsonProperty( "resource_type" )
    protected String resourceType;

    /**
     * The type of the task. As the tasks are generic, this type can be whatever you want but must allow you to identify the task management in your
     * implementation.
     */
    @JsonProperty( "task_type" )
    protected String taskType;

    /**
     * Date of the creation of the Task
     */
    @JsonProperty( "creation_date" )
    protected Timestamp creationDate;

    /**
     * Date of the last update of the task
     */
    @JsonProperty( "last_update_date" )
    protected Timestamp lastUpdateDate;

    /**
     * Code of the last client that updated the task
     */
    @JsonProperty( "last_update_client_code" )
    protected String lastUpdateClientCode;

    /**
     * Status of the task
     */
    @JsonProperty( "task_status" )
    protected TaskStatusType taskStatus;

    @JsonProperty( "task_history" )
    protected List<TaskChangeDto> taskChanges = new ArrayList<>( );

    /**
     * Additional data that must be provided as key:value strings.
     */
    @JsonProperty( "metadata" )
    protected Map<String, String> metadata = new HashMap<>( );

    public String getTaskCode( )
    {
        return taskCode;
    }

    public void setTaskCode( String taskCode )
    {
        this.taskCode = taskCode;
    }

    public String getResourceId( )
    {
        return resourceId;
    }

    public void setResourceId( String resourceId )
    {
        this.resourceId = resourceId;
    }

    public String getResourceType( )
    {
        return resourceType;
    }

    public void setResourceType( String resourceType )
    {
        this.resourceType = resourceType;
    }

    public String getTaskType( )
    {
        return taskType;
    }

    public void setTaskType( String taskType )
    {
        this.taskType = taskType;
    }

    public Timestamp getCreationDate( )
    {
        return creationDate;
    }

    public void setCreationDate( Timestamp creationDate )
    {
        this.creationDate = creationDate;
    }

    public Timestamp getLastUpdateDate( )
    {
        return lastUpdateDate;
    }

    public void setLastUpdateDate( Timestamp lastUpdateDate )
    {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateClientCode( )
    {
        return lastUpdateClientCode;
    }

    public void setLastUpdateClientCode( String lastUpdateClientCode )
    {
        this.lastUpdateClientCode = lastUpdateClientCode;
    }

    public TaskStatusType getTaskStatus( )
    {
        return taskStatus;
    }

    public void setTaskStatus( TaskStatusType taskStatus )
    {
        this.taskStatus = taskStatus;
    }

    public List<TaskChangeDto> getTaskChanges( )
    {
        return taskChanges;
    }

    public void setTaskChanges( List<TaskChangeDto> taskChanges )
    {
        this.taskChanges = taskChanges;
    }

    public Map<String, String> getMetadata( )
    {
        return metadata;
    }

    public void setMetadata( Map<String, String> metadata )
    {
        this.metadata = metadata;
    }
}
