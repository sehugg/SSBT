package de.fub.bytecode.generic;

/** 
 * CASTORE -  Store into char array
 * <PRE>Stack: ..., arrayref, index, value -&gt; ...</PRE>
 *
 * @version $Id: CASTORE.java,v 1.1.1.1 1999/05/04 13:12:48 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class CASTORE extends ArrayInstruction {
  public CASTORE() {
    super(CASTORE);
  }
}

