package de.fub.bytecode.generic;

/**
 * Super class for the family of arithmetic instructions.
 *
 * @version $Id: ArithmeticInstruction.java,v 1.2 2000/02/10 06:51:22 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class ArithmeticInstruction extends Instruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  ArithmeticInstruction() {}

  /**
   * @param tag opcode of instruction
   */
  protected ArithmeticInstruction(short tag) {
    super(tag, (short)1);
  }
  
  static final Type[][] ARITH_INSN_TYPES = {
  	/* iadd */	{ Type.INT, Type.INT, Type.INT },
  	/* ladd */	{ Type.LONG, Type.LONG, Type.LONG },
  	/* fadd */	{ Type.FLOAT, Type.FLOAT, Type.FLOAT },
  	/* dadd */	{ Type.DOUBLE, Type.DOUBLE, Type.DOUBLE },
  	/* isub */	{ Type.INT, Type.INT, Type.INT },
  	/* lsub */	{ Type.LONG, Type.LONG, Type.LONG },
  	/* fsub */	{ Type.FLOAT, Type.FLOAT, Type.FLOAT },
  	/* dsub */	{ Type.DOUBLE, Type.DOUBLE, Type.DOUBLE },
  	/* imul */	{ Type.INT, Type.INT, Type.INT },
  	/* lmul */	{ Type.LONG, Type.LONG, Type.LONG },
  	/* fmul */	{ Type.FLOAT, Type.FLOAT, Type.FLOAT },
  	/* dmul */	{ Type.DOUBLE, Type.DOUBLE, Type.DOUBLE },
  	/* idiv */	{ Type.INT, Type.INT, Type.INT },
  	/* ldiv */	{ Type.LONG, Type.LONG, Type.LONG },
  	/* fdiv */	{ Type.FLOAT, Type.FLOAT, Type.FLOAT },
  	/* ddiv */	{ Type.DOUBLE, Type.DOUBLE, Type.DOUBLE },
  	/* irem */	{ Type.INT, Type.INT, Type.INT },
  	/* lrem */	{ Type.LONG, Type.LONG, Type.LONG },
  	/* frem */	{ Type.FLOAT, Type.FLOAT, Type.FLOAT },
  	/* drem */	{ Type.DOUBLE, Type.DOUBLE, Type.DOUBLE },
  	/* ineg */	{ Type.INT, Type.INT },
  	/* lneg */	{ Type.LONG, Type.LONG },
  	/* fneg */	{ Type.FLOAT, Type.FLOAT },
  	/* dneg */	{ Type.DOUBLE, Type.DOUBLE },
  	/* ishl */	{ Type.INT, Type.INT, Type.INT },
  	/* lshl */	{ Type.LONG, Type.INT, Type.LONG },
  	/* ishr */	{ Type.INT, Type.INT, Type.INT },
  	/* lshr */	{ Type.LONG, Type.INT, Type.LONG },
  	/* iushl */	{ Type.INT, Type.INT, Type.INT },
  	/* lushl */	{ Type.LONG, Type.INT, Type.LONG },
  	/* iadd */	{ Type.INT, Type.INT, Type.INT },
  	/* ladd */	{ Type.LONG, Type.LONG, Type.LONG },
  	/* ior */	{ Type.INT, Type.INT, Type.INT },
  	/* lor */	{ Type.LONG, Type.LONG, Type.LONG },
  	/* ixor */	{ Type.INT, Type.INT, Type.INT },
  	/* lxor */	{ Type.LONG, Type.LONG, Type.LONG }
  };
  
  /**
   * Returns the types for this instruction.
   * The result type is at index 0, the first item to be popped
   * is at index 1, and so on.
   * There will be at least 2 items in the array returned.
   */
  public Type[] getTypes()
  {
  	// todo: check range
    return ARITH_INSN_TYPES[tag-96];
  }
}

