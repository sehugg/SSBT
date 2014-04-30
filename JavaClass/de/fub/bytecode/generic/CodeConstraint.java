package de.fub.bytecode.generic;

/**
 * Code patterns found with the FindPattern class may receive an additional
 * CodeConstraint argument that checks the found piece of code for user-defined
 * constraints. I.e. FindPattern.search() returns the matching code if and
 * only if CodeConstraint.checkCode() returns true.
 *
 * @version $Id: CodeConstraint.java,v 1.1.1.1 1999/05/04 13:12:52 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see FindPattern
 */
public interface CodeConstraint {
  /**
   * @param match array of instructions matching the requested pattern
   */
  public boolean checkCode(InstructionHandle[] match);
}

