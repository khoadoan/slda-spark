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
    fwrite(fileID, filters{i}, 'double');
end
fclose(fileID);