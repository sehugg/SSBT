/*************************************************************************
/* InputStreamReader.java -- Reader than transforms bytes to chars
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

import gnu.java.io.EncodingManager;
import gnu.java.io.decode.Decoder;

/**
  * This class reads characters from a byte input stream.   The characters
  * read are converted from bytes in the underlying stream by a 
  * decoding layer.  The decoding layer transforms bytes to chars according
  * to an encoding standard.  There are many available encodings to choose 
  * from.  The desired encoding can either be specified by name, or if no
  * encoding is selected, the system default encoding will be used.  The
  * system default encoding name is determined from the system property
  * <code>file.encoding</code>.  The only encodings that are guaranteed to 
  * be availalbe are "8859_1" (the Latin-1 character set) and "UTF8".
  * Unforunately, Java does not provide a mechanism for listing the
  * ecodings that are supported in a given implementation.
  * <p>
  * Here is a list of standard encoding names that may be available:
  * <p>
  * <ul>
  * <li>8859_1 (ISO-8859-1/Latin-1)
  * <li>8859_2 (ISO-8859-2/Latin-2)
  * <li>8859_3 (ISO-8859-3/Latin-3)
  * <li>8859_4 (ISO-8859-4/Latin-4)
  * <li>8859_5 (ISO-8859-5/Latin-5)
  * <li>8859_6 (ISO-8859-6/Latin-6)
  * <li>8859_7 (ISO-8859-7/Latin-7)
  * <li>8859_8 (ISO-8859-8/Latin-8)
  * <li>8859_9 (ISO-8859-9/Latin-9)
  * <li>ASCII (7-bit ASCII)
  * <li>UTF8 (UCS Transformation Format-8)
  * <li>More later
  * </ul>
  * <p>
  * It is recommended that applications do not use <code>InputStreamReader</code>'s
  * directly.  Rather, for efficiency purposes, an object of this class
  * should be wrapped by a <code>BufferedReader</code>.
  * <p>
  * Due to a deficiency the Java class library design, there is no standard
  * way for an application to install its own byte-character encoding.
  *
  * @see BufferedReader
  * @see InputStream
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class InputStreamReader extends Reader
{

/*************************************************************************/

/*
 * Instance Variables
 */

 /* 
  * This is the byte-character decoder class that does the reading and
  * translation of bytes from the underlying stream.
  */
private Decoder in;

/*************************************************************************/

/**
  * This method initializes a new instance of <code>InputStreamReader</code>
  * to read from the specified stream using the default encoding.
  *
  * @param in The <code>InputStream</code> to read from 
  */
public
InputStreamReader(InputStream in)
{
  this.in = EncodingManager.getDecoder(in);
}

/*************************************************************************/

/**
  * This method initializes a new instance of <code>InputStreamReader</code>
  * to read from the specified stream using a caller supplied character
  * encoding scheme.  Note that due to a deficiency in the Java language
  * design, there is no way to determine which encodings are supported.
  * 
  * @param in The <code>InputStream</code> to read from
  * @param encoding_name The name of the encoding scheme to use
  *
  * @exception UnsupportedEncodingException If the encoding scheme requested is not available.
  */
public
InputStreamReader(InputStream in, String encoding_name) throws
                                             UnsupportedEncodingException
{
  this.in = EncodingManager.getDecoder(in, encoding_name);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the name of the encoding that is currently in use
  * by this object.  If the stream has been closed, this method is allowed
  * to return <code>null</code>.
  *
  * @param The current encoding name
  */
public String
getEncoding()
{
  return(in.getSchemeName());
}

/*************************************************************************/

/**
  * This method closes this stream, as well as the underlying 
  * <code>InputStream</code>.
  *
  * @exception IOException If an error occurs
  */
public void
close() throws IOException
{
  in.close();
}

/*************************************************************************/

/**
  * This method checks to see if the stream is read to be read.  It
  * will return <code>true</code> if is, or <code>false</code> if it is not.
  * If the stream is not ready to be read, it could (although is not required
  * to) block on the next read attempt.
  *
  * @return <code>true</code> if the stream is ready to be read, <code>false</code> otherwise
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
  * This method reads a single character of data from the stream.
  *
  * @return The char read, as an int, or -1 if end of stream.
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
  * This method reads up to <code>len</code> characters from the stream into
  * the specified array starting at index <code>offset</code> into the
  * array.
  *
  * @param buf The character array to recieve the data read
  * @param offset The offset into the array to start storing characters
  * @param len The requested number of characters to read.
  *
  * @return The actual number of characters read, or -1 if end of stream.
  *
  * @exception IOException If an error occurs
  */
public int
read(char[] buf, int offset, int len) throws IOException
{
  return(in.read(buf, offset, len));
}

} // class InputStreamReader

