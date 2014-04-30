package de.fub.bytecode.generic;

/** 
 * DSTORE - Store double into local variable
 * Stack ..., value.word1, value.word2 -> ... 
 *
 * @version $Id: DSTORE.java,v 1.1.1.1 1999/08/13 08:18:27 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DSTORE extends LocalVariableInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  DSTORE() {
    super(DSTORE, DSTORE_0);
  }

  public DSTORE(int n) {
    super(DSTORE, DSTORE_0, n);
  }
}

