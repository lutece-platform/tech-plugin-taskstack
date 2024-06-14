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
package fr.paris.lutece.plugins.taskstack.business.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.taskstack.dto.CreationDateOrdering;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskDAO implements ITaskDAO
{

    public static final String BEAN_NAME = "taskstack.task.dao";
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id, code, resource_id, resource_type, type, creation_date, last_update_date, last_update_client_code, status, metadata::text FROM stack_task WHERE id = ?";
    private static final String SQL_QUERY_SELECT_BY_CODE = "SELECT id, code, resource_id, resource_type, type, creation_date, last_update_date, last_update_client_code, status, metadata::text FROM stack_task WHERE code = ?";
    private static final String SQL_QUERY_SEARCH = "SELECT id, code, resource_id, resource_type, type, creation_date, last_update_date, last_update_client_code, status, metadata::text FROM stack_task WHERE ${task_type_criteria} AND ${task_status_criteria} AND ${nb_days_creation_criteria} ${order_criteria}";
    private static final String SQL_QUERY_INSERT = "INSERT INTO stack_task ( code, resource_id, resource_type, type, creation_date, last_update_date, last_update_client_code, status, metadata ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, to_json(?::json) ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM stack_task WHERE id = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE stack_task SET code = ?, resource_id = ?, resource_type = ?, type = ?, creation_date = ?, last_update_date = ?, last_update_client_code = ?, status = ?, metadata = to_json(?::json) WHERE code = ?";
    private static final String SQL_QUERY_UPDATE_STATUS = "UPDATE stack_task SET last_update_date = ?, last_update_client_code = ?, status = ? WHERE code = ?";

    private final ObjectMapper objectMapper = new ObjectMapper( );

    @Override
    public void insert( final Task task, final Plugin plugin ) throws JsonProcessingException
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            final ZonedDateTime now = ZonedDateTime.now( ZoneId.systemDefault( ) );
            task.setCreationDate( Timestamp.from( now.toInstant( ) ) );
            task.setLastUpdateDate( Timestamp.from( now.toInstant( ) ) );

            int i = 0;
            daoUtil.setString( ++i, task.getTaskCode( ) );
            daoUtil.setString( ++i, task.getResourceId( ) );
            daoUtil.setString( ++i, task.getResourceType( ) );
            daoUtil.setString( ++i, task.getTaskType( ) );
            daoUtil.setTimestamp( ++i, task.getCreationDate( ) );
            daoUtil.setTimestamp( ++i, task.getLastUpdateDate( ) );
            daoUtil.setString( ++i, task.getLastUpdateClientCode( ) );
            daoUtil.setString( ++i, task.getTaskStatus( ).name( ) );
            daoUtil.setString( ++i, objectMapper.writeValueAsString( task.getMetadata( ) ) );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                task.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    @Override
    public void update( final Task task, final Plugin plugin ) throws JsonProcessingException
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            final ZonedDateTime now = ZonedDateTime.now( ZoneId.systemDefault( ) );
            task.setLastUpdateDate( Timestamp.from( now.toInstant( ) ) );
            int i = 0;
            daoUtil.setString( ++i, task.getTaskCode( ) );
            daoUtil.setString( ++i, task.getResourceId( ) );
            daoUtil.setString( ++i, task.getResourceType( ) );
            daoUtil.setString( ++i, task.getTaskType( ) );
            daoUtil.setTimestamp( ++i, task.getCreationDate( ) );
            daoUtil.setTimestamp( ++i, task.getLastUpdateDate( ) );
            daoUtil.setString( ++i, task.getLastUpdateClientCode( ) );
            daoUtil.setString( ++i, task.getTaskStatus( ).name( ) );
            daoUtil.setString( ++i, objectMapper.writeValueAsString( task.getMetadata( ) ) );
            daoUtil.setString( ++i, task.getTaskCode( ) );
            daoUtil.executeUpdate( );
        }
    }

    @Override
    public Timestamp updateStatus( final String taskCode, final TaskStatusType newStatus, final String clientCode, final Plugin plugin )
            throws JsonProcessingException
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_STATUS, plugin ) )
        {
            final ZonedDateTime now = ZonedDateTime.now( ZoneId.systemDefault( ) );
            final Timestamp timestamp = Timestamp.from( now.toInstant( ) );
            int i = 0;
            daoUtil.setTimestamp( ++i, timestamp );
            daoUtil.setString( ++i, clientCode );
            daoUtil.setString( ++i, newStatus.name( ) );
            daoUtil.setString( ++i, taskCode );
            daoUtil.executeUpdate( );
            return timestamp;
        }
    }

    @Override
    public Task load( final int nIdTask, final Plugin plugin ) throws JsonProcessingException
    {

        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nIdTask );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                return this.getTask( daoUtil );
            }
        }

        return null;
    }

    @Override
    public Task selectByCode( final String strCode, final Plugin plugin ) throws JsonProcessingException
    {

        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODE, plugin ) )
        {
            daoUtil.setString( 1, strCode );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                return this.getTask( daoUtil );
            }
        }

        return null;
    }

    @Override
    public void delete( final int nIdTask, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nIdTask );
            daoUtil.executeUpdate( );
        }
    }

    @Override
    public List<Task> search( String strTaskType, List<TaskStatusType> enumTaskStatus, Integer nNbDaysSinceCreated, CreationDateOrdering creationDateOrdering,
            Plugin plugin ) throws JsonProcessingException
    {

        String sqlQuerySearch = SQL_QUERY_SEARCH;
        if ( StringUtils.isNotBlank( strTaskType ) )
        {
            sqlQuerySearch = sqlQuerySearch.replace( "${task_type_criteria}", "type='" + strTaskType + "'" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( "${task_type_criteria}", "1=1" );
        }

        if ( enumTaskStatus != null && !enumTaskStatus.isEmpty( ) )
        {
            sqlQuerySearch = sqlQuerySearch.replace( "${task_status_criteria}",
                    "status IN ('" + enumTaskStatus.stream( ).map( Enum::name ).collect( Collectors.joining( "', '" ) ) + "')" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( "${task_status_criteria}", "1=1" );
        }

        if ( nNbDaysSinceCreated != null && nNbDaysSinceCreated > 0 )
        {
            sqlQuerySearch = sqlQuerySearch.replace( "${nb_days_creation_criteria}", "creation_date > now() - interval '" + nNbDaysSinceCreated + "' day" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( "${nb_days_creation_criteria}", "1=1" );
        }

        if ( creationDateOrdering != null )
        {
            sqlQuerySearch = sqlQuerySearch.replace( "${order_criteria}", " ORDER BY creation_date " + creationDateOrdering.name( ) );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( "${order_criteria}", "" );
        }

        try ( final DAOUtil daoUtil = new DAOUtil( sqlQuerySearch, plugin ) )
        {
            final List<Task> tasks = new ArrayList<>( );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                tasks.add( this.getTask( daoUtil ) );
            }
            return tasks;
        }
    }

    private Task getTask( final DAOUtil daoUtil ) throws JsonProcessingException
    {
        final Task task = new Task( );
        int i = 1;
        task.setId( daoUtil.getInt( i++ ) );
        task.setTaskCode( daoUtil.getString( i++ ) );
        task.setResourceId( daoUtil.getString( i++ ) );
        task.setResourceType( daoUtil.getString( i++ ) );
        task.setTaskType( daoUtil.getString( i++ ) );
        task.setCreationDate( daoUtil.getTimestamp( i++ ) );
        task.setLastUpdateDate( daoUtil.getTimestamp( i++ ) );
        task.setLastUpdateClientCode( daoUtil.getString( i++ ) );
        task.setTaskStatus( TaskStatusType.valueOf( daoUtil.getString( i++ ) ) );
        final String jsonMap = daoUtil.getString( i );
        if ( StringUtils.isNotEmpty( jsonMap ) )
        {
            final Map<String, String> mapMetaData = objectMapper.readValue( jsonMap, new TypeReference<Map<String, String>>( )
            {
            } );
            task.getMetadata( ).clear( );
            if ( mapMetaData != null && !mapMetaData.isEmpty( ) )
            {
                task.getMetadata( ).putAll( mapMetaData );
            }
        }
        return task;
    }

}
