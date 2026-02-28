package org.baoxdev.hotelbooking_test.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.Map;

import org.baoxdev.hotelbooking_test.dto.response.CloudinarySaveResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryService {
     Cloudinary cloudinary;

     public CloudinaryService(
          @Value("${cloudinary.cloud-name}") String cloudName,
          @Value("${cloudinary.api-key}") String apiKey,
          @Value("${cloudinary.api-secret}") String apiSecret
     ) {
          this.cloudinary = new Cloudinary(Map.of(
               "cloud_name", cloudName,
               "api_key", apiKey,
               "api_secret", apiSecret
          ));
     }

     public CloudinarySaveResponse upLoadFile(MultipartFile file) throws IOException {
          Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

          return CloudinarySaveResponse.builder()
                  .imageUrl(uploadResult.get("url").toString())
                  .imagePublicId(uploadResult.get("public_id").toString())
                  .build();
     }

     public void deleteFile(String publicId) throws IOException {
          cloudinary.uploader().destroy(publicId , ObjectUtils.emptyMap());
     }
}
