#Milestone 1 Report#

** Khoa Doan **

*** I. The Problem ***

Large scale computer vision is recently being popular due to the emerging of large scale imagery data and in need of more generally trained visual models.  Image labeling has been investigated for decades, but the scale of the dataset is limited due to computational issues. However, the demand for large scale image labeling has become greater in recent years, simply because of the enormous amount of data that are generated everyday. Millions of satellite images are generated for every day and the task of understanding these data is never feasible for human to do exhaustively. The automatic way will benefit the community tremendously in different areas such as image retrieval, surveillance, city planning, national defense, etc. 

The increasing popularity of generic Big Data Frameworks such as Hadoop/MapReduce and the more recent Apache Spark has given the scientific community an opportunity to approach this problem at a much larger scale, in a way that is not possible for many previously. In addition, with the successfull adaptation of various bayesian methods from topic modeling into the vison domain, we also have more and more better ways to solve the image labeling problem. One particular algorithm is Spatial Latent Dirichlet Allocation (SLDA), which was demonstrated to produce promising results. In this project, we'd like to parallelize the existing SLDA algorithm into the Big Data paradigm. 

SLDA, however, uses Gibbs Sampling in solving its optimization problem. Gibbs Sampling, although expressing low bias, is difficult to parallelize in a Shared-Nothing architecture, such as Hadoop or Spark. Therefore, instead of using Gibbs Sampling, we propose a Variational Inference optimization alternative, which is more amenable to paralellization.

*** II. The Literature ***

1. *Spatial Latent Dirichlet Allocation*

    SLDA is a variation of a popular algorithm in topic modeling: Latent Dirichlet Allocation (LDA). Since LDA describes a document as a bag-of-word, direct application of LDA on images would cause several problems. In LDA, co-occurring words tend to cluster into the same topic even if these words are further away. For images, this is often not correct because some objects, such as buildings and streets, usually occur together in the same document but they are in fact different objects. This is because LDA does not take in to account the spatial constraint of the visual words, i.e. words that are close in space should be more likely from the same object.

    Spatial LDA proposes a solution the above problems. In general, Spatial LDA consider an image as a collection of several documents. The probability of a visual word to belong to a document depends on its spatial distance to the document, thus it encourages grouping of visual words that are close in proximity (which are more likely to belong to the same object) into the same documents.

    SLDA, however, uses Gibbs Sampling as an approximation to the inference problem. The nature of Gibbs Sampling needs many iterations to update the distributions, which is not efficient in a shared-nothing paradigm such as Hadoop/MapReduce. Also, as inspired by Mr.LDA, we believe Variational methods should be a reasonable way to do inference on graphical models in Hadoop. We propose to use the Variational Inference method in solving Spatial LDA as a primary direction of this work. We expect this approach will allow us to extend Mr.LDA using Spatial LDA for our image dataset.

2. *Mr. LDA: a flexible large scale topic modeling package using variational inference in MapReduce.*

    On the high level, LDA computes the likelihood of the generating the the corpus $p(w | \alpha, \eta) = E_{p(\beta, \theta, z | \alpha, \eta)} p(w, \beta, \theta, z | \alpha, \eta)$. Since this is intractable to compute, Variational Inference or Monte-Carlos Markov-Chain (MCMC) sampling is used for approximation. MCMC techinique, such as Gibbs Sampling, is unbiased but undeterministic. In addition, it requires many iterations (in the order of thousands) to adequately walk the sample space, and synchonizatation of all variables at each iteration. For these reasons, Gibbs Sampling is not ideal for a MapReduce environment.

    Variational inference, on the other hand, is deterministic procedure. Interestingly, maximizing the loglikelihood of the data with respect to the variational distribution results in updates of the variational parameters that can be efficiently distributed in MapReduce environment. Intuitively, document specific variables, $\phi_{v,k}$ and $\gamma_m$, can be computed in Mappers, and topic specific variables, $\lambda_k$, which is updated from aggregation of other document-specific variables for a specific topic k, can be computed in Reducers by shuffling all values for a specific topic to the same reducer.

    Mr.LDA is a MapReduce implementation of Latent Dirichlet Allocation (LDA) using variational inference.

3. *J. Shi and J. Malik. Normalized cuts and image segmentation.*

    Shi and Malik [1] builded a graph model to address the problem of segmenting an image into multiple pieces where pixels within each piece or region belong to the same semantic meaning. In their graph, each pixel is represented as a node and it is connected to neighboring pixels. Based on such graph representation, they casted the segmentation problem as a graph cut problem and solved it using an approximate solution based on eigenvalue decomposition.

4. *D. Comaniciu et al. Mean shift: a robust approach toward feature space analysis.*
    
    Image labeling is closely related to unsupervised feature space analysis. An important algorithm is called Mean-shift [2]. Mean-shift algorithm efficiently finds the modes of the data given the kernel function (or the metric of the data space) by shifting the location towards higher density regions. This algorithm has been widely applied in image segmentation and data clustering.

*** III. Datasets ***

In our project, we use 2 different set of image data. To evaluate the performance of our proposed method, Spatial LDA using Variational inference, we use "Microsoft Common Objects in Context" dataset, which contain approximately 150,000 labeled images. To evaluate the processing performance (or scaling out) of our methods, we also use unlabeled images that were previously downloaded from Google Map using its API.

*** IV. Dataset Analysis Preprocessing *** 

We combine the images in each dataset into bigger subset of images because small files (such as that of normal images) are efficient in a parallel environment such as MapReduce or Spark. Essentially, we index the images by a unique number and pack them into a set of sequence files, which is a key-value collection, where the key, in our case, is the index number of the image, and the value is its byte representation. 

Since image pixels do not by themselves represent interesting features and are too small to efficiently operate, we divide an image into a grid, whose grids cell (called patches) are more amenable to feature extraction of various methods. We then describe each patch by a vector of visual descriptors, or features. In our project, we use Texton as our descriptors.

Due to the inherent high dimensionality of visual data, extracted features can be very noisy and thus are not representative for images. Feature quantization is introduced to group similar features into the same low level semantics. This process also allows us to better translate image data into words when we consider each group of similar semantic as a word. K-means clustering is one of the usual choices for quantization while it may suffer from either time or space problems in ordinary environment. 


*** V. Engineering Contribution ***

We’ve created a Github repository for our project. Our repository currently contains:

1.  Packing code: combine images into sequence files. We’ve encountered 2 engineering problems: (1) very large number of small files and (2) large collection of data (since we’re using different environments for our evaluation, moving large sets of data is a non-trivial issue). We solve our problems by using Dropbox, where raw data are pushed regularly and a MapReduce job is invoked to combine them into sequence files for each environment. This is essentially using Dropbox as a “universal” distributed file system, so that we don’t have to worry about actually managing one. This method, although slower, has been shown to be tolerable in our experiment.
    
2. Feature extraction: extraction of Texton features from the original images.
    
3. Codebook quantization: clustering feature vectors into similar semantic groups. We currently use K-means algorithm from the Apache Mahout library. 

*** VI. Unit of Works ***

Khoa Doan: Coded (1) and (3) in section **V**. I’m also deriving the variational inference for spatial latent dirichlet allocation.
