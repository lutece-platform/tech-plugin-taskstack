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

import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class TaskDto
{
    /**
     * Code of the task, generated at creation time.
     */
    protected String _strTaskCode;

    /**
     * ID of the resource associated with the task. As the tasks are generic, this ID can be whatever you want but must allow you find the resource in your
     * implementation.
     */
    protected String _strResourceId;

    /**
     * Type of the resource. As the tasks are generic, this type can be whatever you want but must allow you find the resource in your implementation.
     */
    protected String _strResourceType;

    /**
     * The type of the task. As the tasks are generic, this type can be whatever you want but must allow you to identify the task management in your
     * implementation.
     */
    protected String _strTaskType;

    /**
     * Date of the creation of the Task
     */
    protected Timestamp _dateCreationDate;

    /**
     * Date of the last update of the task
     */
    protected Timestamp _dateLastUpdateDate;

    /**
     * Code of the last client that updated the task
     */
    protected String _strLastUpdateClientCode;

    /**
     * Status of the task
     */
    protected TaskStatusType _enumTaskStatus;

    /**
     * Additional data that must be provided as key:value strings.
     */
    protected Map<String, String> _mapMetadata = new HashMap<>( );

    public String getTaskCode( )
    {
        return _strTaskCode;
    }

    public void setTaskCode( String _strTaskCode )
    {
        this._strTaskCode = _strTaskCode;
    }

    public String getResourceId( )
    {
        return _strResourceId;
    }

    public void setResourceId( String _strResourceId )
    {
        this._strResourceId = _strResourceId;
    }

    public String getResourceType( )
    {
        return _strResourceType;
    }

    public void setResourceType( String _strResourceType )
    {
        this._strResourceType = _strResourceType;
    }

    public String getTaskType( )
    {
        return _strTaskType;
    }

    public void setTaskType( String _strTaskType )
    {
        this._strTaskType = _strTaskType;
    }

    public Timestamp getCreationDate( )
    {
        return _dateCreationDate;
    }

    public void setCreationDate( Timestamp _dateCreateDate )
    {
        this._dateCreationDate = _dateCreateDate;
    }

    public Timestamp getLastUpdateDate( )
    {
        return _dateLastUpdateDate;
    }

    public void setLastUpdateDate( Timestamp _dateLastUpdateDate )
    {
        this._dateLastUpdateDate = _dateLastUpdateDate;
    }

    public String getLastUpdateClientCode( )
    {
        return _strLastUpdateClientCode;
    }

    public void setLastUpdateClientCode( String _strLastUpdateClientCode )
    {
        this._strLastUpdateClientCode = _strLastUpdateClientCode;
    }

    public TaskStatusType getTaskStatus( )
    {
        return _enumTaskStatus;
    }

    public void setTaskStatus( TaskStatusType _enumTaskStatus )
    {
        this._enumTaskStatus = _enumTaskStatus;
    }

    public Map<String, String> getMetadata( )
    {
        return _mapMetadata;
    }

    public void setMetadata( Map<String, String> _mapMetadata )
    {
        this._mapMetadata = _mapMetadata;
    }
}
