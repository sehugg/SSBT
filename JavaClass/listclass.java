import java.io.*;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.*;
import de.fub.bytecode.ClassPath;

/**
 * Read class file(s) and display its contents.
 *
 * @version $Id: listclass.java,v 1.1.1.1 1999/07/09 15:53:33 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class listclass {
  public static void main(String[] argv) { 
    String[]    file_name = new String[argv.length];
    int         files=0;
    boolean     code=false, constants=false, verbose=true;
    String      name=null;

    /* Parse command line arguments.
     */
    for(int i=0; i < argv.length; i++) {
      if(argv[i].charAt(0) == '-') {  // command line switch
	if(argv[i].equals("-constants"))
	  constants=true;
	else if(argv[i].equals("-code"))
	  code=true;
	else if(argv[i].equals("-brief"))
	  verbose=false;
	else
	  System.err.println("Unknown switch " + argv[i] + " ignored.");
      }
      else { // add file name to list
	file_name[files++] = argv[i];
      }
    }
	
    try {
      if(files == 0)
	System.err.println("list: No input files specified");
      else {
	for(int i=0; i < files; i++) {
	  JavaClass java_class;

	  name = file_name[i];

	  if((java_class = Repository.lookupClass(name)) == null)
	    java_class = new ClassParser(name).parse(); // May throw IOException

	  System.out.println(java_class);             // Dump the contents
	  
	  if(constants) // Dump the constant pool ?
	      System.out.println(java_class.getConstantPool());

	  if(code) // Dump the method code ?
	    printCode(java_class.getMethods(), verbose);
	}
      }	  
    } catch(Exception e) {
      System.err.println("Couldn't find class " + name);
    }
  }        
  /**
   * Dump the disassembled code of all methods in the class.
   */
  public static void printCode(Method[] methods, boolean verbose) {
    for(int i=0; i < methods.length; i++) {
      System.out.println(methods[i]); // public static void main(String argv[])

      Code code = methods[i].getCode();
      if(code != null)
	System.out.println(code.toString(verbose));
    }
  }
}
