package de.fub.bytecode.generic;

/** 
 * LRETURN -  Return long from method
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; &lt;empty&gt;</PRE>
 *
 * @version $Id: LRETURN.java,v 1.1.1.1 1999/05/04 13:14:29 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LRETURN extends ReturnInstruction {
  public LRETURN() {
    super(LRETURN);
  }
}

