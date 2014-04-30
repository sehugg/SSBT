package de.fub.bytecode.generic;

/**
 * Denotes a push instruction that produces a literal on the stack
 * such as  SIPUSH, BIPUSH, ICONST, etc.
 *
 * @version $Id: ConstantPushInstruction.java,v 1.1.1.1 1999/05/04 13:12:56 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>

 * @see ICONST
 * @see SIPUSH
 */
public interface ConstantPushInstruction extends PushInstruction {
  public Number getValue();
}

