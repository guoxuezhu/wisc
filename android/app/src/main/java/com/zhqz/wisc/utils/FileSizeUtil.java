package com.zhqz.wisc.utils;

import android.app.DownloadManager;
import android.os.Environment;
import android.os.StatFs;

import com.zhqz.wisc.WiscApplication;
import com.zhqz.wisc.data.DbDao.ZuoPinDbDao;
import com.zhqz.wisc.data.model.ZuoPinDb;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jingjingtan on 3/14/17.
 */

public class FileSizeUtil {

    public static int size = 1024 * 1; //1G

    public static String createFile() {
        //创建文件夹 ，在存储卡下
        String dirName = Environment.getExternalStorageDirectory() + "/班牌作品/";
        File file = new File(dirName);
        //不存在创建
        if (!file.exists()) {
            file.mkdir();
        }
        return dirName;
    }

    public static String createFileLog() {
        //创建文件夹 ，在存储卡下
        String dirName = Environment.getExternalStorageDirectory() + "/班牌wisclog/";
        File file = new File(dirName);
        //不存在创建
        if (!file.exists()) {
            file.mkdir();
        }
        return dirName;
    }


    public static void delete(DownloadManager downloadManager) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ZuoPinDbDao zuoPinDbDao = WiscApplication.getDaoSession().getZuoPinDbDao();
                    if (zuoPinDbDao.loadAll().size() != 0 && readSDCard() - size < 0) {
                        List<ZuoPinDb> zuoPinTwoList = zuoPinDbDao.queryBuilder().orderAsc(ZuoPinDbDao.Properties.DownloadTime).list();//排序
                        ELog.i("=============数据库===" + zuoPinDbDao.loadAll().size() + "=====" + zuoPinDbDao.loadAll().toString());
                        downloadManager.remove(zuoPinTwoList.get(0).downloadTaskId);
                        zuoPinDbDao.deleteByKey(zuoPinTwoList.get(0).getFileId());
                        ELog.i("=============删除之后的数据库===" + zuoPinDbDao.loadAll().size() + "=====" + zuoPinDbDao.loadAll().toString());
                        deletefile(zuoPinTwoList.get(0).getFileName());
                    } else {
                        break;
                    }
                }
            }
        }).start();
    }

    public static void deletefile(String fileName) {
        String dirName = Environment.getExternalStorageDirectory() + "/班牌作品/";
        File file = new File(dirName + fileName);
        deleteFile(file);
    }

    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
                file.delete();
            }
        }
    }

    public static int readSDCard() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long availCount = sf.getAvailableBlocks();
            //ELog.i("================" + "剩余空间:" + availCount * blockSize / 1024 / 1024 + " M");
            int residualSize = (int) (availCount * blockSize / 1024 / 1024);
            return residualSize;
        } else {
            return -1;
        }
    }


    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file) {

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        ELog.i("==TAGtjj===555===大小＝＝" + getFormatSize(size));
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte(s)";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

}
