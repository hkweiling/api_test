package com.rest.autotest.utils;

import okio.ByteString;

import java.nio.charset.Charset;

/**
 * @description:
 * @author:
 * @time: 2019/4/10 15:49
 */
public class test {
    public static void main(String[] args) {
        System.out.println(basic("18951852260", "123456", Charset.forName("ISO-8859-1")));
    }

    public static String basic(String userName, String password, Charset charset) {
        String usernameAndPassword = userName + ":" + password;
        byte[] bytes = usernameAndPassword.getBytes(charset);
        String encoded = ByteString.of(bytes).base64();
        return "Basic " + encoded;
    }
}
