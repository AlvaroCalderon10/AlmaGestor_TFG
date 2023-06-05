package com.example.almagestor;

import static org.junit.Assert.*;

import com.example.almagestor.Validation.Encryption;

import org.junit.Test;

public class EncryptionTest {
    final Encryption encrypt=new Encryption();

    @Test
    public void ValiderEncryptionAndDesencryption() throws Exception {
        String data="456Password654";
        String password="50263675689";

        String Encrypted=encrypt.EncryptString(data,password);
        String DesEncrypted= encrypt.DesEncryptString(Encrypted,password);
        assertEquals(data,DesEncrypted);
    }
}
