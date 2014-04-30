
#include "fastj.hpp"
#include "jo__java__lang__NullPointerException.hpp"
#include "jo__java__lang__ClassCastException.hpp"
#include "jo__java__lang__AbstractMethodError.hpp"
#include "jo__java__lang__ArrayIndexOutOfBoundsException.hpp"

#include "fjnative.hpp"
#include <signal.h>

void fj_throw_cce(jobject* obj)
{
	FJ_THROWNEW(jo__java__lang__ClassCastException);
}

void fj_throw_npe()
{
	FJ_THROWNEW(jo__java__lang__NullPointerException);
}

void fj_throw_aioob()
{
	FJ_THROWNEW(jo__java__lang__ArrayIndexOutOfBoundsException);
}

void fj_throw_abstracterror()
{
	FJ_THROWNEW(jo__java__lang__AbstractMethodError);
}

static void fj_segv_handler(int sig)
{
	fj_throw_npe();
}

void fj_init_signals()
{
	struct sigaction sa ;
	sigset_t        sa_mask ;
	
	sigemptyset(&sa.sa_mask) ;
	sa.sa_handler = &fj_segv_handler ;
	sa.sa_flags  = 0;
	sigaction(SIGSEGV, &sa, (struct sigaction *)0);
}
