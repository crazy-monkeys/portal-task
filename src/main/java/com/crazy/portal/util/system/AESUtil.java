package com.crazy.portal.util.system;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密过程为 key得到md5的16位字符串 得到AES加密后的数组 将数组转成BASE64字符中输出
 * 
 * <pre>
 * 
 * </pre>
 * 
 * @Title:
 * @Description:
 * @Author:xiaozhou.zhou
 * @Since:2015年5月19日
 * @Copyright:Copyright (c) 2015
 * @ModifyDate:
 * @Version:1.1.0
 */
public class AESUtil{

    /**
     * 
     * @param content
     *            待加密字节
     * @param password
     *            密钥字节
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @Description:
     */
    public static byte[] encrypt(byte[] content,byte[] password) throws NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,BadPaddingException{
        SecretKeySpec key = new SecretKeySpec(password, "AES");// password的长度必须为16否则报错
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        return cipher.doFinal(content);
    }

    /**
     * 
     * @param content
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @Description:
     */
    public static byte[] decrypt(byte[] content,byte[] password) throws NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,BadPaddingException{
        SecretKeySpec key = new SecretKeySpec(password, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
        return cipher.doFinal(content);
    }

    /**
     * password必须为16位字符串
     * 
     * @param content
     * @param password
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @Description:
     */
    public static byte[] encrypt(byte[] content,String password) throws UnsupportedEncodingException,NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,BadPaddingException{
        return encrypt(content, password.getBytes("UTF-8"));
    }

    /**
     * 
     * @param content
     * @param password
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @Description:
     */
    public static byte[] decrypt(byte[] content,String password) throws UnsupportedEncodingException,NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,BadPaddingException{
        return decrypt(content, password.getBytes("UTF-8"));
    }

    /**
     * 加密，password为16位md5，这个要在线加密后，双方约定 password
     * 
     * @param content
     *            需要加密的内容
     * @param password
     *            加密密码
     * @return
     */
    public static String encrypt(String content,String password){
        try{
            MD5Util md5Util = new MD5Util();
            password = md5Util.getMd5_16New(password);// 密码必须16位长度
            byte[] byteContent = content.getBytes("utf-8");
            return BASE64Util.encode(encrypt(byteContent, password)); // 加密
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (NoSuchPaddingException e){
            e.printStackTrace();
        }catch (InvalidKeyException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (IllegalBlockSizeException e){
            e.printStackTrace();
        }catch (BadPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * 
     * @param content
     *            待解密内容
     * @param password
     *            解密密钥--一定要16位长度
     * @return
     */
    public static String decrypt(String content,String password){
        try{
            MD5Util util = new MD5Util();
            password = util.getMd5_16New(password);
            byte[] contentByte = BASE64Util.decode(content);
            return new String(decrypt(contentByte, password), "UTF-8"); // 加密
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (NoSuchPaddingException e){
            e.printStackTrace();
        }catch (InvalidKeyException e){
            e.printStackTrace();
        }catch (IllegalBlockSizeException e){
            e.printStackTrace();
        }catch (BadPaddingException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param content
     * @param password
     * @param ivArray
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     * @Description:
     */
    public static byte[] encrypt(byte[] content,byte[] password,byte[] ivArray)
                    throws NoSuchAlgorithmException,NoSuchProviderException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,BadPaddingException,InvalidAlgorithmParameterException{
        // "AES" encrypt name
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // 256 securerandom
        SecureRandom securerandom = SecureRandom.getInstance("SHA1PRNG");
        securerandom.setSeed(password);
        kgen.init(256, securerandom);
        // create secret
        SecretKey secretKey = kgen.generateKey();
        // response byte obj
        byte[] enCodeFormat = secretKey.getEncoded();
        // create secret key
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        // instance cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivArray));
        byte[] cryptograph = cipher.doFinal(content);
        return cryptograph;
    }

    /**
     * 
     * @param content
     * @param password
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     * @Description:
     */
    public static byte[] decrypt(byte[] content,byte[] password,byte[] ivArray)
                    throws UnsupportedEncodingException,NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,BadPaddingException,InvalidAlgorithmParameterException{
        // "AES" encrypt name
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // 256 securerandom
        SecureRandom securerandom = SecureRandom.getInstance("SHA1PRNG");
        securerandom.setSeed(password);
        kgen.init(256, securerandom);
        // create secret
        SecretKey secretKey = kgen.generateKey();
        // response byte obj
        byte[] enCodeFormat = secretKey.getEncoded();
        // create secret key
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        // instance cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivArray));
        byte[] cryptograph = cipher.doFinal(content);
        return cryptograph;
    }

    public static void main(String[] args) throws Exception{
        String source = "abcdefghijklmn";
        System.out.println("原文：" + source);
        String key = "2354234523452345234523452345";// 要为16位md5加密的字符串
        String result = encrypt(source, key);
        System.out.println("加密:" + result);
        System.out.println("解密:" + decrypt(result, key));
        String aes = "mw88/4WAgH9mb00cRNuryQ==";
        System.out.println("解密:" + new String(decrypt(aes, key)));
    }
}
