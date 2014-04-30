package de.fub.bytecode.generic;

/** 
 * FLOAD - Load float from local variable
 * Stack ... -> ..., result
 *
 * @version $Id: FLOAD.java,v 1.1.1.1 1999/08/13 08:18:28 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FLOAD extends LocalVariableInstruction implements PushInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  FLOAD() {
    super(FLOAD, FLOAD_0);
  }

  public FLOAD(int n) {
    super(FLOAD, FLOAD_0, n);
  }
}

