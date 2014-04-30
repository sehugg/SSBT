package de.fub.bytecode.generic;

/** 
 * DALOAD - Load double from array
 * <PRE>Stack: ..., arrayref, index -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id: DALOAD.java,v 1.1.1.1 1999/05/04 13:13:00 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DALOAD extends ArrayInstruction {
  public DALOAD() {
    super(DALOAD);
  }
}

