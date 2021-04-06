package com.wedoops.platinumnobleclub.database;

import com.orm.SugarRecord;

public class MemberIDEncryted extends SugarRecord {

    private String EncryptedString;

    public MemberIDEncryted(){}

    public MemberIDEncryted(String encryptedString) {
        EncryptedString = encryptedString;
    }

    public String getEncryptedString() {
        return EncryptedString;
    }

    public void setEncryptedString(String encryptedString) {
        EncryptedString = encryptedString;
    }
}
