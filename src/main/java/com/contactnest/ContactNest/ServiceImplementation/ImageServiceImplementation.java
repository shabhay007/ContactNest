package com.contactnest.ContactNest.ServiceImplementation;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.contactnest.ContactNest.Helpers.AppConstants;
import com.contactnest.ContactNest.Services.ImageService;



@Service
public class ImageServiceImplementation implements ImageService {

    private Cloudinary cloudinary;

    @Autowired
    public void setCloudinary(Cloudinary cloudinary) { // constructor injection
        this.cloudinary = cloudinary;
    }



    @Override
    public String uploadImage(MultipartFile contactImage, String fileName) {

        try {
            byte[] data = new byte[contactImage.getInputStream().available()];

            contactImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id", fileName
            ));

            return this.getUrlFromPublicId(fileName);

        } 
        catch (IOException e) {

            e.printStackTrace();
            return null;

        }

        
    }



    @Override
    public String getUrlFromPublicId(String publicId) {

        return cloudinary
                .url()
                .transformation(
                    new Transformation<>()
                    .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                    .width(AppConstants.CONTACT_IMAGE_WIDTH)
                    .crop(AppConstants.CONTACT_IMAGE_CROP)
                )
                .generate(publicId);

    }

}
