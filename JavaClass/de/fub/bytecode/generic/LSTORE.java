package de.fub.bytecode.generic;

/** 
 * LSTORE - Store long into local variable
 * Stack ..., value.word1, value.word2 -> ... 
 *
 * @version $Id: LSTORE.java,v 1.1.1.1 1999/08/13 08:18:32 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LSTORE extends LocalVariableInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  LSTORE() {
    super(LSTORE, LSTORE_0);
  }

  public LSTORE(int n) {
    super(LSTORE, LSTORE_0, n);
  }
}

