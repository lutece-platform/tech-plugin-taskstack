package fr.paris.lutece.plugins.taskstack.business.taskright;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.paris.lutece.plugins.taskstack.service.TaskStackPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;

public class TaskRightHome
{
    private static final TaskRightDAO _clientTaskDao = SpringContextService.getBean( TaskRightDAO.BEAN_NAME );
    private static final Plugin _plugin = PluginService.getPlugin( TaskStackPlugin.PLUGIN_NAME );

    public static void insert( TaskRight taskRight)
    {
        _clientTaskDao.insert(taskRight, _plugin );
    }

    public static void delete( TaskRight taskRight)
    {
        _clientTaskDao.delete(taskRight, _plugin );
    }

    public static List<TaskRight> getListClientTaskWithAllCodes(String granteeClientCode, String authorizedClientCode)
    {
        return _clientTaskDao.selectWithAllCodes( granteeClientCode, authorizedClientCode, _plugin );
    }

    public static List<TaskRight> getListClientTaskWithGranteeClientCode(String granteeClientCode )
    {
        return _clientTaskDao.selectWithGranteeClientCode( granteeClientCode, _plugin );
    }

    public static List<TaskRight> searchTaskRight( String granteeClientCode, String authorizedClientCode, String taskType )
    {
        return _clientTaskDao.getSearchTaskRight( granteeClientCode, authorizedClientCode, taskType, _plugin );
    }

}
