package de.fub.bytecode.classfile;

import  de.fub.bytecode.Constants;
import  java.io.*;

/** 
 * This class is derived from the abstract 
 * <A HREF="de.fub.bytecode.classfile.Constant.html">Constant</A> class 
 * and represents a reference to a long object.
 *
 * @version $Id: ConstantLong.java,v 1.1.1.1 1999/05/04 13:12:08 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Constant
 */
public final class ConstantLong extends Constant {
  private long bytes;

  /** 
   * @param bytes Data
   */
  public ConstantLong(long bytes)
  {
    super(CONSTANT_Long);
    this.bytes = bytes;
  }    
  /**
   * Initialize from another object.
   */
  public ConstantLong(ConstantLong c) {
    this(c.getBytes());
  }    
  /** 
   * Initialize instance from file data.
   *
   * @param file Input stream
   * @throw IOException
   */
  ConstantLong(DataInputStream file) throws IOException
  {    
    this(file.readLong());
  }    
  /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
  public void accept(Visitor v) {
    v.visitConstantLong(this);
  }    
  /**
   * Dump constant long to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */ 
  public final void dump(DataOutputStream file) throws IOException
  {
    file.writeByte(tag);
    file.writeLong(bytes);
  }    
  /**
   * @return data, i.e. 8 bytes.
   */  
  public final long getBytes() { return bytes; }    
  /**
   * @param bytes.
   */
  public final void setBytes(long bytes) {
    this.bytes = bytes;
  }    
  /**
   * @return String representation.
   */
  public final String toString() {
    return super.toString() + "(bytes = " + bytes + ")";
  }    
}
