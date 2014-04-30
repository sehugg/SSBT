import java.io.*;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.generic.*;
import de.fub.bytecode.Repository;

/**
 * Remove NOPs from given class
 *
 * @version $Id: Peephole.java,v 1.1.1.1 1999/05/07 11:59:30 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class Peephole {
  public static void main(String[] argv) {
    try {
      /* Load the class from CLASSPATH.
       */
      JavaClass       clazz   = Repository.lookupClass(argv[0]);
      Method[]        methods = clazz.getMethods();
      ConstantPoolGen cp      = new ConstantPoolGen(clazz.getConstantPool());

      for(int i=0; i < methods.length; i++) {
	MethodGen mg       = new MethodGen(methods[i],
					   clazz.getClassName(), cp);
	Method    stripped = removeNOPs(mg);

	if(stripped != null)     // Any NOPs stripped?
	  methods[i] = stripped; // Overwrite with stripped method
      }

      /* Dump the class to <class name>_.class
       */
      clazz.setConstantPool(cp.getFinalConstantPool());
      clazz.dump(clazz.getClassName() + "_.class");
    } catch(Exception e) { e.printStackTrace(); }
  }

  private static final Method removeNOPs(MethodGen mg) {
    InstructionList   il    = mg.getInstructionList();
    FindPattern       f     = new FindPattern(il);
    String            pat   = "(`NOP')+"; // Find at least one NOP
    InstructionHandle next  = null;
    int               count = 0;

    for(InstructionHandle ih = f.search(pat); // Search with regular expression
	ih != null;
	ih = f.search(pat, next)) {

      InstructionHandle[] match = f.getMatch();
      InstructionHandle   first = match[0];
      InstructionHandle   last  = match[match.length - 1];
      
      /* Some nasty Java compilers may add NOP at end of method.
       */
      if((next = last.getNext()) == null)
	break;

      count += match.length;

      /* Delete NOPs and redirect any references to them to the following
       * (non-nop) instruction.
       */
      try {
	il.delete(first, last);
      } catch(TargetLostException e) {
	InstructionHandle[] targets = e.getTargets();
	for(int i=0; i < targets.length; i++) {
	  InstructionTargeter[] targeters = targets[i].getTargeters();
	  
	  for(int j=0; j < targeters.length; j++)
	    targeters[j].updateTarget(targets[i], next);
	}
      }
    }
    
    if(count > 0) {
      System.out.println("Removed " + count + " NOP instructions from method " +
			 mg.getMethodName());
      return mg.getMethod();
    }
    else
      return null;
  }
}
