package com.hsbc.financial.domain.common.utils;

import java.security.MessageDigest;

/**
 * MD5Util
 *
 * @author zhaoyongping
 * @date 2023/11/8 15:32
 * @className MD5Util
 **/
public class MD5Util {
    /**
     * MD5 加密
     *
     * @return String
     * @author zhaoyongping
     * @date 2023/10/10 14:15
     * @Param objects:
     */
    public static String md5(Object... objects) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : objects) {
            if (object == null) {
                continue;
            }
            stringBuilder.append(object);
        }
        return md5(stringBuilder.toString());
    }

    /**
     * 向md5方法传入一个你需要转换的原始字符串，将返回字符串的MD5码
     *
     * @return String
     * @author zhaoyongping
     * @date 2023/10/10 14:14
     * @Param code: code码
     */
    public static String md5(String code) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] bytes = code.getBytes();
        byte[] results = messageDigest.digest(bytes);
        StringBuilder stringBuilder = new StringBuilder();

        for (byte result : results) {
            // 将byte数组转化为16进制字符存入stringBuilder中
            stringBuilder.append(String.format("%02x", result));
        }
        return stringBuilder.toString();
    }

    /**
     * toNumericMd5
     *
     * @return String
     * @author zhaoyongping
     * @date 2023/10/10 14:15
     * @Param source:
     * @Param count:
     */
    public static String toNumericMd5(String source, int count) {
        if (source == null) {
            return null;
        }
        String res;
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte[] tmp = md.digest();
            char[] str = new char[count];
            int index = 0;
            for (int i = 0; i < count; i++) {
                byte byte0 = tmp[i];
                //只取高位
                str[index++] = hexDigits[(byte0 >>> 4 & 0xf) % 10];
            }
            res = new String(str);
        } catch (Exception e) {
            return null;
        }
        return res;
    }
}
