package de.fub.bytecode.generic;

/** 
 * DNEG - Negate double
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id: DNEG.java,v 1.1.1.1 1999/05/04 13:13:06 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DNEG extends ArithmeticInstruction {
  public DNEG() {
    super(DNEG);
  }
}

