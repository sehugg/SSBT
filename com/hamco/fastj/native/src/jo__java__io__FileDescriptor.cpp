#include "fastj.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__io__SyncFailedException.hpp"
#include "jo__java__lang__System.hpp"
#include "jo__java__io__FileDescriptor.hpp"

#include "fjnative.hpp"

#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>

FJ_NATIVE void jo__java__io__FileDescriptor::jm__syncInternal(jo__java__io__FileDescriptor* _this, jint jp__0)
{
	// todo
	fsync(jp__0);
}

FJ_NATIVE jboolean jo__java__io__FileDescriptor::jm__validInternal(jo__java__io__FileDescriptor* _this, jint jp__0)
{
	// todo
	struct stat sb;
	return (fstat(jp__0, &sb) == 0);
}
