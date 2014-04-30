package de.fub.bytecode.generic;

/** 
 * BALOAD - Load byte or boolean from array
 * <PRE>Stack: ..., arrayref, index -&gt; ..., value</PRE>
 *
 * @version $Id: BALOAD.java,v 1.1.1.1 1999/05/04 13:12:42 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class BALOAD extends ArrayInstruction {
  public BALOAD() {
    super(BALOAD);
  }
}

