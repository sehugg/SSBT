package de.fub.bytecode.util;
import java.util.Vector;
import de.fub.bytecode.classfile.JavaClass;

/** 
 * Utility class implementing a (typesafe) collection of JavaClass
 * objects. Contains the most important methods of a Vector.
 *
 * @version $Id: ClassVector.java,v 1.1.1.1 1999/05/04 13:15:04 hugg Exp $
 * @author <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A> 
 * @see Vector
*/
public class ClassVector {
  protected Vector vec = new Vector();
  
  public void      addElement(JavaClass clazz) { vec.addElement(clazz); }
  public JavaClass elementAt(int index)        { return (JavaClass)vec.elementAt(index); }
  public void      removeElementAt(int index)  { vec.removeElementAt(index); }

  public JavaClass[] toArray() {
    JavaClass[] classes = new JavaClass[vec.size()];
    vec.copyInto(classes);
    return classes;
  }
}
