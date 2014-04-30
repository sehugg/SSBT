package de.fub.bytecode.generic;

/** 
 * BASTORE -  Store into byte or boolean array
 * <PRE>Stack: ..., arrayref, index, value -&gt; ...</PRE>
 *
 * @version $Id: BASTORE.java,v 1.1.1.1 1999/05/04 13:12:43 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class BASTORE extends ArrayInstruction {
  public BASTORE() {
    super(BASTORE);
  }
}

