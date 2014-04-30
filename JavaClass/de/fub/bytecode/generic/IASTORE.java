package de.fub.bytecode.generic;

/** 
 * IASTORE -  Store into int array
 * <PRE>Stack: ..., arrayref, index, value -&gt; ...</PRE>
 *
 * @version $Id: IASTORE.java,v 1.1.1.1 1999/05/04 13:13:39 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IASTORE extends ArrayInstruction {
  public IASTORE() {
    super(IASTORE);
  }
}

