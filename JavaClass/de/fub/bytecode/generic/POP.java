package de.fub.bytecode.generic;

/**
 * POP - Pop top operand stack word
 *
 * <PRE>Stack: ..., word -&gt; ...</PRE>
 *
 * @version $Id: POP.java,v 1.1.1.1 1999/05/04 13:14:44 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class POP extends StackInstruction {
  public POP() {
    super(POP);
  }
}

