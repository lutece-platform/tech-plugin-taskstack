package fr.paris.lutece.plugins.taskstack.service;

import fr.paris.lutece.plugins.taskstack.business.taskright.TaskRight;
import fr.paris.lutece.plugins.taskstack.business.taskright.TaskRightHome;
import fr.paris.lutece.plugins.taskstack.dto.DtoMapper;
import fr.paris.lutece.plugins.taskstack.dto.TaskRightDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.rs.request.common.RequestAuthor;
import fr.paris.lutece.plugins.taskstack.util.Constants;
import fr.paris.lutece.util.sql.TransactionManager;

import java.util.ArrayList;
import java.util.List;

public class TaskRightService
{

    private static TaskRightService _instance = new TaskRightService( );

    public static TaskRightService instance ( )
    {
        if( _instance == null )
        {
            _instance = new TaskRightService( );
        }
        return _instance;
    }

    public void create (final TaskRightDto taskDto, final RequestAuthor author, final String clientCode) throws TaskStackException
    {
        TaskRight taskRight = DtoMapper.clientTaskDtoToClientTask(taskDto);
        TransactionManager.beginTransaction( null );
        try
        {
            TaskRightHome.insert(taskRight);
            TransactionManager.commitTransaction( null );
        }
        catch( final Exception e )
        {
            TransactionManager.rollBack( null );
            throw new TaskStackException( Constants.PROPERTY_REST_ERROR_DURING_TREATMENT, e );
        }
    }

    public void delete (final TaskRightDto taskDto , final RequestAuthor author, final String clientCode) throws TaskStackException
    {
        TaskRight taskRight = DtoMapper.clientTaskDtoToClientTask(taskDto);
        TransactionManager.beginTransaction( null );

        try
        {
            TaskRightHome.delete(taskRight);
            TransactionManager.commitTransaction( null );

        }
        catch( final Exception e )
        {
            TransactionManager.rollBack( null );
            throw new TaskStackException( Constants.PROPERTY_REST_ERROR_DURING_TREATMENT, e );
        }
    }

    public List<TaskRightDto> partialSearch(final String granteeClientCode, final RequestAuthor author, final String clientCode) throws TaskStackException
    {
        List<TaskRightDto> result = new ArrayList<TaskRightDto>( );

        try
        {
            List<TaskRight> taskRightList = TaskRightHome.getListClientTaskWithGranteeClientCode(granteeClientCode);

            taskRightList.forEach(clientTask ->  result.add(DtoMapper.clientTaskToClientTaskDto(clientTask)));
        }
        catch( final Exception e )
        {
            throw new TaskStackException( Constants.PROPERTY_REST_ERROR_DURING_TREATMENT, e );
        }

        return result;
    }

    public List<TaskRightDto> CompleteSearch(final String granteeClientCode, final String authorizedClientCode, final RequestAuthor author, final String clientCode) throws TaskStackException
    {
        List<TaskRightDto> result = new ArrayList<TaskRightDto>( );

        try
        {
            List<TaskRight> taskRightList = TaskRightHome.getListClientTaskWithAllCodes( granteeClientCode, authorizedClientCode );

            taskRightList.forEach(clientTask ->  result.add(DtoMapper.clientTaskToClientTaskDto(clientTask)));
        }
        catch( final Exception e )
        {
            throw new TaskStackException( Constants.PROPERTY_REST_ERROR_DURING_TREATMENT, e );
        }

        return result;
    }
}
