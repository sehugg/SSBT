package com.hamco.fastj;

import java.util.*;
import de.fub.bytecode.*;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.generic.*;
 
public class FJContext
{
	WeakHashMap fjclasses = new WeakHashMap();
	HashMap type_str_map = new HashMap();
	
	public FJContext()
	{
		type_str_map.put(Type.BOOLEAN, "jboolean");
		type_str_map.put(Type.BYTE, "jbyte");
		type_str_map.put(Type.CHAR, "jchar");
		type_str_map.put(Type.DOUBLE, "jdouble");
		type_str_map.put(Type.FLOAT, "jfloat");
		type_str_map.put(Type.INT, "jint");
		type_str_map.put(Type.LONG, "jlong");
		type_str_map.put(Type.SHORT, "jshort");
		type_str_map.put(Type.VOID, "void");
		type_str_map.put(Type.NULL, "jobject*");
		type_str_map.put(ReturnAddressType.TYPE, "jobject*");
	}
	
	public FJClass lookupFJClass(String classname)
	{
		FJClass fjc = (FJClass)fjclasses.get(classname);
		if (fjc == null)
		{
			System.err.println("Looking up " + classname);
			JavaClass jc = Repository.lookupClass(classname);
			if (jc == null)
				throw new RuntimeException("Class \"" + classname + "\" not found");
			fjc = new FJClass(this, jc);
			fjclasses.put(classname, fjc);
		}
		return fjc;
	}
	
	public String class2ident(String classname)
	{
		StringBuffer st = new StringBuffer();
		int l = classname.length();
		for (int i=0; i<l; i++)
		{
			char ch = classname.charAt(i);
			switch (ch)
			{
				case '.':
				case '/':
					st.append("__"); break;
				case '$':
					st.append("_0"); break;
				default : 
					st.append(ch); break;
			}
		}
		return st.toString();
	}
	
	public String method2ident(String name, boolean statik)
	{
		return (statik ? "jms__" : "jm__") + sig2ident(name);
	}
	
	public String field2ident(String name)
	{
		return "jf__" + sig2ident(name);
	}
	
	public String type2str(Type t)
	{
		String s = (String)type_str_map.get(t);
		if (s == null)
		{
			if (t instanceof ObjectType)
			{
				s = "jo__" + class2ident(((ObjectType)t).getClassName()) + '*';
			}
			else if (t instanceof ArrayType)
			{
				s = "jarray< " + type2str(((ArrayType)t).getElementType()) + " >*";
			}
			else
				throw new RuntimeException("WTF??? " + t);
			type_str_map.put(t, s);
		}
		return s;
	}
	
	public String inlinetype2str(Type t)
	{
		String s;
		if (t instanceof ObjectType)
		{
			s = "jo__" + class2ident(((ObjectType)t).getClassName());
		}
		else if (t instanceof ArrayType)
		{
			s = "jarray< " + type2str(((ArrayType)t).getElementType()) + " >";
		}
		else
			throw new RuntimeException(t + " needs to be array or object type");
		return s;
	}
	
	public static Type type2internaltype(Type t)
	{
		if (t.equals(Type.BYTE) || t.equals(Type.CHAR) || t.equals(Type.SHORT) || t.equals(Type.BOOLEAN))
			return Type.INT;
		else
			return t;
	}
		
	static final int IDENT_BASE = 26+10; //??
	
	public String sig2ident(String s)
	{
		// escape weird chars in the sig
		StringBuffer st = new StringBuffer();
		for (int i=0; i<s.length(); i++)
		{
			char ch = s.charAt(i);
			if ((ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z')||(ch>='0'&&ch<='9'))
			{
				st.append(ch);
			} else {
				st.append('_');
				if (ch != '_')
				{	
					st.append(Integer.toString((int)ch, IDENT_BASE));
				}
				st.append('_');
			}
		}
		return st.toString();
	}

	public String getMethodTypedefName(FJClass c, int vtblindex)
	{
		if (vtblindex < 0)
			throw new RuntimeException("Method is not virtual");
		return "jvft__" + class2ident(c.jc.getClassName()) + "_" + vtblindex;
	}
	
	static Map objtypes = new HashMap();
	static Map arraytypes = new HashMap();
	
	public static ObjectType getObjectType(String classname)
	{
		ObjectType t = (ObjectType)objtypes.get(classname);
		if (t == null)
		{
			t = new ObjectType(classname);
			objtypes.put(classname, t);
		}
		return t;
	}
	
	public static ArrayType getArrayType(Type elemt)
	{
		ArrayType t = (ArrayType)arraytypes.get(elemt);
		if (t == null)
		{
			t = new ArrayType(elemt, 1);
			arraytypes.put(elemt, t);
		}
		return t;
	}
	
}
