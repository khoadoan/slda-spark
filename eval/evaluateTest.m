function evaluateTest

groundtruth = [0 0 0 1 1 1 2 2 2 3 3 3];
data = [1 2 3 2 2 2 3 3 3 1 1 2];

[result, tp, fp] = evaluate(data, groundtruth);
disp(result);

groundtruth = [0 0 0 1 1 1 2 2 2 3 3 3];
data = [1 2 3 2 2 2 3 3 3 1 1 1];

[result, tp, fp] = evaluate(data, groundtruth);
disp(result);

groundtruth = [0 0 0 1 1 1 2 2 2 3 3 3];
data = [1 2 3 1 2 3 1 2 3 1 2 3];

[result, tp, fp] = evaluate(data, groundtruth);
disp(result);