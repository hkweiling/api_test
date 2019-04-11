package com.rest.autotest.exceptions;

/**
 * @description:
 * @author:
 * @time: 2019/4/2 9:43
 */
public class ErrorException extends Exception {

    public ErrorException(String msg){
        super(msg); //父类构造函数
    }
}
