package de.fub.bytecode.generic;

/** 
 * IRETURN -  Return int from method
 * <PRE>Stack: ..., value -&gt; &lt;empty&gt;</PRE>
 *
 * @version $Id: IRETURN.java,v 1.1.1.1 1999/05/04 13:14:02 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IRETURN extends ReturnInstruction {
  public IRETURN() {
    super(IRETURN);
  }
}

