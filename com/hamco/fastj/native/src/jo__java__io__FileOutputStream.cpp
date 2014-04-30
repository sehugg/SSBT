#include "fastj.hpp"
#include "jo__java__lang__SecurityException.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__io__OutputStream.hpp"
#include "jo__java__io__File.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__io__IOException.hpp"
#include "jo__java__lang__System.hpp"
#include "jo__java__lang__SecurityManager.hpp"
#include "jo__java__io__FileOutputStream.hpp"
#include "jo__java__io__FileDescriptor.hpp"

#include "fjnative.hpp"
#include "fjiohelp.hpp"

FJ_NATIVE void jo__java__io__FileOutputStream::jm__closeInternal(jo__java__io__FileOutputStream* _this, jint fd)
{
	close(fd);
}

FJ_NATIVE jint jo__java__io__FileOutputStream::jm__open(jo__java__io__FileOutputStream* _this, 
	jo__java__lang__String* name, jboolean append)
{
	// todo: err check properly
	int fd = fj_fileopen(name, O_RDWR | O_CREAT | (append?0:O_TRUNC), 0777); //todo: const
	if (append && (fd != -1))
	{
		int rc = lseek(fd, 0, SEEK_END);
		if (rc == -1)
		{
			close(fd);
			fj_throwioe(errno);
		}
	}
	return fd;
}

FJ_NATIVE void jo__java__io__FileOutputStream::jm__writeInternal(jo__java__io__FileOutputStream* _this, 
	jint fd, jarray< jbyte >* bytes, jint offset, jint len)
{
	// todo: err checking
	bytes->checkRange(offset, len);
	jbyte* buf = &((*bytes)[offset]);
	write(fd, buf, len);
}
