package com.contactnest.ContactNest.Validators;

// import org.springframework.beans.factory.annotation.Value;

// import java.awt.image.BufferedImage;

// import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;



public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 10; // size MB

    // type

    // height

    // width

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        if(file == null || file.isEmpty()){
            // context.disableDefaultConstraintViolation();
            // context.buildConstraintViolationWithTemplate("File cannot be empty").addConstraintViolation();

            return true;
        }

        // file size
        if(file.getSize() > MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File should be less than 10 MB").addConstraintViolation();

            return false;
        }

        // resolution (you can check it like this)
        // BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

        // if(bufferedImage.getHeight() > ...){

        // }


        return true;
        
    }

}
