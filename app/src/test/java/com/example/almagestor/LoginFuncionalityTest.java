package com.example.almagestor;

import static org.junit.Assert.*;

import com.example.almagestor.DTOs.UserDTO;
import com.example.almagestor.Validation.Encryption;
import com.example.almagestor.Validation.ValidateClases;

import org.junit.Test;

public class LoginFuncionalityTest {
    final Encryption encrypt=new Encryption();
    final ValidateClases validatedto=new ValidateClases();

    @Test
    public void ValiderEncryptionAndDesencryption() throws Exception {
        String data="456Password654";
        String password="50263675689";

        String Encrypted=encrypt.EncryptString(data,password);
        String DesEncrypted= encrypt.DesEncryptString(Encrypted,password);
        assertEquals(data,DesEncrypted);
    }

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
