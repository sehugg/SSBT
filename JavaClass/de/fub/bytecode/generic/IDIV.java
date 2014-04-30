package de.fub.bytecode.generic;

/**
 * IDIV - Divide ints
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id: IDIV.java,v 1.1.1.1 1999/09/09 07:42:49 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IDIV extends ArithmeticInstruction implements ExceptionThrower {
  public IDIV() {
    super(IDIV);
  }

  public Class[] getExceptions() { return new Class[] { ARITHMETIC_EXCEPTION }; }
}

