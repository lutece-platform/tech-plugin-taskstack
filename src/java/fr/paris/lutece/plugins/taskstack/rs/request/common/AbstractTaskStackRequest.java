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
package fr.paris.lutece.plugins.taskstack.rs.request.common;

import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractTaskStackRequest
{

    protected final String _strClientCode;
    protected final RequestAuthor _author;

    protected AbstractTaskStackRequest( final String strClientCode, final String authorName, final String authorType ) throws TaskStackException
    {
        if ( StringUtils.isEmpty( authorName ) || StringUtils.isEmpty( authorType ) )
        {
            throw new TaskStackException( "Author_name and author_type must be provided in request header" );
        }

        if ( !AuthorType.valid( authorType ) )
        {
            throw new TaskStackException( "The provided author type value is not valid: " + authorType );
        }
        this._strClientCode = strClientCode;
        this._author = new RequestAuthor( authorName, authorType );
    }

    /**
     * Valid the request according to parameter
     *
     * @throws TaskStackException
     *             if request not valid
     */
    protected void validateRequest( ) throws TaskStackException
    {
        TaskStackRequestValidator.instance( ).checkClientCode( this._strClientCode );
        TaskStackRequestValidator.instance( ).checkAuthor( this._author );
        this.validateSpecificRequest( );
    }

    /**
     * Valid the request according to parameter
     *
     * @throws TaskStackException
     *             if request not valid
     * @throws TaskStackException
     */
    protected abstract void validateSpecificRequest( ) throws TaskStackException;

    /**
     * Specific action for the request
     *
     * @return html/json string response
     * @throws TaskStackException
     *             in case of request fail
     */
    protected abstract Object doSpecificRequest( ) throws TaskStackException;

    /**
     * Do the request, call the inner validRequest and doSpecificRequest
     *
     * @return html/json string response
     * @throws TaskStackException
     *             in case of failure
     */
    public Object doRequest( ) throws TaskStackException
    {
        this.validateRequest( );
        return doSpecificRequest( );
    }
}
