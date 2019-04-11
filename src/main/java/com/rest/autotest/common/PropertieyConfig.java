package com.rest.autotest.common;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertieyConfig{
	private static Properties properties;
    static Logger log = Logger.getLogger(PropertieyConfig.class);

    /*
     *根据properties文件主键获取对应的值
     */
    public  String getValue(String key,String propertiesFileName) throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(propertiesFileName);
        properties = new Properties();
        properties.load(stream);
        String value = properties.getProperty(key);
        stream.close();
        return value;
    }   
}