package com.msbte.modelanswerpaper.models;

public class CommonItemModel {
    public String name = "", path = "", sub_domain = "";

    public CommonItemModel() {
    }

    public CommonItemModel(String name, String path, String sub_domain) {
        this.name = name;
        this.path = path;
        this.sub_domain = sub_domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getsubDomain() {
        return sub_domain;
    }

    public void setsubDomain(String subDomain) {
        this.sub_domain = subDomain;
    }
}
