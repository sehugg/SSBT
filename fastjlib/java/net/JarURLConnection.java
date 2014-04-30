/*************************************************************************
/* JarURLConnection.java -- Class for manipulating remote jar files
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

package java.net;

import java.io.IOException;
import java.security.Identity;
import java.util.jar.*;

/**
  * This abstract class represents a common superclass for implementations
  * of jar URL's.  A jar URL is a special type of URL that allows JAR
  * files on remote systems to be accessed.  It has the form:
  * <p>
  * jar:<standard URL pointing to jar file>!/file/within/jarfile
  * <p> for example:
  * <p>
  * jar:http://www.urbanophile.com/java/foo.jar!/com/urbanophile/bar.class
  * <p>
  * That example URL points to the file /com/urbanophile/bar.class in the
  * remote JAR file http://www.urbanophile.com/java/foo.jar.  The HTTP
  * protocol is used only as an example.  Any supported remote protocol
  * can be used.
  * <p>
  * This class currently works by retrieving the entire jar file into a
  * local cache file, then performing standard jar operations on it.
  * (At least this is true for the default protocol implementation).
  *
  * @version 0.1
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class JarURLConnection extends URLConnection
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the actual URL that points the remote jar file.  This is parsed
  * out of the jar URL by the constructor.
  */
private URL real_url;

/**
  * This is the jar file "entry name" or portion after the "!/" in the
  * URL which represents the pathname inside the actual jar file
  */
private String entry_name;

/**
  * The JarFile object for the jar file pointed to by the real URL
  */
private JarFile jar_file;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Creates a JarURLConnection from a URL objects
  *
  * @param URL url The URL object for this connection.
  */
protected
JarURLConnection(URL url) throws MalformedURLException
{
  super(url);

  // Now, strip off the "jar:" and everything from the "!/" to the end
  // to get the "real" URL inside
  String url_string = url.toExternalForm();

  if (!url_string.startsWith("jar:"))
    throw new MalformedURLException(url_string);

  if (url_string.indexOf("!/") == -1)
    throw new MalformedURLException(url_string);

  String real_url_string = url_string.substring(4, url_string.indexOf("!/") - 4);

  real_url = new URL(real_url_string);
  if (url_string.length() == (url_string.indexOf("!/") + 1))
    entry_name = "";
  else
    entry_name = url_string.substring(url_string.indexOf("!/") + 2);
}

/*************************************************************************/

/**
  * This method returns the "real" URL where the JarFile is located.
  * //****Is this right?*****
  *
  * @return The remote URL
  */ 
public URL
getJarFileURL()
{
  return(real_url);
}

/*************************************************************************/

/**
  * Returns the "entry name" portion of the jar URL.  This is the portion
  * after the "!/" in the jar URL that represents the pathname inside the
  * actual jar file.
  *
  * @return The entry name.
  */
public String
getEntryName()
{
  return(entry_name);
}

/*************************************************************************/

/**
  * Returns a read-only JarFile object for the remote jar file
  *
  * @return The JarFile object
  *
  * @exception IOException If an error occurs
  */
public abstract JarFile
getJarFile() throws IOException;

/*************************************************************************/

/**
  * Returns a Manifest object for this jar file, or null if there is no
  * manifest.
  *
  * @return The Manifest
  *
  * @exception IOException If an error occurs
  */
public Manifest
getManifest() throws IOException
{
  if (jar_file == null)
    jar_file = getJarFile();

  return(jar_file.getManifest());
}

/*************************************************************************/

/**
  * Returns the entry in this jar file specified by the URL.  
  * 
  * @return The jar entry
  *
  * @exception IOException If an error occurs
  */
public JarEntry
getJarEntry() throws IOException
{
  if (jar_file == null)
    jar_file = getJarFile();

  return(jar_file.getJarEntry(entry_name));
}

/*************************************************************************/

/**
  * Returns the Attributes for the Jar entry specified by the URL or null
  * if none
  *
  * @return The Attributes
  *
  * @exception IOException If an error occurs
  */
public Attributes
getAttributes() throws IOException
{
  return(getJarEntry().getAttributes());
}

/*************************************************************************/

/**
  * Returns the main Attributes for the jar file specified in the URL or
  * null if there are none
  *
  * @return The main Attributes
  *
  * @exception IOException If an error occurs
  */
public Attributes
getMainAttributes() throws IOException
{
  return(getManifest().getMainAttributes());
}

/*************************************************************************/

/**
  * Returns an array of Identity objects for the jar file entry specified
  * by this URL or null if there are none
  *
  * @return An Identity array
  *
  * @exception IOException If an error occurs
  */
public Identity[]
getIdentities() throws IOException
{
  return(getJarEntry().getIdentities());
}

} // class JarURLConnection

