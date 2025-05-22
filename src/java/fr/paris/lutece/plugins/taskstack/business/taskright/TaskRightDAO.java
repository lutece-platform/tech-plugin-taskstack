package fr.paris.lutece.plugins.taskstack.business.taskright;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskRightDAO implements ITaskRightDAO
{

    public static final String BEAN_NAME = "taskstack.task.right.dao";

    //Queries
    private static final String SQL_QUERY_INSERT = "INSERT INTO identitystore_client_code_task_right (authorized_client_code, grantee_client_code, task_type) VALUES (?, ?, ?)";
    private static final String SQL_QUERY_SELECT = "SELECT authorized_client_code, grantee_client_code, task_type FROM identitystore_client_code_task_right WHERE grantee_client_code = ?";
    private static final String SQL_QUERY_SELECT_ALL = "SELECT authorized_client_code, grantee_client_code, task_type FROM identitystore_client_code_task_right";
    private static final String SQL_QUERY_SELECT_WITH_ALL_CODES = "SELECT authorized_client_code, grantee_client_code, task_type FROM identitystore_client_code_task_right WHERE authorized_client_code = ? AND grantee_client_code = ?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM identitystore_client_code_task_right WHERE authorized_client_code = ? AND grantee_client_code = ? AND task_type = ?";

    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String AUTORIZED_CLIENT_CODE = " authorized_client_code = '";
    private static final String GRANTEE_CLIENT_CODE = " grantee_client_code = '";
    private static final String TASK_TYPE = " task_type = '";
    private static final String ORDER_BY = " ORDER BY authorized_client_code, grantee_client_code, task_type ";

    @Override
    public void insert(TaskRight taskRight, Plugin plugin)
    {
        try( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, taskRight.getAuthorizedClientCode( ) );
            daoUtil.setString( nIndex++, taskRight.getGranteeClientCode( ) );
            daoUtil.setString( nIndex, taskRight.getTaskType( ) );

            daoUtil.executeUpdate();
        }
    }

    @Override
    public void delete(TaskRight taskRight, Plugin plugin)
    {
        try( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, taskRight.getAuthorizedClientCode( ) );
            daoUtil.setString( nIndex++, taskRight.getGranteeClientCode( ) );
            daoUtil.setString( nIndex, taskRight.getTaskType( ) );

            daoUtil.executeUpdate();
        }
    }

    @Override
    public List<TaskRight> selectWithAllCodes(String granteeClientCode, String authorizedClientCode, Plugin plugin)
    {
        final List<TaskRight> taskRights = new ArrayList<TaskRight>( );
        try( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_WITH_ALL_CODES, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, authorizedClientCode );
            daoUtil.setString( nIndex, granteeClientCode );

            daoUtil.executeQuery();
            while ( daoUtil.next( ) )
            {
                taskRights.add( this.getClientTask( daoUtil ) );
            }
        }
        return taskRights;
    }

    @Override
    public List<TaskRight> selectWithGranteeClientCode(String granteeClientCode, Plugin plugin)
    {
        final List<TaskRight> taskRights = new ArrayList<>( );
        try( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex, granteeClientCode );

            daoUtil.executeQuery();
            while ( daoUtil.next( ) )
            {
                taskRights.add( this.getClientTask( daoUtil ) );
            }
        }
        return taskRights;
    }

    @Override
    public List<TaskRight> getSearchTaskRight(final String granteeClientCode, final String authorizedClientCode, final String taskType, final Plugin plugin)
    {
        String sql = SQL_QUERY_SELECT_ALL;

        if(StringUtils.isNotBlank( granteeClientCode )
                || StringUtils.isNotBlank( authorizedClientCode )
                || StringUtils.isNotBlank( taskType ))
        {
            boolean onlyOne = true;
            sql = sql.concat( WHERE );
            if(StringUtils.isNotBlank( granteeClientCode ))
            {
                sql = sql.concat( GRANTEE_CLIENT_CODE ).concat( granteeClientCode ).concat("'");
                onlyOne = false;
            }
            if(StringUtils.isNotBlank( authorizedClientCode ))
            {
                if( !onlyOne)
                {
                    sql = sql.concat( AND );
                }
                sql = sql.concat( AUTORIZED_CLIENT_CODE ).concat( authorizedClientCode ).concat("'");
                onlyOne = false;
            }
            if(StringUtils.isNotBlank( taskType ))
            {
                if( !onlyOne)
                {
                    sql = sql.concat( AND );
                }
                sql = sql.concat( TASK_TYPE ).concat( taskType ).concat("'");
            }
        }
        sql = sql.concat( ORDER_BY );

        final List<TaskRight> taskRights = new ArrayList<>( );
        try( final DAOUtil daoUtil = new DAOUtil( sql, plugin ) )
        {
            daoUtil.executeQuery();
            while ( daoUtil.next( ) )
            {
                taskRights.add( this.getClientTask( daoUtil ) );
            }
        }
        return taskRights;
    }

    private TaskRight getClientTask(DAOUtil daoUtil )
    {
        final TaskRight taskRight = new TaskRight( );

        int nIndex = 1;
        taskRight.setAuthorizedClientCode( daoUtil.getString( nIndex++ ) );
        taskRight.setGranteeClientCode( daoUtil.getString( nIndex++ ) );
        taskRight.setTaskType( daoUtil.getString( nIndex ) );

        return taskRight;
    }
}
