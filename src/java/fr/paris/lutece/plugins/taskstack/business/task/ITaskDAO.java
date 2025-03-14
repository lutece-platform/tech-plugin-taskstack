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
import fr.paris.lutece.plugins.taskstack.dto.CreationDateOrdering;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ITaskDAO
{
    void insert( final Task task, final Plugin plugin ) throws JsonProcessingException;

    void update( final Task task, final Plugin plugin ) throws JsonProcessingException;

    Timestamp updateStatus( final String taskCode, final TaskStatusType newStatus, final String clientCode, Plugin plugin ) throws JsonProcessingException;

    Task load( final int nIdTask, final Plugin plugin ) throws JsonProcessingException;

    Task selectByCode( String strCode, Plugin plugin ) throws JsonProcessingException;

    List<Task> selectExpiredTask( final Plugin plugin ) throws JsonProcessingException;

    void delete( final int nIdTask, final Plugin plugin );

    List<Task> search(final String strTaskCode, final String strRessourceId, final String strRessourceType, final String strTaskType, final Date creationDate, final Date lastUpdatedate, final String strLastUpdateClientCode, final List<TaskStatusType> enumTaskStatus, final Integer nNbDaysSinceCreated,
                      final CreationDateOrdering creationDateOrdering, final int nMaxNbIdentityReturned, final Map<String, String> metadata, final Plugin plugin ) throws JsonProcessingException;

    List<Integer> searchId(final String strTaskCode, final String strRessourceId, final String strRessourceType, final String strTaskType, final Date creationDate, final Date lastUpdatedate, final String strLastUpdateClientCode, final List<TaskStatusType> enumTaskStatus, final Integer nNbDaysSinceCreated,
                      final CreationDateOrdering creationDateOrdering, final int nMaxNbIdentityReturned, final Map<String, String> metadata, final Plugin plugin ) throws JsonProcessingException;

    List<Task> selectByIdAndResourceType( final String strResourceId, final String strResourceType, final Plugin plugin ) throws JsonProcessingException;

    List<Task> selectTasksListByIds( final List<Integer> listId, final Plugin plugin ) throws JsonProcessingException;
}
