package de.fub.bytecode.generic;

/** 
 * DUP2 - Duplicate two top operand stack words
 * <PRE>Stack: ..., word2, word1 -&gt; ..., word2, word1, word2, word1</PRE>
 *
 * @version $Id: DUP2.java,v 1.1.1.1 1999/05/04 13:13:10 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DUP2 extends StackInstruction implements PushInstruction {
  public DUP2() {
    super(DUP2);
  }
}

