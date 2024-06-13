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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TaskChangeDAO implements ITaskChangeDAO
{

    // Constants
    public static final String BEAN_NAME = "taskstack-management.task.change.dao";
    private static final String SQL_QUERY_SELECT_ALL_BY_CODE = "SELECT ssc.id, ssc.id_task, ssc.author_name, ssc.author_type, ssc.client_code, ssc.status, ssc.change_type, ssc.change_date FROM stack_task_change ssc JOIN stack_task ss ON ss.id = ssc.id_task WHERE ss.code = ?";
    private static final String SQL_QUERY_SELECT = "SELECT id, id_task, author_name, author_type, client_code, status, change_type, change_date FROM stack_task_change WHERE id = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO stack_task_change ( id_task, author_name, author_type, client_code, status, change_type, change_date ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM stack_task_change WHERE id = ? ";
    private static final String SQL_QUERY_DELETE_ALL_BY_TASK_ID = "DELETE FROM stack_task_change WHERE id_task = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE stack_task_change SET id_task = ?, author_name = ?, author_type = ?, client_code = ?, status = ?, change_type = ?, change_date = ? WHERE id = ?";

    @Override
    public void insert( final TaskChange taskChange, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            this.createOrUpdateTaskChange( taskChange, daoUtil );
        }
    }

    @Override
    public void update( final TaskChange taskChange, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            this.createOrUpdateTaskChange( taskChange, daoUtil );
        }
    }

    @Override
    public TaskChange load( final int nIdTaskChange, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nIdTaskChange );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                return this.getTaskChange( daoUtil );
            }
        }

        return null;
    }

    @Override
    public void delete( final int nIdTaskChange, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nIdTaskChange );
            daoUtil.executeUpdate( );
        }
    }

    @Override
    public List<TaskChange> selectHistory( final String strTaskCode, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_BY_CODE, plugin ) )
        {
            final List<TaskChange> history = new ArrayList<>( );
            daoUtil.setString( 1, strTaskCode );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                history.add( this.getTaskChange( daoUtil ) );
            }
            return history;
        }
    }

    @Override
    public void deleteAllByTaskId( int taskId, Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL_BY_TASK_ID, plugin ) )
        {
            daoUtil.setInt( 1, taskId );
            daoUtil.executeUpdate( );
        }
    }

    private void createOrUpdateTaskChange( final TaskChange taskChange, final DAOUtil daoUtil )
    {
        int i = 0;
        daoUtil.setInt( ++i, taskChange.getIdTask( ) );
        daoUtil.setString( ++i, taskChange.getAuthorName( ) );
        daoUtil.setString( ++i, taskChange.getAuthorType( ) );
        daoUtil.setString( ++i, taskChange.getClientCode( ) );
        daoUtil.setString( ++i, taskChange.getTaskStatus( ).name( ) );
        daoUtil.setString( ++i, taskChange.getTaskChangeType( ).name( ) );
        daoUtil.setTimestamp( ++i, taskChange.getTaskChangeDate( ) );

        daoUtil.executeUpdate( );

        if ( daoUtil.nextGeneratedKey( ) )
        {
            taskChange.setId( daoUtil.getGeneratedKeyInt( 1 ) );
        }
    }

    private TaskChange getTaskChange( final DAOUtil daoUtil )
    {
        int i = 0;
        final TaskChange taskChange = new TaskChange( );
        taskChange.setId( daoUtil.getInt( ++i ) );
        taskChange.setIdTask( daoUtil.getInt( ++i ) );
        taskChange.setAuthorName( daoUtil.getString( ++i ) );
        taskChange.setAuthorType( daoUtil.getString( ++i ) );
        taskChange.setClientCode( daoUtil.getString( ++i ) );
        taskChange.setTaskStatus( TaskStatusType.valueOf( daoUtil.getString( ++i ) ) );
        taskChange.setTaskChangeType( TaskChangeType.valueOf( daoUtil.getString( ++i ) ) );
        taskChange.setTaskChangeDate( daoUtil.getTimestamp( ++i ) );
        return taskChange;
    }
}
