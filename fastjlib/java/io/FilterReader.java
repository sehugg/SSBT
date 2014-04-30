/*************************************************************************
/* FilterReader.java -- Base class for char stream classes that filter input
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.io;

/**
  * This is the common superclass of all standard classes that filter 
  * input.  It acts as a layer on top of an underlying <code>Reader</code>
  * and simply redirects calls made to it to the subordinate Reader
  * instead.  Subclasses of this class perform additional filtering
  * functions in addition to simply redirecting the call.
  * <p>
  * When creating a subclass of <code>FilterReader</code>, override the
  * appropriate methods to implement the desired filtering.  However, note
  * that the <code>read(char[])</code> method does not need to be overridden
  * as this class redirects calls to that method to 
  * <code>read(yte[], int, int)</code> instead of to the subordinate
  * <code>Reader} read(yte[])</code> method.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class FilterReader extends Reader
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the subordinate <code>Reader</code> to which method calls
  * are redirected
  */
protected Reader in;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Create a <code>FilterReader</code> with the specified subordinate
  * <code>Reader</code>.
  *
  * @param in The subordinate <code>Reader</code>
  */
protected 
FilterReader(Reader in)
{
  this.in = in;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Calls the <code>in.mark(int)</code> method.
  *
  * @param readlimit The parameter passed to <code>in.mark(int)</code>
  *
  * @exception IOException If an error occurs
  */
public void
mark(int readlimit) throws IOException
{
  in.mark(readlimit);
}

/*************************************************************************/

/**
  * Calls the <code>in.markSupported()</code> method.
  *
  * @return <code>true</code> if mark/reset is supported, <code>false</code> otherwise
  */
public boolean
markSupported()
{
  return(in.markSupported());
}

/*************************************************************************/

/**
  * Calls the <code>in.reset()</code> method.
  *
  * @exception IOException If an error occurs
  */
public void
reset() throws IOException
{
  in.reset();
}

/*************************************************************************/

/**
  * Calls the <code>in.read()</code> method.
  *
  * @return The value returned from <code>in.available()</code>
  *
  * @exception IOException If an error occurs
  */
public boolean
ready() throws IOException
{
  return(in.ready());
}

/*************************************************************************/

/**
  * Calls the <code>in.skip(long)</code> method
  *
  * @param The requested number of chars to skip. 
  *
  * @return The value returned from <code>in.skip(long)</code>
  *
  * @exception IOException If an error occurs
  */
public long
skip(long num_chars) throws IOException
{
  return(in.skip(num_chars));
}

/*************************************************************************/

/**
  * Calls the <code>in.read()</code> method
  *
  * @return The value returned from <code>in.read()</code>
  *
  * @exception IOException If an error occurs
  */
public int
read() throws IOException
{
  return(in.read());
}

/*************************************************************************/

/**
  * Calls the <code>in.read(char[], int, int)</code> method.
  *
  * @param buf The buffer to read chars into
  * @param offset The index into the buffer to start storing chars
  * @param len The maximum number of chars to read.
  *
  * @return The value retured from <code>in.read(char[], int, int)</code>
  *
  * @exception IOException If an error occurs
  */
public int
read(char[] buf, int offset, int len) throws IOException
{
  return(in.read(buf, offset, len));
}

/*************************************************************************/

/**
  * This method closes the stream by calling the <code>close()</code> method
  * of the underlying stream.
  *
  * @exception IOException If an error occurs
  */
public void
close() throws IOException
{
  in.close();
}

} // class FilterReader

