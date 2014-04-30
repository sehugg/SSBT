package de.fub.bytecode.generic;

/** 
 * Denotes reference such as java.lang.String.
 *
 * @version $Id: ObjectType.java,v 1.2 2000/02/16 02:16:01 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public final class ObjectType extends ReferenceType {
  private String class_name; // Class name of type

  /**
   * @param class_name fully qualified class name, e.g. java.lang.String
   */ 
  public ObjectType(String class_name) {
    super(T_REFERENCE, "L" + class_name.replace('.', '/') + ";");
    this.class_name = class_name.replace('/','.');
  }

  /**
   * @return name of referenced class
   */
  public String getClassName() { return class_name; }

  public boolean equals(Object type) {
  /*
  	System.err.println(class_name.length());
  	if (type instanceof ObjectType)
  	{
  		System.err.println("*"+((ObjectType)type).class_name.length());
  		System.err.println(class_name.equals(((ObjectType)type).class_name));
  		System.err.println(class_name);
  		System.err.println( ((ObjectType)type).class_name );
  	}
  	*/
    return (type instanceof ObjectType)?
      ((ObjectType)type).class_name.equals(class_name) : false;
  }
}
