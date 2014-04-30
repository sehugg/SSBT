package de.fub.bytecode.generic;

/** 
 * DRETURN -  Return double from method
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; &lt;empty&gt;</PRE>
 *
 * @version $Id: DRETURN.java,v 1.1.1.1 1999/05/04 13:13:07 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DRETURN extends ReturnInstruction {
  public DRETURN() {
    super(DRETURN);
  }
}

