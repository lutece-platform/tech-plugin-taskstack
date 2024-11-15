package fr.paris.lutece.plugins.taskstack.web;

import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;
import fr.paris.lutece.plugins.taskstack.dto.CreationDateOrdering;
import fr.paris.lutece.plugins.taskstack.dto.TaskChangeDto;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.service.TaskService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller( controllerJsp = "TaskSearch.jsp", controllerPath = "jsp/admin/plugins/taskstack/", right = "TASKSTACK_MANAGEMENT" )
public class TaskSearchJspBean extends MVCAdminJspBean
{

    //Templates
    private static final String TEMPLATE_TASK_SEARCH = "/admin/plugins/taskstack/task_search.html";
    private static final String TEMPLATE_TASK_HISTORY = "/admin/plugins/taskstack/task_history.html";

    //Parameters

    //Properties
    private static final String PROPERTY_PAGE_TITLE_TASK_SEARCH = "";
    private static final String PROPERTY_PAGE_TITLE_TASK_HISTORY = "";

    //Markers

    //Views
    private static final String VIEW_TASK_SEARCH = "viewTaskSearch";
    private static final String VIEW_TASK_HISTORY = "viewTaskHistory";

    //Actions

    //Infos
    private static final String QUERY_PARAM_TASK_CODE = "task_code";
    private static final String QUERY_PARAM_RESOURCE_ID = "resource_id";
    private static final String QUERY_PARAM_RESOURCE_TYPE = "resource_type";
    private static final String QUERY_PARAM_TASK_TYPE = "task_type";
    private static final String QUERY_PARAM_CREATION_DATE = "creation_date";
    private static final String QUERY_PARAM_AUTHOR_LAST_UPDATE_DATE = "last_update_date";
    private static final String QUERY_PARAM_AUTHOR_LAST_UPDATE_CLIENT_CODE = "last_update_client_code";
    private static final String QUERY_PARAM_TASK_STATUS = "task_status";
    private static final String CREATION_DATE_ORDER = "ASC";

    // Session variable to store working values
    private List<String> _listQuery = new ArrayList<>( );

    @View( value = VIEW_TASK_SEARCH, defaultView = true )
    public String getViewIndicators( HttpServletRequest request )
    {
        initListQuery();
        final Map<String, String> queryParameters = this.getQueryParameters( request );
        final String taskCode = queryParameters.get( QUERY_PARAM_TASK_CODE ) != null ?
                queryParameters.get( QUERY_PARAM_TASK_CODE ) : "";
        final String resourceId = queryParameters.get(QUERY_PARAM_RESOURCE_ID) != null ?
                queryParameters.get(QUERY_PARAM_RESOURCE_ID) : "";
        final String resourceType = queryParameters.get(QUERY_PARAM_RESOURCE_TYPE) != null ?
                queryParameters.get(QUERY_PARAM_RESOURCE_TYPE) : "";
        final String taskType = queryParameters.get( QUERY_PARAM_TASK_TYPE ) != null ?
                StringUtils.replace(queryParameters.get( QUERY_PARAM_TASK_TYPE ).toUpperCase(), " ", "_") : "";
        final String strCreationDate = queryParameters.get( QUERY_PARAM_CREATION_DATE ) != null ?
            queryParameters.get( QUERY_PARAM_CREATION_DATE ) : "";
        final String strLastUpdateDate = queryParameters.get( QUERY_PARAM_AUTHOR_LAST_UPDATE_DATE ) != null ?
                queryParameters.get( QUERY_PARAM_AUTHOR_LAST_UPDATE_DATE ) : "";
        final String lastUpdateClientCode = queryParameters.get( QUERY_PARAM_AUTHOR_LAST_UPDATE_CLIENT_CODE ) != null ?
                queryParameters.get( QUERY_PARAM_AUTHOR_LAST_UPDATE_CLIENT_CODE ) : "";
        final String taskStatus = queryParameters.get( QUERY_PARAM_TASK_STATUS ) != null ?
                StringUtils.replace(queryParameters.get( QUERY_PARAM_TASK_STATUS ).toUpperCase(), " ", "_") : "";
        List<TaskDto> stackTaskList= new ArrayList<>();

        if(StringUtils.isNotBlank(taskCode) || StringUtils.isNotBlank(resourceId) ||
                StringUtils.isNotBlank(resourceType) || StringUtils.isNotBlank(taskType) ||
                StringUtils.isNotBlank(strCreationDate) || StringUtils.isNotBlank(strLastUpdateDate) ||
                StringUtils.isNotBlank(lastUpdateClientCode) || StringUtils.isNotBlank(taskStatus))
        {
            Date creationDate = null;
            Date lastUpdateDate = null;
            try
            {
                creationDate = StringUtils.isNotBlank(strCreationDate) ? this.convertDate(strCreationDate) : null;
                lastUpdateDate = StringUtils.isNotBlank(strLastUpdateDate) ? this.convertDate(strLastUpdateDate) : null;
            } catch (ParseException e)
            {
                addError(e.getMessage());
                return redirectView(request, VIEW_TASK_SEARCH);
            }
            try
            {
                if(StringUtils.isNotBlank(taskStatus))
                {
                    final List<TaskStatusType> taskStatusTypes = new ArrayList<>( );
                    taskStatusTypes.add(TaskStatusType.valueOf(taskStatus));
                    stackTaskList.addAll(TaskService.instance().search(taskCode, resourceId, resourceType, taskType, creationDate, lastUpdateDate, lastUpdateClientCode, taskStatusTypes, null, CreationDateOrdering.valueOf(CREATION_DATE_ORDER)));
                }
                else
                {
                    stackTaskList.addAll(TaskService.instance().search(taskCode, resourceId, resourceType, taskType, creationDate, lastUpdateDate, lastUpdateClientCode, null, null, CreationDateOrdering.valueOf(CREATION_DATE_ORDER)));
                }
            } catch (TaskStackException e)
            {
                addError(e.getMessage());
                return redirectView(request, VIEW_TASK_SEARCH);
            }
        }

        Map<String, Object> model = new HashMap<>();
        model.put( "task_code", taskCode );
        model.put( "resource_id", resourceId );
        model.put( "resource_type", resourceType );
        model.put( "task_type", taskType );
        model.put( "creation_date", strCreationDate );
        model.put( "last_update_date", strLastUpdateDate );
        model.put( "Last_update_client_code", lastUpdateClientCode );
        model.put( "task_status", taskStatus );
        model.put( "stack_task_list", stackTaskList);

        return getPage(PROPERTY_PAGE_TITLE_TASK_SEARCH, TEMPLATE_TASK_SEARCH, model );
    }

    @View( value = VIEW_TASK_HISTORY )
    public String getViewHistory( HttpServletRequest request )
    {
        final String taskCode = request.getParameter( QUERY_PARAM_TASK_CODE ) != null ?
                request.getParameter( QUERY_PARAM_TASK_CODE ) : "";
        List<TaskChangeDto> stackTaskHistoryList= new ArrayList<>( );
        if( StringUtils.isNotBlank( taskCode ) )
        {
            try
            {
                TaskDto task = TaskService.instance( ).getTask( taskCode );
                stackTaskHistoryList.addAll(task.getTaskChanges( ) );
            } catch ( TaskStackException e )
            {
                addError( e.getMessage( ) );
                return redirectView( request, VIEW_TASK_SEARCH );
            }
        }
        Map<String, Object> model = new HashMap<>( );
        model.put( "stack_task_history", stackTaskHistoryList );

        return getPage( PROPERTY_PAGE_TITLE_TASK_HISTORY, TEMPLATE_TASK_HISTORY, model );
    }

    private Map<String, String> getQueryParameters( HttpServletRequest request )
    {
        Map<String, String> queryParameters = new HashMap<>();

        _listQuery.forEach(queryParameter -> {
            final String param = request.getParameter( queryParameter );
            if ( param != null )
            {
                queryParameters.put( queryParameter, param );
            }
        });

        return queryParameters;
    }

    private void initListQuery( )
    {
        _listQuery = new ArrayList<>();
        _listQuery.add(QUERY_PARAM_TASK_CODE);
        _listQuery.add(QUERY_PARAM_RESOURCE_ID);
        _listQuery.add(QUERY_PARAM_RESOURCE_TYPE);
        _listQuery.add(QUERY_PARAM_TASK_TYPE);
        _listQuery.add(QUERY_PARAM_CREATION_DATE);
        _listQuery.add(QUERY_PARAM_AUTHOR_LAST_UPDATE_DATE);
        _listQuery.add(QUERY_PARAM_AUTHOR_LAST_UPDATE_CLIENT_CODE);
        _listQuery.add(QUERY_PARAM_TASK_STATUS);
    }

    private Date convertDate (String date) throws ParseException
    {
        if(StringUtils.isNotBlank(date))
        {
            List<String> listDate = Arrays.asList(date.replaceAll("-","/").split("/"));
            String dateFormated = listDate.get(2) + "/" + listDate.get(1) + "/" + listDate.get(0);
            return new SimpleDateFormat("dd/MM/yyyy").parse(dateFormated);
        }
        return null;
    }
}
