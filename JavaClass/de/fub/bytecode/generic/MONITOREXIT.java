package de.fub.bytecode.generic;

/** 
 * MONITOREXIT - Exit monitor for object
 * <PRE>Stack: ..., objectref -&gt; ...</PRE>
 *
 * @version $Id: MONITOREXIT.java,v 1.1.1.1 1999/09/09 07:42:56 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class MONITOREXIT extends Instruction implements ExceptionThrower{
  public MONITOREXIT() {
    super(MONITOREXIT, (short)1);
  }

  public Class[] getExceptions() {
    return new Class[] {NULL_POINTER_EXCEPTION};
  }
}

