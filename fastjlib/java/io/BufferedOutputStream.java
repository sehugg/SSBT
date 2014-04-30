/*************************************************************************
/* BufferedOutputStream.java -- Buffer output into large blocks before writing
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
  * This class accumulates bytes written in a buffer instead of immediately
  * writing the data to the underlying output sink. The bytes are instead
  * as one large block when the buffer is filled, or when the stream is
  * closed or explicitly flushed. This mode operation can provide a more
  * efficient mechanism for writing versus doing numerous small unbuffered
  * writes.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class BufferedOutputStream extends FilterOutputStream
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * This is the default buffer size
  */
private static final int DEFAULT_BUFFER_SIZE = 512;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the internal byte array used for buffering output before
  * writing it.
  */
protected byte[] buf;

/**
  * This is the number of bytes that are currently in the buffer and
  * are waiting to be written to the underlying stream.  It always points to
  * the index into the buffer where the next byte of data will be stored
  */
protected int count;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new <code>BufferedOutputStream</code> instance
  * that will write to the specified subordinate <code>OutputStream</code>
  * and which will use a default buffer size of 512 bytes.
  *
  * @param out The underlying <code>OutputStream</code> to write data to
  */
public
BufferedOutputStream(OutputStream out)
{
  this(out, DEFAULT_BUFFER_SIZE);
}

/*************************************************************************/

/**
  * This method initializes a new <code>BufferedOutputStream</code> instance
  * that will write to the specified subordinate <code>OutputStream</code>
  * and which will use the specified buffer size
  *
  * @param out The underlying <code>OutputStream</code> to write data to
  * @param size The size of the internal buffer
  */
public
BufferedOutputStream(OutputStream out, int size)
{
  super(out);

  buf = new byte[size];
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method causes any currently buffered bytes to be immediately
  * written to the underlying output stream.
  *
  * @exception IOException If an error occurs
  */
public synchronized void
flush() throws IOException
{
  if (count == 0)
    return;

  out.write(buf, 0, count);
  count = 0;
}

/*************************************************************************/

/*
  * This method flushes any remaining buffered bytes then closes the 
  * underlying output stream.  Any further attempts to write to this stream
  * may throw an exception
  *
public synchronized void
close() throws IOException
{
  flush();
  out.close();
}
*/

/*************************************************************************/

/*
  * This method runs when the object is garbage collected.  It is 
  * responsible for ensuring that all buffered bytes are written and
  * for closing the underlying stream.
  *
  * @exception IOException If an error occurs (ignored by the Java runtime)
  *
protected void
finalize() throws IOException
{
  close();
}
*/

/*************************************************************************/

/**
  * This method writes a single byte of data.  This will be written to the
  * buffer instead of the underlying data source.  However, if the buffer
  * is filled as a result of this write request, it will be flushed to the
  * underlying output stream.
  *
  * @param b The byte of data to be written, passed as an int
  *
  * @exception IOException If an error occurs
  */
public synchronized void
write(int b) throws IOException
{
  buf[count] = (byte)(b & 0xFF);

  ++count;
  if (count == buf.length)
    flush();
}

/*************************************************************************/

/**
  * This method writes <code>len</code> bytes from the byte array 
  * <code>buf</code> starting at position <code>offset</code> in the buffer. 
  * These bytes will be written to the internal buffer.  However, if this
  * write operation fills the buffer, the buffer will be flushed to the
  * underlying output stream.
  *
  * @param buf The array of bytes to write.
  * @param offset The index into the byte array to start writing from.
  * @param len The number of bytes to write.
  *
  * @exception IOException If an error occurs
  */
public synchronized void
write(byte[] buf, int offset, int len) throws IOException
{
  // Buffer can hold everything
  if (len < (this.buf.length - count))
    {
      System.arraycopy(buf, offset, this.buf, count, len);
      count += len;

      if (count == buf.length)
        flush();
    }
  else
    {
      flush();
       
      // Loop and write out full buffer chunks. As an optimization, 
      // don't buffer these, just write them
      int i = 0;
      if ((len / this.buf.length) != 0)
        for (i = 0; i < (len / this.buf.length); i++)
          out.write(buf, offset + (i * this.buf.length), this.buf.length);

      // Buffer the remaining bytes
      if ((len % buf.length) != 0)
        {
          System.arraycopy(buf, offset + (i * this.buf.length), this.buf, count, 
                           len - (i * this.buf.length));
          count += (len - (i * this.buf.length));
        }
    }
}

} // class BufferedOutputStream 

