#!/bin/sh -f

for i
do
    echo Making backup of $i in $i.orig
    cp $i $i.orig
    
    echo Substituting in $i ...
    sed -e 's/DE[.]fub[.]inf[.]JVM[.]JavaClass/de.fub.bytecode.classfile/g' \
        -e 's/DE[.]fub[.]inf[.]JVM[.]ClassGen/de.fub.bytecode.generic/g' \
        -e 's/DE[.]fub[.]inf[.]JVM[.]Util/de.fub.bytecode.util/g' \
        -e 's/DE[.]fub[.]inf[.]JVM/de.fub.bytecode/g' \
        -e 's#DE/fub/inf/JVM/JavaClass#de/fub/bytecode/classfile#g' \
        -e 's#DE/fub/inf/JVM/ClassGen#de/fub/bytecode/generic#g' \
        -e 's#DE/fub/inf/JVM/Util#de/fub/bytecode/util#g' \
        -e 's#DE/fub/inf/JVM#de/fub/bytecode#g' \
        < $i > TMP

    mv TMP $i
done
