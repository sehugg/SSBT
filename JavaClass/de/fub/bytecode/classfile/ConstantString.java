package de.fub.bytecode.classfile;

import  de.fub.bytecode.Constants;
import  java.io.*;

/** 
 * This class is derived from the abstract 
 * <A HREF="de.fub.bytecode.classfile.Constant.html">Constant</A> class 
 * and represents a reference to a String object.
 *
 * @version $Id: ConstantString.java,v 1.1.1.1 1999/05/04 13:12:11 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Constant
 */
public final class ConstantString extends Constant {
  private int string_index; // Identical to ConstantClass except for this name

  /**
   * Initialize from another object.
   */
  public ConstantString(ConstantString c) {
    this(c.getStringIndex());
  }    
  /** 
   * Initialize instance from file data.
   *
   * @param file Input stream
   * @throw IOException
   */
  ConstantString(DataInputStream file) throws IOException
  {    
    this((int)file.readUnsignedShort());
  }    
  /**
   * @param string_index Index of Constant_Utf8 in constant pool
   */
  public ConstantString(int string_index)
  {    
    super(CONSTANT_String);
    this.string_index = string_index;
  }    
  /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
  public void accept(Visitor v) {
    v.visitConstantString(this);
  }    
  /**
   * Dump constant field reference to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */ 
  public final void dump(DataOutputStream file) throws IOException
  {
    file.writeByte(tag);
    file.writeShort(string_index);
  }    
  /**
   * @return Index in constant pool of the string (ConstantUtf8).
   */  
  public final int getStringIndex() { return string_index; }    
  /**
   * @param string_index.
   */
  public final void setStringIndex(int string_index) {
    this.string_index = string_index;
  }    
  /**
   * @return String representation.
   */
  public final String toString()
  {
    return super.toString() + "(string_index = " + string_index + ")";
  }    
}
