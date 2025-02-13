package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.dto.image.UpdImageRequestDTO;
import com.sangto.rental_car_server.domain.entity.Image;
import com.sangto.rental_car_server.domain.mapper.ImageMapper;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.ImageRepository;
import com.sangto.rental_car_server.response.Response;
import com.sangto.rental_car_server.service.CloudinaryService;
import com.sangto.rental_car_server.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepo;
    private final ImageMapper imageMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    public Response<ImageResponseDTO> updImage(UpdImageRequestDTO requestDTO, String folder) {

        Optional<Image> findImage = imageRepo.findById(requestDTO.id());
        if (findImage.isEmpty()) throw new AppException("Image not found");
        Image image = findImage.get();

        // Delete image on cloud
        try {
            cloudinaryService.deleteFile(image.getImage_public_id());
        } catch (IOException e) {
            throw new AppException("Error deleting image" , e);
        }

        // Upload new image to cloud
        try {
            Map newImage = cloudinaryService.uploadFileBase64(requestDTO.image_url(), folder);
            image.setImage_name((String) newImage.get("original_filename"));
            image.setImage_url((String) newImage.get("url"));
            image.setImage_public_id((String) newImage.get("public_id"));
            Image savedImage = imageRepo.save(image);
            return Response.successfulResponse("Updated image successfully", imageMapper.toImageResponseDTO(savedImage));
        } catch (IOException e) {
            throw new AppException("Error uploading image" , e);
        }
    }
}
