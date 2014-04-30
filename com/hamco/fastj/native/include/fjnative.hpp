#ifndef _FJNATIVE_HPP
#define _FJNATIVE_HPP


#include "fastj.hpp"
#include "jo__java__lang__AbstractMethodError.hpp"

#define FJ_NOTYETIMPL(MSG) do { \
	FJ_DEBUGMSG(MSG); FJ_DEBUGMSG("\n"); \
	FJ_THROWNEWMSG(jo__java__lang__AbstractMethodError, FJ_MAKESTRING(MSG)); \
} while (0)

#endif /* _FJNATIVE_HPP */
