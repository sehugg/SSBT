package de.fub.bytecode.generic;

/** 
 * IF_ICMPGT - Branch if int comparison succeeds
 *
 * <PRE>Stack: ..., value1, value2 -&gt; ...</PRE>
 *
 * @version $Id: IF_ICMPGT.java,v 1.1.1.1 1999/05/04 13:13:50 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IF_ICMPGT extends IfInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  IF_ICMPGT() {}

  public IF_ICMPGT(InstructionHandle target) {
    super(IF_ICMPGT, target);
  }

  /**
   * @return negation of instruction
   */
  public IfInstruction negate() {
    return new IF_ICMPLE(target);
  }
}
