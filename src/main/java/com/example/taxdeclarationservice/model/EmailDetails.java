package com.example.taxdeclarationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {

    private String from = "camtax@gmail.com";
    private String subjectConfirmAccount = "no-reply, Confirm your account";
    private String registrationTemplate = "registration.html";

}

