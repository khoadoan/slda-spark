#!/bin/sh
export HADOOP_CLASSPATH="/home/cloudera/shared/slda/slda-spark/code/lib/ST4-4.0.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/activation-1.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/ant-1.6.5.jar:/home/cloudera/shared/slda/slda-spark/code/lib/ant-1.8.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/ant-launcher-1.8.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/antlr-2.7.7.jar:/home/cloudera/shared/slda/slda-spark/code/lib/antlr-3.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/antlr-runtime-3.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/aopalliance-1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/apacheds-i18n-2.0.0-M15.jar:/home/cloudera/shared/slda/slda-spark/code/lib/apacheds-kerberos-codec-2.0.0-M15.jar:/home/cloudera/shared/slda/slda-spark/code/lib/api-asn1-api-1.0.0-M20.jar:/home/cloudera/shared/slda/slda-spark/code/lib/api-util-1.0.0-M20.jar:/home/cloudera/shared/slda/slda-spark/code/lib/asm-3.2.jar:/home/cloudera/shared/slda/slda-spark/code/lib/aspectjrt-1.6.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/avro-1.4.0-cassandra-1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/avro-1.7.5-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/aws-java-sdk-1.8.3-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/aws-java-sdk-1.8.3-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/aws-java-sdk-1.8.3.jar:/home/cloudera/shared/slda/slda-spark/code/lib/bliki-core-3.0.16.jar:/home/cloudera/shared/slda/slda-spark/code/lib/bson-2.5.jar:/home/cloudera/shared/slda/slda-spark/code/lib/cassandra-all-0.8.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/cassandra-thrift-0.8.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/cglib-2.2.1-v20090111.jar:/home/cloudera/shared/slda/slda-spark/code/lib/cloud9-1.4.17-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/cloud9-1.4.17-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/cloud9-1.4.17.jar:/home/cloudera/shared/slda/slda-spark/code/lib/collections-generic-4.01.jar:/home/cloudera/shared/slda/slda-spark/code/lib/colt-1.2.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-cli-1.2.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-cli-2.0-mahout.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-codec-1.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-collections-3.2.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-compress-1.4.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-configuration-1.8.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-daemon-1.0.13.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-dbcp-1.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-el-1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-httpclient-3.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-io-2.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-lang-2.6.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-logging-1.1.3.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-math-2.2.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-math3-3.1.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-net-3.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/commons-pool-1.5.6.jar:/home/cloudera/shared/slda/slda-spark/code/lib/concurrent-1.3.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/concurrentlinkedhashmap-lru-1.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/core-3.1.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/dropbox-core-sdk-1.7.7-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/dropbox-core-sdk-1.7.7-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/dropbox-core-sdk-1.7.7.jar:/home/cloudera/shared/slda/slda-spark/code/lib/dsiutils-2.0.15.jar:/home/cloudera/shared/slda/slda-spark/code/lib/fastutil-6.5.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/freemarker-2.3.9.jar:/home/cloudera/shared/slda/slda-spark/code/lib/gson-2.2.2.jar:/home/cloudera/shared/slda/slda-spark/code/lib/guava-13.0.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/guice-3.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/guice-servlet-3.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-annotations-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-auth-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-common-2.3.0-cdh5.1.0-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-common-2.3.0-cdh5.1.0-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-common-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-hdfs-2.3.0-cdh5.1.0-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-hdfs-2.3.0-cdh5.1.0-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-hdfs-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-mapreduce-client-common-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-mapreduce-client-core-2.3.0-cdh5.1.0-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-mapreduce-client-core-2.3.0-cdh5.1.0-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-mapreduce-client-core-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-mapreduce-client-jobclient-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-mapreduce-client-shuffle-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-streaming-2.3.0-cdh5.1.0-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-streaming-2.3.0-cdh5.1.0-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-streaming-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-api-2.3.0-cdh5.1.0-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-api-2.3.0-cdh5.1.0-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-api-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-client-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-common-2.3.0-cdh5.1.0-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-common-2.3.0-cdh5.1.0-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-common-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-server-common-2.3.0-cdh5.1.0-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-server-common-2.3.0-cdh5.1.0-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-server-common-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-server-nodemanager-2.3.0-cdh5.1.0-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-server-nodemanager-2.3.0-cdh5.1.0-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-server-nodemanager-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-server-resourcemanager-2.3.0-cdh5.1.0-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-server-resourcemanager-2.3.0-cdh5.1.0-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-server-resourcemanager-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hadoop-yarn-server-web-proxy-2.3.0-cdh5.1.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hamcrest-core-1.3.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hector-core-0.8.0-2.jar:/home/cloudera/shared/slda/slda-spark/code/lib/high-scale-lib-1.1.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/hsqldb-1.8.0.10.jar:/home/cloudera/shared/slda/slda-spark/code/lib/htmlparser-1.6.jar:/home/cloudera/shared/slda/slda-spark/code/lib/httpclient-4.2.5.jar:/home/cloudera/shared/slda/slda-spark/code/lib/httpcore-4.2.5.jar:/home/cloudera/shared/slda/slda-spark/code/lib/icu4j-4.8.1.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jackson-annotations-2.1.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jackson-core-2.3.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jackson-core-asl-1.8.8.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jackson-databind-2.1.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jackson-jaxrs-1.8.8.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jackson-mapper-asl-1.8.8.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jackson-xc-1.8.8.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jakarta-regexp-1.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jamm-0.2.2.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jasper-compiler-5.5.23.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jasper-runtime-5.5.23.jar:/home/cloudera/shared/slda/slda-spark/code/lib/java-xmlbuilder-0.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/javax.inject-1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jaxb-api-2.2.2.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jaxb-impl-2.2.3-1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jcommon-1.0.12.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jdiff-1.0.9.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jersey-client-1.9.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jersey-core-1.9.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jersey-guice-1.9.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jersey-json-1.9.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jersey-server-1.9.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jets3t-0.9.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jettison-1.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jetty-6.1.26.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jetty-util-6.1.26.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jfreechart-1.0.8a-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jfreechart-1.0.8a-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jfreechart-1.0.8a.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jline-0.9.94.jar:/home/cloudera/shared/slda/slda-spark/code/lib/joda-time-2.5.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jsap-2.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jsch-0.1.42.jar:/home/cloudera/shared/slda/slda-spark/code/lib/json-simple-1.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jsp-2.1-6.1.14.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jsp-api-2.1-6.1.14.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jsp-api-2.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jsr305-1.3.9.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jul-to-slf4j-1.6.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jung-algorithms-2.0.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jung-api-2.0.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/jung-graph-impl-2.0.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/junit-4.11-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/junit-4.11-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/junit-4.11.jar:/home/cloudera/shared/slda/slda-spark/code/lib/kfs-0.3.jar:/home/cloudera/shared/slda/slda-spark/code/lib/libthrift-0.9.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/log4j-1.2.17.jar:/home/cloudera/shared/slda/slda-spark/code/lib/logback-classic-1.0.9.jar:/home/cloudera/shared/slda/slda-spark/code/lib/logback-core-1.0.9.jar:/home/cloudera/shared/slda/slda-spark/code/lib/lucene-analyzers-3.6.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/lucene-benchmark-3.6.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/lucene-core-3.6.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/lucene-facet-3.6.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/lucene-highlighter-3.6.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/lucene-memory-3.6.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/lucene-queries-3.6.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/mahout-core-0.7-cdh4.5.0.3.jar:/home/cloudera/shared/slda/slda-spark/code/lib/mahout-integration-0.7-cdh4.5.0.3-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/mahout-integration-0.7-cdh4.5.0.3-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/mahout-integration-0.7-cdh4.5.0.3.jar:/home/cloudera/shared/slda/slda-spark/code/lib/mahout-math-0.7-cdh4.5.0.3-javadoc.jar:/home/cloudera/shared/slda/slda-spark/code/lib/mahout-math-0.7-cdh4.5.0.3-sources.jar:/home/cloudera/shared/slda/slda-spark/code/lib/mahout-math-0.7-cdh4.5.0.3.jar:/home/cloudera/shared/slda/slda-spark/code/lib/mail-1.4.jar:/home/cloudera/shared/slda/slda-spark/code/lib/mockito-all-1.8.5.jar:/home/cloudera/shared/slda/slda-spark/code/lib/mongo-java-driver-2.5.jar:/home/cloudera/shared/slda/slda-spark/code/lib/mrunit-0.8.0-incubating.jar:/home/cloudera/shared/slda/slda-spark/code/lib/netty-3.6.2.Final.jar:/home/cloudera/shared/slda/slda-spark/code/lib/oro-2.0.8.jar:/home/cloudera/shared/slda/slda-spark/code/lib/paranamer-2.3.jar:/home/cloudera/shared/slda/slda-spark/code/lib/pcj-1.2.jar:/home/cloudera/shared/slda/slda-spark/code/lib/pig-0.10.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/protobuf-java-2.5.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/servlet-api-2.5-6.1.14.jar:/home/cloudera/shared/slda/slda-spark/code/lib/servlet-api-2.5.jar:/home/cloudera/shared/slda/slda-spark/code/lib/slf4j-api-1.7.5.jar:/home/cloudera/shared/slda/slda-spark/code/lib/slf4j-log4j12-1.7.5.jar:/home/cloudera/shared/slda/slda-spark/code/lib/snakeyaml-1.6.jar:/home/cloudera/shared/slda/slda-spark/code/lib/snappy-java-1.0.4.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/solr-commons-csv-3.5.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/speed4j-0.9.jar:/home/cloudera/shared/slda/slda-spark/code/lib/spring-aop-3.0.7.RELEASE.jar:/home/cloudera/shared/slda/slda-spark/code/lib/spring-asm-3.0.7.RELEASE.jar:/home/cloudera/shared/slda/slda-spark/code/lib/spring-beans-3.0.7.RELEASE.jar:/home/cloudera/shared/slda/slda-spark/code/lib/spring-context-3.0.7.RELEASE.jar:/home/cloudera/shared/slda/slda-spark/code/lib/spring-core-3.0.7.RELEASE.jar:/home/cloudera/shared/slda/slda-spark/code/lib/spring-expression-3.0.7.RELEASE.jar:/home/cloudera/shared/slda/slda-spark/code/lib/spring-test-3.0.7.RELEASE.jar:/home/cloudera/shared/slda/slda-spark/code/lib/stax-api-1.0-2.jar:/home/cloudera/shared/slda/slda-spark/code/lib/stringtemplate-3.2.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/sux4j-3.0.8.jar:/home/cloudera/shared/slda/slda-spark/code/lib/uncommons-maths-1.2.2.jar:/home/cloudera/shared/slda/slda-spark/code/lib/uuid-3.2.0.jar:/home/cloudera/shared/slda/slda-spark/code/lib/xercesImpl-2.9.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/xml-apis-1.3.04.jar:/home/cloudera/shared/slda/slda-spark/code/lib/xmlenc-0.52.jar:/home/cloudera/shared/slda/slda-spark/code/lib/xpp3_min-1.1.4c.jar:/home/cloudera/shared/slda/slda-spark/code/lib/xstream-1.3.1.jar:/home/cloudera/shared/slda/slda-spark/code/lib/xz-1.0.jar:/home/cloudera/shared/slda/slda-spark/code/dist/slda-spark-0.0.1.jar:$HADOOP_CLASSPATH"
hadoop jar /home/cloudera/shared/slda/slda-spark/code/dist/slda-spark-0.0.1.jar $1 -libjars /home/cloudera/shared/slda/slda-spark/code/lib/cloud9-1.4.17-javadoc.jar,/home/cloudera/shared/slda/slda-spark/code/lib/cloud9-1.4.17-sources.jar,/home/cloudera/shared/slda/slda-spark/code/lib/cloud9-1.4.17.jar,/home/cloudera/shared/slda/slda-spark/code/lib/guava-13.0.1.jar,/home/cloudera/shared/slda/slda-spark/code/lib/dropbox-core-sdk-1.7.7-javadoc.jar,/home/cloudera/shared/slda/slda-spark/code/lib/dropbox-core-sdk-1.7.7-sources.jar,/home/cloudera/shared/slda/slda-spark/code/lib/dropbox-core-sdk-1.7.7.jar,/home/cloudera/shared/slda/slda-spark/code/lib/jackson-annotations-2.1.1.jar,/home/cloudera/shared/slda/slda-spark/code/lib/jackson-core-2.3.4.jar,/home/cloudera/shared/slda/slda-spark/code/lib/jackson-core-asl-1.8.8.jar,/home/cloudera/shared/slda/slda-spark/code/lib/jackson-databind-2.1.1.jar,/home/cloudera/shared/slda/slda-spark/code/lib/jackson-jaxrs-1.8.8.jar,/home/cloudera/shared/slda/slda-spark/code/lib/jackson-mapper-asl-1.8.8.jar,/home/cloudera/shared/slda/slda-spark/code/lib/jackson-xc-1.8.8.jar $2 $3 $4 $5 $6 $7 $8 $9 ${10} ${11} ${12} ${13} ${14} ${15} ${16} ${17} ${18} ${19} ${20}
