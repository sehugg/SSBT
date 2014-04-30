package de.fub.bytecode.generic;

/** 
 * DUP_X2 - Duplicate top operand stack word and put three down
 * <PRE>Stack: ..., word3, word2, word1 -&gt; ..., word1, word3, word2, word1</PRE>
 *
 * @version $Id: DUP_X2.java,v 1.1.1.1 1999/05/04 13:13:13 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DUP_X2 extends StackInstruction {
  public DUP_X2() {
    super(DUP_X2);
  }
}

