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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public final class Batch<T> extends AbstractList<List<T>>
{
    private final List<T> innerList;
    private final int partitionSize;

    public Batch(final List<T> innerList, final int partitionSize )
    {
        this.innerList = new ArrayList<>( innerList );
        this.partitionSize = partitionSize;
    }

    public static <T> Batch<T> ofSize( final List<T> list, final int partitionSize )
    {
        return new Batch<>( list, partitionSize );
    }

    @Override
    public List<T> get( final int index )
    {
        int start = index * this.partitionSize;
        int end = Math.min( start + this.partitionSize, this.innerList.size( ) );

        if ( start > end )
        {
            throw new IndexOutOfBoundsException( "Index " + index + " is out of the list range <0," + ( size( ) - 1 ) + ">" );
        }

        return new ArrayList<>( this.innerList.subList( start, end ) );
    }

    @Override
    public int size( )
    {
        return partitionSize == 0 ? 0 : (int) Math.ceil( (double) this.innerList.size( ) / (double) this.partitionSize );
    }

    public int totalSize( )
    {
        return this.innerList.size( );
    }
}
