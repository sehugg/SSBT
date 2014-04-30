#include "fastj.hpp"
#include "jo__java__lang__IllegalMonitorStateException.hpp"
#include "jo__java__lang__NotSupportedError.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__lang__InterruptedException.hpp"
#include "jo__java__lang__CloneNotSupportedException.hpp"
#include "jo__java__lang__Cloneable.hpp"
#include "jo__java__lang__Class.hpp"
#include "jo__java__lang__StringBuffer.hpp"
#include "jo__java__lang__Throwable.hpp"
#include "jo__java__lang__Integer.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__System.hpp"

#include "fjnative.hpp"

#include <stdlib.h>

static jbyte jtype_sizes[11] = {
	sizeof(jboolean),
	sizeof(jchar),
	sizeof(jfloat),
	sizeof(jdouble),
	sizeof(jbyte),
	sizeof(jshort),
	sizeof(jint),
	sizeof(jlong),
	0,
	sizeof(jobject*),
	sizeof(jobject*)
};

FJ_NATIVE jo__java__lang__Object* jo__java__lang__Object::jms__cloneObject(jo__java__lang__Object* jp__0)
{
	// todo: copying more than neccessary
	// todo: arrays
	jobject* o = fj_new_object(jp__0->jmtab);
	// are we copying an array?
	if (o->jmtab->clazz->jf__element__type)
	{
		__jarray* destarr = (__jarray*)o;
		__jarray* srcarr = (__jarray*)jp__0;
		int elsize = jtype_sizes[o->jmtab->clazz->jf__classtype - 4];
		destarr->__allocate(srcarr->length(), elsize);
		memcpy(srcarr->__data(), destarr->__data(), srcarr->length()*elsize);
	}
	else
		memcpy(o, jp__0, jp__0->jmtab->objsize);
	return (jo__java__lang__Object*)o;
}

FJ_NATIVE jo__java__lang__Class* jo__java__lang__Object::jm__getClass(jo__java__lang__Object* _this)
{
	return _this->jmtab->clazz;
}
