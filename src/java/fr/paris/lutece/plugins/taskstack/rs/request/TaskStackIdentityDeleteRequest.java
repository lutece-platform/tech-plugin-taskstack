package fr.paris.lutece.plugins.taskstack.rs.request;

import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusFactory;
import fr.paris.lutece.plugins.taskstack.rs.request.common.AbstractTaskStackRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.common.TaskStackRequestValidator;
import fr.paris.lutece.plugins.taskstack.service.TaskRightService;
import fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseDto;
import fr.paris.lutece.plugins.taskstack.rs.dto.TaskRightDeleteRequest;

public class TaskStackIdentityDeleteRequest extends AbstractTaskStackRequest
{

    TaskRightDeleteRequest _clientTaskRequest;

    public TaskStackIdentityDeleteRequest(String strClientCode, String strAppCode, String authorName,
                                          String authorType) throws TaskStackException
    {
        super(strClientCode, authorName, authorType);
    }

    public TaskStackIdentityDeleteRequest(TaskRightDeleteRequest clientTaskRequest, String strClientCode,
                                          String strAppCode, String authorName,
                                          String authorType) throws TaskStackException
    {
        super(strClientCode, authorName, authorType);
        _clientTaskRequest = clientTaskRequest;
    }

    @Override
    protected void validateSpecificRequest() throws TaskStackException
    {
        TaskStackRequestValidator.instance( ).checkClientTask(_clientTaskRequest.getTaskRight());
    }

    @Override
    protected ResponseDto doSpecificRequest() throws TaskStackException
    {
        ResponseDto response = new ResponseDto( );

        TaskRightService.instance().delete( _clientTaskRequest.getTaskRight(), _author, _strClientCode );
        response.setStatus( ResponseStatusFactory.success( ) );

        return response;
    }
}
