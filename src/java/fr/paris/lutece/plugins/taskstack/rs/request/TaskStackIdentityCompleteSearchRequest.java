package fr.paris.lutece.plugins.taskstack.rs.request;

import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusFactory;
import fr.paris.lutece.plugins.taskstack.rs.request.common.AbstractTaskStackRequest;
import fr.paris.lutece.plugins.taskstack.rs.request.common.TaskStackRequestValidator;
import fr.paris.lutece.plugins.taskstack.service.TaskRightService;
import fr.paris.lutece.plugins.taskstack.rs.dto.TaskRightSearchResponse;

public class TaskStackIdentityCompleteSearchRequest extends AbstractTaskStackRequest
{

    String _clientCodeSource;
    String _clientCodeUserTask;

    public TaskStackIdentityCompleteSearchRequest(String strClientCode, String strAppCode, String authorName,
                                                  String authorType) throws TaskStackException
    {
        super(strClientCode, authorName, authorType);
    }

    public TaskStackIdentityCompleteSearchRequest(String clientCodeSource, String clientCodeUserTask, String strClientCode,
                                                  String strAppCode, String authorName,
                                                  String authorType) throws TaskStackException
    {
        super(strClientCode, authorName, authorType);
        _clientCodeSource = clientCodeSource;
        _clientCodeUserTask = clientCodeUserTask;
    }

    @Override
    protected void validateSpecificRequest() throws TaskStackException
    {
        TaskStackRequestValidator.instance( ).checkClientCode( _clientCodeSource );
        TaskStackRequestValidator.instance( ).checkClientCode( _clientCodeUserTask );
    }

    @Override
    protected TaskRightSearchResponse doSpecificRequest() throws TaskStackException
    {
        final TaskRightSearchResponse response = new TaskRightSearchResponse();

        response.setTaskRightDtoList(TaskRightService.instance().CompleteSearch( _clientCodeSource, _clientCodeUserTask, _author, _strClientCode ));
        response.setStatus( ResponseStatusFactory.success( ) );

        return response;
    }
}
