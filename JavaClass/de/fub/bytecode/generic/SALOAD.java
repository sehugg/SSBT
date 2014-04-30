package de.fub.bytecode.generic;

/** 
 * SALOAD - Load short from array
 * <PRE>Stack: ..., arrayref, index -&gt; ..., value</PRE>
 *
 * @version $Id: SALOAD.java,v 1.1.1.1 1999/05/04 13:14:51 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class SALOAD extends ArrayInstruction {
  public SALOAD() {
    super(SALOAD);
  }
}

