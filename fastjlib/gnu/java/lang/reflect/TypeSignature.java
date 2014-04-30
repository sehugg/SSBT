/*************************************************************************
/* TypeSignature.java -- Class used to compute type signatures
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

package gnu.java.lang.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
   This class provides static methods that can be used to compute
   type-signatures of <code>Class</code>s or <code>Member</code>s.
   More specific methods are also provided for computing the
   type-signature of <code>Constructor</code>s and
   <code>Method</code>s.  Methods are also provided to go in the
   reverse direction.
*/
public class TypeSignature
{
 
  /**
     Returns a <code>String</code> representing the type-encoding of
     CLAZZ.  Type-encodings are computed as follows:

     <pre>
     boolean -> "Z"
     byte    -> "B"
     char    -> "C"
     double  -> "D"
     float   -> "F"
     int     -> "I"
     long    -> "J"
     short   -> "S"
     void    -> "V"
     arrays  -> "[" + type-encoding of component type
     object  -> "L"
                 + fully qualified class name with "."'s replaced by "/"'s
                 + ";"</pre>
  */
  public static String getEncodingOfClass( Class clazz )
  {
    if( clazz.isPrimitive() )
    {
      if( clazz == Boolean.TYPE )
	return "Z";
      if( clazz == Byte.TYPE )
	return "B";
      if( clazz == Character.TYPE )
	return "C";
      if( clazz == Double.TYPE )
	return "D";
      if( clazz == Float.TYPE )
	return "F";
      if( clazz == Integer.TYPE )
	return "I";
      if( clazz == Long.TYPE )
	return "J";
      if( clazz == Short.TYPE )
	return "S";
      if( clazz == Void.TYPE )
	return "V";
      else
	throw new RuntimeException( "Unknown primitive class " + clazz );
    }
    else if( clazz.isArray() )
    {
      return '[' + getEncodingOfClass( clazz.getComponentType() );
    }
    else
    {
      String classname = clazz.getName();
      int name_len = classname.length();
      char[] buf = new char[ name_len + 2 ];
      buf[0] = 'L';
      classname.getChars( 0, name_len, buf, 1 );
      
      int i;
      for( i=1; i <= name_len; i++ )
      {
	if( buf[i] == '.' )
	  buf[i] = '/';
      }
      
      buf[i] = ';';
      return new String( buf );
    }
  }

  
  /**
     This function is the inverse of <code>getEncodingOfClass</code>.

     @see getEncodingOfClass

     @exception ClassNotFoundException If class encoded as type_code
     cannot be located.
  */
  public static Class getClassForEncoding( String type_code )
    throws ClassNotFoundException
  {
    if( type_code.equals( "B" ) )
      return Byte.TYPE;
    if( type_code.equals( "C" ) )
      return Character.TYPE;
    if( type_code.equals( "D" ) )
      return Double.TYPE;
    if( type_code.equals( "F" ) )
      return Float.TYPE;
    if( type_code.equals( "I" ) )
      return Integer.TYPE;
    if( type_code.equals( "J" ) )
      return Long.TYPE;
    if( type_code.equals( "S" ) )
      return Short.TYPE;
    if( type_code.equals( "Z" ) )
      return Boolean.TYPE;
    if( type_code.charAt( 0 ) == 'L' )
    {
      return Class.forName(
	type_code.substring( 1, type_code.length() - 1 ).replace( '/', '.' ));
    }
    if( type_code.charAt( 0 ) == '[' )
    {
      int last_bracket = type_code.lastIndexOf( '[' );
      String brackets = type_code.substring( 0, last_bracket + 1 );
      String component = type_code.substring( last_bracket + 1 );
      
      if( component.charAt( 0 ) == 'L' )
	component =
	  component.substring( 1, component.length() - 1 ).replace('/', '.');

      return Class.forName( brackets + component );
    }
    else
      throw new ClassNotFoundException( "Type code cannot be parsed as a valid class name" );
  }
  

  /**
     Returns a <code>String</code> representing the type-encoding of
     M.  The type-encoding of a method is:

     "(" + type-encodings of parameter types + ")" 
     + type-encoding of return type
  */
  public static String getEncodingOfMethod( Method m )
  {
    String returnEncoding = getEncodingOfClass( m.getReturnType() );
    Class[] paramTypes = m.getParameterTypes();
    String[] paramEncodings = new String[ paramTypes.length ];

    String paramEncoding;
    int size = 2; // make room for parens
    for( int i=0; i < paramTypes.length; i++ )
    {
      paramEncoding = getEncodingOfClass( paramTypes[i] );
      size += paramEncoding.length();
      paramEncodings[i] = paramEncoding;
    }
    
    size += returnEncoding.length();

    StringBuffer buf = new StringBuffer( size );
    buf.append( '(' );
    
    for( int i=0; i < paramTypes.length; i++ )
    {
      buf.append( paramEncodings[i] );
    }
    
    buf.append( ')' );
    buf.append( returnEncoding );
    
    return buf.toString();
  }


  /**
     Returns a <code>String</code> representing the type-encoding of
     C.  The type-encoding of a method is:

     "(" + type-encodings of parameter types + ")V" 
  */
  public static String getEncodingOfConstructor( Constructor c )
  {
    Class[] paramTypes = c.getParameterTypes();
    String[] paramEncodings = new String[ paramTypes.length ];

    String paramEncoding;
    int size = 3; // make room for parens and V for return type
    for( int i=0; i < paramTypes.length; i++ )
    {
      paramEncoding = getEncodingOfClass( paramTypes[i] );
      size += paramEncoding.length();
      paramEncodings[i] = paramEncoding;
    }
    
    StringBuffer buf = new StringBuffer( size );
    buf.append( '(' );
    
    for( int i=0; i < paramTypes.length; i++ )
    {
      buf.append( paramEncodings[i] );
    }
    
    buf.append( ")V" );
    
    return buf.toString();
  }


  /**
     Returns a <code>String</code> representing the type-encoding of
     MEM.  <code>Constructor</code>s are handled by
     <code>getEncodingOfConstructor</code>.  <code>Method</code>s are
     handled by <code>getEncodingOfMethod</code>.  <code>Field</code>s
     are handled by returning the encoding of the type of the
     <code>Field</code>.
  */
  public static String getEncodingOfMember( Member mem )
  {
    if( mem instanceof Constructor )
      return getEncodingOfConstructor( (Constructor)mem );
    if( mem instanceof Method )
      return getEncodingOfMethod( (Method)mem );
    else // Field
      return getEncodingOfClass( ((Field)mem).getType() );
  }
}
