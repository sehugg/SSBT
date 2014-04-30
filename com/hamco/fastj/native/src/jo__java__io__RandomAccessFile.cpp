#include "fastj.hpp"
#include "jo__java__lang__SecurityException.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__lang__Float.hpp"
#include "jo__java__io__RandomAccessFile.hpp"
#include "jo__java__lang__StringBuffer.hpp"
#include "jo__java__io__File.hpp"
#include "jo__java__io__EOFException.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__io__DataInputStream.hpp"
#include "jo__java__io__DataInput.hpp"
#include "jo__java__io__IOException.hpp"
#include "jo__java__lang__System.hpp"
#include "jo__java__lang__SecurityManager.hpp"
#include "jo__java__io__UTFDataFormatException.hpp"
#include "jo__java__io__DataOutput.hpp"
#include "jo__java__io__DataOutputStream.hpp"
#include "jo__java__lang__Double.hpp"
#include "jo__java__lang__IllegalArgumentException.hpp"
#include "jo__java__io__FileDescriptor.hpp"

#include "fjnative.hpp"
#include "fjiohelp.hpp"

FJ_NATIVE void jo__java__io__RandomAccessFile::jm__closeInternal(jo__java__io__RandomAccessFile* _this, jint fd)
{
	close(fd);
}

FJ_NATIVE jlong jo__java__io__RandomAccessFile::jm__getFilePointerInternal(jo__java__io__RandomAccessFile* _this, jint fd)
{
	off_t offset = lseek(fd, 0, SEEK_CUR);
	if (offset == -1)
		fj_throwioe(errno);
	return (jlong)offset;
}

FJ_NATIVE jlong jo__java__io__RandomAccessFile::jm__lengthInternal(jo__java__io__RandomAccessFile* _this, jint fd)
{
	off_t oldofs = lseek(fd, 0, SEEK_CUR);
	if (oldofs == -1) 
		fj_throwioe(errno);
	off_t endofs = lseek(fd, 0, SEEK_END);
	if (endofs == -1) 
		fj_throwioe(errno);
	lseek(fd, oldofs, SEEK_SET);
	return (jlong)endofs;

}

// todo: check this in the .java file -- i don't think it's right
FJ_NATIVE jint jo__java__io__RandomAccessFile::jm__open(jo__java__io__RandomAccessFile* _this, 
	jo__java__lang__String* filename, jboolean readonly)
{
	int fd = fj_fileopen(filename, readonly?O_RDONLY:O_RDWR, 0777); //todo: const
	if (fd == -1)
		fj_throwioe(errno);
	return fd;
}

FJ_NATIVE jint jo__java__io__RandomAccessFile::jm__readInternal(jo__java__io__RandomAccessFile* _this, 
	jint fd, jarray< jbyte >* bytes, jint offset, jint len)
{
	// todo: err checking
	bytes->checkRange(offset, len);
	jbyte* buf = &((*bytes)[offset]);
	return read(fd, buf, len);
}

FJ_NATIVE void jo__java__io__RandomAccessFile::jm__seekInternal(jo__java__io__RandomAccessFile* _this, jint fd, jlong newofs)
{
	off_t ofs = lseek(fd, newofs, SEEK_SET);
	if (ofs == -1) 
		fj_throwioe(errno);
}

FJ_NATIVE void jo__java__io__RandomAccessFile::jm__setLengthInternal(jo__java__io__RandomAccessFile* _this, jint jp__0, jlong jp__1)
{
	FJ_NOTYETIMPL("*** incomplete native method java.io.RandomAccessFile.private native void setLengthInternal(int, long)
		throws java.io.IOException\n");

}

// todo: the spec is pretty durn vague -- is it ok tho?
FJ_NATIVE jint jo__java__io__RandomAccessFile::jm__skipInternal(jo__java__io__RandomAccessFile* _this, jint fd, jint nbytes)
{
	off_t oldofs = lseek(fd, 0, SEEK_CUR);
	if (oldofs == -1) 
		fj_throwioe(errno);
	off_t newofs = lseek(fd, nbytes, SEEK_CUR);
	if (newofs == -1) 
		fj_throwioe(errno);
	return (jint)(newofs - oldofs);
}

FJ_NATIVE void jo__java__io__RandomAccessFile::jm__writeInternal(jo__java__io__RandomAccessFile* _this, 
	jint fd, jarray< jbyte >* bytes, jint offset, jint len)
{
	// todo: err checking
	bytes->checkRange(offset, len);
	jbyte* buf = &((*bytes)[offset]);
	write(fd, buf, len);
}
