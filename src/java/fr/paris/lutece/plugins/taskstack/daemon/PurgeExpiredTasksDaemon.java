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
package fr.paris.lutece.plugins.taskstack.daemon;

import fr.paris.lutece.plugins.taskstack.business.task.Task;
import fr.paris.lutece.plugins.taskstack.business.task.TaskHome;
import fr.paris.lutece.plugins.taskstack.service.TaskService;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class PurgeExpiredTasksDaemon extends Daemon
{
    private static final String RETENTION = AppPropertiesService.getProperty( "taskstack.task.retention.days.number", "" );

    @Override
    public void run( )
    {
        if ( StringUtils.isNotBlank( RETENTION ) )
        {
            try
            {
                final List<Task> expiredTasks = TaskHome.getExpiredTasks( RETENTION );
                for ( final Task task : expiredTasks )
                {
                    AppLogService.info( "Removing expired task " + task.getTaskCode( ) + " for resource ID " + task.getResourceId( ) + " and status "
                            + task.getTaskStatus( ).name( ) );
                    TaskService.instance( ).deleteTask( task );
                }
                if ( expiredTasks.isEmpty( ) )
                {
                    AppLogService.info( "No expired task found. " );
                }
            }
            catch( final Exception e )
            {
                AppLogService.error( "Could not purge expired tasks due to: ", e );
            }
        }
        else
        {
            AppLogService.error( "TaskPurgeDaemon: Task retention days not set. Please use taskstack.task.retention.days.number property." );
        }
    }
}
