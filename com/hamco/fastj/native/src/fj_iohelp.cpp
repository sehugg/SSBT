
#include "fastj.hpp"
#include "fjiohelp.hpp"

void fj_throwioe(int errcode)
{
	FJ_THROWNEWMSG(jo__java__io__IOException, FJ_MAKESTRING(strerror(errcode)));
}

int fj_filestat(jo__java__lang__String* fnstr, struct stat *sb)
{
	char *fn = fj_jstring_to_utf(fnstr);
	int res = stat(fn, sb);
	free(fn);
	if (res == -1)
	{
		int err = errno;
		if (err == ENOTDIR || err == ENOENT || err == EACCES)
			return err;
		fj_throwioe(err);
	} else
		return 0;
}

jboolean fj_fileaccess(jo__java__lang__String* fnstr, int mode)
{
	char *fn = fj_jstring_to_utf(fnstr);
	int res = access(fn, mode);
	free(fn);
	return (res==0);
}

int fj_fileopen(jo__java__lang__String* fnstr, int flags, int mode)
{
	char *fn = fj_jstring_to_utf(fnstr);
	int res = open(fn, flags, mode);
	free(fn);
	return res;
}

