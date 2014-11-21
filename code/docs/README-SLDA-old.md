SLDA
================

**KHOA: The current description is for text data. This will change later; for now, this is for testing the runnability of SLDA**

Install and Build
----------
To download all the dependency packages, please run the following command (assuming you are on the directory of the project)
    ant

Tokenizing and Indexing
----------
To tokenize, parse and index the raw text file, please run either the following command

	etc/hadoop-local.sh cc.slda.ParseCorpus -input /path_to_corpus.txt -output /path_to_output
	etc/hadoop-cluster.sh cc.slda.ParseCorpus -input /path_to_corpus.txt -output /path_to_output -mapper 10 -reducer 5
	*Note* Remember to set the number of mappers and reducers for small cluster(defaults are 100).

To print the help information and usage hints, please run the following command

    etc/hadoop-cluster cc.slda.ParseCorpus -help

By the end of execution, you will end up with three files/dirtories in the specified output, for example,

    hadoop fs -ls /hadoop/index/document/output/directory/
    Found 3 items
    drwxr-xr-x   - user supergroup          0 2012-01-12 12:18 /hadoop/index/document/output/directory/document
    -rw-r--r--   3 user supergroup        282 2012-01-12 12:18 /hadoop/index/document/output/directory/term
    -rw-r--r--   3 user supergroup        189 2012-01-12 12:18 /hadoop/index/document/output/directory/title

File `/hadoop/index/document/output/directory/term` stores the mapping between a unique token and its unique integer ID. Similarly, `/hadoop/index/document/output/directory/title` stores the mapping between a document title to its unique integer ID. Both of these two files are in sequence file format, key-ed by `IntWritable.java` and value-d by `Text.java`. You may use the following command to browse a sequence file in general

Cluster Mode

     hadoop jar bin/slda-0.0.1.jar edu.umd.cloud9.io.ReadSequenceFile /hadoop/index/document/output/directory/term 20

Local Mode

     hadoop jar bin/slda-0.0.1.jar edu.umd.cloud9.io.ReadSequenceFile /hadoop/index/document/output/directory/term 20 local

and option '20' specifies the first 20 records to be displayed.

Input Data Format
----------

The data format for Mr. LDA package is defined in class `Document.java` of every package. It consists an `HMapII.java` object, storing all word:count pairs in a document using an integer:integer hash map. **Take note that the word index starts from 1, whereas index 0 is reserved for system message.** Interesting user could refer following piece of code to convert an *indexed* document `String.java` to `Document.java`:

```java
String inputDocument = "Mr. LDA is a Latent Dirichlet Allocation topic modeling package based on Variational Bayesian learning approach using MapReduce and Hadoop";
Document outputDocument = new Document();
HMapII content = new HMapII();
StringTokenizer stk = new StringTokenizer(inputDocument);
while (stk.hasNext()) {
      content.increment(Integer.parseInt(stk.hasNext), 1);
}
outputDocument.setDocument(content);
```

By defalut, Mr. LDA accepts sequential file format only. The sequence file should be key-ed by a unique document ID of `IntWritable.java` type and value-d by the corresponding `Document.java` data type.

If you preprocessing the raw text using `ParseCorpus.java` command, the directory `/hadoop/index/document/output/directory/document` is the exact input to the following stage.

Latent Dirichlet Allocation
----------

The primary entry point of Mr. LDA package is via `VariationalInference.java` class. You may start training, resume training or launch testing on input data.

To print the help information and usage hints, please run the following command

    etc/hadoop-local.sh cc.slda.VariationalInference -help

To train LDA model on a dataset, please run one of the following command:

Cluster Mode

    etc/hadoop-cluster.sh cc.slda.VariationalInference -input /hadoop/index/document/output/directory/document -output /hadoop/mrlda/output/directory -term 60000 -topic 100
    etc/hadoop-cluster.sh cc.slda.VariationalInference -input /hadoop/index/document/output/directory/document -output /hadoop/mrlda/output/directory -term 60000 -topic 100 -iteration 40
    etc/hadoop-cluster.sh cc.slda.VariationalInference -input /hadoop/index/document/output/directory/document -output /hadoop/mrlda/output/directory -term 60000 -topic 100 -iteration 40 -mapper 50 -reducer 20
    etc/hadoop-cluster.sh cc.slda.VariationalInference -input /hadoop/index/document/output/directory/document -output /hadoop/mrlda/output/directory -term 60000 -topic 100 -iteration 40 -mapper 50 -reducer 20
    etc/hadoop-cluster.sh cc.slda.VariationalInference -input /hadoop/index/document/output/directory/document -output /hadoop/mrlda/output/directory -term 60000 -topic 100 -iteration 40 -mapper 50 -reducer 20 -localmerge

Local Mode

    etc/hadoop-local.sh cc.slda.VariationalInference -input /hadoop/index/document/output/directory/document -output /hadoop/mrlda/output/directory -term 60000 -topic 100
    etc/hadoop-local.sh cc.slda.VariationalInference -input /hadoop/index/document/output/directory/document -output /hadoop/mrlda/output/directory -term 60000 -topic 100 -iteration 40
    etc/hadoop-local.sh cc.slda.VariationalInference -input /hadoop/index/document/output/directory/document -output /hadoop/mrlda/output/directory -term 60000 -topic 100 -iteration 40 -mapper 50 -reducer 20
    etc/hadoop-local.sh cc.slda.VariationalInference -input /hadoop/index/document/output/directory/document -output /hadoop/mrlda/output/directory -term 60000 -topic 100 -iteration 40 -mapper 50 -reducer 20
    etc/hadoop-local.sh cc.slda.VariationalInference -input /hadoop/index/document/output/directory/document -output /hadoop/mrlda/output/directory -term 60000 -topic 100 -iteration 40 -mapper 50 -reducer 20 -localmerge
    
    
To annotate the dataset with topics learned from LDA, use the following command (use 0 reducer for output in Mappers, 1 reducer for output to 1 file, 1+ reducers for output to multiple files; LDA also expects to have `term` and `title` indices, outputs of `ParseCorpus` to be specified with `-index` argument):

Cluster Mode

	etc/hadoop-cluster.sh cc.slda.AnnotateDocuments -index data/index -input output/lda/phi100 -output output/annotation -numReducers 0 -probCutoff 0.7
	etc/hadoop-cluster.sh cc.slda.AnnotateDocuments -index data/index -input output/lda/phi100 -output output/annotation -numReducers 1
	etc/hadoop-cluster.sh cc.slda.AnnotateDocuments -index data/index -input output/lda/phi100 -output output/annotation -numReducers 10
Local Mode
	etc/hadoop-local.sh cc.slda.AnnotateDocuments -index data/index -input output/lda/phi100 -output output/annotation -numReducers 0 -probCutoff 0.7
	
	
ssh -i ec2-hadoop.pem ubuntu@ec2-107-20-18-167.__compute-1.amazonaws.com
	