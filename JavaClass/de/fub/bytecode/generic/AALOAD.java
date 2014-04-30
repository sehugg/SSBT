package de.fub.bytecode.generic;

/** 
 * AALOAD - Load reference from array
 * Stack ..., arrayref, index -> value
 *
 * @version $Id: AALOAD.java,v 1.1.1.1 1999/05/04 13:12:32 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class AALOAD extends ArrayInstruction {
  public AALOAD() {
    super(AALOAD);
  }
}

