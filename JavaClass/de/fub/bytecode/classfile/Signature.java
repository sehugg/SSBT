package de.fub.bytecode.classfile;

import  de.fub.bytecode.Constants;
import  java.io.*;

/**
 * This class is derived from <em>Attribute</em> and represents a reference
 * to a <href="http://wwwipd.ira.uka.de/~pizza/gj/">GJ</a> attribute.
 *
 * @version $Id: Signature.java,v 1.1.1.1 1999/07/09 15:54:28 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Attribute
 */
public final class Signature extends Attribute {
  private int signature_index;

  /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
  public Signature(Signature c) {
    this(c.getNameIndex(), c.getLength(), c.getSignatureIndex(), c.getConstantPool());
  }

  /**
   * Construct object from file stream.
   * @param name_index Index in constant pool to CONSTANT_Utf8
   * @param length Content length in bytes
   * @param file Input stream
   * @param constant_pool Array of constants
   * @throw IOException
   */
  Signature(int name_index, int length, DataInputStream file,
	   ConstantPool constant_pool) throws IOException
  {
    this(name_index, length, file.readUnsignedShort(), constant_pool);
  }

  /**
   * @param name_index Index in constant pool to CONSTANT_Utf8
   * @param length Content length in bytes
   * @param constant_pool Array of constants
   * @param Signature_index Index in constant pool to CONSTANT_Utf8
   */
  public Signature(int name_index, int length, int signature_index,
		  ConstantPool constant_pool)
  {
    super(ATTR_SIGNATURE, name_index, length, constant_pool);
    this.signature_index = signature_index;
  }

  /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
   public void accept(Visitor v) {
     System.err.println("Visiting non-standard Signature object");
   }
   
  /**
   * Dump source file attribute to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */ 
  public final void dump(DataOutputStream file) throws IOException
  {
    super.dump(file);
    file.writeShort(signature_index);
  }    

  /**
   * @return Index in constant pool of source file name.
   */  
  public final int getSignatureIndex() { return signature_index; }    

  /**
   * @param Signature_index.
   */
  public final void setSignatureIndex(int signature_index) {
    this.signature_index = signature_index;
  }    

  /**
   * @return GJ signature.
   */ 
  public final String getSignature() {
    ConstantUtf8 c = (ConstantUtf8)constant_pool.getConstant(signature_index, 
							     CONSTANT_Utf8);
    return c.getBytes();
  }

  /**
   * @return String representation
   */ 
  public final String toString() {
    return "Signature(" +  getSignature() + ")";
  }    

  /**
   * @return deep copy of this attribute
   */
  public Attribute copy(ConstantPool constant_pool) {
    return (Signature)clone();
  }
}
