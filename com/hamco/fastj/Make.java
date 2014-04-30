package com.hamco.fastj;

import java.io.*;
import de.fub.bytecode.*;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.util.*;

public class Make
{
	FJContext ctx;
	FJC_Generator cgen;
	int flags = FJC_Generator.HEADERS | FJC_Generator.CODE;

	Make()
	{
		ctx = new FJContext();	
		cgen = new FJC_Generator(ctx, "include", "src");
	}
	
   void convert(String classname)
   throws IOException
   {
      FJClass c = ctx.lookupFJClass(classname);
      if (c == null)
      {
      	System.err.println("***CLASS " + classname + " NOT FOUND");
      	return;
      }
      cgen.make(c, flags);
   }

   public static void main(String[] args)
   throws Exception
   {
      Make main = new Make();
      for (int i=0; i<args.length; i++)
      {	
      	String arg = args[i];
      	if (arg.charAt(0) == '@')
      	{
      		FileReader fr = new FileReader(args[i].substring(1));
      		BufferedReader br = new BufferedReader(fr);
      		String line;
      		while ((line = br.readLine()) != null)
      		{
      			main.convert(line);
      		}
      		fr.close();
      	}
      	else if (arg.equals("-cp"))
      		Repository.setClassPath(new ClassPath(args[++i]));
      	else if (arg.equals("-r"))
      		main.flags |= FJC_Generator.VISIT_HDRCLASSES;
      	else if (arg.equals("-R"))
      		main.flags |= FJC_Generator.VISIT_ALLCLASSES;
      	else if (arg.equals("-h"))
      		main.flags &= ~FJC_Generator.CODE;
      	else if (arg.equals("-N"))
      		main.flags = FJC_Generator.NATIVESTUBS;
      	else if (arg.equals("-F"))
      		main.flags |= FJC_Generator.FORCE_REBUILD;
      	else if (arg.equals("-Q"))
      		FJMethodInfo.recurse_method_calls = false;
      	else if (arg.equals("-d")) {
      		FJMethodInfo.debug = true;
				FJC_MethodGen.debug = true;
      	} else
	         main.convert(args[i]);
      }
   }
}

