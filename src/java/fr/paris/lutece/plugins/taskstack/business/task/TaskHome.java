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

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.service.TaskStackPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class TaskHome
{
    private static final TaskDAO _taskDao = SpringContextService.getBean( TaskDAO.BEAN_NAME );
    private static final Plugin _plugin = PluginService.getPlugin( TaskStackPlugin.PLUGIN_NAME );

    public static Task get( final int taskId ) throws TaskStackException
    {
        try
        {
            return _taskDao.load( taskId, _plugin );
        }
        catch( JsonProcessingException e )
        {
            throw new TaskStackException( "An error occurred trying to get a Task: ", e );
        }
    }

    public static Task create( final Task task ) throws TaskStackException
    {
        try
        {
            _taskDao.insert( task, _plugin );
        }
        catch( JsonProcessingException e )
        {
            throw new TaskStackException( "An error occurred trying to create a Task: ", e );
        }
        return task;
    }

    public static Task update( final Task task ) throws TaskStackException
    {
        try
        {
            _taskDao.update( task, _plugin );
        }
        catch( JsonProcessingException e )
        {
            throw new TaskStackException( "An error occurred trying to update a Task: ", e );
        }
        return task;
    }

    public static void delete( final int taskId ) throws TaskStackException
    {
        _taskDao.delete( taskId, _plugin );
    }

    public static void delete( final Task task ) throws TaskStackException
    {
        _taskDao.delete( task.getId( ), _plugin );
    }

}
