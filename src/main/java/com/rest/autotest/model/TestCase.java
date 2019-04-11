package com.rest.autotest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class TestCase {

    private String device;
    private String opt;

    private List<Map<String, Object>> requests = new ArrayList<>();
    private List<Map<String, Object>> responses = new ArrayList<>();

    public TestCase(String device, String opt) {
        this.device = device;
        this.opt = opt;
    }

}
