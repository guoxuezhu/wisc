package com.zhqz.wisc.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guoxuezhu on 17-1-3.
 */

public class ZuoPin {


    @SerializedName("startTime")
    public String startTime;
    @SerializedName("endTime")
    public String endTime;
    @SerializedName("filePath")
    public String filePath;
    @SerializedName("thumbnailPath")
    public String thumbnailPath;
    @SerializedName("fileType")
    public String fileType;
    @SerializedName("downloadStatus")
    public int downloadStatus;
    @SerializedName("fileName")
    public String fileName;
    @SerializedName("fileId")
    public int fileId;

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public String getFileType() {
        return fileType;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "ZuoPin{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", filePath='" + filePath + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", fileType='" + fileType + '\'' +
                ", downloadStatus=" + downloadStatus +
                ", fileName='" + fileName + '\'' +
                ", fileId=" + fileId +
                '}';
    }

    public ZuoPin(String startTime, String endTime, String filePath, String thumbnailPath, String fileType, int downloadStatus, String fileName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.filePath = filePath;
        this.thumbnailPath = thumbnailPath;
        this.fileType = fileType;
        this.downloadStatus = downloadStatus;
        this.fileName = fileName;
    }
}
