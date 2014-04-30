package de.fub.bytecode.generic;

/**
 * ISHL - Arithmetic shift left int
 * <PRE>Stack: ..., value1, value2 -&gt; ..., result</PRE>
 *
 * @version $Id: ISHL.java,v 1.1.1.1 1999/05/04 13:14:03 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ISHL extends ArithmeticInstruction {
  public ISHL() {
    super(ISHL);
  }
}

