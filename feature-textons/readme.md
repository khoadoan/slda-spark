Texton feature extraction: using filter bank, a pre-defined set of 17 filters (15 by 15):
* Gaussians with (sigma: 1, 2, 4) applied to each CIE L, a, b channels (9 features)
* Laplacians of Gaussians with 4 different scales (sigma: 1, 2, 4, 8) applied to L channel only (4 features)
* Derivatives of Gaussians with 2 different scales (sigma: 2, 4) for each axis (x and y) (4 features)
Refer to J. Shotton. “TextonBoost for image understand… IJCV.
J Winn et al. Object Categorization by Learned Universal Visual Dictionary. ICCV 2005. Claimed this to be the best filterbank. 

What need to do next is to read the file 'filters.dat', and filter each image using the above rules.
