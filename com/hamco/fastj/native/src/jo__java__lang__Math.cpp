#include "fastj.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__lang__Float.hpp"
#include "jo__java__util__Random.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__System.hpp"
#include "jo__java__lang__Math.hpp"
#include "jo__java__lang__Double.hpp"

#include "fjnative.hpp"
#include <math.h>

// todo: these are just so wrong

FJ_NATIVE jdouble jo__java__lang__Math::jms__IEEEremainder(jdouble jp__0, jdouble jp__1)
{
	return remainder(jp__0, jp__1);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__acos(jdouble jp__0)
{
	return acos(jp__0);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__asin(jdouble jp__0)
{
	return asin(jp__0);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__atan(jdouble jp__0)
{
	return atan(jp__0);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__atan2(jdouble jp__0, jdouble jp__1)
{
	return atan2(jp__0, jp__1);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__ceil(jdouble jp__0)
{
	return ceil(jp__0);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__cos(jdouble jp__0)
{
	return cos(jp__0);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__exp(jdouble jp__0)
{
	return exp(jp__0);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__floor(jdouble jp__0)
{
	return floor(jp__0);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__log(jdouble jp__0)
{
	return log(jp__0);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__pow(jdouble jp__0, jdouble jp__1)
{
	return pow(jp__0, jp__1);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__rint(jdouble jp__0)
{
	return rint(jp__0);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__sin(jdouble jp__0)
{
	return sin(jp__0);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__sqrt(jdouble jp__0)
{
	return sqrt(jp__0);
}

FJ_NATIVE jdouble jo__java__lang__Math::jms__tan(jdouble jp__0)
{
	return tan(jp__0);
}
