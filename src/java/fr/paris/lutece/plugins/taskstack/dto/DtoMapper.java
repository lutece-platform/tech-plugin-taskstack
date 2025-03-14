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

import fr.paris.lutece.plugins.taskstack.business.task.Task;
import fr.paris.lutece.plugins.taskstack.business.task.TaskChange;
import fr.paris.lutece.plugins.taskstack.rs.request.common.AuthorType;
import fr.paris.lutece.plugins.taskstack.rs.request.common.RequestAuthor;

public class DtoMapper
{
    public static Task toTask( final TaskDto taskDto )
    {
        final Task task = new Task( );
        task.setTaskStatus( taskDto.getTaskStatus( ) );
        task.setTaskCode( taskDto.getTaskCode( ) );
        task.setResourceType( taskDto.getResourceType( ) );
        task.setCreationDate( taskDto.getCreationDate( ) );
        task.setTaskType( taskDto.getTaskType( ) );
        task.setLastUpdateClientCode( taskDto.getLastUpdateClientCode( ) );
        task.setLastUpdateDate( taskDto.getLastUpdateDate( ) );
        task.setExpirationDate( taskDto.getExpirationDate( ) );
        task.setResourceId( taskDto.getResourceId( ) );
        task.getMetadata( ).putAll( taskDto.getMetadata( ) );
        return task;
    }

    public static TaskDto toTaskDto( final Task task )
    {
        final TaskDto taskDto = new TaskDto( );
        taskDto.setTaskStatus( task.getTaskStatus( ) );
        taskDto.setTaskCode( task.getTaskCode( ) );
        taskDto.setResourceType( task.getResourceType( ) );
        taskDto.setCreationDate( task.getCreationDate( ) );
        taskDto.setTaskType( task.getTaskType( ) );
        taskDto.setLastUpdateClientCode( task.getLastUpdateClientCode( ) );
        taskDto.setLastUpdateDate( task.getLastUpdateDate( ) );
        taskDto.setExpirationDate( task.getExpirationDate( ) );
        taskDto.setResourceId( task.getResourceId( ) );
        taskDto.getMetadata( ).putAll( task.getMetadata( ) );
        return taskDto;
    }

    public static TaskChangeDto toTaskChangeDto( final TaskChange taskChange, final String taskCode )
    {
        final TaskChangeDto taskChangeDto = new TaskChangeDto( );
        taskChangeDto.setTaskStatus( taskChange.getTaskStatus( ) );
        taskChangeDto.setTaskCode( taskCode );
        taskChangeDto.setTaskChangeDate( taskChange.getTaskChangeDate( ) );
        taskChangeDto.setTaskChangeType( taskChange.getTaskChangeType( ) );
        final RequestAuthor author = new RequestAuthor( );
        author.setName( taskChange.getAuthorName( ) );
        author.setType( AuthorType.valueOf( taskChange.getAuthorType( ) ) );
        taskChangeDto.setAuthor( author );
        taskChangeDto.setClientCode( taskChange.getClientCode( ) );
        return taskChangeDto;
    }
}
