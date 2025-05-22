package fr.paris.lutece.plugins.taskstack.rs.request;

import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusFactory;
import fr.paris.lutece.plugins.taskstack.rs.request.common.AbstractTaskStackRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.common.TaskStackRequestValidator;
import fr.paris.lutece.plugins.taskstack.service.TaskRightService;
import fr.paris.lutece.plugins.taskstack.rs.dto.TaskRightSearchResponse;

public class TaskStackIdentityPartialSearchRequest extends AbstractTaskStackRequest
{

    String _clientCode;

    public TaskStackIdentityPartialSearchRequest(String strClientCode, String strAppCode, String authorName,
                                                 String authorType) throws TaskStackException
    {
        super(strClientCode, authorName, authorType);
    }

    public TaskStackIdentityPartialSearchRequest(String clientCode, String strClientCode,
                                                 String strAppCode, String authorName,
                                                 String authorType) throws TaskStackException
    {
        super(strClientCode, authorName, authorType);
        _clientCode = clientCode;
    }

    @Override
    protected void validateSpecificRequest() throws TaskStackException
    {
        TaskStackRequestValidator.instance( ).checkClientCode( _clientCode );
    }

    @Override
    protected TaskRightSearchResponse doSpecificRequest() throws TaskStackException
    {
        final TaskRightSearchResponse response = new TaskRightSearchResponse();

        response.setTaskRightDtoList(TaskRightService.instance().partialSearch( _clientCode, _author, _strClientCode ));
        response.setStatus( ResponseStatusFactory.success( ) );
        return response;
    }
}
