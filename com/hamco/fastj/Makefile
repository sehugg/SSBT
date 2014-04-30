
include Makefile.in

runtests: tests
	java com.hamco.fastj.test.C1
	./tests

TESTCLASSES=com.hamco.fastj.test.C1 com.hamco.fastj.test.C2 com.hamco.fastj.test.Linpack
CORECLASSES=@fastjlib.lst

coreclasses: .PHONY
	./mk -R ${CORECLASSES}

mauveclasses: .PHONY
	./mk -R @/home/hugg/mauve/classes gnu.testlet.SimpleTestHarness

tests: .PHONY
	rm -f src/jo__com__hamco__fastj__test__C*.cpp include/jo__com__hamco__fastj__test__C*.hpp
	./mk ${TESTCLASSES}
	make Tests.o
	rm -f tests
	cd src ; make
	${CXX} -o tests src/*.o native/src/*.o Tests.o ${LIBS}

mauve: .PHONY Mauve.o
	${CXX} -o mauve src/jo__java__*.o src/jo__gnu__*.o  native/src/*.o Mauve.o ${LIBS}

fhourstones: .PHONY Fhourstones.o
	${CXX} -o fhourstones src/jo__com__*.o src/jo__java__*.o src/jo__gnu__java__*.o native/src/*.o Fhourstones.o ${LIBS}

javac: .PHONY Javac.o
	${CXX} -o javac src/jo__sun__*.o src/jo__java__*.o src/jo__gnu__java__*.o native/src/*.o Javac.o ${LIBS}

native: .PHONY
	cd native/src ; make

code: .PHONY
	cd src ; make

fastjlib.a: .PHONY
	ar r fastjlib.a src/jo__gnu__java__*.o src/jo__java__*.o

clean: .PHONY
	rm -f include/*.hpp
	rm -f src/*.cpp
	rm -f include/*.tmp 
	rm -f src/*.tmp 
	rm -f *.o objs/*.o src/*.o

headers: .PHONY
	./mk -h -R java.lang.Object

sources: .PHONY
	./mk -R java.lang.Object

#depend: .PHONY
#	${CXX} -M ${CFLAGS} src/*.cpp
