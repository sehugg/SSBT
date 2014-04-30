package com.hamco.fastj;

import java.io.*;
import de.fub.bytecode.*;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.util.*;

public class Main
{
	FJContext ctx;

	Main()
	{
		ctx = new FJContext();	
	}
	
   void convert(String classname)
   {
      FJClass c = ctx.lookupFJClass(classname);
      if (c == null)
      {
      	System.err.println("***CLASS " + classname + " NOT FOUND");
      	return;
      }
      PrintStream out = System.out;
      FJC_ClassGen fjcg = new FJC_ClassGen(c);
      fjcg.outputStructDef(out);
      //c.outputMethodProtos(out);
      fjcg.outputMethodCodes(out);
   }

   public static void main(String[] args)
   throws Exception
   {
      Main main = new Main();
      for (int i=0; i<args.length; i++)
      {
      	if (args[i].charAt(0) == '@')
      	{
      		FileReader fr = new FileReader(args[i].substring(1));
      		BufferedReader br = new BufferedReader(fr);
      		String line;
      		while ((line = br.readLine()) != null)
      		{
      			main.convert(line);
      		}
      	} 
      	else if (args[i].equals("-d"))
      		FJMethodInfo.debug = true;
      	else
	         main.convert(args[i]);
      }
   }
}

