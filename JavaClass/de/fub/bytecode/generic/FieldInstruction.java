package de.fub.bytecode.generic;

import de.fub.bytecode.classfile.ConstantPool;
import de.fub.bytecode.classfile.ConstantUtf8;
import de.fub.bytecode.classfile.ConstantNameAndType;
import de.fub.bytecode.classfile.ConstantCP;
import de.fub.bytecode.classfile.*;

/**
 * Super class for the GET/PUTxxx family of instructions.
 *
 * @version $Id: FieldInstruction.java,v 1.1.1.1 1999/07/09 15:55:08 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class FieldInstruction extends FieldOrMethod {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  FieldInstruction() {}

  /**
   * @param index to constant pool
   */
  protected FieldInstruction(short tag, int index) {
    super(tag, index);
  }

  /**
   * @return mnemonic for instruction with symbolic references resolved
   */
  public String toString(ConstantPool cp) {
    return OPCODE_NAMES[tag] + " " + cp.constantToString(index, CONSTANT_Fieldref);
  }
  
  /** @return size of field (1 or 2)
   */
  protected int getFieldSize(ConstantPoolGen cpg) {
    return getType(cpg).getSize();
  }

  /** @return return type of referenced field
   */
  public Type getType(ConstantPoolGen cpg) {
    return getFieldType(cpg);
  }

  /** @return type of field
   */
  public Type getFieldType(ConstantPoolGen cpg) {
    return Type.getType(getSignature(cpg));
  }

  /** @return name of referenced field.
   */
  public String getFieldName(ConstantPoolGen cpg) {
    return getName(cpg);
  }
}
