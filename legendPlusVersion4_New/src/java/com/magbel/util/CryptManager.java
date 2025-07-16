// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   Cryptomanager.java

package com.magbel.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class CryptManager {

    public CryptManager() {
    }

   

 public String encrypt(String message) throws Exception {
	 String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArW5s4V2fZ5IFrV1u8U8sunPNv3ZDDL3YQGZffx+37Dz3h02CS/X37u/VVTTxu+TOkI8PJEizNAiKybpnu9SAHFkdvXwlvE05eHMTngFEXPFE/zTOT2gH5fNdcLF/Pfdqxg+mn36egjlggU4x1CBsLjXgk3ERpqxDaL6/lKgPGGEaBM0wvz+TezDVyiS0Ovz+PuD+b8m6XPMVJfZipLuuytfKxO+uumpoAlYfIdxYkSELHNsj+H24JKF69VXxkG0ggjiigKzxtZTQa9bCB1p1mk2gB76dKEhbXBGF2OAUudbkCwbRlWrtLreL49l9XKc85aX+z4+P77tVxtjbOyqwaQIDAQAB";
     byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString.getBytes());
     KeyFactory keyFactory = KeyFactory.getInstance("RSA");
     X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
     PublicKey publicKey =  keyFactory.generatePublic(publicKeySpec);

     Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
     cipher.init(Cipher.ENCRYPT_MODE, publicKey);

     return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
 }

 public  String decrypt(String encryptedMessage) throws Exception {
	 String privateKeyString = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCtbmzhXZ9nkgWtXW7xTyy6c82/dkMMvdhAZl9/H7fsPPeHTYJL9ffu79VVNPG75M6Qjw8kSLM0CIrJume71IAcWR29fCW8TTl4cxOeAURc8UT/NM5PaAfl811wsX8992rGD6affp6COWCBTjHUIGwuNeCTcRGmrENovr+UqA8YYRoEzTC/P5N7MNXKJLQ6/P4+4P5vybpc8xUl9mKku67K18rE7666amgCVh8h3FiRIQsc2yP4fbgkoXr1VfGQbSCCOKKArPG1lNBr1sIHWnWaTaAHvp0oSFtcEYXY4BS51uQLBtGVau0ut4vj2X1cpzzlpf7Pj4/vu1XG2Ns7KrBpAgMBAAECggEABCoz34D15tupBZNJ/+3Z/KDy+XHtGp1lLT1oIK+Rz/tc1GPQ87hfRl59vd3syjQsnmhubCgkyY9E9KLkgoG6u941/8q/RejdhEjQg/RdgGqOMMoYruvSzhEMlua5pV16M8caG21ugAvBsgk/MBbBPPuFPkqHqnwrjZSQbuZO9SkMWCyaDTJ59nx7lyfT3YCZlaw6Gj94zHAmQpUefRJ+KWbytlKuOMJ8pEy6X1R4ndhp6JQK/klaOxEDU2hW6HxJZbY0jL3SO5vcRX1gHtEfJRrORcKp7jxUrEHccJk81uzGItFUi8UQNecsznHZkdOJnIape3vCQB0QU6oVOoHe4wKBgQDWpLKMpr5hixAdGZHQwZk8kYNUL0az0Psxhx0B4expdQ8g/P60vnTVrZYP//WJCpenrZMgyLOrg/JcFgyuD3IeVpvEvAUK3zXN2MS+gKjgURZQ3SsQIp+RdP2pTIEwVWPNBIVBwF3zfQgm3sf+5g24zpw4OED4dRFI8mDaL5C9MwKBgQDO2PE1DwL4y88peNlRKXKnJXeCpfj5Rwk1E1VeIJYVuEiofWf7dTcxox6QVFMYx5GWrIffPUm8GNK7zEhDc3Kw8u+lFvufJIqRd/2b7z6rgarKc7PVv1/uioS3Mn/JZDbXb/8oXTTicGw8cZdqSrfcurz/sC6QV8bZxZKrsDKD8wKBgQCflbhCpr6pm1ERsa8ePLKHu9CeyL+SdfAtNL22Zpj1F1l2N2PQUkJflD4cCzws9bknq//VwiMDhIivOp2W1FkOgWoy7W/6U1aXg3FUCiiFDwS+fpHD31owNYFbXEw4+WPITHIfHr07iVyEOeMMe1L8cnDnw7tafv+o8rxbvD2qzQKBgQCw2QphMtR3oskbzBcBLU65WW80ZHLZrcJRfpYeQ/4N0+FLLO0aML1f0vburyncpcPz7s68Lso7531E0wN1p98HKq15mIzJI5TViqFqEMKkH9jF/uZieZyrdgLCHorixKuexkKBYC826qefhf7PAdLxBTdR5G/rOSDqT+F2qM6HwQKBgFrwadnJ0QkKy16FO6+58Kx4e42gsXHcCJZOwP7YUeAqzW0nixV7ORBBxftTQavNxhfo0WlE97kh9liGJdQj3H2jhxMTtkM4WFeO46fbXnlsQH3VHOSY4j9yJjnH/G6+Gs2AXLVR/ks+P/Vf6zLcAZpFL+Upfg9Js5IZlD0GQFCk";
     byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString.getBytes());
     PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
     KeyFactory keyFactory = KeyFactory.getInstance("RSA");

     PrivateKey privateKey= keyFactory.generatePrivate(privateKeySpec);

     Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
     cipher.init(Cipher.DECRYPT_MODE, privateKey);
     return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedMessage)), StandardCharsets.UTF_8);
 }
}
