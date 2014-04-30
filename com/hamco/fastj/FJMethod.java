package com.hamco.fastj;

import java.lang.ref.*;
import java.io.*;
import de.fub.bytecode.*;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.generic.*;
import java.util.*;

class FJMethod
{
	FJClass c;
	Method m;
	MethodGen mg;
	//FJMethodInfo info;
	//SoftReference methodinfo;
	boolean info_locked;
	int vtblindex = -1;
	
	HashMap methodinfos = new HashMap();
	static int num_gets, num_hits;
	
	public int getNumParamLocals()
	{
		return (mg.getArgTypes().length + (mg.isStatic() ? 0 : 1));
	}
	
	public FJMethod(FJClass c, Method m)
	{
		this.c = c;
		this.m = m;
		mg = new MethodGen(m, c.jc.getClassName(), c.jcg.getConstantPool());
	}
	
	public FJMethodInfo getInfo()
	throws RecursiveIntrospectionException
	{
		return getInfo(null);
	}
	
	public FJMethodInfo getInfo(Map param_assums)
	throws RecursiveIntrospectionException
	{
		Object key = (param_assums != null) ? (Object)param_assums.toString() : (Object)Boolean.TRUE;
		
		if (info_locked)
		{
			// todo: is this bad??
			//methodinfos.put(key, Boolean.FALSE);
			throw new RecursiveIntrospectionException("Recursive call for method " + 
				c.getClazz().getClassName() + "::" + m); 	//todo: new exception
		}

		// don't do native or abstract
		if (m.isNative() || m.isAbstract())
			return null;
			
		Object value = methodinfos.get(key);
		if (value == Boolean.FALSE)
			throw new RecursiveIntrospectionException("Recursive call stored in " +
				c.getClazz().getClassName() + "::" + m);
		FJMethodInfo info = (FJMethodInfo)value;
		num_gets++;

		if (info == null)
		{
			if (methodinfos.size() > 0)
				System.err.println("info cache: " + (100.0f*num_hits/num_gets) + " % hits, cache size = " + methodinfos.size());
			info_locked = true;
			System.err.print("Getting info for : " + c.getClazz().getClassName() + "::" + m);
			if (param_assums != null)
				System.err.print(" with " + param_assums);
			System.err.println();
			try {
				info = new FJMethodInfo(this, param_assums);
			} catch (RuntimeException re) {
				System.err.println("Error getting info for " + c.getClazz().getClassName() + "::" + m);
				throw re;
			}
			methodinfos.put(key, info);
			info_locked = false;
		} else
			num_hits++;
		//	System.err.println("Found info for " + c.getClazz().getClassName() + "::" + m + " with " + param_assums);
		return info;
	}
	
	public boolean isVirtual()
	{
		// if method is static or private, not virtual
		if (m.isStatic() || m.isPrivate())
			return false;
		// if a constructor, not virtual
		String mname = mg.getMethodName();
		if ("<init>".equals(mname) || "<clinit>".equals(mname))
			return false;
		// if method is final, or class is final,
		// and the method is not overridden from anywhere, 
		// it is not virtual
		if (m.isFinal() || c.jc.isFinal())
		{
			if (c.getSuper() == null || 
				 c.getSuper().findMethod(mg.getMethodName(), mg.getMethodSignature()) == null)
				return false;
		}
		return true;
	}
	
	public String getMethodTypedefName()
	{
		if (vtblindex < 0)
			throw new RuntimeException("Method " + m + " is not virtual");
		return "jvft__" + c.ctx.class2ident(c.jc.getClassName()) + "_" + vtblindex;
	}
	
	public String getMethodTypedef()
	{
		return getMethodTypedef(null);
	}
	
	public String getMethodTypedef(String typedefname)
	{
		FJContext ctx = c.ctx;
		StringBuffer st = new StringBuffer();
		st.append(ctx.type2str(mg.getReturnType()));
		st.append("(*");
		if (typedefname != null)
			st.append(typedefname);
		st.append(")(");
		if (!m.isStatic())
		{
			st.append(ctx.type2str(new ObjectType(c.jc.getClassName())));
		}
		Type[] args = mg.getArgTypes();
		for (int j=0; j<args.length; j++)
		{
			if (j>0 || !m.isStatic())
				st.append(", ");
			st.append(ctx.type2str(args[j]));
		}
		st.append(")");
		return st.toString();
	}
	
	public String toString()
	{
		return m.toString();
	}
	
	public String getMethodFuncName()
	{
		return c.ctx.method2ident(m.getName(), m.isStatic());
	}
	
	private void addInsnDeps(Instruction insn, Set set)
	{
		Type t;
		if (insn instanceof de.fub.bytecode.generic.FieldOrMethod)
		{
			t = ((de.fub.bytecode.generic.FieldOrMethod)insn).getClassType(c.jcg.getConstantPool());
			FJClass.addDepends(t, set);
		}
		else if ((insn instanceof NEW) || (insn instanceof ANEWARRAY) || (insn instanceof MULTIANEWARRAY))
		{
			t = ((CPInstruction)insn).getType(c.jcg.getConstantPool());
			FJClass.addDepends(t, set);
		}
	}
	
	// gets the classes that depend on this static method
	public Set getStaticDependencies()
	{
		if (!mg.isStatic())
			throw new RuntimeException("method must be static: " + m);
		HashSet set = new HashSet();
		InstructionList il = mg.getInstructionList();
		InstructionHandle ih = il.getStart();
		while (ih != null)
		{
			addInsnDeps(ih.getInstruction(), set);
			ih = ih.getNext();
		}
		return set;
	}

	// get parameter type -- if not static, param 0 is "this"	
	// returns the "internal" type
	public FJMethodInfo.TypeAssumption getParamInternalType(int i)
	{
		Type t = getParamType(i);
		t = c.ctx.type2internaltype(t);
		return new FJMethodInfo.TypeAssumption(c.ctx, t);
	}

	// get parameter type -- if not static, param 0 is "this"	
	// returns the "internal" type
	public Type getParamType(int i)
	{
		Type t;
		if (i==0 && !mg.isStatic())
		{
			t = new ObjectType(c.jc.getClassName());
		} else {
			t = mg.getArgType(i-(mg.isStatic()?0:1));
		}
		return t;
	}

}

