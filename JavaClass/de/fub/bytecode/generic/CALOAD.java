package de.fub.bytecode.generic;

/** 
 * CALOAD - Load char from array
 * <PRE>Stack: ..., arrayref, index -&gt; ..., value</PRE>
 *
 * @version $Id: CALOAD.java,v 1.1.1.1 1999/05/04 13:12:47 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class CALOAD extends ArrayInstruction {
  public CALOAD() {
    super(CALOAD);
  }
}

