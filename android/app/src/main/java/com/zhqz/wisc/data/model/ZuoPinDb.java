package com.zhqz.wisc.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by guoxuezhu on 17-1-3.
 */
@Entity
public class ZuoPinDb {

    @Id
    public long fileId;

    public long downloadTime;

    public long downloadTaskId;// 1为下载完成, 2为正在下载,　-1为下载失败

    public String fileName;

    public int downloadStatus; // 1为下载完成, 2为正在下载,　-1为下载失败

    @Generated(hash = 2025310149)
    public ZuoPinDb(long fileId, long downloadTime, long downloadTaskId,
            String fileName, int downloadStatus) {
        this.fileId = fileId;
        this.downloadTime = downloadTime;
        this.downloadTaskId = downloadTaskId;
        this.fileName = fileName;
        this.downloadStatus = downloadStatus;
    }

    @Generated(hash = 452495006)
    public ZuoPinDb() {
    }

    @Override
    public String toString() {
        return "ZuoPinDb{" +
                "fileId=" + fileId +
                ", downloadTime=" + downloadTime +
                ", downloadTaskId=" + downloadTaskId +
                ", fileName='" + fileName + '\'' +
                ", downloadStatus=" + downloadStatus +
                '}';
    }

    public long getFileId() {
        return this.fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public long getDownloadTime() {
        return this.downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }

    public long getDownloadTaskId() {
        return this.downloadTaskId;
    }

    public void setDownloadTaskId(long downloadTaskId) {
        this.downloadTaskId = downloadTaskId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDownloadStatus() {
        return this.downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }
}
