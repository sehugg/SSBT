package de.fub.bytecode.generic;

/** 
 * LASTORE -  Store into long array
 * <PRE>Stack: ..., arrayref, index, value.word1, value.word2 -&gt; ...</PRE>
 *
 * @version $Id: LASTORE.java,v 1.1.1.1 1999/05/04 13:14:19 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LASTORE extends ArrayInstruction {
  public LASTORE() {
    super(LASTORE);
  }
}

