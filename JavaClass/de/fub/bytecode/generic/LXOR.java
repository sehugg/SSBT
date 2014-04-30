package de.fub.bytecode.generic;

/** 
 * LXOR - Bitwise XOR long
 * <PRE>Stack: ..., value1, value2 -&gt; ..., result</PRE>
 *
 * @version $Id: LXOR.java,v 1.1.1.1 1999/05/04 13:14:34 hugg Exp $
 * @authXOR  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LXOR extends ArithmeticInstruction {
  public LXOR() {
    super(LXOR);
  }
}

