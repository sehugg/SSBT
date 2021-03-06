/* DigestInputStream.java --- An Input stream tied to a message digest
   
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
//import java.security.MessageDigest;
import java.io.InputStream;
import java.io.FilterInputStream;
import java.io.IOException;

/**
	DigestInputStream is a class that ties an InputStream with a 
	MessageDigest. The Message Digest is used by the class to 
	update it self as bytes are read from the InputStream.

	The updating to the digest depends on the on flag which is set
	to true by default to tell the class to update the data
	in the message digest.

	@version 0.0
  
	@author Mark Benvenuto <ivymccough@worldnet.att.net>
*/
public class DigestInputStream extends FilterInputStream
{

/**
	The message digest for the DigestInputStream 
*/
protected MessageDigest digest;

//Manages the on flag
private boolean state = true;

/**
	Constructs a new DigestInputStream.
	It associates a MessageDigest with the stream to 
	compute the stream as data is written.

	@param stream An InputStream to associate this stream with
	@param digest A MessageDigest to hash the stream with
*/
public DigestInputStream(InputStream stream, MessageDigest digest)
{
	super(stream);
	//this.in = stream;
	this.digest = digest;
}

/**
	Returns the MessageDigest associated with this DigestInputStream 

	@return The MessageDigest used to hash this stream
*/
public MessageDigest getMessageDigest()
{
	return digest;
}

/**
	Sets the current MessageDigest to current parameter
	
	@param digest A MessageDigest to associate with this stream
*/
public void setMessageDigest(MessageDigest digest)
{
	this.digest = digest;
}

/** 
	Reads a byte from the input stream and updates the digest.
	This method reads the underlying input stream and if the 
	on flag is true then updates the message digest.

	@return Returns a byte from the input stream, -1 is returned to indicate that 
		the end of stream was reached before this read call

	@throws IOException if an IO error occurs in the underlying input stream,
		this error is thrown
*/
public int read() throws IOException
{
	int temp = in.read();

	if( state == true && temp != -1)
		digest.update((byte)temp);

	return temp;
}

/** 
	Reads bytes from the input stream and updates the digest.
	This method reads the underlying input stream and if the 
	on flag is true then updates the message digest.

	@param b a byte array to store the data from the input stream
	@param off an offset to start at in the array
	@param len length of data to read
	@return Returns count of bytes read, -1 is returned to indicate that 
		the end of stream was reached before this read call

	@throws IOException if an IO error occurs in the underlying input stream,
		this error is thrown
*/
public int read(byte[] b, int off, int len) throws IOException
{
	int temp = in.read(b, off, len);

	if( state == true && temp != -1)
		digest.update(b, off, len);

	return temp;
}

/**
	Sets the flag specifing if this DigestInputStream updates the
	digest in the write() methods. The default is on;

	@param on True means it digests stream, false means it does not
*/
public void on(boolean on)
{
	state = on;
}

/**
	Converts the input stream and underlying message digest to a string.

	@return A string representing the input stream and message digest.
*/
public String toString()
{
	return "[Digest Input Stream] " + digest.toString();
}

}