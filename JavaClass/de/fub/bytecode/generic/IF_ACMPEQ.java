package de.fub.bytecode.generic;

/** 
 * IF_ACMPEQ - Branch if reference comparison succeeds
 *
 * <PRE>Stack: ..., value1, value2 -&gt; ...</PRE>
 *
 * @version $Id: IF_ACMPEQ.java,v 1.1.1.1 1999/05/04 13:13:47 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IF_ACMPEQ extends IfInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  IF_ACMPEQ() {}

  public IF_ACMPEQ(InstructionHandle target) {
    super(IF_ACMPEQ, target);
  }

  /**
   * @return negation of instruction
   */
  public IfInstruction negate() {
    return new IF_ACMPNE(target);
  }
}
