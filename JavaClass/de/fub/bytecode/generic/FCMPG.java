package de.fub.bytecode.generic;

/** 
 * FCMPG - Compare floats: value1 > value2
 * <PRE>Stack: ..., value1, value2 -&gt; ..., result</PRE>
 *
 * @version $Id: FCMPG.java,v 1.1.1.1 1999/05/04 13:13:19 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FCMPG extends Instruction {
  public FCMPG() {
    super(FCMPG, (short)1);
  }
}

