package com.example.demo.user;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
@ToString(includeFieldNames = true)
@Data
public class UserSignupPayload {

    private String userName;
    private String password;
    private String email;
    private String userRole;
    private String subscribedCategories;
    private String signConfirmationCode;
}
