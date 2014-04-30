#include "fastj.hpp"
#include "jo__java__lang__Comparable.hpp"
#include "jo__java__lang__ClassCastException.hpp"
#include "jo__java__lang__SecurityException.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__net__URL.hpp"
#include "jo__java__net__MalformedURLException.hpp"
#include "jo__java__lang__StringBuffer.hpp"
#include "jo__java__util__StringTokenizer.hpp"
#include "jo__java__io__File.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__NullPointerException.hpp"
#include "jo__java__io__IOException.hpp"
#include "jo__java__io__FilenameFilter.hpp"
#include "jo__java__lang__System.hpp"
#include "jo__java__lang__SecurityManager.hpp"
#include "jo__java__io__Serializable.hpp"
#include "jo__java__io__FileFilter.hpp"
#include "jo__java__lang__IllegalArgumentException.hpp"

#include "fjnative.hpp"
#include "fjiohelp.hpp"
#include <dirent.h>

const int FILEMODE = 0777;

FJ_NATIVE jboolean jo__java__io__File::jm__canReadInternal(jo__java__io__File* _this, jo__java__lang__String* jp__0)
{
	return fj_fileaccess(jp__0, R_OK);
}

FJ_NATIVE jboolean jo__java__io__File::jm__canWriteInternal(jo__java__io__File* _this, jo__java__lang__String* jp__0)
{
	return fj_fileaccess(jp__0, W_OK);
}

FJ_NATIVE jboolean jo__java__io__File::jms__createInternal(jo__java__lang__String* jp__0)
{
	int fd = fj_fileopen(jp__0, O_CREAT | O_EXCL, FILEMODE);
	if (fd > 0) {
		close(fd);
		return true;
	}
	int err = errno;
	if (err == EEXIST)
		return false;
	fj_throwioe(err);
}

FJ_NATIVE jboolean jo__java__io__File::jm__deleteInternal(jo__java__io__File* _this, jo__java__lang__String* jp__0)
{
	FJ_NOTYETIMPL("*** incomplete native method java.io.File.private native boolean deleteInternal(String)\n");
}

FJ_NATIVE jboolean jo__java__io__File::jm__existsInternal(jo__java__io__File* _this, jo__java__lang__String* jp__0)
{
	return fj_fileaccess(jp__0, F_OK);
}

FJ_NATIVE jboolean jo__java__io__File::jm__isDirectoryInternal(jo__java__io__File* _this, jo__java__lang__String* jp__0)
{
	struct stat sb;
	return (fj_filestat(jp__0, &sb)==0 && (sb.st_mode & S_IFDIR));
}

FJ_NATIVE jboolean jo__java__io__File::jm__isFileInternal(jo__java__io__File* _this, jo__java__lang__String* jp__0)
{
	struct stat sb;
	return (fj_filestat(jp__0, &sb)==0 && (sb.st_mode & S_IFREG));
}

FJ_NATIVE jlong jo__java__io__File::jm__lastModifiedInternal(jo__java__io__File* _this, 
	jo__java__lang__String* jp__0)
{
	struct stat sb;
	if (fj_filestat(jp__0, &sb)!=0)
		return 0;
	return ((jlong)sb.st_mtime)*((jlong)1000);
}

FJ_NATIVE jlong jo__java__io__File::jm__lengthInternal(jo__java__io__File* _this, jo__java__lang__String* jp__0)
{
	struct stat sb;
	if (fj_filestat(jp__0, &sb)==0)
		return sb.st_size;
	else
		return 0;
}

FJ_NATIVE jarray< jo__java__lang__String* >* jo__java__io__File::jm__listInternal(jo__java__io__File* _this, 
	jo__java__lang__String* jp__0)
{
	char* dirpath = fj_jstring_to_utf(jp__0);
	DIR* dir = opendir(dirpath);
	free(dirpath);
	if (dir == NULL)
		return NULL;
	// first count how many entries there are
	int count = 0;
	while ( readdir(dir) != NULL)
		count++;
	// now allocate an array & fill 'em
	jarray<jo__java__lang__String*>* arr = 
		(jarray<jo__java__lang__String*>*) FJ_ANEWARRAY(fj_getarrayclass(&jmtjo__java__lang__String), count);
	struct dirent *entry;
	rewinddir(dir);
	int n = 0;
	while ( n < count && (entry=readdir(dir)) != NULL )
	{
		jo__java__lang__String* fname = FJ_MAKESTRING(entry->d_name);
		arr->set(n, fname);
		n++;
	}
	closedir(dir);
	return arr;
}

FJ_NATIVE jboolean jo__java__io__File::jm__mkdirInternal(jo__java__io__File* _this, jo__java__lang__String* jp__0)
{
	FJ_NOTYETIMPL("*** incomplete native method java.io.File.private native boolean mkdirInternal(String)\n");
}

FJ_NATIVE jboolean jo__java__io__File::jm__renameToInternal(jo__java__io__File* _this, jo__java__lang__String* jp__0, jo__java__lang__String* jp__1)
{
	FJ_NOTYETIMPL("*** incomplete native method java.io.File.private native boolean renameToInternal(String, String)\n");
}

FJ_NATIVE jboolean jo__java__io__File::jm__setLastModifiedInternal(jo__java__io__File* _this, jo__java__lang__String* jp__0, jlong jp__1)
{
	FJ_NOTYETIMPL("*** incomplete native method java.io.File.private native boolean setLastModifiedInternal(String, long)\n");
}

FJ_NATIVE jboolean jo__java__io__File::jm__setReadOnlyInternal(jo__java__io__File* _this, jo__java__lang__String* jp__0)
{
	FJ_NOTYETIMPL("*** incomplete native method java.io.File.private native boolean setReadOnlyInternal(String)\n");
}
