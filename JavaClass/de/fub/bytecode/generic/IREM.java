package de.fub.bytecode.generic;

/**
 * IREM - Remainder of int
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id: IREM.java,v 1.1.1.1 1999/09/09 07:42:52 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IREM extends ArithmeticInstruction implements ExceptionThrower {
  public IREM() {
    super(IREM);
  }

  public Class[] getExceptions() { return new Class[] { ARITHMETIC_EXCEPTION }; }
}

