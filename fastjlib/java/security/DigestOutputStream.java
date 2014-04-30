/* DigestOutputStream.java --- An output stream tied to a message digest
   
  Copyright (c) 1999 by Free Software Foundation, Inc.
  Written by Mark Benvenuto <ivymccough@worldnet.att.net>

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU Library General Public License as published 
  by the Free Software Foundation, version 2. (see COPYING.LIB)

  This program is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software Foundation
  Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307 USA. */

package java.security;

import java.io.OutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

/**
   DigestOutputStream is a class that ties an OutputStream with a
   MessageDigest. The Message Digest is used by the class to update it
   self as bytes are written to the OutputStream.

   The updating to the digest depends on the on flag which is set to
   true by default that tells the class to update the data in the
   message digest.

   @version 0.0
  
   @author Mark Benvenuto <ivymccough@worldnet.att.net>
*/
public class DigestOutputStream extends FilterOutputStream
{
  /**
     The message digest for the DigestOutputStream
  */
  protected MessageDigest digest;

  //Manages the on flag
  private boolean state = true;

  /**
     Constructs a new DigestOutputStream.  It associates a
     MessageDigest with the stream to compute the stream as data is
     written.

     @param stream An OutputStream to associate this stream with
     @param digest A MessageDigest to hash the stream with
  */
  public DigestOutputStream (OutputStream stream, MessageDigest digest)
  {
    super (stream);
    this.digest = digest;
  }

  /**
     Returns the MessageDigest associated with this DigestOutputStream

     @return The MessageDigest used to hash this stream
  */
  public MessageDigest getMessageDigest ()
  {
    return digest;
  }
  
  /**
     Sets the current MessageDigest to current parameter
	
     @param digest A MessageDigest to associate with this stream
  */
  public void setMessageDigest (MessageDigest digest)
  {
    this.digest = digest;
  }


  /**
     Updates the hash if the on flag is true and then writes a byte to
     the underlying output stream.

     @param b A byte to write to the output stream
     
     @exception IOException if the underlying output stream 
     cannot write the byte, this is thrown.
  */
  public void write (int b) throws IOException
  {
    if (state)
      digest.update ((byte)b);
    
    super.write (b);
  }

  /**
     Updates the hash if the on flag is true and then writes the bytes
     to the underlying output stream.

     @param b Bytes to write to the output stream
     @param off Offset to start to start at in array
     @param len Length of data to write

     @exception IOException if the underlying output stream 
     cannot write the bytes, this is thrown.
  */
  public void write (byte[] b, int off, int len) throws IOException
  {
    if (state)
      digest.update (b, off, len);

    super.write (b, off, len);
  }

  /**
     Sets the flag specifying if this DigestOutputStream updates the
     digest in the write() methods. The default is on;

     @param on True means it digests stream, false means it does not
  */
  public void on (boolean on)
  {
    state = on;
  }

  /**
     Converts the output stream and underlying message digest to a string.

     @return A string representing the output stream and message digest.
  */
  public String toString()
  {
    return "[Digest Output Stream] " + digest.toString();
  }

}
