
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
#include "jo__com__hamco__fastj__test__C1.hpp"
#include "jo__com__hamco__fastj__test__C2.hpp"
#include "jo__com__hamco__fastj__test__Linpack.hpp"

extern void fj_init_VM();

extern char* fj_encode_utf(const jchar* src, char* dest, int len);

void printstring(jo__java__lang__String* s)
{
	char buf[s->jf__count*4];
	fj_encode_utf(&(*s->jf__value)[0], buf, s->jf__count);
	printf("%s\n", buf);
}

int main()
{
	fj_init_VM();
	
	printf("Obj size = %d\n", sizeof(jobject));

	try {
		FJ_ATHROW(FJ_NEWOBJECT(jo__java__lang__Exception));
	} catch (jo__java__lang__Exception* __jex) {
		printf("Caught exception\n");
	}
	
	try {

		printf("C1 : Test 1\n");
		jo__com__hamco__fastj__test__C1::jms__test1();
		printf("Test 2\n");
		jo__com__hamco__fastj__test__C1::jms__test2();
		printf("Test 3\n");
		jo__com__hamco__fastj__test__C1::jms__test3();
		printf("Test 4\n");
		jo__com__hamco__fastj__test__C1::jms__test4();
		printf("Test 5\n");
		jo__com__hamco__fastj__test__C1::jms__test5();
		printf("Test 6\n");
		jo__com__hamco__fastj__test__C1::jms__test6();
		printf("Test 7\n");
		jo__com__hamco__fastj__test__C1::jms__test7();
		printf("Test 8\n");
		jo__com__hamco__fastj__test__C1::jms__test8();
		printf("Test 9\n");
		jo__com__hamco__fastj__test__C1::jms__test9();
		printf("Test 10\n");
		jo__com__hamco__fastj__test__C1::jms__test10();
		
		printf("C2 : Test 1\n");
		jo__com__hamco__fastj__test__C2::jms__test1();
		
		printf("Linpack\n");
		jo__com__hamco__fastj__test__Linpack::jms__main(NULL);
		
#if 0	
	// install a security mananger
//	jo__java__lang__SecurityManager* sm = FJ_NEWOBJECT(jo__java__lang__SecurityManager);
//	jo__java__lang__System::jms__setSecurityManager(sm);
	
//	jinit__java__lang__System();
	printf("%d\n", jo__java__lang__Math::jms__abs(-20));
	printf("%5.2f\n", jo__java__lang__Math::jms__abs(-20.0f));
//	jo__java__lang__Integer::jms___1o_clinit_1q_();
//	jo__java__lang__Character::jms___1o_clinit_1q_();
//	jo__java__lang__Runtime::jms___1o_clinit_1q_();
//	jo__java__io__FileDescriptor::jms___1o_clinit_1q_();
//	jo__java__lang__System::jms___1o_clinit_1q_();
	jo__java__lang__String* str = jo__java__lang__Integer::jms__toString(1000);
//	jo__java__io__PrintStream::jm__println(jo__java__lang__System::jf__err, 69);
	printf("%d\n", jo__java__lang__String::jm__length(str));
	jint i = jo__java__lang__Integer::jms__parseInt(str);
	printf("%d\n", i);
#endif
	
	} catch (jo__java__lang__Throwable* throwable) {
		printf("Caught exception %x\n", (int)throwable);
		printstring(throwable->jmtab->clazz->jf__name);
	}
	
	printf("Done.\n");
	return 0;
}
