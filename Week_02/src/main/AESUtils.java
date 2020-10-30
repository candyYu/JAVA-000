/**
 * Created by candy on 2020/10/29.
 */

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @ClassName: AES
 * @Description: AES可逆加密
 */
public class AESUtils {

    /**
     * @throws NoSuchAlgorithmException SecretKeySpec
     * @Title: getKey4AES
     * @Description: linux 与windows下不兼容设置
     */
    private static SecretKeySpec getKey4AES(String keyStr) throws NoSuchAlgorithmException {
        if (null == keyStr || keyStr.length() == 0) {
            throw new NullPointerException("key not is null");
        }
        SecretKeySpec secretKeySpec = null;

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");// 设置加密秘钥创建方式【linux与windows不同】
        random.setSeed(keyStr.getBytes());
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, random);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        return secretKeySpec;
    }


    /**
     * 加密
     *
     * @param content 待加密内容
     * @param key     加密的密钥 encryptKey+8位日期
     */
    public static String encrypt(String content, String key) {
        try {
            SecretKeySpec secretKeySpec = getKey4AES(key);
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] byteRresult = cipher.doFinal(byteContent);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteRresult.length; i++) {
                String hex = Integer.toHexString(byteRresult[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @param key     解密的密钥
     */
    public static String decrypt(String content, String key) {
        if (content.length() < 1) {
            return null;
        }
        byte[] byteRresult = new byte[content.length() / 2];
        for (int i = 0; i < content.length() / 2; i++) {
            int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);
            byteRresult[i] = (byte) (high * 16 + low);
        }
        try {
            SecretKeySpec secretKeySpec = getKey4AES(key);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] result = cipher.doFinal(byteRresult);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        String s = AESUtils.decrypt("B057C2D687746536E9BC3613C8BB717B5DB934C35AC8FE2BE429D5DBF5A2C47855F8421AD6F8F65C0A7503EF8927BE88C8E750FF1485C0D0BDB7972AB2A96E63DF3E6ACC1F2D9AAE9A08828ED3F83872B4093707407C36182062472081D5B662D186056C56AC64356E707528FB15C2E75DFDC48B114F4D99F305220572F76B70","421b47ffd946");
        System.out.println(s);
    }

}
