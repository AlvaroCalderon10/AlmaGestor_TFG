package com.example.almagestor;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.example.almagestor.DTOs.UserDTO;
import com.example.almagestor.FO.SellFo;
import com.example.almagestor.Validation.Encryption;
import com.example.almagestor.Validation.ValidateClases;

import org.junit.Test;

public class SellFoFuncionalityTest {
    final SellFo fo=new SellFo();
    private static final int STORAGE_PERMISSION_REQUEST_CODE=9;
    final ValidateClases validatedto=new ValidateClases();



    @Test
    public void ValiderValidateUserDTO() throws Exception{

        UserDTO example=new UserDTO("10014","28999999","ALVAROPrueba","Contraseña");
        UserDTO example_pdv=new UserDTO("10014","","ALVAROPrueba","Contraseña");
        UserDTO example_password=new UserDTO("10014","28999999","ALVAROPrueba","");
        UserDTO example_company=new UserDTO("10014","28999999","","Contraseña");

        assertEquals("pdv",validatedto.validteUserDto(example_pdv));
        assertEquals("password",validatedto.validteUserDto(example_password));
        assertEquals("shopname",validatedto.validteUserDto(example_company));
        assertEquals("correct", validatedto.validteUserDto(example));
    }

}
