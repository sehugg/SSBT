#include "fastj.hpp"
#include "jo__java__lang__ThreadGroup.hpp"
#include "jo__java__lang__SecurityException.hpp"
#include "jo__java__security__Permission.hpp"
#include "jo__java__net__InetAddress.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__lang__Class.hpp"
#include "jo__java__lang__ClassLoader.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__SecurityManager.hpp"
#include "jo__java__lang__SecurityContext.hpp"
#include "jo__java__io__FileDescriptor.hpp"
#include "jo__java__lang__Thread.hpp"

#include "fjnative.hpp"

FJ_NATIVE jo__java__lang__ClassLoader* jo__java__lang__SecurityManager::jm__currentClassLoader(jo__java__lang__SecurityManager* _this)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.SecurityManager.protected native ClassLoader currentClassLoader()\n");

}

FJ_NATIVE jarray< jo__java__lang__Class* >* jo__java__lang__SecurityManager::jm__getClassContext(jo__java__lang__SecurityManager* _this)
{
	FJ_NOTYETIMPL("*** incomplete native method java.lang.SecurityManager.protected native Class[] getClassContext()\n");

}
