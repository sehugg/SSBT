package de.fub.bytecode;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.util.*;
import java.util.*;
import java.io.*;

/** 
 * Repository maintains informations about class interdependencies, e.g.
 * whether a class is a sub-class of another. JavaClass objects are put
 * into a cache which can be purged with clearCache().
 *
 * All JavaClass objects used as arguments must have been obtained via
 * the repository or been added with addClass() manually. This is
 * because we check for object equality (==).
 *
 * @version $Id: Repository.java,v 1.3 2000/02/24 21:52:41 hugg Exp $
 * @author <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A
 */
public abstract class Repository {
  private static ClassPath class_path = new ClassPath();
  private static WeakHashMap classes;
  private static JavaClass OBJECT; // should be final ...

  static { clearCache(); }
  
  public static void setClassPath(ClassPath new_class_path)
  {
  	class_path = new_class_path;
  	clearCache();
  }

  /** @return class object for given class name.
   */
  public static JavaClass lookupClass(String class_name) {
    JavaClass clazz = (JavaClass)classes.get(class_name);

    if(clazz == null) {
      try {
	InputStream is = class_path.getInputStream(class_name);
	clazz = new ClassParser(is, class_name).parse();
      } catch(IOException e) { return null; }

      classes.put(class_name, clazz);
    }

    return clazz;
  }
  
  public static JavaClass lookupClassReq(String class_name) {
  	JavaClass c = lookupClass(class_name);
  	if (c == null)
  		throw new RuntimeException("Could not find class \"" + class_name + "\"");
  	return c;
  }

  /** @return class file object for given Java class.
   */
  public static ClassPath.ClassFile lookupClassFile(String class_name) {
    try {
      return class_path.getClassFile(class_name);
    } catch(IOException e) { return null; }
  }

  /** Clear the repository.
   */
  public static void clearCache() {
    classes = new WeakHashMap();
    OBJECT  = lookupClass("java.lang.Object");

    if(OBJECT == null)
      System.err.println("Warning: java.lang.Object not found on CLASSPATH!");
    else
      classes.put("java.lang.Object", OBJECT);
  }

  /**
   * Add clazz to repository if there isn't an equally named class already in there.
   *
   * @return old entry in repository
   */
  public static JavaClass addClass(JavaClass clazz) {
    String    name = clazz.getClassName();
    JavaClass cl   = (JavaClass)classes.get(name);

    if(cl == null)
      classes.put(name, cl = clazz);

    return cl;
  }

  private static final JavaClass getSuperClass(JavaClass clazz) {
    if(clazz == OBJECT)
      return null;

    return lookupClassReq(clazz.getSuperclassName());
  }

  /**
   * @return list of super classes of clazz in ascending order, i.e.
   * Object is always the last element
   */
  public static JavaClass[] getSuperClasses(JavaClass clazz) {
    ClassVector vec = new ClassVector();

    for(clazz = getSuperClass(clazz); clazz != null; clazz = getSuperClass(clazz))
      vec.addElement(clazz);

    return vec.toArray();
  }

  /**
   * @return list of super classes of clazz in ascending order, i.e.
   * Object is always the last element
   */
  public static JavaClass[] getSuperClasses(String class_name) {
  	 JavaClass c = lookupClassReq(class_name);
    return getSuperClasses(c);
  }

  /**
   * @return all interfaces implemented by class and its super
   * classes and the interfaces that those interfaces extend, and so on
   */
  public static JavaClass[] getInterfaces(JavaClass clazz) {
    ClassVector vec   = new ClassVector();
    ClassQueue  queue = new ClassQueue();

    queue.enqueue(clazz);

    while(!queue.empty()) {
      clazz = queue.dequeue();

      String   s          = clazz.getSuperclassName();
      String[] interfaces = clazz.getInterfaceNames();

      if(clazz.isInterface())
	vec.addElement(clazz);
      else if(!s.equals("java.lang.Object"))
	queue.enqueue(lookupClassReq(s));
      
      for(int i=0; i < interfaces.length; i++)
	queue.enqueue(lookupClassReq(interfaces[i]));
    }

    return vec.toArray();
  }

  /**
   * @return all interfaces implemented by class and its super
   * classes and the interfaces that extend those interfaces, and so on
   */
  public static JavaClass[] getInterfaces(String class_name) {
    return getInterfaces(lookupClassReq(class_name));
  }

  /**
   * @return true, if clazz is an instance of super_class
   */
  public static boolean instanceOf(JavaClass clazz, JavaClass super_class) {
    if(clazz == super_class)
      return true;

    JavaClass[] super_classes = getSuperClasses(clazz);

//for(int i=0; i < super_classes.length; i++)
//	System.err.println(super_classes[i]);

    for(int i=0; i < super_classes.length; i++)
      if(super_classes[i] == super_class)
	return true;

    if(super_class.isInterface())
      return implementationOf(clazz, super_class);

    return false;
  }

  /**
   * @return true, if clazz is an instance of super_class
   */
  public static boolean instanceOf(String clazz, String super_class) {
    return instanceOf(lookupClassReq(clazz), lookupClassReq(super_class));
  }
    
  /**
   * @return true, if clazz is an instance of super_class
   */
  public static boolean instanceOf(JavaClass clazz, String super_class) {
    return instanceOf(clazz, lookupClassReq(super_class));
  }

  /**
   * @return true, if clazz is an instance of super_class
   */
  public static boolean instanceOf(String clazz, JavaClass super_class) {
    return instanceOf(lookupClassReq(clazz), super_class);
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   */
  public static boolean implementationOf(JavaClass clazz, JavaClass inter) {
    if(clazz == inter)
      return true;

    JavaClass[] super_interfaces = getInterfaces(clazz);

    for(int i=0; i < super_interfaces.length; i++)
      if(super_interfaces[i] == inter)
	return true;

    return false;
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   */
  public static boolean implementationOf(String clazz, String inter) {
    return implementationOf(lookupClassReq(clazz), lookupClassReq(inter));
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   */
  public static boolean implementationOf(JavaClass clazz, String inter) {
    return implementationOf(clazz, lookupClassReq(inter));
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   */
  public static boolean implementationOf(String clazz, JavaClass inter) {
    return implementationOf(lookupClassReq(clazz), inter);
  }
}
