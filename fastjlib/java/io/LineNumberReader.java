/*************************************************************************
/* LineNumberReader.java -- A character input stream which counts line numbers
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
  * This class functions like a standard <code>Reader</code> except that it
  * counts line numbers, and canonicalizes newline characters.  As data
  * is read, whenever the char sequences "\r", "\n", or "\r\n" are encountered,
  * the running line count is incremeted by one.  Additionally, the whatever
  * line termination sequence was encountered will be converted to a "\n"
  * char.  Note that this class numbers lines from 0.  When the first
  * line terminator is encountered, the line number is incremented to 1, and
  * so on.  Also note that actual "\r" and "\n" characters are looked for.
  * The system dependent line separator sequence is ignored.
  * <p>
  * This class counts only line termination characters.  If the last line
  * read from the stream does not end in a line termination sequence, it
  * will not be counted as a line.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class LineNumberReader extends BufferedReader
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This variable is used to keep track of the current line number
  */
private int line_number;

/**
  * This variable is used to keep track of the line number that was
  * current when the <code>mark()</code> method was called.
  */
private int marked_line_number;

/**
  * Determines whether or not a '\n' as the first character of a read
  * is the continuation of a '\r' as the last character of the previous
  * read or not
  */
private boolean continued_newline;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Create a new <code>LineNumberReader</code> that reads from the 
  * specified subordinate <code>Reader</code>.  A default 512 char sized
  * buffer will be used for reads.
  *
  * @param in The subordinate <code>Reader</code> to read from
  */
public
LineNumberReader(Reader in)
{
  this(in, DEFAULT_BUFFER_SIZE);
}

/*************************************************************************/

/**
  * This method initializes a new <code>LineNumberReader</code> to read
  * from the specified subordinate <code>Reader</code> using the specified
  * read buffer size.
  *
  * @param in The subordinate <code>Reader</code> to read from
  * @param size The buffer size to use for reading
  */
public
LineNumberReader(Reader in, int size)
{
  super(new PushbackReader(in), size);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the current line number
  *
  * @returns The current line number
  */
public int 
getLineNumber()
{
  return(line_number);
}

/*************************************************************************/

/**
  * This method sets the current line number to the specified value.
  * 
  * @param line_number The new line number
  */
public void
setLineNumber(int line_number)
{
  this.line_number = line_number;
}

/*************************************************************************/

/**
  * This method marks a position in the input to which the stream can be
  * "reset" char calling the <code>reset()</code> method.  The parameter
  * <code>readlimit</code> is the number of chars that can be read from the
  * stream after setting the mark before the mark becomes invalid.   For
  * example, if <code>mark()</code> is called with a read limit of 10, then when
  * 11 chars of data are read from the stream before the <code>reset()</code>
  * method is called, then the mark is invalid and the stream object
  * instance is not required to remember the mark.
  * <p>
  * In this class, this method will remember the current line number as well
  * as the current position in the stream.  When the <code>reset()</code> method 
  * is called, the line number will be restored to the saved line number in
  * addition to the stream position.
  *
  * @param readlimit The number of chars that can be read before the mark becomes invalid
  *
  * @exception IOException If an error occurs
  */
public void
mark(int readlimit) throws IOException
{
  synchronized (lock) {

  super.mark(readlimit);

  marked_line_number = line_number;

  } // synchronized
}

/*************************************************************************/

/**
  * This method resets a stream to the point where the <code>mark()</code> method
  * was called.  Any chars that were read after the mark point was set will
  * be re-read during subsequent reads.
  * <p>
  * In this class, this method will also restore the line number that was
  * current when the <code>mark()</code> method was called.
  * 
  * @exception IOException If an error occurs
  */
public void
reset() throws IOException
{
  synchronized (lock) {

  super.reset();

  line_number = marked_line_number;

  } // synchronized
}

/*************************************************************************/

/**
  * This method reads an unsigned char from the input stream and returns it
  * as an int in the range of 0-255.  This method will return -1 if the
  * end of the stream has been reached.
  * <p>
  * Note that if a line termination sequence is encountered (ie, "\r",
  * "\n", or "\r\n") then that line termination sequence is converted to
  * a single "\n" value which is returned from this method.  This means
  * that it is possible this method reads two chars from the subordinate
  * stream instead of just one.
  * <p>
  * Note that this method will block until a char of data is available
  * to be read.
  *
  * @return The char read or -1 if end of stream
  * 
  * @exception IOException If an error occurs
  */
public int
read() throws IOException
{
  synchronized (lock) {

  int char_read = super.read();

  if (char_read == '\n')
    ++line_number;

  if (char_read == '\r')
    {
      int extra_char_read = super.read();

      if ((extra_char_read != '\n') && (extra_char_read != -1))
        ((PushbackReader)in).unread(extra_char_read);

      char_read = '\n';
      ++line_number;
    }

  return(char_read);

  } // synchronized
}

/*************************************************************************/

/**
  * This method reads chars from a stream and stores them into a caller
  * supplied buffer.  It starts storing data at index <code>offset</code> into
  * the buffer and attemps to read <code>len</code> chars.  This method can
  * return before reading the number of chars requested.  The actual number
  * of chars read is returned as an int.  A -1 is returned to indicated the
  * end of the stream.
  * <p>
  * This method will block until some data can be read.
  * <p>
  * Note that if a line termination sequence is encountered (ie, "\r",
  * "\n", or "\r\n") then that line termination sequence is converted to
  * a single "\n" value which is stored in the buffer.  Only a single
  * char is counted towards the number of chars read in this case.
  *
  * @param buf The array into which the chars read should be stored
  * @param offset The offset into the array to start storing chars
  * @param len The requested number of chars to read
  *
  * @return The actual number of chars read, or -1 if end of stream
  *
  * @exception IOException If an error occurs.
  */
public int
read(char[] buf, int offset, int len) throws IOException
{
  if (len == 0)
    return(0);

  synchronized (lock) {

  // Read the first char here in order to allow IOException's to 
  // propagate up
  int char_read = read();
  if (char_read == -1)
    return(-1);
  buf[offset] = (char)char_read;

  int total_read = 1;

  // Read the rest of the chars.  We do this in a single char read() loop,
  // which is bad and should be fixed in the future. *****RECODE THIS****
  try
    {
      for (int i = 1; i < len; i++)
        {
          char_read = read();
          if (char_read == -1)
            return(total_read);

          buf[offset + i] = (char)char_read;
          
          ++total_read;  
        }
    }
  catch (IOException e)
    {
      return(total_read);
    }

  return(total_read);

  } // synchronized
}

/*************************************************************************/

/**
  * This method reads a line of text from the input stream and returns
  * it as a <code>String</code>.  A line is considered to be terminated
  * by a "\r", "\n", or "\r\n" sequence, not by the system dependent line
  * separator.
  *
  * @return The line read as a <code>String</code> or <code>null</code> if end of stream.
  *
  * @exception IOException If an error occurs
  */
public String
readLine() throws IOException
{
  synchronized (lock) {

  String line = super.readLine();
  if (line != null)
    ++line_number;

  return(line);

  } // synchronized
}

/*************************************************************************/

/**
  * This method skips over characters in the stream.  This method will
  * skip the specified number of characters if possible, but is not required
  * to skip them all.  The actual number of characters skipped is returned.
  * This method returns 0 if the specified number of chars is less than 1.
  *
  * @param num_chars The specified number of chars to skip.
  *
  * @return The actual number of chars skipped. 
  *
  * @exception IOException If an error occurs
  */
public long
skip(long num_chars) throws IOException
{
  // The spec says to override this method so we do even though we
  // don't need to.  Assume we aren't counting lines in skipped chars.
  return(super.skip(num_chars));
}

} // class LineNumberReader

