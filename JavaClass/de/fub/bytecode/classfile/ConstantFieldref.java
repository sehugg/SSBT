package de.fub.bytecode.classfile;

import de.fub.bytecode.Constants;
import java.io.*;

/** 
 * This class represents a constant pool reference to a field.
 *
 * @version $Id: ConstantFieldref.java,v 1.1.1.1 1999/05/04 13:12:03 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public final class ConstantFieldref extends ConstantCP {
  /**
   * Initialize from another object.
   */
  public ConstantFieldref(ConstantFieldref c) {
    super(CONSTANT_Fieldref, c.getClassIndex(), c.getNameAndTypeIndex());
  }

  /**
   * Initialize instance from file data.
   *
   * @param file input stream
   * @throw IOException
   */
  ConstantFieldref(DataInputStream file) throws IOException
  {
    super(CONSTANT_Fieldref, file);
  }

  /**
   * @param class_index Reference to the class containing the Field
   * @param name_and_type_index and the Field signature
   */
  public ConstantFieldref(int class_index, 
			   int name_and_type_index) {
    super(CONSTANT_Fieldref, class_index, name_and_type_index);
  }    

  /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of Fields,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
  public void accept(Visitor v) {
    v.visitConstantFieldref(this);
  }    
}
