package de.fub.bytecode.generic;

/** 
 * IFLT - Branch if int comparison with zero succeeds
 *
 * <PRE>Stack: ..., value -&gt; ...</PRE>
 *
 * @version $Id: IFLT.java,v 1.1.1.1 1999/05/04 13:13:44 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFLT extends IfInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  IFLT() {}

  public IFLT(InstructionHandle target) {
    super(IFLT, target);
  }

  /**
   * @return negation of instruction
   */
  public IfInstruction negate() {
    return new IFGE(target);
  }
}
