package de.fub.bytecode.generic;

/** 
 * LUSHR - Logical shift right long
 * <PRE>Stack: ..., value1, value2 -&gt; ..., result</PRE>
 *
 * @version $Id: LUSHR.java,v 1.1.1.1 1999/05/04 13:14:33 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LUSHR extends ArithmeticInstruction {
  public LUSHR() {
    super(LUSHR);
  }
}

