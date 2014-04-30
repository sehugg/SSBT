package de.fub.bytecode.generic;

/**
 * Super class for the IFxxx  family of instructions.
 *
 * @version $Id: IfInstruction.java,v 1.1.1.1 1999/05/04 13:14:07 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class IfInstruction extends BranchInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  IfInstruction() {}

  /**
   * @param instruction Target instruction to branch to
   */
  protected IfInstruction(short tag, InstructionHandle target) {
    super(tag, target);
  }

  /**
   * @return negation of instruction, e.g. IFEQ.negate() == IFNE
   */
  public abstract IfInstruction negate();
}

