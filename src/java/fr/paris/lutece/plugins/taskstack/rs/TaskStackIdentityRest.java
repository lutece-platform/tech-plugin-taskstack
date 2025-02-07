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
package fr.paris.lutece.plugins.taskstack.rs;

import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.rs.dto.CreateTaskRequest;
import fr.paris.lutece.plugins.taskstack.rs.dto.CreateTaskResponse;
import fr.paris.lutece.plugins.taskstack.rs.dto.GetTaskListResponse;
import fr.paris.lutece.plugins.taskstack.rs.dto.GetTaskResponse;
import fr.paris.lutece.plugins.taskstack.rs.dto.GetTaskStatusResponse;
import fr.paris.lutece.plugins.taskstack.rs.dto.SearchTaskRequest;
import fr.paris.lutece.plugins.taskstack.rs.dto.SearchTaskResponse;
import fr.paris.lutece.plugins.taskstack.rs.dto.UpdateTaskStatusRequest;
import fr.paris.lutece.plugins.taskstack.rs.dto.UpdateTaskStatusResponse;
import fr.paris.lutece.plugins.taskstack.rs.request.TaskStackCreateTaskRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.TaskStackGetTaskByResourceRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.TaskStackGetTaskRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.TaskStackGetTaskStatusRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.TaskStackSearchTaskRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.TaskStackUpdateTaskStatusRequest;
import fr.paris.lutece.plugins.taskstack.util.Constants;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * TaskStackIdentityRest
 */
@Path( RestConstants.BASE_PATH + Constants.PLUGIN_PATH + Constants.VERSION_V1 )
public class TaskStackIdentityRest
{

    @POST
    @Path( Constants.TASK_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response createTask( final CreateTaskRequest taskCreateRequest, @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode,
            @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName, @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode ) throws TaskStackException
    {
        final TaskStackCreateTaskRequest request = new TaskStackCreateTaskRequest( taskCreateRequest, strHeaderClientCode, authorName, authorType );
        final CreateTaskResponse response = (CreateTaskResponse) request.doRequest( );
        return Response.status( response.getStatus( ).getHttpCode( ) ).entity( response ).type( MediaType.APPLICATION_JSON_TYPE ).build( );
    }

    @PUT
    @Path( Constants.TASK_PATH + Constants.TASK_STATUS_PATH_WITH_PARAM )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response updateTaskStatus( @PathParam( Constants.TASK_CODE_PARAM ) final String taskCode, final UpdateTaskStatusRequest taskUpdateStatusRequest,
            @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode, @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode ) throws TaskStackException
    {
        final TaskStackUpdateTaskStatusRequest request = new TaskStackUpdateTaskStatusRequest( taskCode, taskUpdateStatusRequest, strHeaderClientCode,
                authorName, authorType );
        final UpdateTaskStatusResponse response = (UpdateTaskStatusResponse) request.doRequest( );
        return Response.status( response.getStatus( ).getHttpCode( ) ).entity( response ).type( MediaType.APPLICATION_JSON_TYPE ).build( );
    }

    @GET
    @Path( Constants.TASK_PATH + Constants.TASK_STATUS_PATH_WITH_PARAM )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getTaskStatus( @PathParam( Constants.TASK_CODE_PARAM ) final String taskCode,
            @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode, @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode ) throws TaskStackException
    {
        final TaskStackGetTaskStatusRequest request = new TaskStackGetTaskStatusRequest( taskCode, strHeaderClientCode, authorName, authorType );
        final GetTaskStatusResponse response = (GetTaskStatusResponse) request.doRequest( );
        return Response.status( response.getStatus( ).getHttpCode( ) ).entity( response ).type( MediaType.APPLICATION_JSON_TYPE ).build( );
    }

    @GET
    @Path( Constants.TASK_PATH + "/{" + Constants.TASK_CODE_PARAM + "}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getTask( @PathParam( Constants.TASK_CODE_PARAM ) final String taskCode,
            @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode, @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode ) throws TaskStackException
    {
        final TaskStackGetTaskRequest request = new TaskStackGetTaskRequest( taskCode, strHeaderClientCode, authorName, authorType );
        final GetTaskResponse response = (GetTaskResponse) request.doRequest( );
        return Response.status( response.getStatus( ).getHttpCode( ) ).entity( response ).type( MediaType.APPLICATION_JSON_TYPE ).build( );
    }

    @GET
    @Path( Constants.TASK_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getTaskByResourceIdAndType( @QueryParam( Constants.TASK_RESOURCE_ID_PARAM ) final String taskResourceId,
            @QueryParam( Constants.TASK_RESOURCE_TYPE_PARAM ) final String taskResourceType,
            @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode, @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode ) throws TaskStackException
    {
        final TaskStackGetTaskByResourceRequest request = new TaskStackGetTaskByResourceRequest( taskResourceId, taskResourceType, strHeaderClientCode,
                authorName, authorType );
        final GetTaskListResponse response = (GetTaskListResponse) request.doRequest( );
        return Response.status( response.getStatus( ).getHttpCode( ) ).entity( response ).type( MediaType.APPLICATION_JSON_TYPE ).build( );
    }

    @POST
    @Path( Constants.TASK_PATH + Constants.SEARCH_TASK_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response search( final SearchTaskRequest taskSearchRequest, @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode,
            @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName, @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode ) throws TaskStackException
    {
        final TaskStackSearchTaskRequest request = new TaskStackSearchTaskRequest( taskSearchRequest, strHeaderClientCode, authorName, authorType );
        final SearchTaskResponse response = (SearchTaskResponse) request.doRequest( );
        return Response.status( response.getStatus( ).getHttpCode( ) ).entity( response ).type( MediaType.APPLICATION_JSON_TYPE ).build( );
    }

}
