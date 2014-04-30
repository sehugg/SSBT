package de.fub.bytecode.generic;

/** 
 * IFLE - Branch if int comparison with zero succeeds
 *
 * <PRE>Stack: ..., value -&gt; ...</PRE>
 *
 * @version $Id: IFLE.java,v 1.1.1.1 1999/05/04 13:13:43 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFLE extends IfInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  IFLE() {}

  public IFLE(InstructionHandle target) {
    super(IFLE, target);
  }

  /**
   * @return negation of instruction
   */
  public IfInstruction negate() {
    return new IFGT(target);
  }
}

