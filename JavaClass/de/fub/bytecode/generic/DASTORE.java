package de.fub.bytecode.generic;

/** 
 * DASTORE -  Store into double array
 * <PRE>Stack: ..., arrayref, index, value.word1, value.word2 -&gt; ...</PRE>
 *
 * @version $Id: DASTORE.java,v 1.1.1.1 1999/05/04 13:13:01 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DASTORE extends ArrayInstruction {
  public DASTORE() {
    super(DASTORE);
  }
}

