package de.fub.bytecode.generic;

/** 
 * IFNE - Branch if int comparison with zero succeeds
 *
 * <PRE>Stack: ..., value -&gt; ...</PRE>
 *
 * @version $Id: IFNE.java,v 1.1.1.1 1999/05/04 13:13:45 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFNE extends IfInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  IFNE() {}

  public IFNE(InstructionHandle target) {
    super(IFNE, target);
  }

  /**
   * @return negation of instruction
   */
  public IfInstruction negate() {
    return new IFEQ(target);
  }
}
