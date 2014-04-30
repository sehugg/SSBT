package de.fub.bytecode.generic;

/**
 * POP2 - Pop two top operand stack words
 *
 * <PRE>Stack: ..., word2, word1 -&gt; ...</PRE>
 *
 * @version $Id: POP2.java,v 1.1.1.1 1999/05/04 13:14:44 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class POP2 extends StackInstruction {
  public POP2() {
    super(POP2);
  }
}

