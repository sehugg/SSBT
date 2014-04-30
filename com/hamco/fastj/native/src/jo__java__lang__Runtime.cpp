#include "fastj.hpp"
#include "jo__java__lang__UnsatisfiedLinkError.hpp"
#include "jo__java__io__InputStream.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__lang__Runtime.hpp"
#include "jo__java__io__OutputStream.hpp"
#include "jo__java__lang__StringBuffer.hpp"
#include "jo__java__util__StringTokenizer.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__Process.hpp"
#include "jo__java__lang__System.hpp"
#include "jo__java__lang__SecurityManager.hpp"
#include "jo__java__util__Vector.hpp"

#include "fjnative.hpp"

// todo: lots of this

FJ_NATIVE jo__java__lang__Object* jo__java__lang__Runtime::jms__copyCurrentStackTrace()
{
	FJ_DEBUGMSG("*** incomplete native method java.lang.Runtime.static native Object copyCurrentStackTrace()\n");
	return NULL;
}

FJ_NATIVE jo__java__lang__Process* jo__java__lang__Runtime::jm__execInternal(jo__java__lang__Runtime* _this, jarray< jo__java__lang__String* >* jp__0, jarray< jo__java__lang__String* >* jp__1)
{
	FJ_DEBUGMSG("*** incomplete native method java.lang.Runtime.native Process execInternal(String[], String[])\n");
	return NULL;
}

FJ_NATIVE void jo__java__lang__Runtime::jm__exitInternal(jo__java__lang__Runtime* _this, jint jp__0)
{
	exit(jp__0);
}

FJ_NATIVE jlong jo__java__lang__Runtime::jm__freeMemory(jo__java__lang__Runtime* _this)
{
	FJ_DEBUGMSG("*** incomplete native method java.lang.Runtime.public native long freeMemory()\n");
	return 0;
}

FJ_NATIVE void jo__java__lang__Runtime::jm__gc(jo__java__lang__Runtime* _this)
{
	// not yet implemented
}

FJ_NATIVE jo__java__lang__String* jo__java__lang__Runtime::jms__getLibraryPath()
{
	return FJ_MAKESTRING(".");
}

FJ_NATIVE jo__java__lang__String* jo__java__lang__Runtime::jm__nativeGetLibname(jo__java__lang__Runtime* _this, jo__java__lang__String* jp__0, jo__java__lang__String* jp__1)
{
	return jp__1;
}

FJ_NATIVE void jo__java__lang__Runtime::jm__nativeLoad(jo__java__lang__Runtime* _this, jo__java__lang__String* jp__0)
{
	FJ_DEBUGMSG("*** incomplete native method java.lang.Runtime.native void nativeLoad(String)\n");
}

FJ_NATIVE void jo__java__lang__Runtime::jm__runFinalization(jo__java__lang__Runtime* _this)
{
	// not yet implemented
}

FJ_NATIVE void jo__java__lang__Runtime::jms__runFinalizersOnExitInternal(jboolean jp__0)
{
	FJ_DEBUGMSG("*** incomplete native method java.lang.Runtime.public static native void runFinalizersOnExitInternal(boolean)\n");
}

FJ_NATIVE jlong jo__java__lang__Runtime::jm__totalMemory(jo__java__lang__Runtime* _this)
{
	FJ_DEBUGMSG("*** incomplete native method java.lang.Runtime.public native long totalMemory()\n");
	return 0;
}

FJ_NATIVE void jo__java__lang__Runtime::jm__traceInstructions(jo__java__lang__Runtime* _this, jboolean jp__0)
{
	FJ_DEBUGMSG("*** incomplete native method java.lang.Runtime.public native void traceInstructions(boolean)\n");

}

FJ_NATIVE void jo__java__lang__Runtime::jm__traceMethodCalls(jo__java__lang__Runtime* _this, jboolean jp__0)
{
	FJ_DEBUGMSG("*** incomplete native method java.lang.Runtime.public native void traceMethodCalls(boolean)\n");

}
