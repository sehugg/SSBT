package de.fub.bytecode.generic;

/** 
 * ARRAYLENGTH -  Get length of array
 * <PRE>Stack: ..., arrayref -&gt; ..., length</PRE>
 *
 * @version $Id: ARRAYLENGTH.java,v 1.1.1.1 1999/09/09 07:42:43 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ARRAYLENGTH extends Instruction implements ExceptionThrower{
  public ARRAYLENGTH() {
    super(ARRAYLENGTH, (short)1);
  }

  public Class[] getExceptions() {
    return new Class[] { NULL_POINTER_EXCEPTION };
  }
}

