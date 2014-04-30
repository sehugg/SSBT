package de.fub.bytecode.generic;

/** 
 * IFNULL - Branch if reference is not null
 *
 * <PRE>Stack: ..., reference -&gt; ...</PRE>
 *
 * @version $Id: IFNULL.java,v 1.1.1.1 1999/05/04 13:13:46 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFNULL extends IfInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  IFNULL() {}

  public IFNULL(InstructionHandle target) {
    super(IFNULL, target);
  }

  /**
   * @return negation of instruction
   */
  public IfInstruction negate() {
    return new IFNONNULL(target);
  }
}
