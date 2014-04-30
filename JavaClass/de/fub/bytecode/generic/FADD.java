package de.fub.bytecode.generic;

/** 
 * FADD - Add floats
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id: FADD.java,v 1.1.1.1 1999/05/04 13:13:17 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FADD extends ArithmeticInstruction {
  public FADD() {
    super(FADD);
  }
}

