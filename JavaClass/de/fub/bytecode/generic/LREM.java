package de.fub.bytecode.generic;

/**
 * LREM - Remainder of long
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id: LREM.java,v 1.1.1.1 1999/09/22 13:00:12 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LREM extends ArithmeticInstruction implements ExceptionThrower {
  public LREM() {
    super(LREM);
  }

  public Class[] getExceptions() { return new Class[] { ARITHMETIC_EXCEPTION }; }
}

