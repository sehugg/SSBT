#include "fastj.hpp"
#include "jo__java__lang__ThreadGroup.hpp"
#include "jo__java__lang__NotSupportedError.hpp"
#include "jo__java__lang__Runnable.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__lang__InterruptedException.hpp"
#include "jo__java__lang__StringBuffer.hpp"
#include "jo__java__lang__Throwable.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__IllegalArgumentException.hpp"
#include "jo__java__lang__Thread.hpp"

#include "fjnative.hpp"
#include <unistd.h>

FJ_NATIVE jint jo__java__lang__Thread::jm__countStackFrames(jo__java__lang__Thread* _this)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.Thread::countStackFrames\n");
}

FJ_NATIVE void jo__java__lang__Thread::jms__sleep(jlong jp__0, jint jp__1)
{
	jlong sec = jp__0/1000;
	jlong usec = jp__1+((jp__0%1000)*1000);
	sleep(sec);
	usleep(usec);
}
