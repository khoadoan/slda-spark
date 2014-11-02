% function D = textons(rgbImage)

% if ~isrgb(rgbimage)
%     error('The argument of textons has to be RGB image');
% end

% Convert RGB to CIE Lab
colorTransform = makecform('srgb2lab');
% lab = applycform(rgbImage, colorTransform);

% Set the size of the filter
hsize = [15 15];

filters = {};
cnt = 1;

% Gaussians with sigma=1,2,4, applied to LAB
for sigma = [1 2 4]
    filters{cnt} = fspecial('gaussian', hsize, sigma);
    cnt = cnt + 1;
end

for sigma = [1 2 4 8]
    filters{cnt} = fspecial('log', hsize, sigma);
    cnt = cnt + 1;
end

for sigma = [2 4]
    filters{cnt} = conv2(fspecial('gaussian', hsize, sigma), [1 0 -1], 'same');
    cnt = cnt + 1;
    filters{cnt} = conv2(fspecial('gaussian', hsize, sigma), [1 0 -1]', 'same');
    cnt = cnt + 1;
end

fileID = fopen('filters.dat', 'wb+');
for i = 1:length(filters)
    fwrite(fileID, filters{i}, 'double', 0, 'b');
end
fclose(fileID);

%% read output.dat
fileID = fopen('output.dat', 'rb');
sz = fread(fileID, 2, 'int32', 0, 'b');
output = fread(fileID, [sz(2) sz(1)], 'double', 0, 'b');
output = output';
fclose(fileID);

rgbImage = imread('test.jpg');
colorTransform = makecform('srgb2lab');
labImage = applycform(rgbImage, colorTransform);
labImage2 = RGB2Lab(rgbImage);
figure(1); imshow(rgbImage);
figure(2); imshow(labImage);
A = reshape(labImage(7, 7, :), [1, 3])
labImage2(:, :, 1) = labImage2(:, :, 1) / 100;
labImage2(:, :, [2 3]) = (labImage2(:, :, [2 3]) + 128) / 256;
figure(3); imagesc(labImage2);
B = reshape(labImage2(7, 7, :), [1, 3])
labImage = labImage2;

%%
resp1 = filter2(filters{1}, labImage(:, :, 2));
resp1(1)