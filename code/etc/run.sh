#!/bin/sh
export HADOOP_CLASSPATH="/Users/Khoa/git/slda/slda-spark/hadoop/lib/ant-1.6.5.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/bliki-core-3.0.16.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/cloud9-1.4.13.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/collections-generic-4.01.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/colt-1.2.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-beanutils-1.7.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-beanutils-core-1.8.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-cli-1.2.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-codec-1.4.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-collections-3.2.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-compress-1.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-configuration-1.6.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-digester-1.8.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-el-1.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-httpclient-3.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-lang-2.6.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-logging-1.1.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-math-2.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-net-1.4.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/concurrent-1.3.4.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/core-3.1.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/dropbox-core-sdk-1.7.7-javadoc.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/dropbox-core-sdk-1.7.7-sources.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/dropbox-core-sdk-1.7.7.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/gson-2.2.2.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/guava-13.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/hadoop-core-1.0.3.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/hamcrest-core-1.3.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/hipi-0.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/hsqldb-1.8.0.10.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/htmlparser-1.6.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jackson-core-2.3.4.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jackson-core-asl-1.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jackson-mapper-asl-1.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jasper-compiler-5.5.12.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jasper-runtime-5.5.12.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jets3t-0.7.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jetty-6.1.26.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jetty-util-6.1.26.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jsp-2.1-6.1.14.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jsp-api-2.1-6.1.14.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jung-algorithms-2.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jung-api-2.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jung-graph-impl-2.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/junit-4.11.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jwnl-1.3.3.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/kfs-0.3.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/log4j-1.2.16.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/maxent-3.0.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/mockito-all-1.8.5.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/mrunit-0.8.0-incubating.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/oro-2.0.8.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/pcj-1.2.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/pig-0.10.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/servlet-api-2.2.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/servlet-api-2.5-20081211.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/servlet-api-2.5-6.1.14.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/tools-1.5.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/xmlenc-0.52.jar:/Users/Khoa/git/slda/slda-spark/hadoop/dist/mapreduce-assignment-0.0.1.jar:$HADOOP_CLASSPATH"
java -Xmx4g -classpath "/Users/Khoa/git/slda/slda-spark/hadoop/lib/ant-1.6.5.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/bliki-core-3.0.16.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/cloud9-1.4.13.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/collections-generic-4.01.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/colt-1.2.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-beanutils-1.7.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-beanutils-core-1.8.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-cli-1.2.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-codec-1.4.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-collections-3.2.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-compress-1.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-configuration-1.6.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-digester-1.8.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-el-1.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-httpclient-3.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-lang-2.6.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-logging-1.1.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-math-2.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/commons-net-1.4.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/concurrent-1.3.4.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/core-3.1.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/dropbox-core-sdk-1.7.7-javadoc.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/dropbox-core-sdk-1.7.7-sources.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/dropbox-core-sdk-1.7.7.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/gson-2.2.2.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/guava-13.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/hadoop-core-1.0.3.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/hamcrest-core-1.3.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/hipi-0.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/hsqldb-1.8.0.10.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/htmlparser-1.6.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jackson-core-2.3.4.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jackson-core-asl-1.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jackson-mapper-asl-1.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jasper-compiler-5.5.12.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jasper-runtime-5.5.12.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jets3t-0.7.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jetty-6.1.26.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jetty-util-6.1.26.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jsp-2.1-6.1.14.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jsp-api-2.1-6.1.14.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jung-algorithms-2.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jung-api-2.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jung-graph-impl-2.0.1.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/junit-4.11.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/jwnl-1.3.3.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/kfs-0.3.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/log4j-1.2.16.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/maxent-3.0.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/mockito-all-1.8.5.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/mrunit-0.8.0-incubating.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/oro-2.0.8.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/pcj-1.2.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/pig-0.10.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/servlet-api-2.2.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/servlet-api-2.5-20081211.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/servlet-api-2.5-6.1.14.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/tools-1.5.0.jar:/Users/Khoa/git/slda/slda-spark/hadoop/lib/xmlenc-0.52.jar:/Users/Khoa/git/slda/slda-spark/hadoop/dist/mapreduce-assignment-0.0.1.jar" $1 $2 $3 $4 $5 $6 $7 $8 $9 ${10} ${11} ${12} ${13} ${14} ${15} ${16} ${17} ${18} ${19} ${20}