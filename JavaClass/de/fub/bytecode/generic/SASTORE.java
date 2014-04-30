package de.fub.bytecode.generic;

/**
 * SASTORE - Store into short array
 * <PRE>Stack: ..., arrayref, index, value -&gt; ...</PRE>
 *
 * @version $Id: SASTORE.java,v 1.1.1.1 1999/05/04 13:14:52 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class SASTORE extends ArrayInstruction {
  public SASTORE() {
    super(SASTORE);
  }
}

