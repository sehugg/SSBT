package de.fub.bytecode.generic;

/** 
 * IADD - Add ints
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id: IADD.java,v 1.1.1.1 1999/05/04 13:13:37 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IADD extends ArithmeticInstruction {
  public IADD() {
    super(IADD);
  }
}

