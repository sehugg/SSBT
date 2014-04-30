#ifndef _FASTJ_H
#define _FASTJ_H

#include <stdlib.h>
#include <stdio.h>
#include "jconsts.hpp"

// does nothing
#define FJ_NATIVE 

#define FJ_DEBUGMSG(x) fprintf(stderr, x);

typedef bool jboolean;
typedef signed char jbyte;
typedef signed short jshort;
typedef unsigned short jchar;
typedef signed int jint;
typedef float jfloat;
typedef double jdouble;
typedef long long jlong;

typedef unsigned int juint;
typedef unsigned long long julong;

class jo__java__lang__Object;
class jo__java__lang__Class;
class jo__java__lang__String;
class jclass;
class jmethod_table;

// this class represents an array or class instance
class jobject
{
public:
	jmethod_table *jmtab;
};

// the vtbl is a zero-terminated array of method pointers (just void* here)
typedef void (*jvfuncptr)(void);
typedef void (*jvdefaultctor)(jo__java__lang__Object*);

// interface entries are pairs of (interface-class vtbl)
// they are in sorted order; to invoke, a binary search is done on them
// also they are zero-terminated
struct jinterface_entry
{
	jmethod_table *interface;
	jvfuncptr* ivtbl; // array of jvfuncptr's
};

// prototype definition for class
struct jmethod_table
{
	int objsize;	// in bytes
	jo__java__lang__Class *clazz;	// might be null if not initialized
	int interface_count;
	jinterface_entry *interfaces;	// interface table
	jmethod_table* parent;	// superclass
	jmethod_table* elemtype; // if array, the element type
	char* u8name;	// name in utf8 format
	int modifiers; // bits
	jvdefaultctor default_ctor; // the default() constructor
	jvfuncptr vtbl[999];	// todo: this ain't right
};

// to allocate objects
extern jobject* fj_new_object(jmethod_table* jmtab);
extern jobject* fj_new_array(jmethod_table* jmtab);
extern void fj_free_object(jobject* obj);
extern void fj_throw_npe();	//null pointer exception
extern void fj_throw_aioob(); //array index out of bounds
extern void fj_throw_abstracterror();
extern void fj_throw_cce(jobject* obj);
extern jo__java__lang__String* fj_new_string(const char* chars);
extern char* fj_jstring_to_utf(jo__java__lang__String* str);
extern int fj_count_utf_length(const char* chars);
extern void fj_decode_utf(const char* src, jchar* dest);
extern char* fj_encode_utf(const jchar* src, char* dest, int len);
extern jboolean fj_is_assignable(jmethod_table* s, jmethod_table* t);
extern jvfuncptr fj_lookup_interface_method(jobject* obj, jmethod_table* itable, int vindex);

///////////////////
#include "jo__java__lang__Object.hpp"
///////////////////

// objects, arrays, and primitives have classes
// but primitives do not have method tables

extern jmethod_table* jarray_boolean_type;
extern jmethod_table* jarray_byte_type;
extern jmethod_table* jarray_char_type;
extern jmethod_table* jarray_short_type;
extern jmethod_table* jarray_int_type;
extern jmethod_table* jarray_long_type;
extern jmethod_table* jarray_float_type;
extern jmethod_table* jarray_double_type;

jmethod_table* fj_getarrayclass(jmethod_table* objtype);

// arrays are derived from jobject
// so that means that every array needs a corresponding class
class __jarray : public jo__java__lang__Object
{
protected:
	jint len;
	void *data;
public:
	__jarray(jmethod_table* jmt) { jmtab=jmt; data=NULL; }
	__jarray(jmethod_table* jmt, int alen, void* adata) { jmtab=jmt; len=alen; data=adata; }
	~__jarray() { ::free(data); }
	int length() const { return len; }
	void __allocate(jint alen, jint asize);
	void __free();
	void* __data() { return data; }
};

// pointer wrapper for a __jarray type
template <class T>
class jarray : public __jarray
{
public:
	jarray<T>(jmethod_table* jmt) : __jarray(jmt) { }
	jarray<T>* allocate(jint alen) {
		__allocate(alen, sizeof(T));
		return this;
	}
	void checkIndex(jint i) {
		if (i<0 || i>=len)
			fj_throw_aioob();
	}
	void checkRange(jint offset, jint count) {
		if (offset<0 || offset+count>len)
			fj_throw_aioob();
	}
	T& operator[](jint i) const { 
		return ((T*)data)[i]; 
	}
	T get(jint i) { 
		checkIndex(i);
		return (*this)[i]; 
	}
	void set(jint i, const T& v) {
		checkIndex(i); 
		(*this)[i] = v; 
	}
	int size() const { 
		return len*sizeof(T); 
	}
};

///////////////////
#include "jo__java__lang__Class.hpp"
///////////////////

// primitive classes
extern jmethod_table* jprimtype_boolean;
extern jmethod_table* jprimtype_byte;
extern jmethod_table* jprimtype_char;
extern jmethod_table* jprimtype_short;
extern jmethod_table* jprimtype_int;
extern jmethod_table* jprimtype_long;
extern jmethod_table* jprimtype_float;
extern jmethod_table* jprimtype_double;
extern jmethod_table* jprimtype_void;

// macros & stuff

#define FJ_MAKELONG(a,b) (((jlong)a)+(((jlong)b)<<32))

// virtual method call
#define FJ_CALLVIRTUAL(OBJ,VINDEX,FUNCTYPE) ((FUNCTYPE)(OBJ->jmtab->vtbl[VINDEX]))
// todo: interfaces
#define FJ_CALLINTERFACE(OBJ,VINDEX,FUNCTYPE,JMTAB) ((FUNCTYPE)fj_lookup_interface_method(OBJ, JMTAB, VINDEX))
// new object (non-array)
#define FJ_NEWOBJECT(CLASS) ((CLASS*)fj_new_object(&jmt##CLASS))
// new array
#define FJ_NEWARRAY(TYPE,SIZE) ((jarray<j##TYPE>*)fj_new_object(jarray_##TYPE##_type))->allocate(SIZE)
#define FJ_ANEWARRAY(VTBL,SIZE) ((jarray<jobject*>*)fj_new_array(VTBL))->allocate(SIZE)

// create a new multi array
// pass the dimensions in varargs
jarray<jobject*>* fj_new_amultiarray(jmethod_table* reftype, jint ndims, ...);

// string constant
#define FJ_MAKESTRING(CHARS) (fj_new_string(CHARS))

// todo: check these
#define FJ_I2L(x) ((jlong)x)
#define FJ_I2F(x) ((jfloat)x)
#define FJ_I2D(x) ((jdouble)x)
#define FJ_L2I(x) ((jint)x)
#define FJ_L2F(x) ((jfloat)x)
#define FJ_L2D(x) ((jdouble)x)
#define FJ_F2I(x) ((jint)x)
#define FJ_F2L(x) ((jlong)x)
#define FJ_F2D(x) ((jdouble)x)
#define FJ_D2I(x) ((jint)x)
#define FJ_D2L(x) ((jlong)x)
#define FJ_D2F(x) ((jfloat)x)
#define FJ_I2B(x) ((jbyte)x)
#define FJ_I2C(x) ((jchar)x)
#define FJ_I2S(x) ((jshort)x)

#define SIGN(x) (((x)==0)?0:((x)>0)?1:-1)
// todo: check these
#define FJ_LCMP(x,y) SIGN(x-y)
#define FJ_FCMPL(x,y) fj_soft_fcmpl(x,y)
#define FJ_FCMPG(x,y) fj_soft_fcmpg(x,y)
#define FJ_DCMPL(x,y) fj_soft_dcmpl(x,y)
#define FJ_DCMPG(x,y) fj_soft_dcmpg(x,y)

#define IUSHR(x,y) (((juint)x) >> (y))
#define LUSHR(x,y) (((julong)x) >> (y))
#define FREM(x,y) (fj_soft_frem(x,y))
#define DREM(x,y) (fj_soft_freml(x,y))

#define FJ_ATHROW(E) throw (E);

// throw a specific class of exception constructed with ()
#define FJ_THROWNEW(ECLASS) do { \
	ECLASS* _ex = FJ_NEWOBJECT(ECLASS); \
	ECLASS::jm___1o_init_1q_(_ex); \
	FJ_ATHROW(_ex); \
} while (0)

// throw a specific class of exception constructed with (String)
#define FJ_THROWNEWMSG(ECLASS, MSG) do { \
	ECLASS* _ex = FJ_NEWOBJECT(ECLASS); \
	ECLASS::jm___1o_init_1q_(_ex, MSG); \
	FJ_ATHROW(_ex); \
} while (0)

inline jboolean FJ_INSTANCEOF(jobject* obj, jmethod_table* vtbl)
{
	if (obj == NULL)
		return false;
	if (obj->jmtab == vtbl)
		return true;
	return fj_is_assignable(obj->jmtab, vtbl);
}

#define FJ_CHECKCAST(OBJ,VTBL) if (OBJ != NULL && !FJ_INSTANCEOF(OBJ, VTBL)) { fj_throw_cce(OBJ); }

// these are currently not implemented
#define FJ_MONITORENTER(o)
#define FJ_MONITOREXIT(o)

#define Infinity (1e300*1e300)
#define NaN (0.0/0.0)

#define FJ_FATAL_ERROR(msg) // todo

// exceptions

///////////////////
#include "jo__java__lang__Throwable.hpp"
///////////////////

#define FJ_TRY_BEGIN try {
//#define FJ_TRY_END
//#define FJ_TRY_CATCH ; }
#define FJ_CATCH(CLASS,TARGET) ; } catch (CLASS* _jex_##TARGET) { js__0_ref=_jex_##TARGET; goto TARGET; }

#define FJ_CHECKNPE(EXPR) if ((EXPR)==NULL) { fj_throw_npe(); }

// class init methods

typedef jo__java__lang__Class* (jclinit_func)();

// this has a return value so we can call it in an assignment operation
// yeah i know it's lame
extern jo__java__lang__Class* fj_register_clinit_func(char* utf8name, jclinit_func* initfunc);
extern jclinit_func* fj_get_clinit_func(char* utf8name);

// math operations

jint fj_soft_dcmpg(jdouble v1, jdouble v2);
jint fj_soft_dcmpl(jdouble v1, jdouble v2);
jint fj_soft_fcmpg(jfloat v1, jfloat v2);
jint fj_soft_fcmpl(jfloat v1, jfloat v2);
jlong fj_soft_lmul(jlong v1, jlong v2);
jlong fj_soft_ldiv(jlong v1, jlong v2);
jlong fj_soft_lrem(jlong v1, jlong v2);
jfloat fj_soft_fdiv(jfloat v1, jfloat v2);
jdouble fj_soft_fdivl(jdouble v1, jdouble v2);
jfloat fj_soft_frem(jfloat v1, jfloat v2);
jdouble fj_soft_freml(jdouble v1, jdouble v2);
jlong fj_soft_lshll(jlong v1, jint v2);
jlong fj_soft_ashrl(jlong v1, jint v2);
jlong fj_soft_lshrl(jlong v1, jint v2);
jint fj_soft_lcmp(jlong v1, jlong v2);
jint fj_soft_mul(jint v1, jint v2);
jint fj_soft_div(jint v1, jint v2);
jint fj_soft_rem(jint v1, jint v2);
jfloat fj_soft_cvtlf(jlong v);
jdouble fj_soft_cvtld(jlong v);
jlong fj_soft_cvtfl(jfloat v);
jlong fj_soft_cvtdl(jdouble v);
jint fj_soft_cvtfi(jfloat v);
jint fj_soft_cvtdi(jdouble v);

// stuff for local allocation

#include "fjallocator.hpp"

#define FJ_NEWOBJECTLOCAL(CLASS) ((CLASS*)jlalloc.allocate(&jmt##CLASS))
#define FJ_NEWARRAYLOCAL(TYPE,LEN) ((jarray<j##TYPE>*)jlalloc.allocarray(jarray_##TYPE##_type, LEN, sizeof(j##TYPE)))
#define FJ_ANEWARRAYLOCAL(TYPE,LEN) ((jarray<jobject*>*)jlalloc.allocarray(TYPE, LEN, sizeof(jobject*)))

////////////////////
#include "jo__java__lang__String.hpp"
////////////////////

#endif /* _FASTJ_H */
