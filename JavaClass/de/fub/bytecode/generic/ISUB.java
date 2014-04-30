package de.fub.bytecode.generic;

/** 
 * ISUB - Substract ints
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id: ISUB.java,v 1.1.1.1 1999/05/04 13:14:05 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ISUB extends ArithmeticInstruction {
  public ISUB() {
    super(ISUB);
  }
}

