package de.fub.bytecode.generic;

/** 
 * FCMPL - Compare floats: value1 < value2
 * <PRE>Stack: ..., value1, value2 -&gt; ..., result</PRE>
 *
 * @version $Id: FCMPL.java,v 1.1.1.1 1999/05/04 13:13:20 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FCMPL extends Instruction {
  public FCMPL() {
    super(FCMPL, (short)1);
  }
}

