/*************************************************************************
/* HttpURLConnection.java -- URLConnection class for HTTP protocol
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

package gnu.java.net.protocol.http;

import java.net.URL;
import java.net.URLConnection;
import java.net.Socket;
import java.net.ProtocolException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
  * This subclass of java.net.URLConnection models a URLConnection via
  * the HTTP protocol.
  *
  * @version 0.1
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class HttpURLConnection extends java.net.HttpURLConnection
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The socket we are connected to
  */
private Socket socket;

/**
  * The InputStream for this connection
  */
private DataInputStream in_stream;

/**
  * The OutputStream for this connection
  */
private OutputStream out_stream;

/**
  * The PrintWriter for this connection (used internally)
  */
private PrintWriter out_writer;

/**
  * This is the object that holds the header field information
  */
private gnu.java.net.HeaderFieldHelper headers =
    new gnu.java.net.HeaderFieldHelper();

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Calls superclass constructor to initialize
  */
protected
HttpURLConnection(URL url)
{
  super(url);

  /* Set up some variables */
  doOutput = false;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Connects to the remote host, sends the request, and parses the reply
  * code and header information returned
  */
public void
connect() throws IOException
{
  // Connect up
  if (url.getPort() == -1)
    socket = new Socket(url.getHost(), 80);
  else
    socket = new Socket(url.getHost(), url.getPort());

  out_stream = socket.getOutputStream();
  in_stream = new DataInputStream(socket.getInputStream());

  out_writer = new PrintWriter(new OutputStreamWriter(out_stream, "8859_1")); 

  // Send the request
  out_writer.print(getRequestMethod() + " " + getURL().getFile() + 
                   " HTTP/1.1\r\n");

  String propval = getRequestProperty("host");
  if (propval == null)
    out_writer.print("Host: " + getURL().getHost() + "\r\n");
  else
    out_writer.print("Host: " + propval + "\r\n");
  out_writer.print("Connection: close" + "\r\n");

  propval = getRequestProperty("user-agent");
  if (propval == null)
    out_writer.print("User-Agent: jcl/0.0\r\n");
  else
    out_writer.print("User-Agent: " + propval + "\r\n");

  propval = getRequestProperty("accept");
  if (propval == null)
    out_writer.print("Accept: */*\r\n");
  else
    out_writer.print("Accept: " + propval + "\r\n");

  out_writer.print("\r\n");
  out_writer.flush();

  // Parse the reply
  String line = in_stream.readLine();
  String saveline = line;

  int idx = line.indexOf(" " );
  if ((idx == -1) || (line.length() < (idx + 6)))
    throw new IOException("Server reply was unparseable: " + saveline);

  line = line.substring(idx + 1);
  String code = line.substring(0, 3);
  try
    {
      responseCode = Integer.parseInt(code);
    }
  catch (NumberFormatException e)
    {
      throw new IOException("Server reply was unparseable: " + saveline);
    } 
  responseMessage = line.substring(4);

  // Now read the header lines
  String key = null, value = null;
  for (;;)
    {
      line = in_stream.readLine();
      if (line.equals(""))
        break;

      // Check for folded lines
      if (line.startsWith(" ") || line.startsWith("\t"))
        {
          // Trim off leading space
          do
            {
              if (line.length() == 1)
                throw new IOException("Server header lines were unparseable: " + 
                                      line);

              line = line.substring(1);
            }
          while (line.startsWith(" ") || line.startsWith("\t"));

          value = value + " " + line;
        }
      else 
        {
          if (key != null)
            {
              headers.addHeaderField(key, value);
              key = null;
              value = null;
            }

          // Parse out key and value
          idx = line.indexOf(":");
          if ((idx == -1) || (line.length() < (idx + 2)))
            throw new IOException("Server header lines were unparseable: " + line);

          key = line.substring(0, idx);
          value = line.substring(idx + 1);

          // Trim off leading space
          while (value.startsWith(" ") || value.startsWith("\t"))
            {
              if (value.length() == 1)
                throw new IOException("Server header lines were unparseable: " + 
                                      line);

              value = value.substring(1);
            }
         }
     }
  if (key != null)
    {
      headers.addHeaderField(key, value);
    }
}

/*************************************************************************/

/**
  * Disconnects from the remote server
  */
public void
disconnect()
{
  try
    {
      socket.close();
    }
  catch(IOException e) { ; }
}

/*************************************************************************/

/**
  * Overrides java.net.HttpURLConnection.setRequestMethod() in order to
  * restrict the available methods to only those we support.
  *
  * @param method The RequestMethod to use
  *
  * @exception ProtocolException If the specified method is not valid
  */
public void
setRequestMethod(String method) throws ProtocolException
{
  method = method.toUpperCase();
  if (method.equals("GET") || method.equals("HEAD"))
    super.setRequestMethod(method);
  else
    throw new ProtocolException("Unsupported or unknown request method " +
                                method);
}

/*************************************************************************/

/**
  * Return a boolean indicating whether or not this connection is
  * going through a proxy
  *
  * @return true if using a proxy, false otherwise
  */
public boolean
usingProxy()
{
  return(false);
}

/*************************************************************************/

/**
  * This method returns the header field key at the specified numeric
  * index.
  *
  * @param n The index into the header field array
  *
  * @return The name of the header field key, or <code>null</code> if the
  * specified index is not valid.
  */
public String
getHeaderFieldKey(int n)
{
  return(headers.getHeaderFieldKeyByIndex(n));
}

/*************************************************************************/

/**
  * This method returns the header field value at the specified numeric
  * index.
  *
  * @param n The index into the header field array
  *
  * @return The value of the specified header field, or <code>null</code>
  * if the specified index is not valid.
  */
public String
getHeaderField(int n)
{
  return(headers.getHeaderFieldValueByIndex(n));
}

/*************************************************************************/

/**
  * Returns an InputStream for reading from this connection.  This stream
  * will be "queued up" for reading just the contents of the requested file.
  * Overrides URLConnection.getInputStream()
  *
  * @return An InputStream for this connection.
  *
  * @exception IOException If an error occurs
  */
public InputStream
getInputStream() throws IOException
{
  if (!connected)
    connect();

  return(in_stream);
}

} // class HttpURLConnection

