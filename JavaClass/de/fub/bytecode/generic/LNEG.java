package de.fub.bytecode.generic;

/** 
 * LNEG - Negate long
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id: LNEG.java,v 1.1.1.1 1999/05/04 13:14:25 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LNEG extends ArithmeticInstruction {
  public LNEG() {
    super(LNEG);
  }
}

