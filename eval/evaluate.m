% suppose the image is MxN
% data is a vector with length MxN 
% truth is a vector with length MxN
% each element of the vector is the label {0, 1, 2, ..., K}
% if the element is 0, that means no label information
% output ratio is the score (between 0 and 1, 1 is the best)
% countp and countn is # of positive pairs and negative pairs

function [ratio, countp, countn] = evaluate(data, truth)
index = find(truth > 0);
countp = 0;
countn = 0;
for i = 1:length(index)
    for j = i+1:length(index)
        k = (data(index(i)) == data(index(j)));
        k1 = truth(index(i)) == truth(index(j));
        if k == k1
            countp = countp + 1;
        else
            countn = countn + 1;
        end
    end
end
ratio = countp / (countn + countp);