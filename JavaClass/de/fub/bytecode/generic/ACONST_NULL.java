package de.fub.bytecode.generic;

/** 
 * ACONST_NULL -  Push null
 * <PRE>Stack: ... -&gt; ..., null</PRE>
 *
 * @version $Id: ACONST_NULL.java,v 1.1.1.1 1999/05/04 13:12:34 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ACONST_NULL extends Instruction implements PushInstruction {
  public ACONST_NULL() {
    super(ACONST_NULL, (short)1);
  }
}

