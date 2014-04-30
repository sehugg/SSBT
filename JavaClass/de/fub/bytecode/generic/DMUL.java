package de.fub.bytecode.generic;

/** 
 * DMUL - Multiply doubles
 * <PRE>Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 -&gt;</PRE>
 *        ..., result.word1, result.word2
 *
 * @version $Id: DMUL.java,v 1.1.1.1 1999/05/04 13:13:05 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DMUL extends ArithmeticInstruction {
  public DMUL() {
    super(DMUL);
  }
}

