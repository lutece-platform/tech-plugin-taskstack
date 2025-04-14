package fr.paris.lutece.plugins.taskstack.web;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;
import fr.paris.lutece.plugins.taskstack.consumer.ProvidedTaskType;
import fr.paris.lutece.plugins.taskstack.csv.Batch;
import fr.paris.lutece.plugins.taskstack.csv.CsvTaskService;
import fr.paris.lutece.plugins.taskstack.dto.CreationDateOrdering;
import fr.paris.lutece.plugins.taskstack.dto.TaskChangeDto;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.service.TaskService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.url.UrlItem;

@Controller( controllerJsp = "TaskSearch.jsp", controllerPath = "jsp/admin/plugins/taskstack/", right = "TASKSTACK_MANAGEMENT" )
public class TaskSearchJspBean extends MVCAdminJspBean
{

    //Messages
    protected static final String MESSAGE_DATE_ERROR = "#i18n{taskstack.message.date.error}";
    protected static final String MESSAGE_TASK_RECUPERATION_ERROR = "#i18n{taskstack.message.task.recuperation.error}";
    protected static final String MESSAGE_TASK_CHANGE_RECUPERATION_ERROR = "#i18n{taskstack.message.task.change.recuperation.error}";

    //Templates
    private static final String TEMPLATE_TASK_SEARCH = "/admin/plugins/taskstack/task_search.html";
    private static final String TEMPLATE_TASK_HISTORY = "/admin/plugins/taskstack/task_history.html";

    //Parameters

    //Properties
    private static final String PROPERTY_PAGE_TITLE_TASK_SEARCH = "";
    private static final String PROPERTY_PAGE_TITLE_TASK_HISTORY = "";
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "taskstack.listItems.itemsPerPage";

    //Markers
    private static final String JSP_TASK_STACK = "jsp/admin/plugins/taskstack/TaskSearch.jsp";
    private static final String MARK_STACK_TASK_LIST = "stack_task_list";
    private static final String MARK_STACK_TASK_LIST_TIMESTAMP = "stack_task_list_timestamp";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_TASK_TYPE_LIST = "task_type_list";

    //Views
    private static final String VIEW_TASK_SEARCH = "viewTaskSearch";
    private static final String VIEW_TASK_HISTORY = "viewTaskHistory";

    //Actions
    private static final String ACTION_EXPORT_TASK_STACK = "exportTaskStack";

    //Infos
    private static final String QUERY_PARAM_TASK_CODE = "task_code";
    private static final String QUERY_PARAM_RESOURCE_ID = "resource_id";
    private static final String QUERY_PARAM_RESOURCE_TYPE = "resource_type";
    private static final String QUERY_PARAM_TASK_TYPE = "task_type";
    private static final String QUERY_PARAM_CREATION_DATE = "creation_date";
    private static final String QUERY_PARAM_AUTHOR_LAST_UPDATE_DATE = "last_update_date";
    private static final String QUERY_PARAM_AUTHOR_LAST_UPDATE_CLIENT_CODE = "last_update_client_code";
    private static final String QUERY_PARAM_TASK_STATUS = "task_status";
    private static final String QUERY_PARAM_CUID_LINK = "cuid_link";
    private static final String CREATION_DATE_ORDER = "DESC";
    private static final int BATCH_PARTITION_SIZE = AppPropertiesService.getPropertyInt( "identitystore.export.batch.size", 100 );
    private static final String RESOURCE_SEARCH_LINK = AppPropertiesService.getProperty("taskstack.search.resource.link", "");

    //Variables
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;

    // Session variable to store working values
    private List<String> _listQuery = new ArrayList<>( );
    private List<TaskDto> _stackTaskList = new ArrayList<>();

    @View( value = VIEW_TASK_SEARCH, defaultView = true )
    public String getViewTaskSearch( HttpServletRequest request )
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

        List<Integer> listTaskIds = new ArrayList<>( );
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
                addError( MESSAGE_DATE_ERROR + e.getMessage());
                return redirectView(request, VIEW_TASK_SEARCH);
            }
            try
            {
                if(StringUtils.isNotBlank(taskStatus))
                {
                    final List<TaskStatusType> taskStatusTypes = new ArrayList<>( );
                    taskStatusTypes.add(TaskStatusType.valueOf(taskStatus));
                    listTaskIds.addAll(TaskService.instance().searchId(taskCode, resourceId, resourceType, taskType, creationDate, lastUpdateDate, lastUpdateClientCode, taskStatusTypes, null, CreationDateOrdering.valueOf(CREATION_DATE_ORDER), null, 0));
                }
                else
                {
                    listTaskIds.addAll(TaskService.instance().searchId(taskCode, resourceId, resourceType, taskType, creationDate, lastUpdateDate, lastUpdateClientCode, null, null, CreationDateOrdering.valueOf(CREATION_DATE_ORDER), null, 0));
                }
            } catch (TaskStackException e)
            {
                addError( MESSAGE_TASK_RECUPERATION_ERROR + e.getMessage());
                return redirectView(request, VIEW_TASK_SEARCH);
            }
        }
        else
        {
            try
            {
                listTaskIds.addAll(TaskService.instance().searchId("", "", "", "", null, null, "", null, null, CreationDateOrdering.valueOf(CREATION_DATE_ORDER), null, 0));
            }
            catch (TaskStackException e)
            {
                addError( MESSAGE_TASK_RECUPERATION_ERROR + e.getMessage());
                return redirectView(request, VIEW_TASK_SEARCH);
            }
        }
        List<String> taskTypeList =  ProvidedTaskType.values ( );
        try
        {
            Map<String, Object> model = getPaginatedListModel(request, listTaskIds);
            model.put( QUERY_PARAM_TASK_CODE, taskCode );
            model.put( QUERY_PARAM_RESOURCE_ID, resourceId );
            model.put( QUERY_PARAM_RESOURCE_TYPE, resourceType );
            model.put( QUERY_PARAM_TASK_TYPE, taskType );
            model.put( QUERY_PARAM_CREATION_DATE, strCreationDate );
            model.put( QUERY_PARAM_AUTHOR_LAST_UPDATE_DATE, strLastUpdateDate );
            model.put( QUERY_PARAM_AUTHOR_LAST_UPDATE_CLIENT_CODE, lastUpdateClientCode );
            model.put( QUERY_PARAM_TASK_STATUS, taskStatus );
            model.put( QUERY_PARAM_CUID_LINK, RESOURCE_SEARCH_LINK );
            model.put( MARK_TASK_TYPE_LIST, taskTypeList);

            return getPage(PROPERTY_PAGE_TITLE_TASK_SEARCH, TEMPLATE_TASK_SEARCH, model );
        }
                catch (TaskStackException e)
        {
            addError( MESSAGE_TASK_RECUPERATION_ERROR + e.getMessage());
            return redirectView(request, VIEW_TASK_SEARCH);
        }
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
                addError( MESSAGE_TASK_CHANGE_RECUPERATION_ERROR + e.getMessage( ) );
                return redirectView( request, VIEW_TASK_SEARCH );
            }
        }
        Map<String, Object> model = getModel();
        model.put( "stack_task_history", stackTaskHistoryList );

        return getPage( PROPERTY_PAGE_TITLE_TASK_HISTORY, TEMPLATE_TASK_HISTORY, model );
    }

    @Action( ACTION_EXPORT_TASK_STACK )
    public void doExportIdentities( HttpServletRequest request )
    {
        try
        {

            final Batch<TaskDto> batches = Batch.ofSize( _stackTaskList, BATCH_PARTITION_SIZE );

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            final ZipOutputStream zipOut = new ZipOutputStream( outputStream );

            int i = 0;
            for ( final List<TaskDto> batch : batches )
            {
                final byte [ ] bytes = CsvTaskService.instance( ).writeTask( batch );
                final ZipEntry zipEntry = new ZipEntry( "taskStack-" + ++i + ".csv" );
                zipEntry.setSize( bytes.length );
                zipOut.putNextEntry( zipEntry );
                zipOut.write( bytes );
            }
            zipOut.closeEntry( );
            zipOut.close( );
            this.download( outputStream.toByteArray( ), "taskStack.zip", "application/zip" );
        }
        catch( Exception e )
        {
            addError( e.getMessage( ) );
            redirectView( request, VIEW_TASK_SEARCH );
        }
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

    private Map<String, Object> getPaginatedListModel( HttpServletRequest request, List<Integer> list ) throws TaskStackException
    {

        int nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, nDefaultItemsPerPage );

        UrlItem url = new UrlItem( JSP_TASK_STACK );
        final Map<String, String> queryParameters = this.getQueryParameters( request );
        queryParameters.forEach( url::addParameter );
        String strUrl = url.getUrl(  );

        // PAGINATOR
        LocalizedPaginator<Integer> paginator = new LocalizedPaginator<>( list, _nItemsPerPage, strUrl, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale(  ) );
        _stackTaskList = getTasksListByIds( paginator.getPageItems());
        Map< String, List< Long > > taskTimestamp = new HashMap< String, List< Long > >();
        _stackTaskList.forEach(task ->
        {
            List<Long> taskTimestampList = new ArrayList<>( );
            taskTimestampList.add(task.getCreationDate().getTime());
            taskTimestampList.add(task.getLastUpdateDate().getTime());
         taskTimestamp.put(task.getTaskCode(), taskTimestampList );
        });
        Map<String, Object> model = getModel(  );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_STACK_TASK_LIST, _stackTaskList);
        model.put( MARK_STACK_TASK_LIST_TIMESTAMP, taskTimestamp);
        return model;
    }

    private List<TaskDto> getTasksListByIds( List<Integer> listId) throws TaskStackException
    {
        List<TaskDto> taskDtoList = TaskService.instance( ).getTasksListByIds(listId);
        taskDtoList.sort(Comparator.comparing(TaskDto::getCreationDate));
        Collections.reverse(taskDtoList);
        return taskDtoList;
    }
}