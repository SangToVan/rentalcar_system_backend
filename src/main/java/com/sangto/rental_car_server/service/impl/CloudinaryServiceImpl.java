package com.sangto.rental_car_server.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sangto.rental_car_server.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder.default}")
    private String defaultFolder;

    @Override
    public Map uploadFileBase64(String base64String, String folder) throws IOException {
        if (folder == null) folder = defaultFolder;
        if (folder == null) folder = defaultFolder;
        Map result = cloudinary.uploader().upload(base64String, ObjectUtils.asMap("folder", folder));
        return result;
    }

    @Override
    public Map deleteFile(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
