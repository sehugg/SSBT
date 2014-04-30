package de.fub.bytecode.generic;

/** 
 * IOR - Bitwise OR int
 * <PRE>Stack: ..., value1, value2 -&gt; ..., result</PRE>
 *
 * @version $Id: IOR.java,v 1.1.1.1 1999/05/04 13:14:01 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IOR extends ArithmeticInstruction {
  public IOR() {
    super(IOR);
  }
}

