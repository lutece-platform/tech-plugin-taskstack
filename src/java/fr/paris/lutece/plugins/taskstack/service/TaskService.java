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
package fr.paris.lutece.plugins.taskstack.service;

import fr.paris.lutece.plugins.taskstack.business.task.Task;
import fr.paris.lutece.plugins.taskstack.business.task.TaskChange;
import fr.paris.lutece.plugins.taskstack.business.task.TaskChangeHome;
import fr.paris.lutece.plugins.taskstack.business.task.TaskChangeType;
import fr.paris.lutece.plugins.taskstack.business.task.TaskHome;
import fr.paris.lutece.plugins.taskstack.dto.AuthorDto;
import fr.paris.lutece.plugins.taskstack.dto.DtoMapper;
import fr.paris.lutece.plugins.taskstack.dto.TaskChangeDto;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.sql.TransactionManager;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskService
{
    private static TaskService _instance;

    public static TaskService instance( )
    {
        if ( _instance == null )
        {
            _instance = new TaskService( );
        }
        return _instance;
    }

    private TaskService( )
    {
    }

    public String createTask( final TaskDto taskDto, final AuthorDto author, final String clientCode ) throws TaskStackException
    {
        final TaskManagement taskManagement = SpringContextService.getBeansOfType( TaskManagement.class ).stream( )
                .filter( t -> Objects.equals( t.getType( ), taskDto.getTaskType( ) ) ).findFirst( ).orElse( null );
        TransactionManager.beginTransaction( null );
        try
        {
            if ( taskManagement != null )
            {
                taskManagement.doBefore( taskDto );
            }

            final Timestamp creationDate = new Timestamp( new Date( ).getTime( ) );
            taskDto.setTaskCode( UUID.randomUUID( ).toString( ) );
            taskDto.setCreationDate( creationDate );
            taskDto.setLastUpdateDate( creationDate );
            taskDto.setLastUpdateClientCode( clientCode );

            final Task task = DtoMapper.toTask( taskDto );
            TaskHome.create( task );

            final TaskChange taskChange = new TaskChange( );
            taskChange.setTaskChangeDate( creationDate );
            taskChange.setTaskChangeType( TaskChangeType.CREATED );
            taskChange.setTaskStatus( taskDto.getTaskStatus( ) );
            taskChange.setIdTask( task.getId( ) );
            taskChange.setClientCode( clientCode );
            taskChange.setAuthorType( author.getType( ) );
            taskChange.setAuthorName( author.getName( ) );
            TaskChangeHome.create( taskChange );

            if ( taskManagement != null )
            {
                taskManagement.doAfter( taskDto );
            }

            TransactionManager.commitTransaction( null );
            return taskDto.getTaskCode( );
        }
        catch( final Exception e )
        {
            TransactionManager.rollBack( null );
            throw new TaskStackException( "An error occurred during task creation: ", e );
        }
    }

    public void updateTask( final TaskDto taskDto, final AuthorDto author, final String clientCode ) throws TaskStackException
    {
        final TaskManagement taskManagement = SpringContextService.getBeansOfType( TaskManagement.class ).stream( )
                .filter( t -> Objects.equals( t.getType( ), taskDto.getTaskType( ) ) ).findFirst( ).orElse( null );
        TransactionManager.beginTransaction( null );
        try
        {
            final Task existingTask = TaskHome.get( taskDto.getTaskCode( ) );
            if ( existingTask == null )
            {
                throw new TaskStackException( "Could not find task with code " + taskDto.getTaskCode( ) );
            }

            if ( taskManagement != null )
            {
                taskManagement.doBefore( taskDto );
            }
            final Timestamp updateDate = new Timestamp( new Date( ).getTime( ) );
            taskDto.setLastUpdateDate( updateDate );
            taskDto.setLastUpdateClientCode( clientCode );
            final Task updatedTask = DtoMapper.toTask( taskDto );
            TaskHome.update( updatedTask );

            final TaskChange taskChange = new TaskChange( );
            taskChange.setTaskChangeDate( updateDate );
            taskChange.setTaskChangeType( TaskChangeType.UPDATED );
            taskChange.setTaskStatus( taskDto.getTaskStatus( ) );
            taskChange.setIdTask( updatedTask.getId( ) );
            taskChange.setClientCode( clientCode );
            taskChange.setAuthorType( author.getType( ) );
            taskChange.setAuthorName( author.getName( ) );
            TaskChangeHome.create( taskChange );
            if ( taskManagement != null )
            {
                taskManagement.doAfter( taskDto );
            }

            TransactionManager.commitTransaction( null );
        }
        catch( final Exception e )
        {
            TransactionManager.rollBack( null );
            throw new TaskStackException( "An error occurred during task update: ", e );
        }
    }

    public TaskDto getTask( final String _strTaskCode ) throws TaskStackException
    {
        final Task existingTask = TaskHome.get( _strTaskCode );

        if ( existingTask == null )
        {
            throw new TaskStackException( "Could not find task with code " + _strTaskCode );
        }

        return DtoMapper.toTaskChangeDto( existingTask );
    }

    public List<TaskChangeDto> getTaskHistory( final String _strTaskCode ) throws TaskStackException
    {
        final Task existingTask = TaskHome.get( _strTaskCode );

        if ( existingTask == null )
        {
            throw new TaskStackException( "Could not find task with code " + _strTaskCode );
        }

        final List<TaskChange> history = TaskChangeHome.getHistory( _strTaskCode );

        return history.stream( ).map( taskChange -> DtoMapper.toTaskChangeDto( taskChange, _strTaskCode ) ).collect( Collectors.toList( ) );
    }

    public void deleteTask( final String _strTaskCode, final AuthorDto author, final String clientCode )
    {

    }

}
