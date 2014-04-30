package de.fub.bytecode.generic;

/** 
 * DUP_X1 - Duplicate top operand stack word and put two down
 * <PRE>Stack: ..., word2, word1 -&gt; ..., word1, word2, word1</PRE>
 *
 * @version $Id: DUP_X1.java,v 1.1.1.1 1999/05/04 13:13:13 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DUP_X1 extends StackInstruction {
  public DUP_X1() {
    super(DUP_X1);
  }
}

