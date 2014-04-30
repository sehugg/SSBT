package de.fub.bytecode.generic;

/** 
 * ISTORE - Store int into local variable
 * Stack ..., value -> ... 
 *
 * @version $Id: ISTORE.java,v 1.1.1.1 1999/08/13 08:18:31 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ISTORE extends LocalVariableInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  ISTORE() {
    super(ISTORE, ISTORE_0);
  }

  public ISTORE(int n) {
    super(ISTORE, ISTORE_0, n);
  }
}

