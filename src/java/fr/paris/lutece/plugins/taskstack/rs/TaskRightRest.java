package fr.paris.lutece.plugins.taskstack.rs;

import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.rs.dto.TaskRightCreateRequest;
import fr.paris.lutece.plugins.taskstack.rs.dto.TaskRightDeleteRequest;
import fr.paris.lutece.plugins.taskstack.rs.dto.TaskRightSearchResponse;
import fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseDto;
import fr.paris.lutece.plugins.taskstack.rs.request.TaskStackIdentityCompleteSearchRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.TaskStackIdentityCreateRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.TaskStackIdentityDeleteRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.TaskStackIdentityPartialSearchRequest;
import fr.paris.lutece.plugins.taskstack.util.Constants;


import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(RestConstants.BASE_PATH + Constants.PLUGIN_PATH + Constants.VERSION_V1 + Constants.TASK_RIGHT_PATH)
public class TaskRightRest
{
    @POST
    @Path( "" )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response createClientTask(
            TaskRightCreateRequest taskRightCreateRequest,
            @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode,
            @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode)
            throws TaskStackException
    {
        final TaskStackIdentityCreateRequest request = new TaskStackIdentityCreateRequest(taskRightCreateRequest, strHeaderClientCode, strHeaderAppCode, authorName, authorType);
        final ResponseDto response = (ResponseDto) request.doRequest( );
        return Response.status( response.getStatus( ).getHttpCode( ) ).entity( response ).type( MediaType.APPLICATION_JSON_TYPE ).build( );
    }

    @DELETE
    @Path( Constants.DELETE_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response deleteClientTask(
            TaskRightDeleteRequest taskRightDeleteRequest,
            @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode,
            @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode)
            throws TaskStackException
    {
        final TaskStackIdentityDeleteRequest request = new TaskStackIdentityDeleteRequest(taskRightDeleteRequest, strHeaderClientCode, strHeaderAppCode, authorName, authorType);
        final ResponseDto response = (ResponseDto) request.doRequest( );
        return Response.status( response.getStatus( ).getHttpCode( ) ).entity( response ).type( MediaType.APPLICATION_JSON_TYPE ).build( );
    }

    @GET
    @Path( Constants.PARTIAL_SEARCH_PATH + "{" + Constants.PARAM_SOURCE_CLIENT_CODE + "}" )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response partialSearchClientTask(
            @PathParam( Constants.PARAM_SOURCE_CLIENT_CODE ) String sourceClientCode,
            @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode,
            @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode)
            throws TaskStackException
    {
        final TaskStackIdentityPartialSearchRequest request = new TaskStackIdentityPartialSearchRequest( sourceClientCode, strHeaderClientCode, strHeaderAppCode, authorName, authorType);
        final TaskRightSearchResponse response = (TaskRightSearchResponse) request.doRequest( );
        return Response.status( response.getStatus( ).getHttpCode( ) ).entity( response ).type( MediaType.APPLICATION_JSON_TYPE ).build( );
    }

    @GET
    @Path( Constants.COMPLETE_SEARCH_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response completeSearchClientTask(
            @QueryParam( Constants.PARAM_SOURCE_CLIENT_CODE ) final String sourceClientCode,
            @QueryParam( Constants.PARAM_CLIENT_CODE_TASK_USER ) final String clientCodeTaskUser,
            @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode,
            @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode)
            throws TaskStackException
    {
        final TaskStackIdentityCompleteSearchRequest request = new TaskStackIdentityCompleteSearchRequest( sourceClientCode, clientCodeTaskUser, strHeaderClientCode, strHeaderAppCode, authorName, authorType);
        final TaskRightSearchResponse response = (TaskRightSearchResponse) request.doRequest( );
        return Response.status( response.getStatus( ).getHttpCode( ) ).entity( response ).type( MediaType.APPLICATION_JSON_TYPE ).build( );
    }

}
