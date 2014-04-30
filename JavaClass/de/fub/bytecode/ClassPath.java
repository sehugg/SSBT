package de.fub.bytecode;

import java.util.*;
import java.util.zip.*;
import java.io.*;

/**
 * Responsible for loading (class) files from CLASSPATH. Inspired by
 * sun.tools.ClassPath.
 *
 * @version $Id: ClassPath.java,v 1.1.1.1 1999/12/16 16:30:40 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ClassPath {
  private PathEntry[] paths;

  /**
   * Search for classes in given path.
   */
  public ClassPath(String class_path) {
    Vector vec = new Vector();

    for(StringTokenizer tok=new StringTokenizer(class_path,
						System.getProperty("path.separator"));
	tok.hasMoreTokens();)
    {
      String path = tok.nextToken();
      
      if(!path.equals("")) {
	File file = new File(path);

	try {
	  if(file.exists()) {
	    if(file.isDirectory())
	      vec.addElement(new Dir(path));
	    else
	      vec.addElement(new Zip(new ZipFile(file)));
	  }
	} catch(IOException e) {
	  System.err.println("CLASSPATH component " + file + ": " + e);
	}
      }
    }

    paths = new PathEntry[vec.size()];
    vec.copyInto(paths);
  }

  private static final String getClassPath() {
    String class_path = System.getProperty("java.class.path");

    if(class_path == null)
      class_path = "";

    String boot_path = System.getProperty("sun.boot.class.path");

    if(boot_path == null)
      boot_path = "";
    
    return class_path + System.getProperty("path.separator") + boot_path;
  }

  /**
   * Search for classes in CLASSPATH.
   */
  public ClassPath() {
    this(getClassPath());
  }

  /**
   * @param name fully qualified class name, e.g. java.lang.String
   * @return input stream for class
   */
  public InputStream getInputStream(String name) throws IOException {
    return getInputStream(name, ".class");
  }
    
  /**
   * @param name fully qualified file name, e.g. java/lang/String
   * @param suffix file name ends with suff, e.g. .java
   * @return input stream for file on class path
   */
  public InputStream getInputStream(String name, String suffix) throws IOException {
    return getClassFile(name, suffix).getInputStream();
  }

  /**
   * @param name fully qualified file name, e.g. java/lang/String
   * @param suffix file name ends with suff, e.g. .java
   * @return class file for the java class
   */
  public ClassFile getClassFile(String name, String suffix) throws IOException {
    for(int i=0; i < paths.length; i++) {
      ClassFile cf;

      if((cf = paths[i].getClassFile(name, suffix)) != null)
	return cf;
    }

    throw new IOException("Couldn't find: " + name + suffix);
  }

  /**
   * @param name fully qualified class name, e.g. java.lang.String
   * @return input stream for class
   */
  public ClassFile getClassFile(String name) throws IOException {
    return getClassFile(name, ".class");
  }

  /**
   * @param name fully qualified file name, e.g. java/lang/String
   * @param suffix file name ends with suffix, e.g. .java
   * @return byte array for file on class path
   */
  public byte[] getBytes(String name, String suffix) throws IOException {
    InputStream is = getInputStream(name, suffix);
    
    if(is == null)
      throw new IOException("Couldn't find: " + name + suffix);

    DataInputStream dis   = new DataInputStream(is);
    byte[]          bytes = new byte[is.available()];
    dis.readFully(bytes);
    dis.close(); is.close();

    return bytes;
  }

  /**
   * @return byte array for class
   */
  public byte[] getBytes(String name) throws IOException {
    return getBytes(name, ".class");
  }

  /**
   * @param name name of file to search for, e.g. java/lang/String.java
   * @return full (canonical) path for file
   */
  public String getPath(String name) throws IOException {
    int    index  = name.lastIndexOf('.');
    String suffix = "";

    if(index > 0) {
      suffix = name.substring(index);
      name   = name.substring(0, index);
    }
    
    return getPath(name, suffix);
  }

  /**
   * @param name name of file to search for, e.g. java/lang/String
   * @param suffix file name suffix, e.g. .java
   * @return full (canonical) path for file, if it exists
   */
  public String getPath(String name, String suffix) throws IOException {
    return getClassFile(name, suffix).getPath();
  }

  private static abstract class PathEntry {
    abstract ClassFile getClassFile(String name, String suffix) throws IOException;
  }

  /** Contains information about file/ZIP entry of the Java class.
   */
  public abstract static class ClassFile {
    /** @return input stream for class file.
     */
    public abstract InputStream getInputStream() throws IOException;

    /** @return canonical path to class file.
     */
    public abstract String getPath();

    /** @return modification time of class file.
     */
    public abstract long getTime();

    /** @return size of class file.
     */
    public abstract long getSize();
  }
    
  private static class Dir extends PathEntry {
    private String dir;

    Dir(String d) { dir = d; }

    ClassFile getClassFile(String name, String suffix) throws IOException {
      final File file = new File(dir + File.separatorChar +
				 name.replace('.', File.separatorChar) + suffix);
      
      return file.exists()? new ClassFile() {
	public InputStream getInputStream() throws IOException { return new FileInputStream(file); }

	public String      getPath()        { try {
	  return file.getCanonicalPath(); 
	} catch(IOException e) { return null; }

	}
	public long        getTime()        { return file.lastModified(); }
	public long        getSize()        { return file.length(); }
      } : null;
    }

    public String toString() { return dir; }
  }

  private static class Zip extends PathEntry {
    private ZipFile zip;

    Zip(ZipFile z) { zip = z; }

    ClassFile getClassFile(String name, String suffix) throws IOException {
      final ZipEntry entry = zip.getEntry(name.replace('.', '/') + suffix);

      return (entry != null)? new ClassFile() {
	public InputStream getInputStream() throws IOException { return zip.getInputStream(entry); }
	public String      getPath()        { return entry.toString(); }
	public long        getTime()        { return entry.getTime(); }
	public long        getSize()       { return entry.getSize(); }
      } : null;
    }
  }
}


