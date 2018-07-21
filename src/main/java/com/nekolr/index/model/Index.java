package com.nekolr.index.model;

import lombok.Data;

import java.util.List;

@Data
public class Index {
    private String period;
    private List<String> pc;
    private List<String> mobile;
}
