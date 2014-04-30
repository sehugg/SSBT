package de.fub.bytecode.generic;

/**
 * Super class for the x2y family of instructions.
 *
 * @version $Id: ConversionInstruction.java,v 1.2 2000/02/10 06:51:24 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class ConversionInstruction extends Instruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  ConversionInstruction() {}

  /**
   * @param tag opcode of instruction
   */
  protected ConversionInstruction(short tag) {
    super(tag, (short)1);
  }
  
  static final Type[] CONSUME_TYPES = {
    Type.INT, Type.INT, Type.INT,
    Type.LONG, Type.LONG, Type.LONG,
    Type.FLOAT, Type.FLOAT, Type.FLOAT,
    Type.DOUBLE, Type.DOUBLE, Type.DOUBLE,
    Type.INT, Type.INT, Type.INT
  };

  static final Type[] PRODUCE_TYPES = {
    Type.LONG, Type.FLOAT, Type.DOUBLE,
    Type.INT, Type.FLOAT, Type.DOUBLE,
    Type.INT, Type.LONG, Type.DOUBLE,
    Type.INT, Type.LONG, Type.FLOAT,
    Type.BYTE, Type.CHAR, Type.SHORT,
  };
  
  public Type getConsumeType()
  {
    return CONSUME_TYPES[tag-133];
  }

  public Type getProduceType()
  {
    return PRODUCE_TYPES[tag-133];
  }

}

