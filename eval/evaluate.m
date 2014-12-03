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