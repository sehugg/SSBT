package de.fub.bytecode.generic;

/** 
 * ICONST - Push value between -1, ..., 5, other values cause an exception
 *
 * <PRE>Stack: ... -&gt; ..., <i></PRE>
 *
 * @version $Id: ICONST.java,v 1.1.1.1 1999/05/04 13:13:40 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ICONST extends Instruction implements ConstantPushInstruction {
  private int value;

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  ICONST() {}

  public ICONST(int i) {
    super(ICONST_0, (short)1);

    if((i >= -1) && (i <= 5))
      tag    = (short)(ICONST_0 + i); // Even works for i == -1
    else
      throw new ClassGenException("ICONST can be used only for value between -1 and 5: " +
				  i);
    value = i;
  }
  
  public Number getValue() { return new Integer(value); }
}

