/*************************************************************************
/* ObjectInputStream.java -- Class used to read serialized objects 
/*
/* Copyright (c) 1998 by Free Software Foundation, Inc.
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.io;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

import gnu.java.io.ObjectIdentityWrapper;
import gnu.java.lang.reflect.TypeSignature;


/** 
   An <code>ObjectInputStream</code> can be used to read objects
   as well as primitive data in a platform-independent manner from an
   <code>InputStream</code>.

   The data produced by an <code>ObjectOutputStream</code> can be read
   and reconstituted by an <code>ObjectInputStream</code>.

   <code>readObject(Object)</code> is used to read Objects, the
   <code>read&lt;type&gt;</code> methods are used to read primitive
   data (as in <code>DataInputStream</code>). Strings can be read
   as objects or as primitive data, depending on how they were
   written.

   Example usage:
     <pre>
     Hashtable map = new Hashtable();
     map.put( "one", new Integer( 1 ) );
     map.put( "two", new Integer( 2 ) );

     ObjectOutputStream oos =
       new ObjectOutputStream( new FileOutputStream( "numbers" ) );
     oos.writeObject( map );
     oos.close();

     ObjectInputStream ois =
       new ObjectInputStream( new FileInputStream( "numbers" ) );
     Hashtable newmap = (Hashtable)ois.readObject();
     
     System.out.println( newmap );
     </pre>

   The default deserialization can be overriden in two ways.

   By defining a method <code>private void
   readObject(ObjectInputStream)</code>, a class can dictate exactly
   how information about itself is reconstituted.
   <code>defaultReadObject()</code> may be called from this method to
   carry out default deserialization.  This method is not
   responsible for dealing with fields of super-classes or subclasses.

   By implementing <code>java.io.Externalizable</code>.  This gives
   the class complete control over the way it is read from the
   stream.  If this approach is used the burden of reading superclass
   and subclass data is transfered to the class implementing
   <code>java.io.Externalizable</code>.

   @see java.io.DataInputStream
   @see java.io.Externalizable
   @see java.io.ObjectOutputStream
   @see java.io.Serializable
   @see XXX: java serialization spec
*/
public class ObjectInputStream extends InputStream
  implements ObjectInput, ObjectStreamConstants
{
  /**
     Creates a new <code>ObjectInputStream</code> that will do all of
     its reading from <code>in</code>.  This method also checks
     the stream by reading the header information (stream magic number
     and stream version).

     @exception IOException Reading stream header from underlying
     stream cannot be completed.

     @exception StreamCorruptedException An invalid stream magic
     number or stream version was read from the stream.

     @see readStreamHeader()
  */
  public ObjectInputStream( InputStream in )
    throws IOException, StreamCorruptedException
  {
    this.resolveEnabled = false;
    this.isDeserializing = false;
    this.blockDataPosition = 0;
    this.blockDataBytes = 0;
    this.blockData = new byte[ BUFFER_SIZE ];
    this.blockDataInput = new DataInputStream( this );
    this.realInputStream = new DataInputStream( in );
    this.nextOID = baseWireHandle;
    this.objectLookupTable = new Hashtable();
    this.validators = new Vector();
    setBlockDataMode( true );
    readStreamHeader();
  }


  /**
     Returns the next deserialized object read from the underlying stream.

     This method can be overriden by a class by implementing
     <code>private void readObject(ObjectInputStream)</code>.

     If an exception is thrown from this method, the stream is left in
     an undefined state.

     @exception ClassNotFoundException The class that an object being
     read in belongs to cannot be found.

     @exception IOException Exception from underlying
     <code>InputStream</code>.
  */
  public final Object readObject() throws ClassNotFoundException, IOException
  {
    if( this.useSubclassMethod )
      return readObjectOverride();
    
    boolean was_deserializing;    
    boolean first_time = true;

    Object ret_val;
    while( true )
    {
      //DEBUG
      if( !first_time )
	throw new RuntimeException( "Help me!! I'm in an infinite loop." );
      first_time = false;
      //eDEBUG
      
      was_deserializing = this.isDeserializing;

      if( ! was_deserializing )
	setBlockDataMode( false );

      this.isDeserializing = true;

      DEBUG( "MARKER " );
      byte marker = this.realInputStream.readByte();

      if( marker == TC_BLOCKDATA || marker == TC_BLOCKDATALONG )
      {
	readNextBlock( marker );
	throw new BlockDataException( this.blockDataBytes );
      }
    
      if( marker == TC_NULL )
      {
	ret_val = null;
	break;
      }      

      if( marker == TC_REFERENCE )
      {
	DEBUG( "REFERENCE " );
	Integer oid = new Integer( this.realInputStream.readInt() );
	ret_val = ((ObjectIdentityWrapper)
		   this.objectLookupTable.get(oid)).object;
	break;
      }
    
      if( marker == TC_CLASS )
      {
	ObjectStreamClass osc = (ObjectStreamClass)readObject();
	Class clazz = osc.forClass();
	assignNewHandle( clazz );
	ret_val = clazz;
	break;
      }

      if( marker == TC_CLASSDESC )
      {
	DEBUG( "CLASSDESC NAME " );
	String name = this.realInputStream.readUTF();
	DEBUG( "UID " );
	long uid = this.realInputStream.readLong();
	DEBUG( "FLAGS " );
	byte flags = this.realInputStream.readByte();
	DEBUG( "FIELD COUNT " );
	short field_count = this.realInputStream.readShort();
	ObjectStreamField[] fields = new ObjectStreamField[ field_count ];

	ObjectStreamClass osc = new ObjectStreamClass( name, uid,
						       flags, fields );
	assignNewHandle( osc );
      
	for( int i=0; i < field_count; i++ )
	{
	  DEBUG( "TYPE CODE " );
	  char type_code = (char)this.realInputStream.readByte();
	  DEBUG( "FIELD NAME " );
	  String field_name = this.realInputStream.readUTF();
	  String class_name;

	  if( type_code == 'L' || type_code == '[' )
	    class_name = (String)readObject();
	  else
	    class_name = String.valueOf( type_code );
	
	  fields[i] =
	    new ObjectStreamField( field_name,
				   TypeSignature.getClassForEncoding
				   ( class_name ) );
	}
      
	setBlockDataMode( true );
	osc.setClass( resolveClass( osc ) );
	setBlockDataMode( false );
      
	DEBUG( "ENDBLOCKDATA " );
	if( this.realInputStream.readByte() != TC_ENDBLOCKDATA )
	  throw new IOException( "Data annotated to class was not consumed." );
      
	osc.setSuperclass( (ObjectStreamClass)readObject() );
	ret_val = osc;
	break;
      }
    
      if( marker == TC_STRING )
      {
	DEBUG( "STRING " );
	String s = this.realInputStream.readUTF();
	ret_val = processResoultion( s, assignNewHandle( s ) );
	break;
      }
    
      if( marker == TC_ARRAY )
      {
	ObjectStreamClass osc = (ObjectStreamClass)readObject();
	Class componenetType = osc.forClass().getComponentType();
	DEBUG( "ARRAY LENGTH " );
	int length = this.realInputStream.readInt();
	Object array = Array.newInstance( componenetType, length );
	int handle = assignNewHandle( array );
	readArrayElements( array, componenetType );
	ret_val = processResoultion( array, handle );
	break;
      }

      if( marker == TC_OBJECT )
      {
	ObjectStreamClass osc = (ObjectStreamClass)readObject();
	Class clazz = osc.forClass();
    
	if( !Serializable.class.isAssignableFrom( clazz ) )
	  throw new NotSerializableException( clazz + " is not Serializable, and thus cannot be deserialized." );
    
	if( Externalizable.class.isAssignableFrom( clazz ) )
	{
	  Externalizable obj = null;
	
	  try
	  {
	    obj = (Externalizable)clazz.newInstance();
	  }
	  catch( InstantiationException e )
	  {
	    throw new ClassNotFoundException( "Instance of " + clazz
					      + " could not be created" );
	  }
	  catch( IllegalAccessException e )
	  {
	    throw new ClassNotFoundException( "Instance of " + clazz
					      + " could not be created because class or zero-argument constructor is not accessible" );
	  }
	  catch( NoSuchMethodError e )
	  {
	    throw new ClassNotFoundException( "Instance of " + clazz
					      + " could not be created because zero-argument constructor is not defined" );
	  }

	  int handle = assignNewHandle( obj );

	  boolean read_from_blocks = ((osc.getFlags() & SC_BLOCK_DATA) != 0);
	
	  if( read_from_blocks )
	    setBlockDataMode( true );

	  obj.readExternal( this );

	  if( read_from_blocks )
	    setBlockDataMode( false );

	  ret_val = processResoultion( obj, handle );
	  break;
	}

	// find the first non-serializable, non-abstract
	// class in clazz's inheritance hierarchy
	Class first_nonserial = clazz.getSuperclass();
	while( Serializable.class.isAssignableFrom( first_nonserial )
	       || Modifier.isAbstract( first_nonserial.getModifiers() ) )
	  first_nonserial = first_nonserial.getSuperclass();
	
	DEBUGln( "Using " + first_nonserial
		 + " as starting point for constructing " + clazz );

	Object obj = null;
	obj = newObject( clazz, first_nonserial );
      
	if( obj == null )
	  throw new ClassNotFoundException( "Instance of " + clazz +
					    " could not be created" );

	int handle = assignNewHandle( obj );
	this.currentObject = obj;
	ObjectStreamClass[] hierarchy =
	  ObjectStreamClass.getObjectStreamClasses( clazz );

	DEBUGln( "Got class hierarchy of depth " + hierarchy.length );

	// XXX: rewrite this loop natively?
	boolean has_read;
	for( int i=0; i < hierarchy.length; i++ )
	{
	  this.currentObjectStreamClass = hierarchy[i];

	  DEBUGln( "Reading fields of "
		   + this.currentObjectStreamClass.getName() );

	  has_read = true;
	
	  try
	  {
	    this.currentObjectStreamClass.forClass().
	      getDeclaredMethod( "readObject", ourReadObjectParams );
	  }
	  catch( NoSuchMethodException e )
	  {
	    has_read = false;
	  }

	  // XXX: should initialize fields in classes in the hierarchy
	  // that aren't in the stream
	  // should skip over classes in the stream that aren't in the
	  // real classes hierarchy
	  readFields( obj, this.currentObjectStreamClass.getFields(),
		      has_read, this.currentObjectStreamClass );

	  if( has_read )
	  {
	    DEBUG( "ENDBLOCKDATA? " );
	    if( this.realInputStream.readByte() != TC_ENDBLOCKDATA )
	      throw new IOException( "No end of block data seen for class with readObject(ObjectInputStream) method." );
	  }
	}

	this.currentObject = null;
	this.currentObjectStreamClass = null;
	ret_val = processResoultion( obj, handle );
	break;
      }

      if( marker == TC_RESET )
      {
	clearHandles();
	ret_val = readObject();
	break;
      }
    
      if( marker == TC_EXCEPTION )
      {
	Exception e = (Exception)readObject();
	clearHandles();
	throw new WriteAbortedException( "Exception thrown during writing of stream", e );
      }

      throw new IOException( "Unknown marker on stream" );
    }
    
    this.isDeserializing = was_deserializing;

    if( ! was_deserializing )
    {
      setBlockDataMode( true );

      if( validators.size() > 0 )
	invokeValidators();
    }
    
    return ret_val;
  }
  

  /** 
     Reads the current objects non-transient, non-static fields from
     the current class from the underlying output stream.

     This method is intended to be called from within a object's
     <code>private void readObject(ObjectInputStream)</code>
     method.

     @exception ClassNotFoundException The class that an object being
     read in belongs to cannot be found.

     @exception NotActiveException This method was called from a
     context other than from the current object's and current class's
     <code>private void readObject(ObjectInputStream)</code>
     method.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.
  */
  public void defaultReadObject()
    throws ClassNotFoundException, IOException, NotActiveException
  {
    if( this.currentObject == null || this.currentObjectStreamClass == null )
      throw new NotActiveException( "defaultReadObject called by non-active class and/or object" );
    
    if( fieldsAlreadyRead )
      throw new NotActiveException( "defaultReadObject called but fields already read from stream (by defaultReadObject or readFields)" );

    readFields( this.currentObject,
		this.currentObjectStreamClass.getFields(),
		false, this.currentObjectStreamClass );

    fieldsAlreadyRead = true;
  }


  /**
     Registers a <code>ObjectInputValidation</code> to be carried out
     on the object graph currently being deserialized before it is
     returned to the original caller of <code>readObject()</code>.
     The order of validation for multiple
     <code>ObjectInputValidation</code>s can be controled using
     <code>priority</code>.  Validators with higher priorities are
     called first.

     @see java.io.ObjectInputValidation

     @exception InvalidObjectException <code>validator</code> is
     <code>null</code>

     @exception NotActiveException an attempt was made to add a
     validator outside of the <code>readObject</code> method of the
     object currently being deserialized
  */
  public void registerValidation( ObjectInputValidation validator,
				  int priority )
    throws InvalidObjectException, NotActiveException
  {
    if( this.currentObject == null || this.currentObjectStreamClass == null )
      throw new NotActiveException( "registerValidation called by non-active class and/or object" );
    
    if( validator == null )
      throw new InvalidObjectException( "attempt to add a null ObjectInputValidation object" );
    
    this.validators.addElement( new ValidatorAndPriority( validator,
							  priority ) );
  }
  

  /** 
     Called when a class is being deserialized.  This is a hook to
     allow subclasses to read in information written by the
     <code>annotateClass(Class)</code> method of an
     <code>ObjectOutputStream</code>.

     This implementation looks up the active call stack for a
     <code>ClassLoader</code>; if a <code>ClassLoader</code> is found,
     it is used to load the class associated with <code>osc</code>,
     otherwise, the default system <code>ClassLoader</code> is used.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.

     @see java.io.ObjectOutputStream#annotateClass(java.lang.Class)
  */
  protected Class resolveClass( ObjectStreamClass osc )
    throws ClassNotFoundException, IOException
  {
    DEBUGln( "Resolving " + osc );

    SecurityManager sm = System.getSecurityManager();
    
    if( sm == null )
      sm = new SecurityManager() {};
    
    ClassLoader cl = currentClassLoader( sm );

    if( cl == null )
    {
      DEBUGln( "No class loader found" );
      return Class.forName( osc.getName() );
    }
    else
    {
      DEBUGln( "Using " + cl );
      return cl.loadClass( osc.getName() );
    }
  }


  /**
     Allows subclasses to resolve objects that are read from the
     stream with other objects to be returned in their place.  This
     method is called the first time each object is encountered.

     This method must be enabled before it will be called in the
     serialization process.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.

     @see enableResolveObject(boolean)
  */
  protected Object resolveObject( Object obj ) throws IOException
  {
    return obj;
  }

  
  /**
     If <code>enable</code> is <code>true</code> and this object is
     trusted, then <code>resolveObject(Object)</code> will be called
     in subsequent calls to <code>readObject(Object)</code>.
     Otherwise, <code>resolveObject(Object)</code> will not be called.

     @exception SecurityException This class is not trusted.
  */
  protected boolean enableResolveObject( boolean enable )
    throws SecurityException
  {
    if( enable )
      if( getClass().getClassLoader() != null )
	throw new SecurityException( "Untrusted ObjectInputStream subclass attempted to enable object resolution" );

    boolean old_val = this.resolveEnabled;
    this.resolveEnabled = enable;
    return old_val;
  }


  /**
     Reads stream magic and stream version information from the
     underlying stream. 

     @exception IOException Exception from underlying stream.

     @exception StreamCorruptedException An invalid stream magic
     number or stream version was read from the stream.     
  */
  protected void readStreamHeader()
    throws IOException, StreamCorruptedException
  {
    DEBUG( "STREAM MAGIC " );
    if( this.realInputStream.readShort() != STREAM_MAGIC )
      throw new StreamCorruptedException( "Invalid stream magic number" );
    
    DEBUG( "STREAM VERSION " );
    if( this.realInputStream.readShort() != STREAM_VERSION )
      throw new StreamCorruptedException( "Invalid stream version number" );
  }


  public int read() throws IOException
  {
    if( this.readDataFromBlock )
    {
      if( this.blockDataPosition >= this.blockDataBytes )
	readNextBlock();
      return this.blockData[ this.blockDataPosition++ ];
    }
    else
      return this.realInputStream.read();
  }

  public int read( byte data[], int offset, int length ) throws IOException
  {
    if( this.readDataFromBlock )
    {
      if( this.blockDataPosition + length >= this.blockDataBytes )
	readNextBlock();

      System.arraycopy( this.blockData, this.blockDataPosition,
			data, offset, length );
      return length;
    }
    else
      return this.realInputStream.read( data, offset, length );
  }

  public int available() throws IOException
  {
    if( this.readDataFromBlock )
    {
      if( this.blockDataPosition >= this.blockDataBytes )
	readNextBlock();
      
      return this.blockDataBytes - this.blockDataPosition;
    }
    else
      return this.realInputStream.available();
  }

  public void close() throws IOException
  {
    this.realInputStream.close();
  }

  public boolean readBoolean() throws IOException
  {
    return this.dataInputStream.readBoolean();
  }

  public byte readByte() throws IOException
  {
    return this.dataInputStream.readByte();
  }

  public int readUnsignedByte() throws IOException
  {
    return this.dataInputStream.readUnsignedByte();
  }
  
  public short readShort() throws IOException
  {
    return this.dataInputStream.readShort();
  }

  public int readUnsignedShort() throws IOException
  {
    return this.dataInputStream.readUnsignedShort();
  }

  public char readChar() throws IOException
  {
    return this.dataInputStream.readChar();
  }

  public int readInt() throws IOException
  {
    return this.dataInputStream.readInt();
  }

  public long readLong() throws IOException
  {
    return this.dataInputStream.readLong();
  }

  public float readFloat() throws IOException
  {
    return this.dataInputStream.readFloat();
  }

  public double readDouble() throws IOException
  {
    return this.dataInputStream.readDouble();
  }

  public void readFully( byte data[] ) throws IOException
  {
    this.dataInputStream.readFully( data );
  }

  public void readFully( byte data[], int offset, int size )
    throws IOException
  {
    this.dataInputStream.readFully( data, offset, size );
  }

  public int skipBytes( int len ) throws IOException
  {
    return this.dataInputStream.skipBytes( len );
  }

  /**
     @deprecated
     @see java.io.DataInputStream#readLine()
  */
  public String readLine() throws IOException
  {
    return this.dataInputStream.readLine();
  }

  public String readUTF() throws IOException
  {
    return this.dataInputStream.readUTF();
  }

  
  /**
     This class allows a class to specify exactly which fields should
     be read, and what values should be read for these fields.
     
     XXX: finish up comments
  */
  public static abstract class GetField
  {
    public abstract ObjectStreamClass getObjectStreamClass();

    public abstract boolean defaulted( String name )
      throws IOException, IllegalArgumentException;

    public abstract boolean get( String name, boolean defvalue )
      throws IOException, IllegalArgumentException;

    public abstract char get( String name, char defvalue)
      throws IOException, IllegalArgumentException;
    
    public abstract byte get( String name, byte defvalue )
      throws IOException, IllegalArgumentException;

    public abstract short get( String name, short defvalue )
      throws IOException, IllegalArgumentException;

    public abstract int get( String name, int defvalue )
      throws IOException, IllegalArgumentException;

    public abstract long get( String name, long defvalue )
      throws IOException, IllegalArgumentException;

    public abstract float get( String name, float defvalue )
      throws IOException, IllegalArgumentException;

    public abstract double get( String name, double defvalue )
      throws IOException, IllegalArgumentException;

    public abstract Object get( String name, Object defvalue)
      throws IOException, IllegalArgumentException;
  }

  public GetField readFields()
    throws IOException, ClassNotFoundException, NotActiveException
  {
    if( true )
      throw new RuntimeException( "putFields() not implemented!" );

    if( this.currentObject == null || this.currentObjectStreamClass == null )
      throw new NotActiveException( "readFields called by non-active class and/or object" );

    if( fieldsAlreadyRead )
      throw new NotActiveException( "readFields called but fields already read from stream (by defaultReadObject or readFields)" );
    
    // XXX actually read the data
    return new GetField()
    {
      public ObjectStreamClass getObjectStreamClass()
      {
	return currentObjectStreamClass;
      }

      public boolean defaulted( String name )
	throws IOException, IllegalArgumentException
      {
	return currentObjectStreamClass.getField( name ) == null;
      }
      
      public boolean get( String name, boolean defvalue )
	throws IOException, IllegalArgumentException
      {
	ObjectStreamField field = getField( name, Boolean.TYPE );

	if( field == null )
	  return defvalue;
	
	return primFieldData[ field.getOffset() ] == 0 ? false : true;
      }
      
      public char get( String name, char defvalue )
	throws IOException, IllegalArgumentException
      {
	ObjectStreamField field = getField( name, Character.TYPE );

	if( field == null )
	  return defvalue;
	
	int off = field.getOffset();
	
	return (char)(((primFieldData[off++] & 0xFF) << 8)
		      | (primFieldData[off] & 0xFF));
      }
      
      public byte get( String name, byte defvalue )
	throws IOException, IllegalArgumentException
      {
	ObjectStreamField field = getField( name, Byte.TYPE );

	if( field == null )
	  return defvalue;
	
	return primFieldData[ field.getOffset() ];
      }
      
      public short get( String name, short defvalue )
	throws IOException, IllegalArgumentException
      {
	ObjectStreamField field = getField( name, Short.TYPE );

	if( field == null )
	  return defvalue;
	
	int off = field.getOffset();
	
	return (short)(((primFieldData[off++] & 0xFF) << 8)
		       | (primFieldData[off] & 0xFF));	
      }
      
      public int get( String name, int defvalue )
	throws IOException, IllegalArgumentException
      {
	ObjectStreamField field = getField( name, Integer.TYPE );

	if( field == null )
	  return defvalue;
	
	int off = field.getOffset();
	
	return ((primFieldData[off++] & 0xFF) << 24)
	  | ((primFieldData[off++] & 0xFF) << 16)
	  | ((primFieldData[off++] & 0xFF) << 8)
	  | (primFieldData[off] & 0xFF);
      }
      
      public long get( String name, long defvalue )
	throws IOException, IllegalArgumentException
      {
	ObjectStreamField field = getField( name, Long.TYPE );

	if( field == null )
	  return defvalue;
	
	int off = field.getOffset();
	
	return (long)(((primFieldData[off++] & 0xFF) << 56)
		      | ((primFieldData[off++] & 0xFF) << 48)
		      | ((primFieldData[off++] & 0xFF) << 40)
		      | ((primFieldData[off++] & 0xFF) << 32)
		      | ((primFieldData[off++] & 0xFF) << 24)
		      | ((primFieldData[off++] & 0xFF) << 16)
		      | ((primFieldData[off++] & 0xFF) << 8)
		      | (primFieldData[off] & 0xFF));
      }

      public float get( String name, float defvalue )
	throws IOException, IllegalArgumentException
      {
	ObjectStreamField field = getField( name, Float.TYPE );

	if( field == null )
	  return defvalue;
	
	int off = field.getOffset();
	
	return Float.intBitsToFloat(((primFieldData[off++] & 0xFF) << 24)
				    | ((primFieldData[off++] & 0xFF) << 16)
				    | ((primFieldData[off++] & 0xFF) << 8)
				    | (primFieldData[off] & 0xFF));
      }

      public double get( String name, double defvalue )
	throws IOException, IllegalArgumentException
      {
	ObjectStreamField field = getField( name, Double.TYPE );

	if( field == null )
	  return defvalue;
	
	int off = field.getOffset();
	
	return Double.longBitsToDouble(
	  (long)(((primFieldData[off++] & 0xFF) << 56)
		 | ((primFieldData[off++] & 0xFF) << 48)
		 | ((primFieldData[off++] & 0xFF) << 40)
		 | ((primFieldData[off++] & 0xFF) << 32)
		 | ((primFieldData[off++] & 0xFF) << 24)
		 | ((primFieldData[off++] & 0xFF) << 16)
		 | ((primFieldData[off++] & 0xFF) << 8)
		 | (primFieldData[off] & 0xFF)));
      }

      public Object get( String name, Object defvalue )
	throws IOException, IllegalArgumentException
      {
	ObjectStreamField field = getField( name, null );

	if( field == null )
	  return defvalue;
	
	return objects[ field.getOffset() ];
      }

      private ObjectStreamField getField( String name, Class type )
	throws IllegalArgumentException
      {
	ObjectStreamField field = currentObjectStreamClass.getField( name );

	if( field == null )
	  return null;
	
	Class field_type = field.getType();

	if( type == field_type ||
	    ( type != null && field_type.isPrimitive() ) )
	  return field;

	throw new IllegalArgumentException( "Field requested is of type "
					    + field_type.getName()
					    + ", but requested type was "
					    + (type == null ? 
					       "Object" : type.getName()) );
      }

      private byte[] primFieldData;
      private Object[] objects;
    };
    
  }


  /**
     Protected constructor that allows subclasses to override
     deserialization.  This constructor should be called by subclasses
     that wish to override <code>readObject(Object)</code>.  This
     method does a security check <i>NOTE: currently not
     implemented</i>, then sets a flag that informs
     <code>readObject(Object)</code> to call the subclasses
     <code>readObjectOverride(Object)</code> method.

     @see readObjectOverride(Object)
  */
  protected ObjectInputStream()
    throws IOException, StreamCorruptedException
  {
    //XXX: security check
    this.useSubclassMethod = true;
  }


  /**
     This method allows subclasses to override the default
     de serialization mechanism provided by
     <code>ObjectInputStream</code>.  To make this method be used for
     writing objects, subclasses must invoke the 0-argument
     constructor on this class from there constructor.

     @see ObjectInputStream()
  */
  protected Object readObjectOverride()
    throws ClassNotFoundException, IOException, OptionalDataException
  {
    throw new IOException( "Subclass of ObjectInputStream must implement readObjectOverride" );
  }
  

  // assigns the next availible handle to OBJ
  private int assignNewHandle( Object obj )
  {
    this.objectLookupTable.put( new Integer( this.nextOID ),
			     new ObjectIdentityWrapper( obj ) );

    try
    {
      DEBUG( "Assigning handle " + this.nextOID );
      DEBUGln( " to " + obj );
    }
    catch( Throwable t ) {}

    return this.nextOID++;
  }


  private Object processResoultion( Object obj, int handle )
    throws IOException
  {
    if( obj instanceof Resolvable )
      obj = ((Resolvable)obj).readResolve();
    
    if( this.resolveEnabled )
      obj = resolveObject( obj );
    
    this.objectLookupTable.put( new Integer( handle ),
				new ObjectIdentityWrapper( obj ) );

    return obj;
  }
  

  private void clearHandles()
  {
    this.objectLookupTable.clear();
    this.nextOID = baseWireHandle;
  }
  

  private void readNextBlock() throws IOException
  {
    DEBUG( "MARKER " );
    readNextBlock( this.realInputStream.readByte() );
  }


  private void readNextBlock( byte marker ) throws IOException
  {
    if( marker == TC_BLOCKDATA )
    {
      DEBUG( "BLOCK DATA SIZE " );
      this.blockDataBytes = this.realInputStream.readUnsignedByte();
    }
    else if( marker == TC_BLOCKDATALONG )
    {
      DEBUG( "BLOCK DATA LONG SIZE " );
      this.blockDataBytes = this.realInputStream.readInt();
    }
    else
    {
      throw new EOFException( "Attempt to read primitive data, but no data block is active." );
    }

    if( this.blockData.length < this.blockDataBytes )
      this.blockData = new byte[ this.blockDataBytes ];
	
    this.realInputStream.readFully( this.blockData, 0, this.blockDataBytes );
    this.blockDataPosition = 0;
  }


  private void readArrayElements( Object array, Class clazz )
    throws ClassNotFoundException, IOException
  {
    if( clazz.isPrimitive() )
    {
      if( clazz == Boolean.TYPE )
      {
	boolean[] cast_array = (boolean[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = this.realInputStream.readBoolean();
	return;
      }
      if( clazz == Byte.TYPE )
      {
	byte[] cast_array = (byte[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = this.realInputStream.readByte();
	return;
      }
      if( clazz == Character.TYPE )
      {
	char[] cast_array = (char[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = this.realInputStream.readChar();
	return;
      }
      if( clazz == Double.TYPE )
      {
	double[] cast_array = (double[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = this.realInputStream.readDouble();
	return;
      }
      if( clazz == Float.TYPE )
      {
	float[] cast_array = (float[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = this.realInputStream.readFloat();
	return;
      }
      if( clazz == Integer.TYPE )
      {
	int[] cast_array = (int[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = this.realInputStream.readInt();
	return;
      }
      if( clazz == Long.TYPE )
      {
	long[] cast_array = (long[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = this.realInputStream.readLong();
	return;
      }
      if( clazz == Short.TYPE )
      {
	short[] cast_array = (short[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = this.realInputStream.readShort();
	return;
      }
    }
    else
    {
      Object[] cast_array = (Object[])array;
      for( int i=0; i < cast_array.length; i++ )
 	  cast_array[i] = readObject();
    }
  }
  

  private void readFields( Object obj, ObjectStreamField[] stream_fields,
			   boolean call_read_method,
			   ObjectStreamClass stream_osc )
    throws ClassNotFoundException, IOException
  {
    if( call_read_method )
    {
      fieldsAlreadyRead = false;
      setBlockDataMode( true );
      callReadMethod( obj, stream_osc.forClass() );
      setBlockDataMode( false );
      return;
    }

    ObjectStreamField[] real_fields =
      ObjectStreamClass.lookup( stream_osc.forClass() ).getFields();

    boolean default_initialize, set_value;
    String field_name = null;
    Class type = null;
    ObjectStreamField stream_field = null;
    ObjectStreamField real_field = null;
    int stream_idx = 0;
    int real_idx = 0;

    while( stream_idx < stream_fields.length
	   && real_idx < real_fields.length )
    {
      default_initialize = false;
      set_value = true;

      if( stream_idx == stream_fields.length )
	default_initialize = true;
      else
      {
	stream_field = stream_fields[ stream_idx ];
	type = stream_field.getType();
      }
      
      if( real_idx == real_fields.length )
	set_value = false;
      else
      {
	real_field = real_fields[ real_idx ];
	type = real_field.getType();
	field_name = real_field.getName();
      }

      if( set_value && !default_initialize )
      {
	int comp_val =
	  real_field.compareTo( stream_field );

	if( comp_val < 0 )
	{
	  default_initialize = true;
	  real_idx++;
	}
	else if( comp_val > 0 )
	{
	  set_value = false;
	  stream_idx++;
	}
	else
	{
	  real_idx++;
	  stream_idx++;
	}
      }

      if( type == Boolean.TYPE )
      {
	boolean value =
	  default_initialize ? false : this.realInputStream.readBoolean();
	if( set_value )
	  setBooleanField( obj, field_name, value );
      }
      else if( type == Byte.TYPE )
      {
	byte value =
	  default_initialize ? 0 : this.realInputStream.readByte();
	if( set_value )
	  setByteField( obj, field_name, value );
      }
      else if( type == Character.TYPE )
      {
	char value =
	  default_initialize ? 0 : this.realInputStream.readChar();
	if( set_value )
	  setCharField( obj, field_name, value );
      }
      else if( type == Double.TYPE )
      {
	double value =
	  default_initialize ? 0 : this.realInputStream.readDouble();
	if( set_value )
	  setDoubleField( obj, field_name, value );
      }
      else if( type == Float.TYPE )
      {
	float value =
	  default_initialize ? 0 : this.realInputStream.readFloat();
	if( set_value )
	  setFloatField( obj, field_name, value );
      }
      else if( type == Integer.TYPE )
      {
	int value =
	  default_initialize ? 0 : this.realInputStream.readInt();
	if( set_value )
	  setIntField( obj, field_name, value );
      }
      else if( type == Long.TYPE )
      {
	long value =
	  default_initialize ? 0 : this.realInputStream.readLong();
	if( set_value )
	  setLongField( obj, field_name, value );
      }
      else if( type == Short.TYPE )
      {
	short value =
	  default_initialize ? 0 : this.realInputStream.readShort();
	if( set_value )
	  setShortField( obj, field_name, value );
      }
      else
      {
	Object value =
	  default_initialize ? null : readObject();
	if( set_value )
	  setObjectField( obj, field_name,
			  real_field.getTypeString(), value );
      }
    }
  }
  

  // Toggles writing primitive data to block-data buffer.
  private void setBlockDataMode( boolean on )
  {
    DEBUGln( "Setting block data mode to " + on );

    this.readDataFromBlock = on;

    if( on )
      this.dataInputStream = this.blockDataInput;
    else
      this.dataInputStream = this.realInputStream;
  }

  
  // returns a new instance of REAL_CLASS that has been constructed
  // only to th level of CONSTRUCTOR_CLASS (a super class of REAL_CLASS)
  private Object newObject( Class real_class, Class constructor_class )
  {
    try
    {
      Object obj = allocateObject( real_class );
      callConstructor( constructor_class, obj );
      return obj;
    }
    catch( InstantiationException e )
    {
      return null;
    }
  }
  

  // runs all registered ObjectInputValidations in prioritized order
  // on OBJ
  private void invokeValidators() throws InvalidObjectException
  {
    Object[] validators = new Object[ this.validators.size() ];
    this.validators.copyInto( validators );
    Arrays.sort( validators );
    
    try
    {
      for( int i=0; i < validators.length; i++ )
	((ObjectInputValidation)validators[i]).validateObject();
    }
    finally
    {
      this.validators.removeAllElements();
    }
  }
  

  // this native method is used to get access to the protected method
  // of the same name in SecurityManger
  private static native ClassLoader currentClassLoader( SecurityManager sm );

  private native void callReadMethod( Object obj, Class clazz );

  private native Object allocateObject( Class clazz )
    throws InstantiationException;

  private native void callConstructor( Class clazz, Object obj );

  private native void setBooleanField( Object obj, String field_name,
				       boolean val );
  private native void setByteField( Object obj, String field_name,
				    byte val );
  private native void setCharField( Object obj, String field_name,
				    char val );
  private native void setDoubleField( Object obj, String field_name,
				      double val );
  private native void setFloatField( Object obj, String field_name,
				     float val );
  private native void setIntField( Object obj, String field_name,
				   int val );
  private native void setLongField( Object obj, String field_name,
				    long val );
  private native void setShortField( Object obj, String field_name,
				     short val );
  private native void setObjectField( Object obj, String field_name,
					String type_code, Object val );


  private static final int BUFFER_SIZE = 1024;
  private static final Class[] ourReadObjectParams = { ObjectInputStream.class };
  
  private DataInputStream realInputStream;
  private DataInputStream dataInputStream;
  private DataInputStream blockDataInput;
  private int blockDataPosition;
  private int blockDataBytes;
  private byte[] blockData;
  private boolean useSubclassMethod;
  private int nextOID;
  private boolean resolveEnabled;
  private Hashtable objectLookupTable;
  private Object currentObject;
  private ObjectStreamClass currentObjectStreamClass;
  private boolean readDataFromBlock;
  private boolean isDeserializing;
  private boolean fieldsAlreadyRead;
  private Vector validators;

  static
  {
    //DEBUG
    //System.out.println( "Using ObjectInputStream" );
    //eDEBUG

    System.loadLibrary( "javaio" );
  }


  private void DEBUG( String msg )
  {
    System.out.print( msg );
  }
  

  private void DEBUGln( String msg )
  {
    System.out.println( msg );
  }
  
}


// used to keep a prioritized list of object validators
class ValidatorAndPriority implements Comparable
{
  int priority;
  ObjectInputValidation validator;

  ValidatorAndPriority( ObjectInputValidation validator, int priority )
  {
    this.priority = priority;
    this.validator = validator;
  }

  public int compareTo( Object o )
  {
    ValidatorAndPriority vap = (ValidatorAndPriority)o;
    return this.priority - vap.priority;
  }
}
