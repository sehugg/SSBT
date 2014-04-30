package de.fub.bytecode.generic;

/** 
 * FALOAD - Load float from array
 * <PRE>Stack: ..., arrayref, index -&gt; ..., value</PRE>
 *
 * @version $Id: FALOAD.java,v 1.1.1.1 1999/05/04 13:13:17 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FALOAD extends ArrayInstruction {
  public FALOAD() {
    super(FALOAD);
  }
}

