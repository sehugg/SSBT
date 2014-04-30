
#include "fastj.hpp"

// this class defines class types for the 9 primitive types

#include "jo__java__lang__Class.hpp"

#include "jo__java__lang__Boolean.hpp"
#include "jo__java__lang__Character.hpp"
#include "jo__java__lang__Float.hpp"
#include "jo__java__lang__Double.hpp"
#include "jo__java__lang__Byte.hpp"
#include "jo__java__lang__Short.hpp"
#include "jo__java__lang__Integer.hpp"
#include "jo__java__lang__Long.hpp"
#include "jo__java__lang__Void.hpp"

#define MOD_PRIMITIVE 0x100000

// primitive classes
jmethod_table* jprimtype_boolean;
jmethod_table* jprimtype_char;
jmethod_table* jprimtype_float;
jmethod_table* jprimtype_double;
jmethod_table* jprimtype_byte;
jmethod_table* jprimtype_short;
jmethod_table* jprimtype_int;
jmethod_table* jprimtype_long;
jmethod_table* jprimtype_void;

jo__java__lang__Class* jprimclass_boolean;
jo__java__lang__Class* jprimclass_char;
jo__java__lang__Class* jprimclass_float;
jo__java__lang__Class* jprimclass_double;
jo__java__lang__Class* jprimclass_byte;
jo__java__lang__Class* jprimclass_short;
jo__java__lang__Class* jprimclass_int;
jo__java__lang__Class* jprimclass_long;
jo__java__lang__Class* jprimclass_void;

#define DEFINE_PRIMITIVE_CLASS(SHORTNAME, LONGNAME, TYPE, SIZE, SIG) \
	jprimclass_##SHORTNAME = FJ_NEWOBJECT(jo__java__lang__Class); \
	jo__java__lang__Class::jm___1o_init_1q_(jprimclass_##SHORTNAME, TYPE); \
	jo__java__lang__##LONGNAME##::jf__TYPE = jprimclass_boolean; \
	jprimtype_##SHORTNAME = (jmethod_table*)calloc(1, sizeof(jmethod_table)); \
	jprimtype_##SHORTNAME->objsize = SIZE; \
	jprimtype_##SHORTNAME->clazz = jprimclass_##SHORTNAME; \
	jprimtype_##SHORTNAME->u8name = SIG; \
	jprimtype_##SHORTNAME->modifiers = MOD_PRIMITIVE; \
	jprimclass_##SHORTNAME->jf__method__table = (jo__java__lang__Object*)jprimtype_##SHORTNAME; \
	//todo

void fj_init_primitive_classes()
{
	DEFINE_PRIMITIVE_CLASS(boolean, Boolean, 4, 1, "Z");
	DEFINE_PRIMITIVE_CLASS(char, Character, 5, 2, "C");
	DEFINE_PRIMITIVE_CLASS(float, Float, 6, 4, "F");
	DEFINE_PRIMITIVE_CLASS(double, Double, 7, 8, "D");
	DEFINE_PRIMITIVE_CLASS(byte, Byte, 8, 1, "B");
	DEFINE_PRIMITIVE_CLASS(short, Short, 9, 2, "S");
	DEFINE_PRIMITIVE_CLASS(int, Integer, 10, 4, "I");
	DEFINE_PRIMITIVE_CLASS(long, Long, 11, 8, "J");
	DEFINE_PRIMITIVE_CLASS(void, Void, 12, 0, "V");
}

