package com.hamco.fastj;

import java.io.*;
import de.fub.bytecode.*;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.generic.*;
import java.util.*;

public class FJClass
{
	FJContext ctx;
	JavaClass jc;
	String classname;
	ClassGen jcg;
	FJField[] fields;
	FJMethod[] methods;
	int size;
	int vtblsize;
	
	FJClass(FJContext ctx, JavaClass jc)
	{
		this.ctx = ctx;
		this.jc = jc;
		classname = jc.getClassName();
		jcg = new ClassGen(jc);
		computeFields();
		computeMethods();
	}
	
	public FJMethod getMethod(String name, String signature)
	{
		for (int i=0; i<methods.length; i++)
		{
			if (methods[i].mg.getMethodName().equals(name) &&
				methods[i].mg.getMethodSignature().equals(signature))
				return methods[i];
		}
		return null;
	}
	
	public FJMethod findMethod(String name, String signature)
	{
		FJMethod fjm = getMethod(name, signature);
//		System.err.println("Looking for " + name + signature + " in " + this + ", " + fjm);
		if (fjm == null)
		{
//			System.err.println("Super = " + getSuper());
			if (getSuper() != null)
				return getSuper().findMethod(name, signature);
		}
		return fjm;
	}

	int getMethodVtblIndex(String name, String signature)
	{
		FJMethod fjm = findMethod(name, signature);
		return (fjm == null) ? -1 : fjm.vtblindex;
	}

	public FJMethod getMethodByVtblIndex(int vindex)
	{
		for (int i=0; i<methods.length; i++)
		{	
//		System.err.println(jc.getClassName() + " : " + methods[i].vtblindex + " , " + vindex);
			if (methods[i].vtblindex == vindex)
				return methods[i];
		}
		if (getSuper() != null)
			return getSuper().getMethodByVtblIndex(vindex);
		else
			return null;
	}
	
	void computeMethods()
	{
		// todo: interfaces don't get computed right
		FJClass sup = getSuper();
		if (sup != null)
			vtblsize = sup.vtblsize;
		Method[] marr = jc.getMethods();
		int l = marr.length;
		methods = new FJMethod[l];
		for (int i=0; i<l; i++)
		{
			FJMethod fjm = methods[i] = new FJMethod(this, marr[i]);
			MethodGen mg = fjm.mg;
			// compute the index into the vtbl for this object
			int vindex = -1;
			if (fjm.isVirtual())
			{
				if (sup != null)
					vindex = sup.getMethodVtblIndex(mg.getMethodName(), mg.getMethodSignature());
				if (vindex < 0)
					vindex = vtblsize++;
//				System.err.println(fjm.c + "::" + fjm + " is virtual");
			}
			fjm.vtblindex = vindex;
		}
	}
	
	void computeFields()
	{
		int curoffset = 4;	// every class needs 1st slot for vtbl
		if (getSuper() != null)
			curoffset = getSuper().size;
		size = curoffset;
		Field[] farr = jc.getFields();
		int l = farr.length;
		fields = new FJField[l];
		for (int i=0; i<l; i++)
		{
			// this cons will modify 'size'
			fields[i] = new FJField(this, farr[i]);
		}
		// align to int boundary
		size = (size+3) & (~3);
		//System.err.println("size of " + jc.getClassName() + " = " + size);
	}
	
	public JavaClass getClazz()
	{
		return jc;
	}
	
	public FJClass getSuper()
	{
		String sname = jc.getSuperclassName();
		if (sname != null && !sname.equals(classname) 
			&& !(jc.isInterface() && sname.equals("java.lang.Object")) 
			)
			return ctx.lookupFJClass(jc.getSuperclassName());
		else
			return null;
	}
	
	public String getStructName()
	{
		return "jo__" + ctx.class2ident(jc.getClassName());
	}
	
	public String getClassStructName()
	{
		return "jc__" + ctx.class2ident(jc.getClassName());
	}
	
	public String getClassInitFuncName()
	{
		return "jinit__" + ctx.class2ident(jc.getClassName());
	}
	
	public String getMethodTableName()
	{
		return "jmt" + getStructName();
	}

	// add the class name of an object type references by type 't'
	// to set 'set'	
	static void addDepends(Type t, Set set)
	{
		if (t instanceof ObjectType)
		{
			set.add( ((ObjectType)t).getClassName() );
		}
		else if (t instanceof ArrayType)
		{
			addDepends( ((ArrayType)t).getElementType(), set );
		}
	}
	
	public Set getInterfaceDepends()
	{
		Set set = new HashSet();
		// depends on superclasses
		JavaClass[] supers = Repository.getSuperClasses(jc);
		for (int i=0; i<supers.length; i++)
			set.add(supers[i].getClassName());
		// depends on interfaces
		FJClass[] inters = this.getImplements();
		for (int i=0; i<inters.length; i++)
			set.add(inters[i].jc.getClassName());
		// depends on stuff in the methods & the fields
		for (int i=0; i<methods.length; i++)
		{
			addDepends(methods[i].mg.getReturnType(), set);
			Type[] argtypes = methods[i].mg.getArgTypes();
			for (int j=0; j<argtypes.length; j++)
			{
				//System.err.println("===="+argtypes[j]);
				addDepends(argtypes[j], set);
			}
		}
		for (int i=0; i<fields.length; i++)
		{
			addDepends(fields[i].fg.getType(), set);
		}
		return set;
	}
	
	// converts [java/util/String to java.util.String
	// and [B to 'null'
	private String demunge(String s)
	{
		if (s.length()<=1)
			return null;
		if (s.charAt(0)=='[')
			return demunge(s.substring(1));
		else if (s.charAt(0)=='L' && s.charAt(s.length()-1)==';')
			return demunge(s.substring(1,s.length()-1));
		else
			return s.replace('/','.');
	}

	// just look for L...; inside a string and extract those
	private Vector extractClassNames(String sig)
	{
		Vector v = new Vector();
		if (sig.charAt(0) != '(')
			throw new RuntimeException("Signature " + sig + " needs to begin with '('");
		int i=1;
		int p;
		boolean go=true;
		do {
			switch (sig.charAt(i))
			{
				case 'L' :
					p = sig.indexOf(';',i);
					v.addElement(sig.substring(i+1,p));
					i = p;
					break;
				case ')':
					go = false;
					break;
				default:
					i++;
			}
		} while (go);
		v.addElement(sig.substring(i+1));
		return v;
	}
	
	public Set getAllDepends()
	{
		Set set = new HashSet();
		// go thru constant pool, add all class files
		ConstantPoolGen cpg = jcg.getConstantPool();
		for (int i=0; i<cpg.getSize(); i++)
		{
			Constant cons = cpg.getConstant(i);
			if (cons != null)
			{
				if (cons instanceof ConstantCP)
				{
					// todo: this all blowz
					int ntindex = ((ConstantCP)cons).getNameAndTypeIndex();
					ConstantNameAndType cnat = (ConstantNameAndType)cpg.getConstant(ntindex);
					int nameindex = cnat.getSignatureIndex();
					ConstantUtf8 u8 = (ConstantUtf8)cpg.getConstant(nameindex);
					String sig = (u8.getBytes());
					//System.err.println(sig);
					if (cons instanceof ConstantFieldref)
					{
						String s = demunge(sig);
						if (s != null)
							set.add(s);
					} else {
						Vector v = extractClassNames(sig);
						for (int j=0; j<v.size(); j++)
						{
							String s = demunge(v.elementAt(j).toString());
							if (s != null)
								set.add(s);
						}
					}
					// now get the class
					int classindex = ((ConstantCP)cons).getClassIndex();
					cons = cpg.getConstant(classindex);
				}
				if (cons instanceof ConstantClass)
				{
					int nameindex = ((ConstantClass)cons).getNameIndex();
					ConstantUtf8 u8 = (ConstantUtf8)cpg.getConstant(nameindex);
					String name = demunge(u8.getBytes());
					if (name != null)
						set.add(name);
				}
			}
		}
		Set ideps = getInterfaceDepends();
		set.addAll(ideps);
		return set;
	}
	
	public String toString()
	{
		return jc.getClassName();
	}
	
	void addImplementsToSet(Set set)
	{
		String[] names = jc.getInterfaceNames();
		for (int i=0; i<names.length; i++)
		{
			FJClass c = ctx.lookupFJClass(names[i]);
			if (c == null)
				throw new RuntimeException("Could not look up interface \"" + names[i] + "\"");
			if (!set.contains(c))
			{
				set.add(c);
				c.addImplementsToSet(set);
			}
		}
		// add parent to set also
		if (getSuper() != null && !set.contains(getSuper()))
		{
			getSuper().addImplementsToSet(set);
		}
	}

	// get all interfaces implemented by this class and any of its parents!
	// (including superinterfaces)
	// returns length 0 array if class itself is an interface
	public FJClass[] getImplements()
	{
		if (jc.isInterface())
			return new FJClass[0];
		Set set = new HashSet();
		addImplementsToSet(set);
		FJClass[] classes = new FJClass[set.size()];
		Iterator it = set.iterator();
		for (int i=0; i<classes.length; i++)
			classes[i] = (FJClass)it.next();
		return classes;
	}
	
	public String getInterfaceTableName(String iname)
	{
		return "jit__" + ctx.class2ident(jc.getClassName()) + "_i" + ctx.class2ident(iname);
	}
	
	public String getInterfaceTableName()
	{
		return "jie" + getStructName();
	}
}
