package com.hamco.fastj;

import java.io.*;
import de.fub.bytecode.*;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.generic.*;
import java.util.*;

public class FJC_ClassGen
{
	FJClass fjc;
	Set depends = new HashSet(); // class names of dependencies
	
	public FJC_ClassGen(FJClass fjc)
	{
		this.fjc = fjc;
	}

	static final String[] ACCESS_NAMES = {	"public", "protected", "private" };
	
	static int getAccessIndex(de.fub.bytecode.classfile.FieldOrMethod fm)
	{
		return 0; // always public
		/*
		if (fm.isPrivate())
			return 2;
		else
			return 0;
		*/
	}
	
	static String constantToString(Constant c)
	{
		// todo: fix
		if (c instanceof ConstantInteger)
			return ( Integer.toString( ((ConstantInteger)c).getBytes() ) );
		else if (c instanceof ConstantLong)
		{
			long l = ((ConstantLong)c).getBytes();
			return ("FJ_MAKELONG(" + (l&0xffffffffl) + "u, " + ((l&0xffffffff00000000l)>>32) + "u)");
		}
		else if (c instanceof ConstantFloat)
			return ( Float.toString( ((ConstantFloat)c).getBytes() ) );
		else if (c instanceof ConstantDouble)
			return ( Double.toString( ((ConstantDouble)c).getBytes() ) );
		else if (c instanceof ConstantString)
		{
			return "jsc__" + ((ConstantString)c).getStringIndex();
		}
		else
			throw new RuntimeException("Constant " + c + " not recognized");
	}
	
	static String quote(String s)
	{
		byte[] barr = string2UTF(s);
		StringBuffer st = new StringBuffer();
		st.append('"');
		for (int i=2; i<barr.length; i++)
		{
			char ch = (char)(barr[i]&0xff);
			if (ch == '"')
				// quote quotes
				st.append("\\\"");
			else if (ch == '\\')
				st.append("\\\\");
			else if (ch >= 32 && ch <= 127)
				// regular char
				st.append(ch);
			else {
				// octal constant
				String oc = Integer.toString(ch, 8);
				while (oc.length() < 3)
					oc = '0'+oc;
				st.append('\\');
				st.append(oc);
			}
		}
		st.append('"');
		return st.toString();
	}
	
	static String quote(ConstantUtf8 u8)
	{
		return quote(u8.getBytes());
	}
	
	public void outputClassDepends(PrintStream out)
	{
		Set set = fjc.getInterfaceDepends();
		Iterator it = set.iterator();
		while (it.hasNext())
		{
			String cname = (String)it.next();
			out.println("class jo__" + fjc.ctx.class2ident(cname) + ';');
		}
		out.println();
	}
	
	public void outputCodeIncludes(PrintStream out)
	{
		out.println("#include \"fastj.hpp\"");
		Set set = fjc.getAllDepends();
		set.addAll(this.depends); // add stuff added by FJC_MethodGen
		Iterator it = set.iterator();
		while (it.hasNext())
		{
			String cname = (String)it.next();
			out.println("#include \"jo__" + fjc.ctx.class2ident(cname) + FJC_Generator.HEADER_SUFFIX + '\"');
		}
		out.println();
	}
	
	public void outputMethodTypedefs(PrintStream out)
	{
		FJContext ctx = fjc.ctx;
		for (int i=0; i<fjc.vtblsize; i++)
		{
			FJMethod m = fjc.getMethodByVtblIndex(i);
			out.print("typedef ");
			out.print(m.getMethodTypedef(ctx.getMethodTypedefName(fjc, i)));
			out.println(";");
		}
		out.println();
	}
	
	static byte[] string2UTF(String s)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bos);
		try {
			dout.writeUTF(s);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe.toString());
		}
		return bos.toByteArray();
	}
	
	public void outputStaticFields(PrintStream out)
	{
		// output string constants
		ConstantPool cp = fjc.jc.getConstantPool();
		for (int i=0; i<cp.getLength(); i++)
		{
			Constant c = cp.getConstant(i);
			if (c instanceof ConstantString)
			{
				ConstantString cs = (ConstantString)c;
				ConstantUtf8 u8 = (ConstantUtf8)cp.getConstant(cs.getStringIndex());
				String cname = "jsc__" + cs.getStringIndex();
				out.println( "static jo__java__lang__String* " + cname + ";");
			}
		}
		// output fields of the class which are static,
		// we assign values to those that have values in the class decl
		for (int i=0; i<fjc.fields.length; i++)
		{
			FJField f = fjc.fields[i];
			if (f.f.isStatic() && !f.isConst())
			{
				f.outputPrototype(out, false);
			}
		}
		out.println();
	}
	
	void outputInitStrings(PrintStream out)
	{
		// initialize string constants
		ConstantPool cp = fjc.jc.getConstantPool();
		for (int i=0; i<cp.getLength(); i++)
		{
			Constant c = cp.getConstant(i);
			if (c instanceof ConstantString)
			{
				ConstantString cs = (ConstantString)c;
				ConstantUtf8 u8 = (ConstantUtf8)cp.getConstant(cs.getStringIndex());
				String cname = "jsc__" + cs.getStringIndex();
				out.println( "\t" + cname + " = FJ_MAKESTRING(" + quote(u8) + ");" );
			}
		}
	}
	
	public void outputClassTable(PrintStream out)
	{
		out.println("jo__java__lang__Class* " + fjc.getClassStructName() + " = fj_register_clinit_func(" +
			quote(fjc.jc.getClassName()) + ", &" + fjc.getClassInitFuncName() + ");");
		out.println();
		out.println("jo__java__lang__Class* " + fjc.getClassInitFuncName() + "()");
		out.println("{");
		// if we already init'ed, return the class we created last time
		out.println("\tif (" + fjc.getClassStructName() + " != NULL)");
		out.println("\t\treturn " + fjc.getClassStructName() + ";");
		out.println(
			"\tFJ_DEBUGMSG(\"Initializing class `" + fjc.jc.getClassName() + "'\\n\");"
		);
		// create the class object
		out.println("\tjo__java__lang__Class* clazz = (jo__java__lang__Class*)FJ_NEWOBJECT(jo__java__lang__Class);");
		// todo: call constructor
		out.println("\tclazz->jf__classtype == jo__java__lang__Class::jf__OBJECT;\n");
		// fill in the method table, and vice versa
		out.println("\tclazz->jf__method__table = (jo__java__lang__Object*)&" + fjc.getMethodTableName() + ";");
		out.println("\t" + fjc.getMethodTableName() + ".clazz = clazz;");
		// todo: utf8?
		out.println("\tclazz->jf__name = FJ_MAKESTRING(" + fjc.getMethodTableName() + ".u8name);");
		if (fjc.getSuper() != null)
			out.println("\tclazz->jf__superclass = " + fjc.getSuper().getClassInitFuncName() + "();");
		out.println("\tclazz->jf__modifiers = 0x" + Integer.toString(fjc.jc.getAccessFlags(), 16) + ";");
		out.println("\t" + fjc.getClassStructName() + " = clazz;");
		// init string constants
		outputInitStrings(out);
		// call <clinit>
		FJMethod clinit = fjc.getMethod("<clinit>", "()V");
		if (clinit != null)
		{
			out.println("\t" + fjc.getStructName() + "::jms___1o_clinit_1q_();");
		}
		// return the class that was created
		out.println("\treturn clazz;");
		out.println("}");
		out.println();
	}
	
	public void outputInterfaceTables(PrintStream out)
	{
		// interfaces don't have interface tables 'cuz they're abstract
		FJClass intrfcs[] = fjc.getImplements();
		if (intrfcs.length == 0)
			return;
		for (int i=0; i<intrfcs.length; i++)
		{
			FJClass ifc = intrfcs[i];
			out.println("jvfuncptr " + fjc.getInterfaceTableName(intrfcs[i].jc.getClassName()) + "[] = {");
			for (int j=0; j<ifc.methods.length; j++)
			{
				FJMethod m = ifc.methods[j];
				String name = m.mg.getMethodName();
				// ignore the <clinit> method
				// todo: this should be better handled
				if (name.startsWith("<"))
				{
					out.println("\tNULL,\t// " + name);
					continue;
				}
				String sig = m.mg.getMethodSignature();
				FJMethod cm = fjc.findMethod(name, sig);
				if (cm == null)
					throw new RuntimeException("Could not find interface method " + m + " in " + fjc);
				out.println("\t(jvfuncptr)(" + cm.getMethodTypedef() + ")&" + 
					cm.c.getStructName() + "::" + cm.getMethodFuncName() + ",\t// " + i);
			}
			out.println("\tNULL");
			out.println("};");
		}
		out.println();
		out.println("jinterface_entry " + fjc.getInterfaceTableName() + "[] = {");
		for (int i=0; i<intrfcs.length; i++)
		{
			out.println("\t{ &" + intrfcs[i].getMethodTableName() + ", " +
				fjc.getInterfaceTableName(intrfcs[i].jc.getClassName()) + " },");
		}
		out.println("\t{ NULL, NULL }");
		out.println("};");
	}
	
	public void outputMethodTable(PrintStream out)
	{
		out.println("jmethod_table " + fjc.getMethodTableName() + " = {");
		out.println("\tsizeof(" + fjc.getStructName() + "),");
		out.println("\tNULL,\t// class object will go here");
		// interfaces
		FJClass intrfcs[] = fjc.getImplements();
		out.println("\t" + intrfcs.length + ",\t// interface count");
		if (intrfcs.length == 0)
			out.println("\tNULL,\t// no interfaces");
		else
			out.println("\t" + fjc.getInterfaceTableName() + ",\t// interfaces");
		// superclass
		if (fjc.getSuper() != null)
			out.println("\t&" + fjc.getSuper().getMethodTableName() + ",\t// superclass");
		else
			out.println("\tNULL,\t// no parent");
		out.println("\tNULL,\t// not an array");
		out.println("\t" + quote(fjc.jc.getClassName()) + ",\t// class name in UTF-8");
		out.println("\t" + fjc.jc.getAccessFlags() + ",\t//modifier bits");
		// print addr of default ctor
		if (!fjc.jc.isInterface())
		{
			FJMethod defctor = fjc.findMethod("<init>", "()V");
			if (defctor == null)
				throw new RuntimeException("Could not find default ctor in " + fjc);
			out.println("\t(jvdefaultctor)(" + defctor.getMethodTypedef() + ")&" + 
				defctor.c.getStructName() + "::" + defctor.getMethodFuncName() + ",\t// default ctor");
		} else 
			out.println("\tNULL,\t// interface has no ctor");
		// print addrs of virtual methods
		for (int i=0; i<fjc.vtblsize; i++)
		{
			FJMethod m = fjc.getMethodByVtblIndex(i);
//			System.err.println(i + " : " + m.c);
			out.println("\t(jvfuncptr)(" + m.getMethodTypedef() + ")&" + 
				m.c.getStructName() + "::" +
				m.getMethodFuncName() + ",\t// " + i);
		}
		out.println("};");
		out.println();
		//out.println("const jmethod_table* " + fjc.getStructName() + 
		//	"::JMETHOD_TABLE = &" + fjc.getMethodTableName() + ';');
		//out.println();
	}
	
	public void outputStructDef(PrintStream out)
	{
		out.println("extern jmethod_table " + fjc.getMethodTableName() + ";");
		out.println("extern jo__java__lang__Class* " + fjc.getClassInitFuncName() + "();");
		out.println();
		out.print("class " + fjc.getStructName());
		FJClass sup = fjc.getSuper();
		out.println(" : public " + (sup != null ? sup.getStructName() : "jobject"));
		out.println("{");
		// print out the static variable that points to the class info
		out.println("public:");
		out.println("\t" + fjc.getStructName() + "() { jmtab=&" + fjc.getMethodTableName() + "; }");
		//out.println("\tstatic const jmethod_table* JMETHOD_TABLE;");
		// now print out protos of the fields & the methods
		int access = -1;
		int oldaccess = -1;
		for (int i=0; i<fjc.fields.length; i++)
		{
			access = getAccessIndex(fjc.fields[i].f);
			if (access != oldaccess)
			{
				out.println(ACCESS_NAMES[access]+':');
				oldaccess = access;
			}
			out.print('\t');
			fjc.fields[i].outputPrototype(out, true);
		}
		for (int i=0; i<fjc.methods.length; i++)
		{
			access = getAccessIndex(fjc.methods[i].m);
			if (access != oldaccess)
			{
				out.println(ACCESS_NAMES[access]+':');
				oldaccess = access;
			}
			out.print('\t');
			new FJC_MethodGen(fjc.methods[i]).outputPrototype(out, true);
		}
		out.println("};");
	}
	
	public void outputMethodProtos(PrintStream out)
	{
		for (int i=0; i<fjc.methods.length; i++)
		{
			new FJC_MethodGen(fjc.methods[i]).outputPrototype(out, true);
		}
	}

	public void outputMethodCodes(PrintStream out)
	{
		for (int i=0; i<fjc.methods.length; i++)
		{
			// don't output code for native methods
			if (!fjc.methods[i].mg.isNative())
			{
				out.println();
				FJC_MethodGen mgen = new FJC_MethodGen(fjc.methods[i]);
				mgen.outputMethodCode(out);
				depends.addAll(mgen.depends);
				out.println();
			}
		}
	}

	public void outputNativeStubs(PrintStream out)
	{
		for (int i=0; i<fjc.methods.length; i++)
		{
			// *do* output headers for native methods
			if (fjc.methods[i].mg.isNative())
			{
				out.println();
				new FJC_MethodGen(fjc.methods[i]).outputPrototype(out, false);
				out.println("{");
				out.println(
					"\tFJ_DEBUGMSG(\"*** incomplete native method " + fjc + "." +
					fjc.methods[i] + "\\n\");\n" 
				);
				out.println("}");
			}
		}
	}

}

