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
package fr.paris.lutece.plugins.taskstack.rs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;
import fr.paris.lutece.plugins.taskstack.dto.CreationDateOrdering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonRootName( "task_search" )
public class SearchTaskRequest
{

    @JsonProperty( "task_type" )
    protected String identityTaskType;

    @JsonProperty( "task_status" )
    protected List<TaskStatusType> _enumTaskStatus;

    @JsonProperty( "nb_days_since_creation" )
    protected Integer _nNbDaysSinceCreated;

    @JsonProperty( "creation_date_ordering" )
    protected CreationDateOrdering creationDateOrdering;

    @JsonProperty( "metadata" )
    protected Map<String, String> metadata = new HashMap<String, String>( );

    public String getTaskType( )
    {
        return identityTaskType;
    }

    public void setTaskType( String _strTaskType )
    {
        this.identityTaskType = _strTaskType;
    }

    public List<TaskStatusType> getTaskStatus( )
    {
        return _enumTaskStatus;
    }

    public void setTaskStatus( List<TaskStatusType> _enumTaskStatus )
    {
        this._enumTaskStatus = _enumTaskStatus;
    }

    public Integer getNbDaysSinceCreated( )
    {
        return _nNbDaysSinceCreated;
    }

    public void setNbDaysSinceCreated( Integer _nNbDaysSinceCreated )
    {
        this._nNbDaysSinceCreated = _nNbDaysSinceCreated;
    }

    public CreationDateOrdering getCreationDateOrdering( )
    {
        return creationDateOrdering;
    }

    public void setCreationDateOrdering( CreationDateOrdering creationDateOrdering )
    {
        this.creationDateOrdering = creationDateOrdering;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
