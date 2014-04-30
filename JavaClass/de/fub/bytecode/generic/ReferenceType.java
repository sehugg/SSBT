package de.fub.bytecode.generic;

/** 
 * Super class for objects and arrays.
 *
 * @version $Id: ReferenceType.java,v 1.1.1.1 1999/06/08 16:59:55 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ReferenceType extends Type {
  protected ReferenceType(byte t, String s) {
    super(t, s);
  }

  /** Class is non-abstract but not instantiable from the outside
   */
  ReferenceType() {
    super(T_OBJECT, "<null object>");
  }

  public String toString() { return signature; }
}
