import de.fub.bytecode.classfile.*;
import de.fub.bytecode.generic.*;
import de.fub.bytecode.Constants;
import de.fub.bytecode.*;

/**
 * Read class file(s) and examine all of its methods, determining the
 * maximum stack depth used by analyzing control flow.
 *
 * @version $Id: maxstack.java,v 1.1.1.1 1999/05/04 13:11:37 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public final class maxstack {
  public static void main(String[] argv) { 
    try {
      for(int i = 0; i < argv.length; i++) {
	String    class_name = argv[i];
	JavaClass java_class = Repository.lookupClass(class_name);

	if(java_class == null) // Look for .class file?
	  java_class = new ClassParser(class_name).parse();

	ConstantPoolGen cp      = new ConstantPoolGen(java_class.getConstantPool());
   	Method[]        methods = java_class.getMethods();
	
	for(int j = 0; j < methods.length; j++) {
	  Method    m  = methods[j];
	  MethodGen mg = new MethodGen(m, argv[i], cp);

	  int compiled = mg.getMaxStack();
	  mg.setMaxStack(); // Recompute value
	  int computed = mg.getMaxStack();

	  System.out.print(m);
	  if(computed == compiled)
	    System.out.println(" Ok.");
	  else
	    System.out.println("\nCompiled stack size " + compiled + " computed size " + computed);
	}
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
