package fr.paris.lutece.plugins.taskstack.web;

import fr.paris.lutece.plugins.taskstack.business.taskright.TaskRight;
import fr.paris.lutece.plugins.taskstack.business.taskright.TaskRightHome;
import fr.paris.lutece.plugins.taskstack.business.taskright.TaskRightType;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.service.TaskService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.url.UrlItem;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller( controllerJsp = "TaskRight.jsp", controllerPath = "jsp/admin/plugins/taskstack/", right = "TASKSTACK_MANAGEMENT" )
public class TaskRightJspBean extends MVCAdminJspBean
{
    //Messages
    protected static final String MESSAGE_TASK_RECUPERATION_ERROR = "#i18n{taskstack.message.task.recuperation.error}";

    //Templates
    private static final String TEMPLATE_TASK_RIGHT = "/admin/plugins/taskstack/task_right.html";
    private static final String TEMPLATE_CREATE_TASK_RIGHT = "/admin/plugins/taskstack/create_task_right.html";

    //Views
    private static final String VIEW_TASK_RIGHT = "viewTaskRight";
    private static final String VIEW_CREATE_TASK_RIGHT = "createTaskRight";

    // Actions
    private static final String ACTION_CREATE_TASK_RIGHT = "createTaskRight";
    private static final String ACTION_DELETE_TASK_RIGHT = "deleteTaskRight";
    private static final String ACTION_CONFIRM_DELETE_TASK_RIGHT = "confirmDeleteTaskRight";

    //Infos
    private static final String QUERY_PARAM_AUTORIZED_CLIENT_CODE = "authorized_client_code";
    private static final String QUERY_PARAM_GRANTEE_CLIENT_CODE = "grantee_client_code";
    private static final String QUERY_PARAM_TASK_TYPE = "task_type";
    private static final String INFO_TASK_RIGHT_CREATED = "taskstack.info.taskright.created";
    private static final String INFO_TASK_RIGHT_DELETED = "taskstack.info.taskright.deleted";

    //Properties
    private static final String PROPERTY_PAGE_TITLE_TASK_RIGHT = "";
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "taskstack.listItems.itemsPerPage";
    private static final String MESSAGE_CONFIRM_REMOVE_TASK_RIGHT = "taskstack.info.taskright.confirm.delete";

    //Markers
    private static final String JSP_TASK_RIGHT = "jsp/admin/plugins/taskstack/TaskRight.jsp";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_STACK_RIGHT_LIST = "task_right_list";
    private static final String MARK_TASK_TYPE_LIST = "task_type_list";

    // Variables
    private List<String> _listQuery = new ArrayList<>( );
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;
    private TaskRight _taskRight;

    @View( value = VIEW_TASK_RIGHT, defaultView = true )
    public String getViewTaskSearch( HttpServletRequest request )
    {
        _taskRight = new TaskRight();
        initListQuery();
        final Map<String, String> queryParameters = this.getQueryParameters( request );
        final String granteeClientCode = queryParameters.get(QUERY_PARAM_GRANTEE_CLIENT_CODE) != null ?
                queryParameters.get(QUERY_PARAM_GRANTEE_CLIENT_CODE) : "";
        final String authorizedClientCode = queryParameters.get( QUERY_PARAM_AUTORIZED_CLIENT_CODE ) != null ?
                queryParameters.get( QUERY_PARAM_AUTORIZED_CLIENT_CODE ) : "";
        final String taskType = queryParameters.get(QUERY_PARAM_TASK_TYPE) != null ?
                queryParameters.get(QUERY_PARAM_TASK_TYPE) : "";

        List<TaskRight> taskRightList = TaskRightHome.searchTaskRight( granteeClientCode, authorizedClientCode, taskType );
        List<String> taskTypeList = Arrays.stream(TaskRightType.values()).map(Enum::name).collect( Collectors.toList( ) );

        try {
            Map<String, Object> model = getPaginatedListModel(request, taskRightList);

            model.put( MARK_TASK_TYPE_LIST, taskTypeList);
            model.put(QUERY_PARAM_AUTORIZED_CLIENT_CODE, authorizedClientCode);
            model.put(QUERY_PARAM_GRANTEE_CLIENT_CODE, granteeClientCode);
            model.put(QUERY_PARAM_TASK_TYPE, taskType);
            return getPage(PROPERTY_PAGE_TITLE_TASK_RIGHT, TEMPLATE_TASK_RIGHT, model );
        }
        catch ( TaskStackException e)
        {
            addError( MESSAGE_TASK_RECUPERATION_ERROR + e.getMessage());
            return redirectView(request, VIEW_TASK_RIGHT);
        }
    }

    @View( value = VIEW_CREATE_TASK_RIGHT )
    public String getViewCreateTaskRight ( HttpServletRequest request )
    {
        List<String> taskTypeList = Arrays.stream(TaskRightType.values()).map(Enum::name).collect( Collectors.toList( ) );

        Map<String, Object> model = getModel(  );
        model.put( MARK_TASK_TYPE_LIST, taskTypeList);

        return getPage(PROPERTY_PAGE_TITLE_TASK_RIGHT, TEMPLATE_CREATE_TASK_RIGHT, model );
    }

    @Action( ACTION_CREATE_TASK_RIGHT )
    public String doCreateTaskRight( HttpServletRequest request )
    {
        this.populateTaskRight( _taskRight, request);

        TaskRightHome.insert( _taskRight );
        addInfo( INFO_TASK_RIGHT_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_TASK_RIGHT );
    }


    @Action( ACTION_CONFIRM_DELETE_TASK_RIGHT )
    public String getConfirmRemoveCountry( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( getActionUrl( ACTION_DELETE_TASK_RIGHT ) );
        url.addParameter(QUERY_PARAM_AUTORIZED_CLIENT_CODE, request.getParameter(QUERY_PARAM_AUTORIZED_CLIENT_CODE));
        url.addParameter(QUERY_PARAM_GRANTEE_CLIENT_CODE, request.getParameter(QUERY_PARAM_GRANTEE_CLIENT_CODE));
        url.addParameter(QUERY_PARAM_TASK_TYPE, request.getParameter(QUERY_PARAM_TASK_TYPE));

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_TASK_RIGHT, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    @Action( ACTION_DELETE_TASK_RIGHT )
    public String doDeleteTaskRight( HttpServletRequest request )
    {
        this.populateTaskRight( _taskRight, request);

        TaskRightHome.delete( _taskRight );
        addInfo( INFO_TASK_RIGHT_DELETED, getLocale(  ) );

        return redirectView( request, VIEW_TASK_RIGHT );
    }

    private void initListQuery( )
    {
        _listQuery = new ArrayList<>();
        _listQuery.add(QUERY_PARAM_AUTORIZED_CLIENT_CODE);
        _listQuery.add(QUERY_PARAM_GRANTEE_CLIENT_CODE);
        _listQuery.add(QUERY_PARAM_TASK_TYPE);
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

    private Map<String, Object> getPaginatedListModel( HttpServletRequest request, List<TaskRight> list ) throws TaskStackException
    {

        int nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, nDefaultItemsPerPage );

        UrlItem url = new UrlItem( JSP_TASK_RIGHT );
        final Map<String, String> queryParameters = this.getQueryParameters( request );
        queryParameters.forEach( url::addParameter );
        String strUrl = url.getUrl(  );

        // PAGINATOR
        LocalizedPaginator<TaskRight> paginator = new LocalizedPaginator<>( list, _nItemsPerPage, strUrl, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale(  ) );
        List<TaskRight> taskRightList =  paginator.getPageItems();
        Map<String, Object> model = getModel(  );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_STACK_RIGHT_LIST, taskRightList);
        return model;
    }

    private void populateTaskRight( TaskRight taskRight, HttpServletRequest request )
    {
        taskRight.setAuthorizedClientCode(request.getParameter(QUERY_PARAM_AUTORIZED_CLIENT_CODE));
        taskRight.setGranteeClientCode(request.getParameter(QUERY_PARAM_GRANTEE_CLIENT_CODE));
        taskRight.setTaskType( request.getParameter(QUERY_PARAM_TASK_TYPE));
    }
}
