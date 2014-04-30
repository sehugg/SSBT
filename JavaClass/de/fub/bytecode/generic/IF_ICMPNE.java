package de.fub.bytecode.generic;

/** 
 * IF_ICMPNE - Branch if int comparison doesn't succeed
 *
 * <PRE>Stack: ..., value1, value2 -&gt; ...</PRE>
 *
 * @version $Id: IF_ICMPNE.java,v 1.1.1.1 1999/05/04 13:13:52 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IF_ICMPNE extends IfInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  IF_ICMPNE() {}

  public IF_ICMPNE(InstructionHandle target) {
    super(IF_ICMPNE, target);
  }

  /**
   * @return negation of instruction
   */
  public IfInstruction negate() {
    return new IF_ICMPEQ(target);
  }
}
