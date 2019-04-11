package com.rest.autotest.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TestMode {

    //随机测试
    RANDOM(0),
    //覆盖测试
    FULL(1);

    int mode;

}
