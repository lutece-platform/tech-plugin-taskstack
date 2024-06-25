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
package fr.paris.lutece.plugins.taskstack.rs.dto.common;

import static fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusType.BAD_REQUEST;
import static fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusType.CONFLICT;
import static fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusType.FORBIDDEN;
import static fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusType.INTERNAL_SERVER_ERROR;
import static fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusType.NOT_FOUND;
import static fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusType.OK;
import static fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusType.SUCCESS;
import static fr.paris.lutece.plugins.taskstack.rs.dto.common.ResponseStatusType.UNAUTHORIZED;

public class ResponseStatusFactory
{

    public static ResponseStatus ok( )
    {
        return new ResponseStatus( 200, OK );
    }

    public static ResponseStatus success( )
    {
        return new ResponseStatus( 201, SUCCESS );
    }

    public static ResponseStatus badRequest( )
    {
        return new ResponseStatus( 400, BAD_REQUEST );
    }

    public static ResponseStatus unauthorized( )
    {
        return new ResponseStatus( 401, UNAUTHORIZED );
    }

    public static ResponseStatus forbidden( )
    {
        return new ResponseStatus( 403, FORBIDDEN);
    }

    public static ResponseStatus notFound( )
    {
        return new ResponseStatus( 404, NOT_FOUND );
    }

    public static ResponseStatus conflict( )
    {
        return new ResponseStatus( 409, CONFLICT );
    }

    public static ResponseStatus internalServerError( )
    {
        return new ResponseStatus( 500, INTERNAL_SERVER_ERROR );
    }

    public static ResponseStatus noResult( )
    {
        return new ResponseStatus( 200, NOT_FOUND );
    }

    public static ResponseStatus fromHttpCode( final int httpCode )
    {
        switch( httpCode )
        {
            case 200:
                return ok( );
            case 201:
                return success( );
            case 400:
                return badRequest( );
            case 401:
                return unauthorized( );
            case 403:
                return forbidden( );
            case 404:
                return notFound( );
            case 409:
                return conflict( );
            default:
                return internalServerError( );
        }
    }
}
