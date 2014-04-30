package de.fub.bytecode.generic;

/** 
 * IF_ICMPLE - Branch if int comparison succeeds
 *
 * <PRE>Stack: ..., value1, value2 -&gt; ...</PRE>
 *
 * @version $Id: IF_ICMPLE.java,v 1.1.1.1 1999/05/04 13:13:50 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IF_ICMPLE extends IfInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  IF_ICMPLE() {}

  public IF_ICMPLE(InstructionHandle target) {
    super(IF_ICMPLE, target);
  }

  /**
   * @return negation of instruction
   */
  public IfInstruction negate() {
    return new IF_ICMPGT(target);
  }
}

