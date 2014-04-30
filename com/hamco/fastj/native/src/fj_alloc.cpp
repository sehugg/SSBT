
#include <stdlib.h>
#include <strings.h>
#include "fastj.hpp"

#ifdef BOEHM_GC
#include "gc.h"
#endif

jobject* fj_new_object(jmethod_table* jmtab)
{
	jobject* jo = (jobject*)malloc(jmtab->objsize);
	memset(jo, 0, jmtab->objsize);
	jo->jmtab = jmtab;
	return jo;
}

jobject* fj_new_array(jmethod_table* jmtab)
{	
	return fj_new_object(jmtab);
}

void fj_free_object(jobject* obj)
{
	// if it's an array, free the array data
	if (obj->jmtab->elemtype)
	{
		((__jarray*)obj)->__free();
	}
	free(obj);
}

void __jarray::__allocate(jint alen, jint asize)
{
	len = alen;
	int nbytes = alen*asize;
	data = malloc(nbytes);
	memset(data, 0, nbytes);
}

void __jarray::__free()
{
	if (data != NULL)
		free(data);
}

void* jobjallocator::allocate(int nbytes)
{
	void* ptr = calloc(1, nbytes);
	size++;
	if (size > limit)
	{
		limit += LIMIT_INCREMENT;
		recs = (void**)realloc(recs, sizeof(void*)*limit);
	}
	recs[size-1] = ptr;
	return ptr;
}

void* jobjallocator::allocate(jmethod_table* jmtab)
{
	jobject* obj = (jobject*)allocate(jmtab->objsize);
	obj->jmtab = jmtab;
	return obj;
}

jobjallocator::~jobjallocator()
{
	for (int i=0; i<size; i++)
		free(recs[i]);
	free(recs);
}
