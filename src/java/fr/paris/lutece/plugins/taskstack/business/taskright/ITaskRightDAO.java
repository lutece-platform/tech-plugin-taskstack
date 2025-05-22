package fr.paris.lutece.plugins.taskstack.business.taskright;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;

public interface ITaskRightDAO
{
    void insert(final TaskRight client, final Plugin plugin);

    void delete(final TaskRight client, final Plugin plugin);

    List<TaskRight> selectWithAllCodes(final String granteeClientCode, final String authorizedClientCode, final Plugin plugin);

    List<TaskRight> selectWithGranteeClientCode(final String granteeClientCode, final Plugin plugin);

    List<TaskRight> getSearchTaskRight(final String granteeClientCode, final String authorizedClientCode, final String tasktype, final Plugin plugin);
}
