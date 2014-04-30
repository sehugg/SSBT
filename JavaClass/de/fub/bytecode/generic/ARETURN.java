package de.fub.bytecode.generic;

/** 
 * ARETURN -  Return reference from method
 * <PRE>Stack: ..., objectref -&gt; &lt;empty&gt;</PRE>
 *
 * @version $Id: ARETURN.java,v 1.1.1.1 1999/05/04 13:12:36 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ARETURN extends ReturnInstruction {
  public ARETURN() {
    super(ARETURN);
  }
}

