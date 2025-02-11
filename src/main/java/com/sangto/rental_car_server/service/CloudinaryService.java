package com.sangto.rental_car_server.service;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {

    Map uploadFileBase64(String base64String, String folder) throws IOException;

    Map deleteFile(String publicId) throws IOException;
}
