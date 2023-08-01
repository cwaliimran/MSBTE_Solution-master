package com.msbte.modelanswerpaper;

public class model
{
    String title,url;

    public model() {
    }

    public model(String filename, String fileurl, int nod, int nol, int nov) {
        this.title = filename;
        this.url = fileurl;

    }

    public String getFilename() {
        return title;
    }

    public void setFilename(String filename) {
        this.title = filename;
    }

    public String getFileurl() {
        return url;
    }

    public void setFileurl(String fileurl) {
        this.url = fileurl;
    }


}
