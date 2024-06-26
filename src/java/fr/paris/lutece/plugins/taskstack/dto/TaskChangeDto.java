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
import fr.paris.lutece.plugins.taskstack.business.task.TaskChangeType;
import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;
import fr.paris.lutece.plugins.taskstack.rs.request.common.RequestAuthor;

import java.sql.Timestamp;

public class TaskChangeDto
{
    @JsonProperty( "task_code" )
    private String taskCode;

    @JsonProperty( "request_author" )
    private RequestAuthor author;

    @JsonProperty( "client_code" )
    private String clientCode;

    @JsonProperty( "task_status" )
    private TaskStatusType taskStatus;

    @JsonProperty( "task_change_type" )
    private TaskChangeType taskChangeType;

    @JsonProperty( "task_change_date" )
    private Timestamp taskChangeDate;

    public String getTaskCode( )
    {
        return taskCode;
    }

    public void setTaskCode( String taskCode )
    {
        this.taskCode = taskCode;
    }

    public RequestAuthor getAuthor( )
    {
        return author;
    }

    public void setAuthor( RequestAuthor author )
    {
        this.author = author;
    }

    public String getClientCode( )
    {
        return clientCode;
    }

    public void setClientCode( String clientCode )
    {
        this.clientCode = clientCode;
    }

    public TaskStatusType getTaskStatus( )
    {
        return taskStatus;
    }

    public void setTaskStatus( TaskStatusType taskStatus )
    {
        this.taskStatus = taskStatus;
    }

    public TaskChangeType getTaskChangeType( )
    {
        return taskChangeType;
    }

    public void setTaskChangeType( TaskChangeType taskChangeType )
    {
        this.taskChangeType = taskChangeType;
    }

    public Timestamp getTaskChangeDate( )
    {
        return taskChangeDate;
    }

    public void setTaskChangeDate( Timestamp taskChangeDate )
    {
        this.taskChangeDate = taskChangeDate;
    }
}
