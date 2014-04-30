
#include "fastj.hpp"
#include "glib.h"
#include <stdlib.h>
#include <stdarg.h>

#define JARRAY_MTBL jmtjo__java__lang__Object

static jmethod_table* fj_newarrayclass(jmethod_table* objtype);
static void fj_initarrayclass(jmethod_table* mtbl, jmethod_table* objtype);

#define DEFINE_ARRAY_CLASS(TYPE) \
	fj_initarrayclass(jarray_##TYPE##_type, jprimtype_##TYPE);

#define DECLARE_ARRAY_CLASS(TYPE) \
	static jmethod_table jarray_##TYPE##_type_static = JARRAY_MTBL; \
	jmethod_table* jarray_##TYPE##_type = &jarray_##TYPE##_type_static;

// primitive array types	
DECLARE_ARRAY_CLASS(boolean)
DECLARE_ARRAY_CLASS(char)
DECLARE_ARRAY_CLASS(float)
DECLARE_ARRAY_CLASS(double)
DECLARE_ARRAY_CLASS(byte)
DECLARE_ARRAY_CLASS(short)
DECLARE_ARRAY_CLASS(int)
DECLARE_ARRAY_CLASS(long)


// a hash table of (element_mtbl, array_mtbl)
GHashTable* fj_array_hash;

void fj_init_array_classes()
{
	fj_array_hash = g_hash_table_new(NULL, NULL);
	DEFINE_ARRAY_CLASS(boolean)
	DEFINE_ARRAY_CLASS(char)
	DEFINE_ARRAY_CLASS(float)
	DEFINE_ARRAY_CLASS(double)
	DEFINE_ARRAY_CLASS(byte)
	DEFINE_ARRAY_CLASS(short)
	DEFINE_ARRAY_CLASS(int)
	DEFINE_ARRAY_CLASS(long)
}

// creates a new array class from an existing element type
jmethod_table* fj_getarrayclass(jmethod_table* elemtype)
{
	jmethod_table* mtbl = (jmethod_table*)g_hash_table_lookup(fj_array_hash, elemtype);
	if (mtbl == NULL)
	{
		mtbl = fj_newarrayclass(elemtype);
	}
	return mtbl;
}

static void fj_initarrayclass(jmethod_table* mtbl, jmethod_table* elemtype)
{
	// create new array class
	mtbl->clazz = FJ_NEWOBJECT(jo__java__lang__Class);
	jo__java__lang__Class::jm___1o_init_1q_(mtbl->clazz, elemtype->clazz);
	mtbl->objsize = sizeof(__jarray);
	mtbl->elemtype = elemtype;
	// todo: set name of object
	// insert it into the hash
	g_hash_table_insert(fj_array_hash, (gpointer)elemtype, (gpointer)mtbl);
}

// used to create primitive array types
static jmethod_table* fj_newarrayclass(jmethod_table* elemtype)
{
	jmethod_table* mtbl;
	// create new method table for this array type
	mtbl = (jmethod_table*)malloc(sizeof(JARRAY_MTBL));
	// initialize it
	memcpy(mtbl, &JARRAY_MTBL, sizeof(JARRAY_MTBL));
	fj_initarrayclass(mtbl, elemtype);
	return mtbl;
}

jarray<jobject*>* fj_new_amultiarray2(jmethod_table* reftype, jint n, jint* dims)
{
	if (n==0) {
		return FJ_ANEWARRAY(reftype, dims[0]);
	} else {
		int l = dims[n];
		jarray<jobject*>* arr = FJ_ANEWARRAY(reftype, l);
		for (int i=0; i<l; i++)
		{
			arr->set(i, fj_new_amultiarray2(reftype->elemtype, n-1, dims));
		}
		return arr;
	}
}

jarray<jobject*>* fj_new_amultiarray(jmethod_table* reftype, jint ndims, ...)
{
	va_list ap;
	jint dims[ndims];
	
	// build the array of dimensions for the multi-array
	// and create the array type in 'type'
	va_start(ap, ndims);
	for (int i=0; i<ndims; i++)
	{
		dims[i] = va_arg(ap, jint);
	}
	va_end(ap);
	
	// wonderful recursion saves the day
	return fj_new_amultiarray2(reftype, ndims-1, dims);
}
