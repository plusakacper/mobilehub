package com.kacperp.mobilehub.util;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

public class Utils {
    public static String convertImgToText(String path) {
        String base64="";
        try{
            InputStream iSteamReader = new FileInputStream(path);
            byte[] imageBytes = IOUtils.toByteArray(iSteamReader);
            base64 = Base64.getEncoder().encodeToString(imageBytes);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "data:image/png;base64,"+base64;
    }

    public static String saveImageToDisk(String uploadDirectory, MultipartFile productImage) {
        try {

            // Create the folder if it doesn't exist
            File uploadDir = new File(uploadDirectory);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Generate a unique filename based on the current timestamp and the original filename
            String originalFilename = productImage.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = Instant.now().toEpochMilli() + fileExtension;

            // Save the file to the specified folder
            Path imagePath = Paths.get(uploadDirectory + uniqueFilename);
            Files.write(imagePath, productImage.getBytes());

            return uniqueFilename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
