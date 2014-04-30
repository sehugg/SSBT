package de.fub.bytecode.generic;

/**
 * FREM - Remainder of floats
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id: FREM.java,v 1.1.1.1 1999/05/04 13:13:24 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FREM extends ArithmeticInstruction {
  public FREM() {
    super(FREM);
  }
}

