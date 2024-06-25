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
import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;
import fr.paris.lutece.plugins.taskstack.dto.CreationDateOrdering;
import fr.paris.lutece.plugins.taskstack.dto.DtoMapper;
import fr.paris.lutece.plugins.taskstack.dto.TaskChangeDto;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskNotFoundException;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.exception.TaskValidationException;
import fr.paris.lutece.plugins.taskstack.rs.request.common.RequestAuthor;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.sql.TransactionManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public String createTask( final TaskDto taskDto, final RequestAuthor author, final String clientCode ) throws TaskStackException
    {
        final Map<String, ITaskManagement> taskManagementBeans = SpringContextService.getContext( ).getBeansOfType( ITaskManagement.class );
        final ITaskManagement taskManagement = taskManagementBeans.values( ).stream( ).filter( t -> Objects.equals( t.getTaskType( ), taskDto.getTaskType( ) ) )
                .findFirst( ).orElse( null );
        TransactionManager.beginTransaction( null );
        try
        {
            boolean validationError = false;
            taskDto.setTaskCode( UUID.randomUUID( ).toString( ) );
            taskDto.setLastUpdateClientCode( clientCode );
            taskDto.setTaskStatus( TaskStatusType.TODO );

            if ( taskManagement != null )
            {
                try
                {
                    taskManagement.doBefore( taskDto.getResourceId( ), taskDto.getResourceType( ), taskDto.getTaskStatus( ) );
                }
                catch( final TaskValidationException e )
                {
                    validationError = true;
                }
            }

            final Task task = DtoMapper.toTask( taskDto );
            TaskHome.create( task );

            final TaskChange taskChange = new TaskChange( );
            taskChange.setTaskChangeDate( task.getCreationDate( ) );
            taskChange.setTaskChangeType( validationError ? TaskChangeType.REFUSED : TaskChangeType.CREATED );
            taskChange.setTaskStatus( validationError ? TaskStatusType.REFUSED : taskDto.getTaskStatus( ) );
            taskChange.setIdTask( task.getId( ) );
            taskChange.setClientCode( clientCode );
            taskChange.setAuthorType( author.getType( ).name( ) );
            taskChange.setAuthorName( author.getName( ) );
            TaskChangeHome.create( taskChange );

            if ( taskManagement != null )
            {
                taskManagement.doAfter( taskDto.getResourceId( ), taskDto.getResourceType( ), taskDto.getTaskStatus( ) );
            }

            TransactionManager.commitTransaction( null );
            return taskDto.getTaskCode( );
        }
        catch( final Exception e )
        {
            TransactionManager.rollBack( null );
            throw new TaskStackException( "An error occurred during task creation: " + e.getMessage( ), e );
        }
    }

    public void updateTaskStatus( final String strTaskCode, final TaskStatusType newStatus, final RequestAuthor author, final String clientCode )
            throws TaskStackException
    {
        final Task existingTask = TaskHome.get( strTaskCode );
        if ( existingTask == null )
        {
            throw new TaskNotFoundException( "Could not find task with code " + strTaskCode );
        }

        final ITaskManagement taskManagement = SpringContextService.getBeansOfType( ITaskManagement.class ).stream( )
                .filter( t -> Objects.equals( t.getTaskType( ), existingTask.getTaskType( ) ) ).findFirst( ).orElse( null );
        TransactionManager.beginTransaction( null );
        try
        {
            if ( taskManagement != null )
            {
                taskManagement.doBefore( existingTask.getResourceId( ), existingTask.getResourceType( ), newStatus );
            }
            final Timestamp updateStatusTime = TaskHome.updateStatus( strTaskCode, newStatus, clientCode );

            final TaskChange taskChange = new TaskChange( );
            taskChange.setTaskChangeType( TaskChangeType.UPDATED );
            taskChange.setTaskStatus( newStatus );
            taskChange.setIdTask( existingTask.getId( ) );
            taskChange.setClientCode( clientCode );
            taskChange.setAuthorType( author.getType( ).name( ) );
            taskChange.setAuthorName( author.getName( ) );
            taskChange.setTaskChangeDate( updateStatusTime );
            TaskChangeHome.create( taskChange );
            if ( taskManagement != null )
            {
                taskManagement.doAfter( existingTask.getResourceId( ), existingTask.getResourceType( ), newStatus );
            }

            TransactionManager.commitTransaction( null );
        }
        catch( final Exception e )
        {
            TransactionManager.rollBack( null );
            throw new TaskStackException( "An error occurred during task update: " + e.getMessage( ), e );
        }
    }

    public TaskDto getTask( final String strTaskCode ) throws TaskStackException
    {
        final Task existingTask = TaskHome.get( strTaskCode );

        if ( existingTask == null )
        {
            throw new TaskNotFoundException( "Could not find task with code " + strTaskCode );
        }

        final TaskDto taskDto = DtoMapper.toTaskDto( existingTask );
        taskDto.getTaskChanges( ).addAll( this.getTaskHistory( taskDto.getTaskCode( ) ) );
        return taskDto;
    }

    public List<TaskDto> getTasks( final String strResourceId, final String strResourceType ) throws TaskStackException
    {
        final List<Task> tasks = TaskHome.get( strResourceId, strResourceType );

        final List<TaskDto> taskDtos = new ArrayList<>( );
        if( tasks != null && !tasks.isEmpty( ) )
        {
            for ( final Task task : tasks )
            {
                final TaskDto taskDto = DtoMapper.toTaskDto( task );
                taskDto.getTaskChanges( ).addAll( this.getTaskHistory( task.getTaskCode( ) ) );
                taskDtos.add( taskDto );
            }
        }

        return taskDtos;
    }

    public List<TaskDto> search( final String _strTaskType, final List<TaskStatusType> _enumTaskStatus, final Integer _nNbDaysSinceCreated,
            final CreationDateOrdering creationDateOrdering ) throws TaskStackException
    {
        try
        {
            final List<Task> search = TaskHome.search( _strTaskType, _enumTaskStatus, _nNbDaysSinceCreated, creationDateOrdering );
            return search.stream( ).map( DtoMapper::toTaskDto )
                    .peek( taskDto -> taskDto.getTaskChanges( ).addAll( this.getTaskHistory( taskDto.getTaskCode( ) ) ) ).collect( Collectors.toList( ) );
        }
        catch( final Exception e )
        {
            throw new TaskStackException( "An error occurred during task search", e );
        }
    }

    public void deleteTask( final Task task ) throws TaskStackException
    {
        if ( task == null || task.getId( ) == null )
        {
            throw new TaskStackException( "Could not delete null task " );
        }
        TaskChangeHome.deleteAllByTaskId( task.getId( ) );
        TaskHome.delete( task.getId( ) );
    }

    private List<TaskChangeDto> getTaskHistory( final String strTaskCode )
    {
        final List<TaskChange> history = TaskChangeHome.getHistory( strTaskCode );
        return history.stream( ).map( taskChange -> DtoMapper.toTaskChangeDto( taskChange, strTaskCode ) ).collect( Collectors.toList( ) );
    }

}
