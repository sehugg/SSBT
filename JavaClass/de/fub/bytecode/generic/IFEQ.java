package de.fub.bytecode.generic;

/** 
 * IFEQ - Branch if int comparison with zero succeeds
 *
 * <PRE>Stack: ..., value -&gt; ...</PRE>
 *
 * @version $Id: IFEQ.java,v 1.1.1.1 1999/05/04 13:13:41 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFEQ extends IfInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  IFEQ() {}

  public IFEQ(InstructionHandle target) {
    super(IFEQ, target);
  }

  /**
   * @return negation of instruction, e.g. IFEQ.negate() == IFNE
   */
  public IfInstruction negate() {
    return new IFNE(target);
  }
}

