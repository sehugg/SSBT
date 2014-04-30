package de.fub.bytecode.generic;

/** 
 * LMUL - Multiply longs
 * <PRE>Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 -&gt;</PRE>
 *        ..., result.word1, result.word2
 *
 * @version $Id: LMUL.java,v 1.1.1.1 1999/05/04 13:14:25 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LMUL extends ArithmeticInstruction {
  public LMUL() {
    super(LMUL);
  }
}

