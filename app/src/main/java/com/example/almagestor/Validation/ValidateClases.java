package com.example.almagestor.Validation;

import com.example.almagestor.DTOs.UserDTO;

public class ValidateClases {

    public String validteUserDto(UserDTO user){
        if(isEmptyUser(user.getCodePDV())){
            return "pdv";
        } else if (isEmptyUser(user.getPassword())) {
            return "password";
        } else if (isEmptyUser(user.getCompany_name())) {
            return "shopname";
        }
        return "correct";
    }

    public boolean isEmptyUser(String data){
        return data.isEmpty();
    }
}
