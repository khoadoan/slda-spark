### Preprocessing ###

1. We keep our images on Dropbox, and combine them into Sequence File, as described below:

To run the Combine Task, run:

```
etc/hadoop-cluster.sh slda.processing.pack.DropboxImageDownloader -input [dropbox input locations] -output [output location] -reducers [number of reducers] -auth [auth file]
```

where:
	- [dropbox input locations]: a file containing folders containing images  relative to root path of the dropbox account
	- [output location]: HDFS output location of the sequence file
	- [auth file]: authentication file for authentication into dropbox
	- output: PairOfIntString(Folder Index, Filename) -> Image's byte array

For example:

```
etc/hadoop-cluster.sh slda.processing.pack.DropboxImageDownloader -input /user/kdoan1/test.dbox.txt -output /user/kdoan1/slda/mscoco/eval/orig -reducers 41 -auth /user/kdoan1/dropbox.auth
```