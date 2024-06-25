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
package fr.paris.lutece.plugins.taskstack.rs.request.common;

import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;
import fr.paris.lutece.plugins.taskstack.consumer.ProvidedTaskType;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.rs.dto.CreateTaskRequest;
import fr.paris.lutece.plugins.taskstack.rs.dto.SearchTaskRequest;
import fr.paris.lutece.plugins.taskstack.rs.dto.UpdateTaskStatusRequest;
import fr.paris.lutece.plugins.taskstack.util.Constants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class TaskStackRequestValidator
{

    private static TaskStackRequestValidator instance;

    private TaskStackRequestValidator( )
    {
    }

    public static TaskStackRequestValidator instance( )
    {
        if ( instance == null )
        {
            instance = new TaskStackRequestValidator( );
        }
        return instance;
    }

    /**
     * Check whether the parameters related to the author are valid or not
     *
     * @param author
     *            the request author to validate
     * @throws TaskStackException
     *             in case of error
     */
    public void checkAuthor( RequestAuthor author ) throws TaskStackException
    {

        if ( author == null )
        {
            throw new TaskStackException( "Provided Author is null" );
        }

        if ( StringUtils.isEmpty( author.getName( ) ) || author.getType( ) == null )
        {
            throw new TaskStackException( "Author and author type fields shall be set" );
        }

    }

    /**
     * check whether the parameters related to the application are valid or not
     *
     * @param strClientCode
     *            client application code
     * @throws TaskStackException
     *             if the parameters are not valid
     */
    public void checkClientCode( String strClientCode ) throws TaskStackException
    {
        if ( StringUtils.isBlank( strClientCode ) )
        {
            throw new TaskStackException( Constants.PARAM_CLIENT_CODE + " is missing" );
        }
    }

    public void validateTaskCreateRequest( final CreateTaskRequest request ) throws TaskStackException
    {
        if ( request == null || request.getTask( ) == null )
        {
            throw new TaskStackException( "Provided request is null or empty" );
        }

        if ( StringUtils.isBlank( request.getTask( ).getTaskType( ) ) )
        {
            throw new TaskStackException( "Provided task must have a type" );
        }
        else
            if ( ProvidedTaskType.values( ).stream( ).noneMatch( type -> type.equals( request.getTask( ).getTaskType( ) ) ) )
            {
                throw new TaskStackException( "Provided task type is invalid. Authorized values : " + ProvidedTaskType.values( ).toString( ) );
            }

        if ( StringUtils.isBlank( request.getTask( ).getResourceId( ) ) )
        {
            throw new TaskStackException( "Provided task must have a resource id" );
        }

        if ( StringUtils.isBlank( request.getTask( ).getResourceType( ) ) )
        {
            throw new TaskStackException( "Provided task must have a resource type." );
        }
    }

    public void validateTaskStatusUpdateRequest( final UpdateTaskStatusRequest request ) throws TaskStackException
    {
        if ( request == null || request.getStatus( ) == null )
        {
            throw new TaskStackException( "Provided request is null or empty" );
        }
    }

    public void validateTaskSearchRequest( final SearchTaskRequest request ) throws TaskStackException
    {
        if ( request == null || ( CollectionUtils.isEmpty( request.getTaskStatus( ) ) && request.getCreationDateOrdering( ) == null
                && request.getNbDaysSinceCreated( ) == null && request.getTaskType( ) == null ) )
        {
            throw new TaskStackException( "Provided request is null or empty" );
        }
    }

    public void validateTaskCode( final String taskCode ) throws TaskStackException
    {
        if ( StringUtils.isBlank( taskCode ) )
        {
            throw new TaskStackException( "Provided task code is null or empty" );
        }
    }

    public void validateTaskResourceId( final String taskResourceId ) throws TaskStackException
    {
        if ( StringUtils.isBlank( taskResourceId ) )
        {
            throw new TaskStackException( "Provided task resource id is null or empty" );
        }
    }

    public void validateTaskResourceType( final String taskResourceType ) throws TaskStackException
    {
        if ( StringUtils.isBlank( taskResourceType ) )
        {
            throw new TaskStackException( "Provided task resource type is null or empty" );
        }
    }
}
