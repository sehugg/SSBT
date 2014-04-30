package com.hamco.fastj;

import java.io.*;
import java.util.*;

public class FJC_Generator
{
	FJContext ctx;
	String hdrpath, codepath, nativecodepath;
	FJC_ClassGen fjcg;
	boolean force;
	
	static String HEADER_SUFFIX = ".hpp";
	static String CODE_SUFFIX = ".cpp";
	static String TEMP_SUFFIX = ".tmp";
	
	public FJC_Generator(FJContext ctx, String hdrpath, String codepath)
	{
		this.ctx = ctx;
		this.hdrpath = hdrpath;
		this.codepath = codepath;
		// todo: not good
		this.nativecodepath = codepath + File.separator + ".." + File.separator + 
			"native" + File.separator + "src";
	}
	
	boolean needsUpdate(File f, FJClass fjc)
	{
		return (force || !f.exists());
	}
	
	PrintStream createFileStream(File f)
	throws IOException
	{
		FileOutputStream fout = new FileOutputStream(f);
		BufferedOutputStream bout = new BufferedOutputStream(fout);
		return new PrintStream(bout);
	}
	
	public void writeHeader(FJClass fjc, File f)
	throws IOException
	{
		PrintStream out = createFileStream(f);
		String struct = fjc.getStructName();
		out.println("#ifndef _H_" + struct);
		out.println("#define _H_" + struct);
		out.println();
		out.println("#ifndef _FASTJ_H");
		out.println("#include \"fastj.hpp\"");
		out.println("#endif");
		FJClass supr = fjc.getSuper();
		if (supr != null)
		{
			out.println("#include \"" + supr.getStructName() + HEADER_SUFFIX + "\"");
			out.println();
		}
		fjcg = new FJC_ClassGen(fjc);
		fjcg.outputClassDepends(out);
		fjcg.outputStructDef(out);
//		fjcg.outputMethodTypedefs(out);
		out.println();
		out.println("#endif");
		out.close();
		//if (supr != null)
		//	makeHeader(supr);
	}
	
	public boolean makeHeader(FJClass fjc)
	throws IOException
	{
		String filename = hdrpath + File.separator + 
			fjc.getStructName() + HEADER_SUFFIX;
		File f = new File(filename);
		if (needsUpdate(f, fjc))
		{
			File f2 = new File(filename + TEMP_SUFFIX);
			writeHeader(fjc, f2);
			f.delete();
			f2.renameTo(f);
			return true;
		} else
			return false;
	}

	public void writeCode(FJClass fjc, File f)
	throws IOException
	{
		PrintStream out = createFileStream(f);
		String struct = fjc.getStructName();
		fjcg = new FJC_ClassGen(fjc);
		
		// output method code to baos, generating dependencies
		// todo: unfortunately StringWriter *AND* ByteArrayOutputStream seem to
		// be buggy ... thanx Sun ...
		//StringWriter baos = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		fjcg.outputMethodCodes(new PrintStream(baos));
		
		// now can do other stuff because dependencies are built
		fjcg.outputCodeIncludes(out);
		fjcg.outputStaticFields(out);
		fjcg.outputMethodCodes(out);
		fjcg.outputClassTable(out);
		fjcg.outputInterfaceTables(out);
		fjcg.outputMethodTable(out);
		if (out.checkError())
			throw new IOException("Error while writing code");
		out.close();
	}
	
	public boolean makeCode(FJClass fjc)
	throws IOException
	{
		String filename = codepath + File.separator + 
			fjc.getStructName() + CODE_SUFFIX;
		File f = new File(filename);
		if (needsUpdate(f, fjc))
		{
			File f2 = new File(filename + TEMP_SUFFIX);
			writeCode(fjc, f2);
			f.delete();
			f2.renameTo(f);
			return true;
		} else
			return false;
	}
	
	public void writeNativeStubs(FJClass fjc, File f)
	throws IOException
	{
		PrintStream out = createFileStream(f);
		String struct = fjc.getStructName();
		fjcg = new FJC_ClassGen(fjc);
		fjcg.outputCodeIncludes(out);
		fjcg.outputNativeStubs(out);
		out.close();
	}
	
	public boolean makeNativeStubs(FJClass fjc)
	throws IOException
	{
		String filename = nativecodepath + File.separator + 
			fjc.getStructName() + CODE_SUFFIX;
		File f = new File(filename);
		if (!f.exists())
		{
			// first check to see if we have native methods!
			boolean havenative = false;
			for (int i=0; i<fjc.methods.length; i++)
			{
				if (fjc.methods[i].mg.isNative())
				{
					havenative = true;
					break;
				}
			}
			if (!havenative)
				return false; // no native methods
			File f2 = new File(filename + TEMP_SUFFIX);
			writeNativeStubs(fjc, f2);
			f.delete();
			f2.renameTo(f);
			return true;
		} else
			return false;
	}
	
	public static final int HEADERS = 1;
	public static final int CODE = 2;
	public static final int VISIT_HDRCLASSES = 4;
	public static final int VISIT_ALLCLASSES = 8;
	public static final int NATIVESTUBS = 16;
	public static final int FORCE_REBUILD = 32;
	public static final int ALL = 0xffff;
	
	int make(FJClass fjc, int flags, Set visited)
	throws IOException
	{
		// if we've already been here, don't make it
		String sname = fjc.getStructName();
		int num_made = 0;
		if (!visited.contains(sname))
		{
			// mark the class as visited
			visited.add(sname);
			// now make the header & code
			if ( ((flags&HEADERS)!=0) && makeHeader(fjc) )
				num_made++;
			if ( ((flags&CODE)!=0) )
				makeCode(fjc);
			if ( ((flags&NATIVESTUBS)!=0) )
				makeNativeStubs(fjc);
			// visit all dependencies
			Set depends = null;
			if ( ((flags&VISIT_ALLCLASSES)!=0) )
				depends = fjc.getAllDepends();
			else if ( ((flags&VISIT_HDRCLASSES)!=0) )
				depends = fjc.getInterfaceDepends();
			if (depends != null)
			{
				Iterator it = depends.iterator();
				while (it.hasNext())
				{
					String name = (String)it.next();
					FJClass c = ctx.lookupFJClass(name);
					if (c == null)
						System.err.println("***CLASS " + name + " NOT FOUND");
					else
						num_made += make(c, flags, visited);
				}
			}
		}
		return num_made;
	}
	
	public int make(FJClass fjc, int flags)
	throws IOException
	{
		Set visited = new HashSet();
		force = ((flags & FORCE_REBUILD) != 0);
		return make(fjc, flags, visited);
	}

}
