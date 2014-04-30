package de.fub.bytecode.generic;

/**
 * FSUB - Substract floats
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id: FSUB.java,v 1.1.1.1 1999/05/04 13:13:26 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FSUB extends ArithmeticInstruction {
  public FSUB() {
    super(FSUB);
  }
}

