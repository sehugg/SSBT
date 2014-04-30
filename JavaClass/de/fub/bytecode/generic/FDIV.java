package de.fub.bytecode.generic;

/**
 * FDIV - Divide floats
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id: FDIV.java,v 1.1.1.1 1999/05/04 13:13:21 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FDIV extends ArithmeticInstruction {
  public FDIV() {
    super(FDIV);
  }
}

