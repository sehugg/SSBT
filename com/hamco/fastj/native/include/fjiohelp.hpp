#ifndef _FJIOHELP_HPP
#define _FJIOHELP_HPP

#include "jo__java__io__IOException.hpp"

#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>


extern void fj_throwioe(int errcode);
extern int fj_filestat(jo__java__lang__String* fnstr, struct stat *sb);
extern jboolean fj_fileaccess(jo__java__lang__String* fnstr, int mode);
extern int fj_fileopen(jo__java__lang__String* fnstr, int flags, int mode);


#endif /* _FJIOHELP_HPP */
