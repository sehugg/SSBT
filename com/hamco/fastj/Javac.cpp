
#include <stdio.h>
#include "fastj.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__Math.hpp"
#include "jo__java__lang__Float.hpp"
#include "jo__java__lang__Integer.hpp"
#include "jo__java__lang__System.hpp"
#include "jo__java__lang__Runtime.hpp"
#include "jo__java__lang__Character.hpp"
#include "jo__java__io__FileDescriptor.hpp"
#include "jo__java__lang__SecurityManager.hpp"
#include "jo__java__lang__RuntimeException.hpp"
#include "jo__java__io__PrintStream.hpp"
#include "jo__sun__tools__javac__Main.hpp"

extern void fj_init_VM();

extern char* fj_encode_utf(const jchar* src, char* dest, int len);

void printstring(jo__java__lang__String* s)
{
	char buf[s->jf__count*4];
	fj_encode_utf(&(*s->jf__value)[0], buf, s->jf__count);
	printf("%s\n", buf);
}

jarray<jo__java__lang__String*>* getjargs(int argc, char** argv)
{
	jarray<jo__java__lang__String*>* arr = (jarray<jo__java__lang__String*>*)
		FJ_ANEWARRAY(&jmtjo__java__lang__String, argc-1);
	for (int i=0; i<argc-1; i++)
	{
		arr->set(i, FJ_MAKESTRING(argv[i+1]));
	}
	return arr;
}

int main(int argc, char** argv)
{
	fj_init_VM();
	
	try {

		jo__sun__tools__javac__Main::jms__main(getjargs(argc, argv));
	
	} catch (jo__java__lang__Throwable* throwable) {
		printf("Caught exception %x\n", (int)throwable);
		printstring(throwable->jmtab->clazz->jf__name);
		printstring(FJ_CALLVIRTUAL(throwable, 4, jo__java__lang__String*(*)(jo__java__lang__Object*))(throwable));
	}
	
	printf("Done.\n");
	return 0;
}
