## Milestone 2 Report ##

*Khoa Doan*

### 1. Data Preprocessing Update ###

We previously use Mahout's Machine Learning library to perform our Visual Word Generation from Images (i.e. an image is divided into regular grid of pixels and each grid cell has a word-like tag to itself). However, the training time has taken significant amount of time because of the inherent performance problem in Hadoop: each iteration is independent of each other and data are generated back and forth to HDFS. Therefore, we are currently experimenting this step in Spark, to use its K-Means library, in the hope that the training time will be significantly decrease. We are still in the process of implementing it.

### 2. Derivation of Spatial LDA using Variation Inference ###

Since our goal is to apply Variational Inference optimization instead of using Gibbs Sampling, we have derived the following equations for optimizing the parameters, here: [SLDA Variation Inference](https://github.com/khoadoan/slda-spark/blob/master/text/derivation/slda/SLDA.pdf).

A reference to the original paper can be found here: [Spatial Latent Dirichlet Allocation](http://papers.nips.cc/paper/3278-spatial-latent-dirichlet-allocation).

To summary, SLDA has the following generative model:

- For each topic k, the word distribution is sampled from a Dirichlet Prior.
- For each document m, the topic distribution is sampled from another Dirichlet Distribution.
- For a visual word i in image m:
  - A document assignment is sampled from a uniform discrete distribution.
  - Location of the word within the document is sample from a Gaussian Kernel, where the intuition is that the farther the word is from a document, the less likely it belongs to that document.
  - A topic label is sampled from a discrete distribution of the document.
  - The word is sampled from a discrete distribution of words in the chosen topic above.

This generative process, therefore, makes sure that words that are close with each other should be more likely to be in the same document (here, a document is a concept that is a bit different from LDA, in which document is more like an image in our case). This, in turn, makes sure that pixels of the same object are more likely to be assigned to the same label.

### 3. Experiments ###

Our dataset is divided into 3 parts:

  1. Labeled Dataset for Evaluation: this comprises of about 6-7GB of compressed data, or about 13K images.
  2. Labeled Dataset for Testing: this comprises of about 13-14GB of compressed data, or about 39-40K images.
  3. Large-Scale Unlabeled Dataset: this dataset is previously extracted from Satellite View of Google Maps. 

Our processing pipe line is as following, with **bolded items** are remaining work of our project:

  1. Convert images into SequenceFile: to prevent processing many small files.
  2. Feature Extractions: Extract texton features for each image's patch. Each image is converted into a matrix, where a row represent a patch in the image, and the columns represent the a texton feature of this patch (this is a real number).
  3. Codebook Generation (Visual Word generation: group closely similar vectors of texton features into groups. This is to reduce the noise of the data and generate more recurring visual words.
  4. **Spatial LDA Variational Inference**: This is to learn the labels of words in the image.
  5. **Evaluation**: Evaluation, for (1) and (2) datasets above. This is essentially to determine the validation and applicability of our method. The evaluation will be carried as following:
    - Randomly choose a pair of pixels in an image. 
    - If the pair belongs to the same ground-truth object, then if both are similarly labeled, output 1, otherwise, 0.
    - If the pair do not belong to the same object, then if both are similarly labeled, output 0, otherwise, 1.
    - We compare the errors against LDA. If we have enough time, we will also compare it against a popular supervise learning method for object recoginition in vision.
  
