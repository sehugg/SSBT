package de.fub.bytecode.generic;

/** 
 * IUSHR - Logical shift right int
 * <PRE>Stack: ..., value1, value2 -&gt; ..., result</PRE>
 *
 * @version $Id: IUSHR.java,v 1.1.1.1 1999/05/04 13:14:06 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IUSHR extends ArithmeticInstruction {
  public IUSHR() {
    super(IUSHR);
  }
}

