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
package fr.paris.lutece.plugins.taskstack.csv;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * This service id dedicated to CSV format of {@link TaskDto}
 */
public class CsvTaskService
{

    private static CsvTaskService instance;

    public static CsvTaskService instance( )
    {
        if ( instance == null )
        {
            instance = new CsvTaskService( );
        }
        return instance;
    }

    public byte [ ] writeTask( final List<TaskDto> identityTaskDtos ) throws TaskStackException
    {
        try
        {
            final ByteArrayOutputStream out = new ByteArrayOutputStream( );
            final Writer writer = new OutputStreamWriter( out );
            final CustomMappingStrategy<CsvTaskStack> mappingStrategy = new CustomMappingStrategy<>( );
            mappingStrategy.setType( CsvTaskStack.class );
            final StatefulBeanToCsv<CsvTaskStack> identitiesWriter = new StatefulBeanToCsvBuilder<CsvTaskStack>( writer ).withMappingStrategy( mappingStrategy )
                    .withOrderedResults( true ).withSeparator( Constants.CSV_SEPARATOR ).withApplyQuotesToAll( false ).build( );
            identitiesWriter.write( this.extractCsvTask( identityTaskDtos ) );
            writer.close( );
            return out.toByteArray( );
        }
        catch( Exception e )
        {
            throw new TaskStackException( "An error occurred while exporting csv identities. ", e );
        }
    }

    public List<CsvTaskStack> extractCsvTask(final List<TaskDto> identityTaskDtos ) throws TaskStackException
    {
        final List<CsvTaskStack> list = new ArrayList<>( );
        for ( final TaskDto taskDto : identityTaskDtos )
        {
            final CsvTaskStack csvTaskStack = new CsvTaskStack( );

            csvTaskStack.setTaskCode( taskDto.getTaskCode( ) );
            csvTaskStack.setResourceId( taskDto.getResourceId( ) );
            csvTaskStack.setResourceType( taskDto.getResourceType( ) );
            csvTaskStack.setTaskType( taskDto.getTaskType( ) );
            csvTaskStack.setCreationDate( taskDto.getCreationDate( ) );
            csvTaskStack.setLastUpdateDate( taskDto.getLastUpdateDate( ) );
            csvTaskStack.setLastUpdateClientCode( taskDto.getLastUpdateClientCode( ) );
            csvTaskStack.setTaskStatus( taskDto.getTaskStatus( ) );
            csvTaskStack.setMetadata( taskDto.getMetadata( ) );

            list.add( csvTaskStack );
        }
        return list;
    }
}
