
#include "fastj.hpp"
#include "glib.h"

/*
 * allows classes to register their class init. functions
 * in a static initalizer
 */

static GHashTable* fj_clinit_registry = NULL;

jo__java__lang__Class* fj_register_clinit_func(char* utf8name, jclinit_func* initfunc)
{
	if (fj_clinit_registry == NULL)
	{
		fj_clinit_registry = g_hash_table_new(g_str_hash, g_str_equal);
	}
	g_hash_table_insert(fj_clinit_registry, utf8name, initfunc);
	return NULL;
}

jclinit_func* fj_get_clinit_func(char* utf8name)
{
	if (fj_clinit_registry == NULL)
		return NULL;
	jclinit_func* initfunc = (jclinit_func*)g_hash_table_lookup(fj_clinit_registry, utf8name);
	return (initfunc);
}

