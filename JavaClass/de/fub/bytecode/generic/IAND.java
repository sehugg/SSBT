package de.fub.bytecode.generic;

/** 
 * IAND - Bitwise AND int
 * <PRE>Stack: ..., value1, value2 -&gt; ..., result</PRE>
 *
 * @version $Id: IAND.java,v 1.1.1.1 1999/05/04 13:13:38 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IAND extends ArithmeticInstruction {
  public IAND() {
    super(IAND);
  }
}

