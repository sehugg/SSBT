#include "fastj.hpp"
#include "jo__java__lang__SecurityException.hpp"
#include "jo__java__io__InputStream.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__lang__Runtime.hpp"
#include "jo__java__io__FileInputStream.hpp"
#include "jo__java__io__OutputStream.hpp"
#include "jo__java__io__PrintStream.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__NullPointerException.hpp"
#include "jo__java__util__Properties.hpp"
#include "jo__java__lang__System.hpp"
#include "jo__java__lang__SecurityManager.hpp"
#include "jo__java__io__FileOutputStream.hpp"
#include "jo__java__io__FileDescriptor.hpp"
#include "jo__java__lang__ArrayStoreException.hpp"
#include "jo__java__lang__IndexOutOfBoundsException.hpp"

#include "fjnative.hpp"

#include <sys/time.h>
#include <string.h>

extern jboolean fj_is_reference_type(jmethod_table* jmtab);

FJ_NATIVE void jo__java__lang__System::jms__arraycopy(
	jo__java__lang__Object* src, jint srcpos, jo__java__lang__Object* dest, jint destpos, jint count)
{
	if (src == NULL || dest == NULL)
		fj_throw_npe();
	// make sure both are array types and are compatible
	if (!(src->jmtab->elemtype && dest->jmtab->elemtype))
	{
		FJ_THROWNEW(jo__java__lang__ArrayStoreException);
	}
	jarray<jbyte>* srcarr = (jarray<jbyte>*)src;
	jarray<jbyte>* destarr = (jarray<jbyte>*)dest;
	// check bounds
	if (srcpos < 0 || destpos < 0 || count < 0 || 
		srcpos+count > srcarr->length() ||
		destpos+count > destarr->length())
	{
		FJ_THROWNEW(jo__java__lang__IndexOutOfBoundsException);
	}
	// if array types are compatible, just copy it with memmove
	if (fj_is_assignable(src->jmtab, dest->jmtab))
	{
		// copy it!
		int elsize;
		if (fj_is_reference_type(src->jmtab->elemtype))
			elsize = sizeof(jobject*);
		else
			elsize = src->jmtab->elemtype->objsize;
		memmove(&(*destarr)[destpos*elsize], &(*srcarr)[srcpos*elsize], count*elsize);
	} 
	// otherwise we have to copy & check each element
	else 
	{
		if (!(fj_is_reference_type(src->jmtab->elemtype) && fj_is_reference_type(dest->jmtab->elemtype)))
		{
			FJ_THROWNEW(jo__java__lang__ArrayStoreException);
		}
		jarray<jobject*>* asrcarr = (jarray<jobject*>*)src;
		jarray<jobject*>* adestarr = (jarray<jobject*>*)dest;
		for (int i=0; i<count; i++)
		{
			jobject* o = (*asrcarr)[i+srcpos];
			if (o!=NULL && !FJ_INSTANCEOF(o, dest->jmtab->elemtype))
			{
				FJ_THROWNEW(jo__java__lang__ArrayStoreException);
			}
			(*adestarr)[i+destpos] = o;
		}
	}
}

FJ_NATIVE jlong jo__java__lang__System::jms__currentTimeMillis()
{
  /* Note: this implementation copied directly from Japhar's, by Chris Toshok. */
  jlong result;
  struct timeval tp;

  if (gettimeofday(&tp, NULL) == -1)
  	FJ_FATAL_ERROR("gettimeofday call failed.\n");

  result = (jlong)tp.tv_sec;
  result *= 1000;
  result += (tp.tv_usec / 1000);

  return result;
}

FJ_NATIVE jint jo__java__lang__System::jms__identityHashCode(jo__java__lang__Object* jp__0)
{
	// just use the address of the durn object
	return (jint)jp__0;
}

FJ_NATIVE void jo__java__lang__System::jms__setErr(jo__java__io__PrintStream* jp__0)
{
	jo__java__lang__System::jf__err = jp__0;
}

FJ_NATIVE void jo__java__lang__System::jms__setIn(jo__java__io__InputStream* jp__0)
{
	jo__java__lang__System::jf__in = jp__0;
}

FJ_NATIVE void jo__java__lang__System::jms__setOut(jo__java__io__PrintStream* jp__0)
{
	jo__java__lang__System::jf__out = jp__0;
}

FJ_NATIVE void jo__java__lang__System::jms__insertSystemProperties(jo__java__util__Properties* props)
{
	// todo: stuff is bad
	// ya know we need a classpath
	char *cpstr = getenv("FJ_CLASSPATH");
	if (cpstr == NULL)
		cpstr = getenv("CLASSPATH");
	if (cpstr == NULL)
		cpstr = ".";
	jo__java__util__Properties::jm__setProperty(props, FJ_MAKESTRING("java.class.path"), FJ_MAKESTRING(cpstr));
	jo__java__util__Properties::jm__setProperty(props, FJ_MAKESTRING("file.separator"), FJ_MAKESTRING("/"));
	jo__java__util__Properties::jm__setProperty(props, FJ_MAKESTRING("path.separator"), FJ_MAKESTRING(":"));
	jo__java__util__Properties::jm__setProperty(props, FJ_MAKESTRING("line.separator"), FJ_MAKESTRING("\n"));
	jo__java__util__Properties::jm__setProperty(props, FJ_MAKESTRING("java.tmpdir"), FJ_MAKESTRING("/tmp"));
}
