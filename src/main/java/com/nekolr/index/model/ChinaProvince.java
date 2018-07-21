package com.nekolr.index.model;

import lombok.Data;

import java.util.List;

@Data
public class ChinaProvince {
    private String type;
    private List<Feature> features;
    private SrcSize srcSize;
}

@Data
class Feature {
    private String type;
    private String id;
    private Properties properties;
    private Geometry geometry;
}

@Data
class Properties {
    private String name;
    List<String> cp;
    private Integer childNum;
}

@Data
class Geometry {
    private String type;
    private List<List<String>> coordinates;
}

@Data
class SrcSize {
    private String left;
    private String top;
    private String width;
    private String height;
}
