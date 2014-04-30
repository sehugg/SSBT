package de.fub.bytecode.generic;

/** 
 * DUP - Duplicate top operand stack word
 * <PRE>Stack: ..., word -&gt; ..., word, word</PRE>
 *
 * @version $Id: DUP.java,v 1.1.1.1 1999/05/04 13:13:10 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DUP extends StackInstruction implements PushInstruction {
  public DUP() {
    super(DUP);
  }
}

