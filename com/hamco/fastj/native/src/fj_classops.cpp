
#include "fastj.hpp"

#define MOD_PRIMITIVE 0x100000

// see instanceof in JVM ref

jboolean fj_is_subclass(jmethod_table* s, jmethod_table* t)
{
	s = s->parent;
	while (s != NULL)
	{
		if (s == t)
			return true;
		s = s->parent;
	}
	return false;
}

// s is an array types -- t might not be
jboolean fj_is_compatible_array(jmethod_table* s, jmethod_table* t)
{
	// if t is Object, true
	if (t == &jmtjo__java__lang__Object)
		return true;
	// otherwise t must be an array type
	if (!t->elemtype)
		return false;
	// now make sure elements are same
	return fj_is_assignable(s->elemtype, t->elemtype);
}

// t is an interface type
jboolean fj_implements(jmethod_table* s, jmethod_table* t)
{
	if (!s->interfaces)
		return false;
	jinterface_entry* ientry = s->interfaces;
	while (ientry->interface != NULL)
	{
		if (ientry->interface == t)
			return true;
		ientry++;
	}
	return false;
}

jboolean fj_is_assignable(jmethod_table* s, jmethod_table* t)
{
	if (s == t)
		return true;
	// only cases left are array, or object
	// because there is only one of each primitive type
	if (s->elemtype)
	{
		return fj_is_compatible_array(s, t);
	} else {
		if (fj_is_subclass(s,t))
			return true;
		if (fj_implements(s,t))
			return true;
	}
}

jvfuncptr fj_lookup_interface_method(jobject* obj, jmethod_table* itable, int vindex)
{
	jinterface_entry* ientry = obj->jmtab->interfaces;
	if (ientry == NULL)
		fj_throw_abstracterror();
	while (ientry->interface != NULL)
	{
		if (ientry->interface == itable)
			return ientry->ivtbl[vindex];
		ientry++;
	}
	fj_throw_abstracterror();
	return NULL;
}

jboolean fj_is_reference_type(jmethod_table* jmtab)
{
	// todo: fix this, classes dont set their fields correctly
	return (jmtab->modifiers & MOD_PRIMITIVE) == 0;
}
