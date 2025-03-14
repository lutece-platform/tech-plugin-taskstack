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
import fr.paris.lutece.plugins.taskstack.rs.dto.GetTaskStatusResponse;
import fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusFactory;
import fr.paris.lutece.plugins.taskstack.rs.request.common.AbstractTaskStackRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.common.TaskStackRequestValidator;
import fr.paris.lutece.plugins.taskstack.service.TaskService;
import fr.paris.lutece.plugins.taskstack.util.Constants;

public class TaskStackGetTaskStatusRequest extends AbstractTaskStackRequest
{

    private final String taskCode;

    public TaskStackGetTaskStatusRequest( final String taskCode, final String strClientCode, final String authorName, final String authorType )
            throws TaskStackException
    {
        super( strClientCode, authorName, authorType );
        this.taskCode = taskCode;
    }

    @Override
    protected void validateSpecificRequest( ) throws TaskStackException
    {
        TaskStackRequestValidator.instance( ).validateTaskCode( taskCode );
    }

    @Override
    protected Object doSpecificRequest( ) throws TaskStackException
    {
        try
        {
            final TaskDto task = TaskService.instance( ).getTask( taskCode );
            final GetTaskStatusResponse response = new GetTaskStatusResponse( );
            response.setTaskStatus( task.getTaskStatus( ).name( ) );
            final String successMessage = String.format( "A task status could be found with code %s.", taskCode );
            response.setStatus( ResponseStatusFactory.ok( ).setMessage( successMessage ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );
            return response;
        }
        catch( TaskStackException e )
        {
            throw new TaskStackException( "Error while retrieving task " + taskCode + ".", e );
        }
    }
}
