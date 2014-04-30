#include "fastj.hpp"
#include "jo__java__lang__SecurityException.hpp"
#include "jo__java__io__InputStream.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__io__FileInputStream.hpp"
#include "jo__java__io__File.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__io__FileNotFoundException.hpp"
#include "jo__java__io__IOException.hpp"
#include "jo__java__lang__SecurityManager.hpp"
#include "jo__java__lang__System.hpp"
#include "jo__java__io__FileDescriptor.hpp"

#include "fjnative.hpp"
#include "fjiohelp.hpp"

#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>

FJ_NATIVE void jo__java__io__FileInputStream::jm__closeInternal(jo__java__io__FileInputStream* _this, jint fd)
{
	close(fd);
}

FJ_NATIVE jlong jo__java__io__FileInputStream::jm__getFileLength(jo__java__io__FileInputStream* _this, jint fd)
{
	struct stat sb;
	if (fstat(fd, &sb)==0)
		return sb.st_size;
	else
		fj_throwioe(errno);
}

FJ_NATIVE jint jo__java__io__FileInputStream::jm__open(jo__java__io__FileInputStream* _this, 
	jo__java__lang__String* name)
{
	char* filename = fj_jstring_to_utf(name);
	int fd = open(filename, O_RDONLY);
	if (fd == -1)
	{
		close(fd);
		fj_throwioe(errno);
	}
	return fd;
}

FJ_NATIVE jint jo__java__io__FileInputStream::jm__readInternal(jo__java__io__FileInputStream* _this, jint fd, 
	jarray< jbyte >* bytes, jint offset, jint len)
{
	// todo: err checking
	bytes->checkRange(offset, len);
	jbyte* buf = &((*bytes)[offset]);
	return read(fd, buf, len);
}

FJ_NATIVE jlong jo__java__io__FileInputStream::jm__skipInternal(jo__java__io__FileInputStream* _this, 
	jint fd, jlong nbytes)
{
	off_t origpos = lseek(fd, 0, SEEK_CUR);
	if (origpos == -1)
	{
		fj_throwioe(errno);
	}
	off_t pos = lseek(fd, nbytes, SEEK_CUR);
	if (pos == -1)
	{
		fj_throwioe(errno);
	}
	return (pos-origpos);
}
