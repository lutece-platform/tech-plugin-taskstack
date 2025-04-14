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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.sql.TransactionManager;

public class TaskService
{
    private static final int PROPERTY_MAX_NB_TASK_RETURNED = AppPropertiesService.getPropertyInt( "taskstack.search.maxNbTaskReturned", 0 );

    private static TaskService _instance;
    private static Map<String, ITaskManagement> taskManagementBeans;

    /**
     * get instance of taskService
     * 
     * @return the instance
     */
    public static TaskService instance( )
    {
        if ( _instance == null )
        {
            _instance = new TaskService( );

            Map<String, ITaskManagement> listOfBeans = SpringContextService.getContext( ).getBeansOfType( ITaskManagement.class );
            taskManagementBeans = new HashMap<>( );
            listOfBeans.forEach( ( key, taskManager ) -> {
                taskManagementBeans.put( taskManager.getTaskType( ), taskManager );
            } );
        }
        return _instance;
    }

    /**
     * Private Constructor
     */
    private TaskService( )
    {
    }

    /**
     * Create task
     * 
     * @param taskDto
     * @param author
     * @param clientCode
     * @return the TaskDto of the created task
     * @throws TaskStackException
     */
    public TaskDto createTask( final TaskDto taskDto, final RequestAuthor author, final String clientCode ) throws TaskStackException
    {
        final ITaskManagement taskManager = taskManagementBeans.get( taskDto.getTaskType( ) );

        TransactionManager.beginTransaction( null );
        try
        {
            boolean validationError = false;
            Map<String, String> errMap = new HashMap<>( );
            taskDto.setTaskCode( UUID.randomUUID( ).toString( ) );
            taskDto.setLastUpdateClientCode( clientCode );
            taskDto.setTaskStatus( TaskStatusType.TODO );

            if ( taskManager != null )
            {
                try
                {
                    taskManager.doBefore( taskDto );
                }
                catch( final TaskValidationException e )
                {
                    validationError = true;
                    taskDto.setTaskStatus( TaskStatusType.REFUSED );
                    errMap.put( "Error", e.getMessage( ) );
                    taskDto.setMetadata( errMap );
                }
            }

            final Task task = DtoMapper.toTask( taskDto );
            TaskHome.create( task );

            final TaskChange taskChange = new TaskChange( );
            taskChange.setTaskChangeDate( task.getCreationDate( ) );
            taskChange.setTaskChangeType( TaskChangeType.CREATED );
            taskChange.setTaskStatus( validationError ? TaskStatusType.REFUSED : taskDto.getTaskStatus( ) );
            taskChange.setIdTask( task.getId( ) );
            taskChange.setClientCode( clientCode );
            taskChange.setAuthorType( author.getType( ).name( ) );
            taskChange.setAuthorName( author.getName( ) );

            TaskChangeHome.create( taskChange );

            if ( taskManager != null )
            {
                taskManager.doAfter( taskDto );
            }

            TransactionManager.commitTransaction( null );

            return taskDto;
        }
        catch( final Exception e )
        {
            TransactionManager.rollBack( null );
            throw new TaskStackException( "An error occurred during task creation: " + e.getMessage( ), e );
        }
    }

    /**
     * Update status
     * 
     * @param strTaskCode
     * @param newStatus
     * @param author
     * @param clientCode
     * @throws TaskStackException
     */
    public void updateTaskStatus( final String strTaskCode, final TaskStatusType newStatus, final RequestAuthor author, final String clientCode )
            throws TaskStackException
    {
        final Task existingTask = TaskHome.get( strTaskCode );
        if ( existingTask == null )
        {
            throw new TaskNotFoundException( "Could not find task with code " + strTaskCode );
        }

        final ITaskManagement taskManager = taskManagementBeans.get( existingTask.getTaskType( ) );

        final TaskDto existingTaskDto = DtoMapper.toTaskDto( existingTask );

        checkAccess( existingTaskDto, TaskChangeType.UPDATED );

        existingTaskDto.setTaskStatus( newStatus );
        TransactionManager.beginTransaction( null );
        try
        {
            if ( taskManager != null )
            {
        	taskManager.doBefore( existingTaskDto );
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
            if ( taskManager != null )
            {
        	taskManager.doAfter( existingTaskDto );
            }

            TransactionManager.commitTransaction( null );
        }
        catch( final Exception e )
        {
            TransactionManager.rollBack( null );
            throw new TaskStackException( "An error occurred during task update: " + e.getMessage( ), e );
        }
    }

    /**
     * Get a task
     * 
     * @param strTaskCode
     * @return the taskDto
     * @throws TaskStackException
     */
    public TaskDto getTask( final String strTaskCode ) throws TaskStackException
    {
        final Task existingTask = TaskHome.get( strTaskCode );

        if ( existingTask == null )
        {
            throw new TaskNotFoundException( "Could not find task with code " + strTaskCode );
        }

        final TaskDto taskDto = DtoMapper.toTaskDto( existingTask );

        checkAccess( taskDto, TaskChangeType.READ );

        taskDto.getTaskChanges( ).addAll( this.getTaskHistory( taskDto.getTaskCode( ) ) );
        return taskDto;
    }

    /**
     * check access
     * 
     * @param taskDto
     * @throws TaskNotFoundException
     */
    private void checkAccess( TaskDto taskDto, TaskChangeType type ) throws TaskNotFoundException
    {
        final ITaskManagement taskManager = taskManagementBeans.get( taskDto.getTaskType( ) );
        if ( taskManager != null )
        {
            try
            {
                taskManager.checkAccess( taskDto, type );
            }
            catch( TaskValidationException e )
            {
                throw new TaskNotFoundException( "Access Denied" );
            }
        }
    }

    /**
     * get the list of tasks for a given resource
     * 
     * @param strResourceId
     * @param strResourceType
     * @return the task list
     * @throws TaskStackException
     */
    public List<TaskDto> getTasks( final String strResourceId, final String strResourceType ) throws TaskStackException
    {
        final List<Task> tasks = TaskHome.get( strResourceId, strResourceType );

        final List<TaskDto> taskDtos = new ArrayList<>( );
        if ( tasks != null && !tasks.isEmpty( ) )
        {
            for ( final Task task : tasks )
            {
                final TaskDto taskDto = DtoMapper.toTaskDto( task );
                try
                {
                    checkAccess( taskDto, TaskChangeType.READ );
                    taskDto.getTaskChanges( ).addAll( this.getTaskHistory( task.getTaskCode( ) ) );
                    taskDtos.add( taskDto );
                }
                catch( TaskNotFoundException e )
                {
                    // do not add to list
                }

            }
        }

        return taskDtos;
    }

    /**
     * Search tasks with criterias
     * 
     * @param _strTaskCode
     * @param _strResourceId
     * @param _strResourceType
     * @param _strTaskType
     * @param creationDate
     * @param lastUpdatedate
     * @param strLastUpdateClientCode
     * @param _enumTaskStatus
     * @param _nNbDaysSinceCreated
     * @param creationDateOrdering
     * @param metadata
     * @param max
     * @return the task list
     * @throws TaskStackException
     */
    public List<TaskDto> search( final String _strTaskCode, final String _strResourceId, final String _strResourceType, final String _strTaskType,
            final Date creationDate, final Date lastUpdatedate, final String strLastUpdateClientCode, final List<TaskStatusType> _enumTaskStatus,
            final Integer _nNbDaysSinceCreated, final CreationDateOrdering creationDateOrdering, final Map<String, String> metadata, final int max )
            throws TaskStackException
    {
        int nMaxNbIdentityReturned = ( max > 0 ) ? max : PROPERTY_MAX_NB_TASK_RETURNED;
        List<TaskDto> taskList = new ArrayList<>( );

        try
        {
            final List<Task> result = TaskHome.search( _strTaskCode, _strResourceId, _strResourceType, _strTaskType, creationDate, lastUpdatedate,
                    strLastUpdateClientCode, _enumTaskStatus, _nNbDaysSinceCreated, creationDateOrdering, nMaxNbIdentityReturned, metadata );

            for ( Task t : result )
            {
                try
                {
                    final TaskDto taskDto = DtoMapper.toTaskDto( t );

                    checkAccess( taskDto, TaskChangeType.READ );
                    taskDto.getTaskChanges( ).addAll( this.getTaskHistory( taskDto.getTaskCode( ) ) );
                    taskList.add( taskDto );
                }
                catch( TaskNotFoundException e )
                {
                    // do not add to list
                }
            }
        }
        catch( final Exception e )
        {
            throw new TaskStackException( "An error occurred during task search", e );
        }

        return taskList;

    }

    /**
     * Search a list of Task Ids
     * 
     * @param _strTaskCode
     * @param _strResourceId
     * @param _strResourceType
     * @param _strTaskType
     * @param creationDate
     * @param lastUpdatedate
     * @param strLastUpdateClientCode
     * @param _enumTaskStatus
     * @param _nNbDaysSinceCreated
     * @param creationDateOrdering
     * @param metadata
     * @param max
     * @return the list of Ids of the tasks
     * @throws TaskStackException
     */
    public List<Integer> searchId( final String _strTaskCode, final String _strResourceId, final String _strResourceType, final String _strTaskType,
            final Date creationDate, final Date lastUpdatedate, final String strLastUpdateClientCode, final List<TaskStatusType> _enumTaskStatus,
            final Integer _nNbDaysSinceCreated, final CreationDateOrdering creationDateOrdering, final Map<String, String> metadata, final int max )
            throws TaskStackException
    {
        int nMaxNbIdentityReturned = ( max > 0 ) ? max : PROPERTY_MAX_NB_TASK_RETURNED;
        try
        {
            return TaskHome.searchId( _strTaskCode, _strResourceId, _strResourceType, _strTaskType, creationDate, lastUpdatedate, strLastUpdateClientCode,
                    _enumTaskStatus, _nNbDaysSinceCreated, creationDateOrdering, nMaxNbIdentityReturned, metadata );
        }
        catch( final Exception e )
        {
            throw new TaskStackException( "An error occurred during task search", e );
        }
    }

    /**
     * get a list of tasks from a list of Ids
     * 
     * @param listId
     * @return the task list
     * @throws TaskStackException
     */
    public List<TaskDto> getTasksListByIds( List<Integer> listId ) throws TaskStackException
    {
        final List<Task> search = TaskHome.getTasksListByIds( listId );
        return search.stream( ).map( DtoMapper::toTaskDto ).peek( taskDto -> taskDto.getTaskChanges( ).addAll( this.getTaskHistory( taskDto.getTaskCode( ) ) ) )
                .collect( Collectors.toList( ) );
    }

    /**
     * Delete task
     * 
     * @param task
     * @throws TaskStackException
     */
    public void deleteTask( final Task task ) throws TaskStackException
    {
        if ( task == null || task.getId( ) == null )
        {
            throw new TaskStackException( "Could not delete null task " );
        }

        try
        {
            checkAccess( DtoMapper.toTaskDto( task ), TaskChangeType.DELETED );

            TaskChangeHome.deleteAllByTaskId( task.getId( ) );
            TaskHome.delete( task.getId( ) );
        }
        catch( TaskNotFoundException e )
        {
            // do not delete
        }

    }

    /**
     * get task history
     * 
     * @param strTaskCode
     * @return the history items list
     */
    private List<TaskChangeDto> getTaskHistory( final String strTaskCode )
    {
        final List<TaskChange> history = TaskChangeHome.getHistory( strTaskCode );
        return history.stream( ).map( taskChange -> DtoMapper.toTaskChangeDto( taskChange, strTaskCode ) ).collect( Collectors.toList( ) );
    }

}
