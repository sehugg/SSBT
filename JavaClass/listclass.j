;; Produced by JasminVisitor (JavaClass package)
;; http://www.inf.fu-berlin.de/~dahm/JavaClass/
;; Fri Jul 09 17:56:03 CEST 1999

.source listclass.java
.class public listclass
.super java/lang/Object


.method public static main([Ljava/lang/String;)V
.limit stack 5
.limit locals 9
.var 0 is arg0 [Ljava/lang/String; from Label19 to Label19

.line 14
	aload_0
	arraylength
	anewarray java/lang/String
	astore_1
.line 15
	iconst_0
	istore_2
.line 16
	iconst_0
	istore_3
	iconst_0
	istore 4
	iconst_1
	istore 5
.line 17
	aconst_null
	astore 6
.line 21
	iconst_0
	istore 7
	goto Label0
Label9:
.line 22
	aload_0
	iload 7
	aaload
	iconst_0
	invokevirtual java/lang/String/charAt(I)C
	bipush 45
	if_icmpne Label1
.line 23
	aload_0
	iload 7
	aaload
	ldc "-constants"
	invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
	ifeq Label2
.line 24
	iconst_1
	istore 4
	goto Label3
Label2:
.line 25
	aload_0
	iload 7
	aaload
	ldc "-code"
	invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
	ifeq Label4
.line 26
	iconst_1
	istore_3
	goto Label3
Label4:
.line 27
	aload_0
	iload 7
	aaload
	ldc "-brief"
	invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
	ifeq Label6
.line 28
	iconst_0
	istore 5
	goto Label3
Label6:
.line 30
	getstatic java.lang.System.err Ljava/io/PrintStream;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc "Unknown switch "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_0
	iload 7
	aaload
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc " ignored."
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
Label3:
	goto Label8
Label1:
.line 33
	aload_1
	iload_2
	iinc 2 1
	aload_0
	iload 7
	aaload
	aastore
Label8:
.line 21
	iinc 7 1
Label0:
	iload 7
	aload_0
	arraylength
	if_icmplt Label9
Label21:
.line 37
.line 38
	iload_2
	ifne Label10
.line 39
	getstatic java.lang.System.err Ljava/io/PrintStream;
	ldc "list: No input files specified"
	invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
	goto Label11
Label10:
.line 41
	iconst_0
	istore 7
	goto Label12
Label16:
.line 42
.line 44
	aload_1
	iload 7
	aaload
	astore 6
.line 46
	aload 6
	invokestatic de/fub/bytecode/Repository/lookupClass(Ljava/lang/String;)Lde/fub/bytecode/classfile/JavaClass;
	dup
	astore 8
	ifnonnull Label13
.line 47
	new de/fub/bytecode/classfile/ClassParser
	dup
	aload 6
	invokespecial de/fub/bytecode/classfile/ClassParser/<init>(Ljava/lang/String;)V
	invokevirtual de/fub/bytecode/classfile/ClassParser/parse()Lde/fub/bytecode/classfile/JavaClass;
	astore 8
Label13:
.line 49
	getstatic java.lang.System.out Ljava/io/PrintStream;
	aload 8
	invokevirtual java/io/PrintStream/println(Ljava/lang/Object;)V
.line 51
	iload 4
	ifeq Label14
.line 52
	getstatic java.lang.System.out Ljava/io/PrintStream;
	aload 8
	invokevirtual de/fub/bytecode/classfile/JavaClass/getConstantPool()Lde/fub/bytecode/classfile/ConstantPool;
	invokevirtual java/io/PrintStream/println(Ljava/lang/Object;)V
Label14:
.line 54
	iload_3
	ifeq Label15
.line 55
	aload 8
	invokevirtual de/fub/bytecode/classfile/JavaClass/getMethods()[Lde/fub/bytecode/classfile/Method;
	iload 5
	invokestatic listclass/printCode([Lde/fub/bytecode/classfile/Method;Z)V
Label15:
.line 41
	iinc 7 1
Label12:
	iload 7
	iload_2
	if_icmplt Label16
Label11:
	goto Label17
Label22:
	nop
Label23:
	astore 7
.line 59
	getstatic java.lang.System.err Ljava/io/PrintStream;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc "Couldn't find class "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 6
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
	goto Label17
Label17:
	nop
Label19:
.line 61
	return

.catch java/lang/Exception from Label21 to Label22 using Label23
.end method

.method public static printCode([Lde/fub/bytecode/classfile/Method;Z)V
.limit stack 3
.limit locals 4
.var 0 is arg0 [Lde/fub/bytecode/classfile/Method; from Label3 to Label3
.var 1 is arg1 Z from Label3 to Label3

.line 66
	iconst_0
	istore_2
	goto Label0
Label2:
.line 67
	getstatic java.lang.System.out Ljava/io/PrintStream;
	aload_0
	iload_2
	aaload
	invokevirtual java/io/PrintStream/println(Ljava/lang/Object;)V
.line 69
	aload_0
	iload_2
	aaload
	invokevirtual de/fub/bytecode/classfile/Method/getCode()Lde/fub/bytecode/classfile/Code;
	astore_3
.line 70
	aload_3
	ifnull Label1
.line 71
	getstatic java.lang.System.out Ljava/io/PrintStream;
	aload_3
	iload_1
	invokevirtual de/fub/bytecode/classfile/Code/toString(Z)Ljava/lang/String;
	invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
Label1:
.line 66
	iinc 2 1
Label0:
	iload_2
	aload_0
	arraylength
	if_icmplt Label2
Label3:
.line 73
	return

.end method

.method public <init>()V
.limit stack 1
.limit locals 1
.var 0 is this Llistclass; from Label0 to Label0

.line 12
	aload_0
	invokespecial java/lang/Object/<init>()V
Label0:
.line 12
	return

.end method
