package de.fub.bytecode.generic;

/**
 * LCMP - Compare longs:
 * <PRE>Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 -&gt;</PRE>
 *        ..., result <= -1, 0, 1>
 *
 * @version $Id: LCMP.java,v 1.1.1.1 1999/05/04 13:14:20 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LCMP extends Instruction {
  public LCMP() {
    super(LCMP, (short)1);
  }
}

