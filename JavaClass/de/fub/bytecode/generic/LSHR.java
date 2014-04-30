package de.fub.bytecode.generic;

/** 
 * LSHR - Arithmetic shift right long
 * <PRE>Stack: ..., value1.word1, value1.word2, value2 -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id: LSHR.java,v 1.1.1.1 1999/05/04 13:14:31 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LSHR extends ArithmeticInstruction {
  public LSHR() {
    super(LSHR);
  }
}

