/*
 * java.lang.Class: part of the Java Class Libraries project.
 * Copyright (C) 1998 John Keiser
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.lang;

import java.lang.reflect.*;
import gnu.java.lang.*;

/**
 ** A Class represents a Java type.  There will never be
 ** multiple Class objects with identical names and
 ** ClassLoaders.<P>
 **
 ** Arrays with identical type and number of dimensions
 ** share the same class (and null "system" ClassLoader,
 ** incidentally).  The name of an array class is
 ** <CODE>[&lt;type name&gt;</CODE> ... for example,
 ** String[]'s class is <CODE>[java.lang.String</CODE>.
 ** boolean, byte, short, char, int, long, float and double
 ** have the "type name" of Z,B,S,C,I,J,F,D for the
 ** purposes of array classes.  If it's a multidimensioned
 ** array, the same principle applies:
 ** <CODE>int[][][]</CODE> == <CODE>[[[I</CODE>.<P>
 **
 ** As of 1.1, this class represents primitive types as
 ** well.  You can get to those by looking at
 ** java.lang.Integer.TYPE, java.lang.Boolean.TYPE, etc.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 6 1998
 ** @since JDK1.0
 **/

public final class Class {
	/**
	 ** This field is for internal use only
	 **/
	private Object method_table;
	
	private Object[] signers = null;
	private String name;
	private Class superclass;
	private Class declaring_class;
	private Class element_type;
	private Class[] interfaces;
	private Class[] inner_classes;
	private Constructor[] constructors;
	private Method[] methods;
	private Field[] fields;
	private ClassLoader classloader;
	private int modifiers;
	private byte classtype;

	private static final int BOOLEAN = 4;
	private static final int CHAR = 5;
	private static final int FLOAT = 6;
	private static final int DOUBLE = 7;
	private static final int BYTE = 8;
	private static final int SHORT = 9;
	private static final int INT = 10;
	private static final int LONG = 11;
	private static final int VOID = 12;
	
	private static final int OBJECT = 13;
	private static final int ARRAY = 14;

	private Class() {
	}
	
	/**
	 ** Constructor for primitive class
	 **/
	private Class(int primtype)
	{
		this.classtype = (byte)primtype;
	}

	/**
	 ** Constructor for array class
	 **/
	private Class(Class elemtype)
	{
		this.classtype = ARRAY;
		this.element_type = elemtype;
	}
	
	/**
	 ** Constructor for object class
	 **/
	private Class(String name)
	{
		this.classtype = OBJECT;
		this.name = name;
	}

	/**
	 ** Used to build names for array types and primitive types
	 **/	
	private String getSignature()
	{
		switch (classtype)
		{
			case BOOLEAN : return "Z";
			case CHAR : return "C";
			case FLOAT : return "F";
			case DOUBLE : return "D";
			case BYTE : return "B";
			case SHORT : return "S";
			case INT : return "I";
			case LONG : return "J";
			case VOID : return "V";
			case OBJECT : return 'L'+name+';';
			case ARRAY : return '[' + element_type.getSignature();
			default :
				throw new Error("Invalid class type: " + classtype);
		}
	}

	/** Return the human-readable form of this Object.  For
	 ** class, that means "interface " or "class " plus the
	 ** classname.
	 ** @return the human-readable form of this Object.
	 ** @since JDK1.0
	 **/
	public String toString() {
		return (isInterface() ? "interface " : "class ") + getName();
	}

	/** Get the name of this class, separated by dots for
	 ** package separators.
	 ** @return the name of this class.
	 ** @since JDK1.0
	 **/ 
	public String getName()
	{
		if (name == null)
			name = getSignature();
		return name;
	}

	/** Get whether this class is an interface or not.  Array
	 ** types are not interfaces.
	 ** @return whether this class is an interface or not.
	 ** @since JDK1.0
	 **/
	public boolean isInterface()
	{
		return ((modifiers&0x200) != 0);
	}

	/** Get the direct superclass of this class.  If this is
	 ** an interface, it will get the direct superinterface.
	 ** @return the direct superclass of this class.
	 ** @since JDK1.0
	 **/
	public Class getSuperclass()
	{
		return superclass;
	}

	/** Get the interfaces this class <EM>directly</EM>
	 ** implements, in the order that they were declared.
	 ** This method may return an empty array, but will
	 ** never return null.
	 ** @return the interfaces this class directly implements.
	 ** @since JDK1.0
	 **/
	public Class[] getInterfaces()
	{
		return (Class[])interfaces.clone();
	}

	/** Get a new instance of this class by calling the
	 ** no-argument constructor.
	 ** @return a new instance of this class.
	 ** @exception InstantiationException if there is not a
	 **            no-arg constructor for this class, or if
	 **            an exception occurred during instantiation,
	 **            or if the target constructor throws an
	 **            exception.
	 ** @exception IllegalAccessException if you are not
	 **            allowed to access the no-arg constructor of
	 **            this Class for whatever reason.
	 ** @since JDK1.0
	 **/
	public native Object newInstance() 
	throws InstantiationException, IllegalAccessException;
	/*
	public Object newInstance() throws InstantiationException, IllegalAccessException {
		try {
			return getConstructor(new Class[0]).newInstance(new Object[0]);
		} catch(SecurityException e) {
			throw new IllegalAccessException("Cannot access no-arg constructor");
		} catch(IllegalArgumentException e) {
			throw new UnknownError("IllegalArgumentException thrown from Constructor.newInstance().  Something is rotten in Denmark.");
		} catch(InvocationTargetException e) {
			throw new InstantiationException("Target threw an exception.");
		} catch(NoSuchMethodException e) {
			throw new InstantiationException("Method not found");
		}
	}
	*/

	/** Get the ClassLoader that loaded this class.  If it was
	 ** loaded by the system classloader, this method will
	 ** return null.
	 ** @return the ClassLoader that loaded this class.
	 ** @since JDK1.0
	 **/
	public ClassLoader getClassLoader()
	{
		return classloader;
	}

	/** Use the system classloader to load and link a class.
	 ** @param name the name of the class to find.
	 ** @return the Class object representing the class.
	 ** @exception ClassNotFoundException if the class was not
	 **            found by the system classloader.
	 ** @since JDK1.0
	 **/
	public static native Class forName(String name) throws ClassNotFoundException;

	/** Discover whether an Object is an instance of this
	 ** Class.  Think of it as almost like
	 ** <CODE>o instanceof (this class)</CODE>.
	 ** @param o the Object to check
	 ** @return whether o is an instance of this class.
	 ** @since JDK1.1
	 **/
	public native boolean isInstance(Object o);

	/** Discover whether an instance of the Class parameter
	 ** would be an instance of this Class as well.  Think of
	 ** doing <CODE>isInstance(c.newInstance())</CODE> or even
	 ** <CODE>c instanceof (this class)</CODE>.
	 ** @param c the class to check
	 ** @return whether an instance of c would be an instance
	 **         of this class as well.
	 ** @since JDK1.1
	 **/
	public native boolean isAssignableFrom(Class c);

	/** Return whether this class is an array type.
	 ** @return whether this class is an array type.
	 ** @since JDK1.1
	 **/
	public boolean isArray() {
		return classtype==ARRAY;
	}

	/** Return whether this class is a primitive type.  A
	 ** primitive type class is a class representing a kind of
	 ** "placeholder" for the various primitive types.  You
	 ** can access the various primitive type classes through
	 ** java.lang.Boolean.TYPE, java.lang.Integer.TYPE, etc.
	 ** @return whether this class is a primitive type.
	 ** @since JDK1.1
	 **/
	public boolean isPrimitive()
	{
		return classtype<=VOID;
	}

	/** If this is an array, get the Class representing the
	 ** type of array.  Examples: [[java.lang.String would
	 ** return [java.lang.String, and calling getComponentType
	 ** on that would give java.lang.String.  If this is not
	 ** an array, returns null.
	 ** @return the array type of this class, or null.
	 ** @since JDK1.1
	 **/
	public Class getComponentType() {
		return element_type;
	}

	/** Get the signers of this class.
	 ** @return the signers of this class.
	 ** @since JDK1.1
	 **/
	public Object[] getSigners() {
		return signers;
	}

	/** Set the signers of this class.
	 ** @param signers the signers of this class.
	 ** @since JDK1.1
	 **/
	void setSigners(Object[] signers) {
		this.signers = signers;
	}

	/** Get a resource URL using this class's package using
	 ** the getClassLoader().getResource() method.  If this
	 ** class was loaded using the system classloader,
	 ** ClassLoader.getSystemResource() is used instead.<P>
	 **
	 ** If the name you supply is absolute (it starts with a
	 ** <CODE>/</CODE>), then it is passed on to getResource()
	 ** as is.  If it is relative, the package name is 
	 ** prepended, with <CODE>.</CODE>'s replaced with
	 ** <CODE>/</CODE> slashes.<P>
	 **
	 ** The URL returned is system- and classloader-
	 ** dependent, and could change across implementations.
	 ** @param name the name of the resource, generally a
	 **        path.
	 ** @return the URL to the resource.
	 **/
	public java.net.URL getResource(String name) {
		if(name.length() > 0 && name.charAt(0) != '/') {
			name = ClassHelper.getPackagePortion(getName()).replace('.','/') + "/" + name;
		}
		ClassLoader c = getClassLoader();
		if(c == null) {
			return ClassLoader.getSystemResource(name);
		} else {
			return c.getResource(name);
		}
	}

	/** Get a resource using this class's package using the
	 ** getClassLoader().getResource() method.  If this class
	 ** was loaded using the system classloader,
	 ** ClassLoader.getSystemResource() is used instead.<P>
	 **
	 ** If the name you supply is absolute (it starts with a
	 ** <CODE>/</CODE>), then it is passed on to getResource()
	 ** as is.  If it is relative, the package name is 
	 ** prepended, with <CODE>.</CODE>'s replaced with
	 ** <CODE>/</CODE> slashes.<P>
	 **
	 ** The URL returned is system- and classloader-
	 ** dependent, and could change across implementations.
	 ** @param name the name of the resource, generally a
	 **        path.
	 ** @return An InputStream with the contents of the
	 **         resource in it.
	 **/
	public java.io.InputStream getResourceAsStream(String name) {
		if(name.length() > 0 && name.charAt(0) != '/') {
			name = ClassHelper.getPackagePortion(getName()).replace('.','/') + "/" + name;
		}
		ClassLoader c = getClassLoader();
		if(c == null) {
			return ClassLoader.getSystemResourceAsStream(name);
		} else {
			return c.getResourceAsStream(name);
		}
	}

	/** Get the modifiers of this class.  These can be checked
	 ** against using java.lang.reflect.Modifier.
	 ** @return the modifiers of this class.
	 ** @see java.lang.reflect.Modifer
	 ** @since JDK1.1
	 **/
	public int getModifiers()
	{
		return modifiers;
	}

	/** If this is an inner class, return the class that
	 ** declared it.  If not, return null.
	 ** @return the declaring class of this class.
	 ** @since JDK1.1
	 **/
	public Class getDeclaringClass()
	{
		return declaring_class;
	}

	/** Get all the public inner classes, declared in this
	 ** class or inherited from superclasses, that are
	 ** members of this class.
	 ** @return all public inner classes in this class.
	 **/
	public Class[] getClasses()
	{
		return (Class[])inner_classes.clone();
	}

	/** Get all the inner classes declared in this class.
	 ** @return all inner classes declared in this class.
	 ** @exception SecurityException if you do not have access
	 **            to non-public inner classes of this class.
	 **/
	public native Class[] getDeclaredClasses() throws SecurityException;

	/** Get a public constructor from this class.
	 ** @param args the argument types for the constructor.
	 ** @return the constructor.
	 ** @exception NoSuchMethodException if the constructor does
	 **            not exist.
	 ** @exception SecurityException if you do not have access to public
	 **            members of this class.
	 **/
	public native Constructor getConstructor(Class[] args) throws NoSuchMethodException, SecurityException;

	/** Get a constructor declared in this class.
	 ** @param args the argument types for the constructor.
	 ** @return the constructor.
	 ** @exception NoSuchMethodException if the constructor does
	 **            not exist in this class.
	 ** @exception SecurityException if you do not have access to
	 **            non-public members of this class.
	 **/
	public native Constructor getDeclaredConstructor(Class[] args) throws NoSuchMethodException, SecurityException;

	/** Get all public constructors from this class.
	 ** @return all public constructors in this class.
	 ** @exception SecurityException if you do not have access to public
	 **            members of this class.
	 **/
	public native Constructor[] getConstructors() throws SecurityException;

	/** Get all constructors declared in this class.
	 ** @return all constructors declared in this class.
	 ** @exception SecurityException if you do not have access to
	 **            non-public members of this class.
	 **/
	public native Constructor[] getDeclaredConstructors() throws SecurityException;


	/** Get a public method from this class.
	 ** @param name the name of the method.
	 ** @param args the argument types for the method.
	 ** @return the method.
	 ** @exception NoSuchMethodException if the method does
	 **            not exist.
	 ** @exception SecurityException if you do not have access to public
	 **            members of this class.
	 **/
	public native Method getMethod(String name, Class[] args) throws NoSuchMethodException, SecurityException;

	/** Get a method declared in this class.
	 ** @param name the name of the method.
	 ** @param args the argument types for the method.
	 ** @return the method.
	 ** @exception NoSuchMethodException if the method does
	 **            not exist in this class.
	 ** @exception SecurityException if you do not have access to
	 **            non-public members of this class.
	 **/
	public native Method getDeclaredMethod(String name, Class[] args) throws NoSuchMethodException, SecurityException;

	/** Get all public methods from this class.
	 ** @return all public methods in this class.
	 ** @exception SecurityException if you do not have access to public
	 **            members of this class.
	 **/
	public native Method[] getMethods() throws SecurityException;

	/** Get all methods declared in this class.
	 ** @return all methods declared in this class.
	 ** @exception SecurityException if you do not have access to
	 **            non-public members of this class.
	 **/
	public native Method[] getDeclaredMethods() throws SecurityException;


	/** Get a public field from this class.
	 ** @param name the name of the field.
	 ** @return the field.
	 ** @exception NoSuchFieldException if the field does
	 **            not exist.
	 ** @exception SecurityException if you do not have access to public
	 **            members of this class.
	 **/
	public native Field getField(String name) throws NoSuchFieldException, SecurityException;

	/** Get a field declared in this class.
	 ** @param name the name of the field.
	 ** @return the field.
	 ** @exception NoSuchFieldException if the field does
	 **            not exist in this class.
	 ** @exception SecurityException if you do not have access to
	 **            non-public members of this class.
	 **/
	public native Field getDeclaredField(String name) throws NoSuchFieldException, SecurityException;

	/** Get all public fields from this class.
	 ** @return all public fields in this class.
	 ** @exception SecurityException if you do not have access to public
	 **            members of this class.
	 **/
	public native Field[] getFields() throws SecurityException;

	/** Get all fields declared in this class.
	 ** @return all fieilds declared in this class.
	 ** @exception SecurityException if you do not have access to
	 **            non-public members of this class.
	 **/
	public native Field[] getDeclaredFields() throws SecurityException;

}

