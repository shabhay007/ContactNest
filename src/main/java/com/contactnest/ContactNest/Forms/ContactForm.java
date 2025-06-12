package com.contactnest.ContactNest.Forms;

import org.springframework.web.multipart.MultipartFile;

import com.contactnest.ContactNest.Validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactForm {

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Phone Number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid Phone Number")
    private String phoneNumber;

    @NotBlank(message = "Address is Required")
    private String address;
    private String description;
    private String websiteLink;
    private String linkedinLink;
    private boolean favourite;

    // it is a custom annotation (created under Validator package)
    @ValidFile(message = "Invalid File")
    private MultipartFile contactImage;
    
    private String picture;

}
