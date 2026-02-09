package com.example.Room_Management_System.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(S3Client s3Client, S3Presigner presigner) {
        this.s3Client = s3Client;
        this.presigner = presigner;
    }

    public String uploadFile(MultipartFile file, String folder) throws IOException {

        String key = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(file.getBytes())
        );

        return key; // store this in DB
    }

//    public String generateSignedUrl(String key) {
//
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .build();
//
//        GetObjectPresignRequest presignRequest =
//                GetObjectPresignRequest.builder()
//                        .signatureDuration(Duration.ofMinutes(5))
//                        .getObjectRequest(getObjectRequest)
//                        .build();
//
//        PresignedGetObjectRequest presignedRequest =
//                presigner.presignGetObject(presignRequest);
//
//        return presignedRequest.url().toString();
//    }

    //for streaming

//    public String uploadFile(MultipartFile file, String folder) throws IOException {
//
//        String key = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
//
//        PutObjectRequest request = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .contentType(file.getContentType())
//                .contentLength(file.getSize())
//                .build();
//
//        s3Client.putObject(
//                request,
//                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
//        );
//
//        return key;
//    }


//validation

//    private static final List<String> ALLOWED_TYPES = List.of(
//            "image/jpeg",
//            "image/png",
//            "video/mp4",
//            "video/quicktime"
//    );
//
//for (MultipartFile file : files) {
//        if (!ALLOWED_TYPES.contains(file.getContentType())) {
//            throw new RuntimeException("Unsupported file type: " + file.getContentType());
//        }
//
//        String key = s3Service.uploadFile(file, "rooms/" + roomId);
//        photoUrls.add(buildS3Url(key));
//    }

}
