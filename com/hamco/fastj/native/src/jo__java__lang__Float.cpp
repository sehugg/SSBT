#include "fastj.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__lang__Float.hpp"
#include "jo__java__lang__Class.hpp"
#include "jo__java__lang__Number.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__NullPointerException.hpp"
#include "jo__java__lang__NumberFormatException.hpp"
#include "jo__java__lang__System.hpp"
#include "jo__java__lang__Double.hpp"

#include "fjnative.hpp"

#include <math.h>

FJ_NATIVE jint jo__java__lang__Float::jms__floatToIntBits(jfloat x)
{
	return *((jint*)&x);
}

FJ_NATIVE jfloat jo__java__lang__Float::jms__intBitsToFloat(jint x)
{
	return *((jfloat*)&x);
}

FJ_NATIVE jfloat jo__java__lang__Float::jms__parseFloat(jo__java__lang__String* x)
{
	return jo__java__lang__Double::jms__parseDouble(x);
}

FJ_NATIVE jo__java__lang__String* jo__java__lang__Float::jms__toString(jfloat x)
{
	return jo__java__lang__Double::jms__toString(x);
}
