package com.rest.autotest.utils;

import java.io.*;

public class FileUtil {

    public static String loadResourceFile(String fileName){
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(FileUtil.class.getClassLoader().getResourceAsStream(fileName)))) {
            String line;
            while ((line=br.readLine())!=null){
                sb.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void copyFiles(File fromFile, File toFile) throws IOException {
        FileInputStream ins = new FileInputStream(fromFile);
        FileOutputStream out = new FileOutputStream(toFile);
        byte[] b = new byte[1024];
        int n=0;
        while((n=ins.read(b))!=-1){
            out.write(b, 0, n);
        }

        ins.close();
        out.close();
    }

}
