package de.fub.bytecode.generic;

/** 
 * SWAP - Swa top operand stack word
 * <PRE>Stack: ..., word2, word1 -&gt; ..., word1, word2</PRE>
 *
 * @version $Id: SWAP.java,v 1.1.1.1 1999/05/04 13:14:53 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class SWAP extends StackInstruction {
  public SWAP() {
    super(SWAP);
  }
}

