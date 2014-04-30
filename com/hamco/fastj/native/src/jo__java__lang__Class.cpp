#include "fastj.hpp"
#include "jo__java__lang__reflect__Field.hpp"
#include "jo__java__lang__ClassNotFoundException.hpp"
#include "jo__java__lang__SecurityException.hpp"
#include "jo__java__io__InputStream.hpp"
#include "jo__gnu__java__lang__ClassHelper.hpp"
#include "jo__java__lang__reflect__Constructor.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__net__URL.hpp"
#include "jo__java__lang__Class.hpp"
#include "jo__java__lang__StringBuffer.hpp"
#include "jo__java__lang__reflect__InvocationTargetException.hpp"
#include "jo__java__lang__ClassLoader.hpp"
#include "jo__java__lang__reflect__Method.hpp"
#include "jo__java__lang__IllegalAccessException.hpp"
#include "jo__java__lang__Error.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__NoSuchFieldException.hpp"
#include "jo__java__lang__NoSuchMethodException.hpp"
#include "jo__java__lang__InstantiationException.hpp"
#include "jo__java__lang__IllegalArgumentException.hpp"
#include "jo__java__lang__UnknownError.hpp"

#include "fjnative.hpp"

FJ_NATIVE jo__java__lang__Class* jo__java__lang__Class::jms__forName(jo__java__lang__String* jp__0)
{
	char *utfname = fj_jstring_to_utf(jp__0);
	FJ_DEBUGMSG("forName(");
	FJ_DEBUGMSG(utfname);
	FJ_DEBUGMSG(")\n");
	jclinit_func* initfunc = fj_get_clinit_func(utfname);
	if (initfunc == NULL)
	{
		free(utfname);
		// todo: include name of class in msg
		FJ_THROWNEW(jo__java__lang__ClassNotFoundException);
	}
	jo__java__lang__Class* clazz = (*initfunc)();
	free(utfname);
	if (clazz == NULL)
	{
		FJ_THROWNEW(jo__java__lang__ClassNotFoundException);
	}
	return clazz;
}

// todo: check access rights of default constructor
// todo: proper exception throwing
FJ_NATIVE jo__java__lang__Object* jo__java__lang__Class::jm__newInstance(jo__java__lang__Class* jp__0)
{
	jmethod_table* jmtab = (jmethod_table*)jp__0->jf__method__table;
	if (!jmtab->default_ctor)
		FJ_THROWNEW(jo__java__lang__NoSuchMethodException);
	jo__java__lang__Object* newobj = (jo__java__lang__Object*)fj_new_object(jmtab);
	jmtab->default_ctor(newobj);
	return newobj;
}

FJ_NATIVE jo__java__lang__reflect__Constructor* jo__java__lang__Class::jm__getConstructor(jo__java__lang__Class* _this, jarray< jo__java__lang__Class* >* jp__0)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Constructor getConstructor(Class[])
		throws NoSuchMethodException, SecurityException\n");

}

FJ_NATIVE jarray< jo__java__lang__reflect__Constructor* >* jo__java__lang__Class::jm__getConstructors(jo__java__lang__Class* _this)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Constructor[] getConstructors()
		throws SecurityException\n");

}

FJ_NATIVE jarray< jo__java__lang__Class* >* jo__java__lang__Class::jm__getDeclaredClasses(jo__java__lang__Class* _this)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native Class[] getDeclaredClasses()
		throws SecurityException\n");

}

FJ_NATIVE jo__java__lang__reflect__Constructor* jo__java__lang__Class::jm__getDeclaredConstructor(jo__java__lang__Class* _this, jarray< jo__java__lang__Class* >* jp__0)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Constructor getDeclaredConstructor(Class[])
		throws NoSuchMethodException, SecurityException\n");

}

FJ_NATIVE jarray< jo__java__lang__reflect__Constructor* >* jo__java__lang__Class::jm__getDeclaredConstructors(jo__java__lang__Class* _this)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Constructor[] getDeclaredConstructors()
		throws SecurityException\n");

}

FJ_NATIVE jo__java__lang__reflect__Field* jo__java__lang__Class::jm__getDeclaredField(jo__java__lang__Class* _this, jo__java__lang__String* jp__0)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Field getDeclaredField(String)
		throws NoSuchFieldException, SecurityException\n");

}

FJ_NATIVE jarray< jo__java__lang__reflect__Field* >* jo__java__lang__Class::jm__getDeclaredFields(jo__java__lang__Class* _this)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Field[] getDeclaredFields()
		throws SecurityException\n");

}

FJ_NATIVE jo__java__lang__reflect__Method* jo__java__lang__Class::jm__getDeclaredMethod(jo__java__lang__Class* _this, jo__java__lang__String* jp__0, jarray< jo__java__lang__Class* >* jp__1)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Method getDeclaredMethod(String, Class[])
		throws NoSuchMethodException, SecurityException\n");

}

FJ_NATIVE jarray< jo__java__lang__reflect__Method* >* jo__java__lang__Class::jm__getDeclaredMethods(jo__java__lang__Class* _this)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Method[] getDeclaredMethods()
		throws SecurityException\n");

}

FJ_NATIVE jo__java__lang__reflect__Field* jo__java__lang__Class::jm__getField(jo__java__lang__Class* _this, jo__java__lang__String* jp__0)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Field getField(String)
		throws NoSuchFieldException, SecurityException\n");

}

FJ_NATIVE jarray< jo__java__lang__reflect__Field* >* jo__java__lang__Class::jm__getFields(jo__java__lang__Class* _this)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Field[] getFields()
		throws SecurityException\n");

}

FJ_NATIVE jo__java__lang__reflect__Method* jo__java__lang__Class::jm__getMethod(jo__java__lang__Class* _this, jo__java__lang__String* jp__0, jarray< jo__java__lang__Class* >* jp__1)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Method getMethod(String, Class[])
		throws NoSuchMethodException, SecurityException\n");

}

FJ_NATIVE jarray< jo__java__lang__reflect__Method* >* jo__java__lang__Class::jm__getMethods(jo__java__lang__Class* _this)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Class.public native java.lang.reflect.Method[] getMethods()
		throws SecurityException\n");

}

FJ_NATIVE jboolean jo__java__lang__Class::jm__isAssignableFrom(jo__java__lang__Class* _this, jo__java__lang__Class* jp__0)
{
	return fj_is_assignable((jmethod_table*)jp__0->jf__method__table, (jmethod_table*)_this->jf__method__table);
}

FJ_NATIVE jboolean jo__java__lang__Class::jm__isInstance(jo__java__lang__Class* _this, jo__java__lang__Object* jp__0)
{
	return fj_is_assignable(jp__0->jmtab, (jmethod_table*)_this->jf__method__table);
}
