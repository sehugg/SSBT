package de.fub.bytecode.generic;

/** 
 * ATHROW -  Throw exception
 * <PRE>Stack: ..., objectref -&gt; objectref</PRE>
 *
 * @version $Id: ATHROW.java,v 1.1.1.1 1999/09/09 07:42:44 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ATHROW extends Instruction implements UnconditionalBranch, ExceptionThrower {
  public ATHROW() {
    super(ATHROW, (short)1);
  }

  public Class[] getExceptions() { return new Class[] { THROWABLE }; }
}

