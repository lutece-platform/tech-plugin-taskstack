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
package fr.paris.lutece.plugins.taskstack.rs.request;

import fr.paris.lutece.plugins.taskstack.dto.TaskDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.rs.dto.GetTaskListResponse;
import fr.paris.lutece.plugins.taskstack.rs.dto.GetTaskResponse;
import fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusFactory;
import fr.paris.lutece.plugins.taskstack.rs.request.common.AbstractTaskStackRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.common.TaskStackRequestValidator;
import fr.paris.lutece.plugins.taskstack.service.TaskService;
import fr.paris.lutece.plugins.taskstack.util.Constants;

import java.util.List;

public class TaskStackGetTaskByResourceRequest extends AbstractTaskStackRequest
{

    private final String taskResourceId;
    private final String taskResourceType;

    public TaskStackGetTaskByResourceRequest( final String taskResourceId, final String taskResourceType, final String strClientCode, final String authorName,
            final String authorType ) throws TaskStackException
    {
        super( strClientCode, authorName, authorType );
        this.taskResourceId = taskResourceId;
        this.taskResourceType = taskResourceType;
    }

    @Override
    protected void validateSpecificRequest( ) throws TaskStackException
    {
        TaskStackRequestValidator.instance( ).validateTaskResourceId( taskResourceId );
        TaskStackRequestValidator.instance( ).validateTaskResourceType( taskResourceType );
    }

    @Override
    protected Object doSpecificRequest( ) throws TaskStackException
    {
        try
        {
            final List<TaskDto> tasks = TaskService.instance( ).getTasks( taskResourceId, taskResourceType );
            final GetTaskListResponse response = new GetTaskListResponse( );
            response.getTasks( ).addAll( tasks );
            final String successMessage = String.format( "One or several tasks could be found for resource %s of type %s.", taskResourceId, taskResourceType );
            response.setStatus( ResponseStatusFactory.ok( ).setMessage( successMessage ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );
            return response;
        }
        catch( final TaskStackException e )
        {
            throw new TaskStackException( "Error while retrieving task " + taskResourceId + ".", e );
        }
    }
}
