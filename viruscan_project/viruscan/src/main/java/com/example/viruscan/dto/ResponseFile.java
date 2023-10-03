package com.example.viruscan.dto;


public class ResponseFile {
    private String name;
    private String url;
    private String type;
    private long size;
    private String scanResult;

    public ResponseFile(String name, String url, String type, long size, String scanResult) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.size = size;
        this.scanResult = scanResult;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }
}
