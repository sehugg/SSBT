package de.fub.bytecode.generic;

/**
 * Super class for instructions dealing with array access such as IALOAD.
 *
 * @version $Id: ArrayInstruction.java,v 1.2 2000/02/10 06:51:23 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class ArrayInstruction extends Instruction implements ExceptionThrower {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  ArrayInstruction() {}

  /**
   * @param tag opcode of instruction
   */
  protected ArrayInstruction(short tag) {
    super(tag, (short)1);
  }
  
  static Type[] ARRAY_INSN_TYPES = {
    Type.INT, Type.LONG, Type.FLOAT, Type.DOUBLE,
    Type.OBJECT, Type.BYTE, Type.CHAR, Type.SHORT
  };
  
  public Type getArrayType()
  {
    if (tag >= 46 && tag <= 53)
      return ARRAY_INSN_TYPES[tag-46];
    else if (tag >= 79 && tag <= 86)
    	return ARRAY_INSN_TYPES[tag-79];
    else
    	// todo: what kinda exception?
    	throw new RuntimeException("Not array instruction (tag=" + tag + ")");
  }

  public Class[] getExceptions() { return EXCS_ARRAY_EXCEPTION; }
}

