package com.example.projectswp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {
    private int userId;
    private String userCode;
    private int roleId;
    private String userName;
    private String userEmail;
    private String userAddress;
    private String userPhone;
    private boolean userSex;
    private Date userDateOfBirth;
    private String userMoreInformation;
    private String userImage;
    private boolean status;
    private Date created;
    private Date update;
    private int reputation;
}
