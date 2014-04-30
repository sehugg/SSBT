package de.fub.bytecode.generic;

/** 
 * IMUL - Multiply ints
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id: IMUL.java,v 1.1.1.1 1999/05/04 13:13:56 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IMUL extends ArithmeticInstruction {
  public IMUL() {
    super(IMUL);
  }
}

