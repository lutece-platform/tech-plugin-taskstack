/*
 * Copyright (c) 2002-2024, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.taskstack.business.task;

import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.service.TaskStackPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;

public class TaskChangeHome
{
    private static final TaskChangeDAO _taskDao = SpringContextService.getBean( TaskChangeDAO.BEAN_NAME );
    private static final Plugin _plugin = PluginService.getPlugin( TaskStackPlugin.PLUGIN_NAME );

    public static TaskChange get( final int taskChangeId ) throws TaskStackException
    {
        return _taskDao.load( taskChangeId, _plugin );
    }

    public static TaskChange create( final TaskChange taskChange ) throws TaskStackException
    {
        _taskDao.insert( taskChange, _plugin );
        return taskChange;
    }

    public static TaskChange update( final TaskChange taskChange ) throws TaskStackException
    {
        _taskDao.update( taskChange, _plugin );
        return taskChange;
    }

    public static void delete( final int taskChangeId ) throws TaskStackException
    {
        _taskDao.delete( taskChangeId, _plugin );
    }

    public static void deleteAllByTaskId( final int taskId ) throws TaskStackException
    {
        _taskDao.deleteAllByTaskId( taskId, _plugin );
    }

    public static void delete( final TaskChange taskChange ) throws TaskStackException
    {
        _taskDao.delete( taskChange.getId( ), _plugin );
    }

    public static List<TaskChange> getHistory( final String strTaskCode )
    {
        return _taskDao.selectHistory( strTaskCode, _plugin );
    }
}
