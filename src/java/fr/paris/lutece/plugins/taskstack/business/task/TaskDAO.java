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
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.sql.DAOUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskDAO implements ITaskDAO
{

    public static final String BEAN_NAME = "taskstack.task.dao";
    private static final int RETENTION = AppPropertiesService.getPropertyInt( "taskstack.task.retention.days.number", 0 );

    // Constants
    private static final String TASK_CODE_CRITERIA = "${task_code_criteria}";
    private static final String TASK_RESOURCE_ID_CRITERIA = "${task_resource_id_criteria}";
    private static final String TASK_RESOURCE_TYPE_CRITERIA = "${task_resource_type_criteria}";
    private static final String TASK_TYPE_CRITERIA = "${task_type_criteria}";
    private static final String TASK_CREATION_DATE_CRITERIA = "${task_creation_date_criteria}";
    private static final String TASK_LAST_UPDATE_CRITERIA = "${task_last_update_criteria}";
    private static final String TASK_LAST_UPDATE_CLIENT_CODE_CRITERIA = "${task_last_update_client_code_criteria}";
    private static final String TASK_STATUS_CRITERIA = "${task_status_criteria}";
    private static final String NB_DAYS_CREATION_CRITERIA = "${nb_days_creation_criteria}";
    private static final String METADATA_CRITERIA = "${metadata_criteria}";
    private static final String ORDER_CRITERIA = "${order_criteria}";
    private static final String LIMIT = "${limit}";

    // Requests
    private static final String SQL_QUERY_SELECT = "SELECT id, code, resource_id, resource_type, type, creation_date, last_update_date, last_update_client_code, status, metadata::text FROM stack_task WHERE id = ?";
    private static final String SQL_QUERY_SELECT_BY_CODE = "SELECT id, code, resource_id, resource_type, type, creation_date, last_update_date, last_update_client_code, status, metadata::text FROM stack_task WHERE code = ?";
    private static final String SQL_QUERY_SELECT_BY_ID_AND_RESOURCE_TYPE = "SELECT id, code, resource_id, resource_type, type, creation_date, last_update_date, last_update_client_code, status, metadata::text FROM stack_task WHERE resource_id = ? AND resource_type = ?";
    private static final String SQL_QUERY_SELECTALL_BY_IDS = "SELECT id, code, resource_id, resource_type, type, creation_date, last_update_date, last_update_client_code, status, metadata::text FROM stack_task WHERE id IN (";
    private static final String SQL_QUERY_SELECT_BY_VALIDITY = "SELECT id, code, resource_id, resource_type, type, creation_date, last_update_date, last_update_client_code, status, metadata::text FROM stack_task WHERE stack_task.expiration_date < now();";
    private static final String SQL_QUERY_SEARCH = "SELECT id, code, resource_id, resource_type, type, creation_date, last_update_date, last_update_client_code, status, metadata::text FROM stack_task WHERE " + TASK_CODE_CRITERIA + " AND " + TASK_RESOURCE_ID_CRITERIA + " AND " + TASK_RESOURCE_TYPE_CRITERIA + " AND " + TASK_TYPE_CRITERIA + " AND " + TASK_CREATION_DATE_CRITERIA + " AND " + TASK_LAST_UPDATE_CRITERIA + " AND " + TASK_LAST_UPDATE_CLIENT_CODE_CRITERIA + " AND " + TASK_STATUS_CRITERIA + " AND " + NB_DAYS_CREATION_CRITERIA + " AND " + METADATA_CRITERIA + " " + ORDER_CRITERIA + " LIMIT " + LIMIT;
    private static final String SQL_QUERY_SEARCH_ID = "SELECT id FROM stack_task WHERE " + TASK_CODE_CRITERIA + " AND " + TASK_RESOURCE_ID_CRITERIA + " AND " + TASK_RESOURCE_TYPE_CRITERIA + " AND " + TASK_TYPE_CRITERIA + " AND " + TASK_CREATION_DATE_CRITERIA + " AND " + TASK_LAST_UPDATE_CRITERIA + " AND " + TASK_LAST_UPDATE_CLIENT_CODE_CRITERIA + " AND " + TASK_STATUS_CRITERIA + " AND " + NB_DAYS_CREATION_CRITERIA + " AND " + METADATA_CRITERIA + " " + ORDER_CRITERIA + " LIMIT " + LIMIT;
    private static final String SQL_QUERY_INSERT = "INSERT INTO stack_task ( code, resource_id, resource_type, type, creation_date, last_update_date, last_update_client_code, expiration_date, status, metadata ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, to_json(?::json) ) ";
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
            task.setExpirationDate( Timestamp.valueOf( task.getCreationDate( ).toLocalDateTime( ).plusDays( RETENTION ) ) );

            int i = 0;
            daoUtil.setString( ++i, task.getTaskCode( ) );
            daoUtil.setString( ++i, task.getResourceId( ) );
            daoUtil.setString( ++i, task.getResourceType( ) );
            daoUtil.setString( ++i, task.getTaskType( ) );
            daoUtil.setTimestamp( ++i, task.getCreationDate( ) );
            daoUtil.setTimestamp( ++i, task.getLastUpdateDate( ) );
            daoUtil.setString( ++i, task.getLastUpdateClientCode( ) );
            daoUtil.setTimestamp( ++i, task.getExpirationDate( ) );
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
    public List<Task> selectExpiredTask( final Plugin plugin ) throws JsonProcessingException
    {
        final List<Task> expiredTasks = new ArrayList<>( );
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_VALIDITY, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                expiredTasks.add( this.getTask( daoUtil ) );
            }
        }
        return expiredTasks;
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
    public List<Task> search(final String strTaskCode, final String strResourceId, final String strResourceType, String strTaskType, final Date creationDate,
                             final Date lastUpdatedate, final String strLastUpdateClientCode, final List<TaskStatusType> enumTaskStatus, final Integer nNbDaysSinceCreated,
                             final CreationDateOrdering creationDateOrdering, final int nMaxNbIdentityReturned, final Map<String, String> metadata, final Plugin plugin ) throws JsonProcessingException
    {

        final String sqlQuerySearch = this.fillCriterias(strTaskCode, strResourceId, strResourceType, strTaskType, creationDate,
                lastUpdatedate, strLastUpdateClientCode, enumTaskStatus, nNbDaysSinceCreated, creationDateOrdering, nMaxNbIdentityReturned, metadata, SQL_QUERY_SEARCH);

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

    @Override
    public List<Integer> searchId(final String strTaskCode, final String strResourceId, final String strResourceType, String strTaskType, final Date creationDate,
                                  final Date lastUpdatedate, final String strLastUpdateClientCode, final List<TaskStatusType> enumTaskStatus, final Integer nNbDaysSinceCreated,
                                  final CreationDateOrdering creationDateOrdering, final int nMaxNbIdentityReturned, final Map<String, String> metadata, Plugin plugin ) throws JsonProcessingException
    {

        final String sqlQuerySearch = this.fillCriterias(strTaskCode, strResourceId, strResourceType, strTaskType, creationDate,
                lastUpdatedate, strLastUpdateClientCode, enumTaskStatus, nNbDaysSinceCreated, creationDateOrdering, nMaxNbIdentityReturned, metadata, SQL_QUERY_SEARCH_ID);

        try ( final DAOUtil daoUtil = new DAOUtil( sqlQuerySearch, plugin ) )
        {
            final List<Integer> tasks = new ArrayList<>( );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                tasks.add( daoUtil.getInt(1));
            }
            return tasks;
        }
    }

    public String fillCriterias(final String strTaskCode, final String strResourceId, final String strResourceType,
                                String strTaskType, final Date creationDate, final Date lastUpdatedate,
                                final String strLastUpdateClientCode, List<TaskStatusType> enumTaskStatus,
                                Integer nNbDaysSinceCreated, CreationDateOrdering creationDateOrdering, Integer limit, Map<String, String> metadata, String sqlQuerySearch )
    {
        if ( StringUtils.isNotBlank( strTaskCode ) )
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_CODE_CRITERIA, "code='" + strTaskCode + "'" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_CODE_CRITERIA, "1=1" );
        }

        if ( StringUtils.isNotBlank( strResourceId ) )
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_RESOURCE_ID_CRITERIA, "resource_id='" + strResourceId + "'" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_RESOURCE_ID_CRITERIA, "1=1" );
        }

        if ( StringUtils.isNotBlank( strResourceType ) )
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_RESOURCE_TYPE_CRITERIA, "resource_type='" + strResourceType + "'" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_RESOURCE_TYPE_CRITERIA, "1=1" );
        }

        if ( StringUtils.isNotBlank( strTaskType ) )
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_TYPE_CRITERIA, "type='" + strTaskType + "'" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_TYPE_CRITERIA, "1=1" );
        }

        if(creationDate != null )
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_CREATION_DATE_CRITERIA, "creation_date >= '" + creationDate +
                    "' AND creation_date < '" + DateUtils.addDays(creationDate, 1) + "'" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_CREATION_DATE_CRITERIA,  "1=1" );
        }

        if(lastUpdatedate != null )
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_LAST_UPDATE_CRITERIA, "last_update_date >= '" + lastUpdatedate +
                    "' AND last_update_date < '" + DateUtils.addDays(lastUpdatedate, 1) + "'" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_LAST_UPDATE_CRITERIA,  "1=1" );
        }

        if ( StringUtils.isNotBlank( strLastUpdateClientCode ) )
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_LAST_UPDATE_CLIENT_CODE_CRITERIA, "last_update_client_code='" + strLastUpdateClientCode + "'" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_LAST_UPDATE_CLIENT_CODE_CRITERIA, "1=1" );
        }

        if ( enumTaskStatus != null && !enumTaskStatus.isEmpty( ) )
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_STATUS_CRITERIA,
                    "status IN ('" + enumTaskStatus.stream( ).map( Enum::name ).collect( Collectors.joining( "', '" ) ) + "')" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( TASK_STATUS_CRITERIA, "1=1" );
        }

        if ( nNbDaysSinceCreated != null && nNbDaysSinceCreated > 0 )
        {
            sqlQuerySearch = sqlQuerySearch.replace( NB_DAYS_CREATION_CRITERIA, "creation_date > now() - interval '" + nNbDaysSinceCreated + "' day" );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( NB_DAYS_CREATION_CRITERIA, "1=1" );
        }

        if ( creationDateOrdering != null )
        {
            sqlQuerySearch = sqlQuerySearch.replace( ORDER_CRITERIA, " ORDER BY creation_date " + creationDateOrdering.name( ) );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( ORDER_CRITERIA, "" );
        }

        if( metadata != null && !metadata.isEmpty( ) )
        {
            sqlQuerySearch = sqlQuerySearch.replace( METADATA_CRITERIA, this.computeMetadaQuery( metadata ) );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( METADATA_CRITERIA, "1=1" );
        }

        if( limit != null )
        {
            sqlQuerySearch = sqlQuerySearch.replace( LIMIT, String.valueOf( limit ) );
        }
        else
        {
            sqlQuerySearch = sqlQuerySearch.replace( LIMIT, "100" );
        }
        return sqlQuerySearch;
    }

    @Override
    public List<Task> selectByIdAndResourceType( final String strResourceId, final String strResourceType, final Plugin plugin ) throws JsonProcessingException
    {
        final List<Task> tasks = new ArrayList<>( );
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID_AND_RESOURCE_TYPE, plugin ) )
        {
            daoUtil.setString( 1, strResourceId );
            daoUtil.setString( 2, strResourceType );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                tasks.add( this.getTask( daoUtil ) );
            }
        }
        return tasks;
    }

    @Override
    public List<Task> selectTasksListByIds( final List<Integer> listIds, final Plugin plugin) throws JsonProcessingException
    {
        final List<Task> tasks = new ArrayList<>( );
        if ( !listIds.isEmpty( ) )
        {
            StringBuilder builder = new StringBuilder( );
            for( int i = 0 ; i < listIds.size(); i++ ) {
                builder.append( "?," );
            }

            String placeHolders =  builder.deleteCharAt( builder.length( ) -1 ).toString( );
            String stmt = SQL_QUERY_SELECTALL_BY_IDS + placeHolders + ")";
            try ( final DAOUtil daoUtil = new DAOUtil( stmt, plugin ) )
            {
                int index = 1;
                for( Integer n : listIds ) {
                    daoUtil.setInt(  index++, n );
                }
                daoUtil.executeQuery( );
                while ( daoUtil.next( ) )
                {
                    tasks.add( this.getTask( daoUtil ) );
                }
            }

        }
        return tasks;
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

    private String computeMetadaQuery( Map<String, String> metadata )
    {
        return metadata.entrySet( ).stream( ).map( entry -> "string_to_array(metadata ->> '" + entry.getKey( ) + "',',') @>'{" + entry.getValue( ) + "}'" )
                .collect( Collectors.joining( " AND " ) );
    }

}
