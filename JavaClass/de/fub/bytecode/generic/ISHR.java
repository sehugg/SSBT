package de.fub.bytecode.generic;

/**
 * ISHR - Arithmetic shift right int
 * <PRE>Stack: ..., value1, value2 -&gt; ..., result</PRE>
 *
 * @version $Id: ISHR.java,v 1.1.1.1 1999/05/04 13:14:04 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ISHR extends ArithmeticInstruction {
  public ISHR() {
    super(ISHR);
  }
}

