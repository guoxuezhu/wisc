package com.zhqz.wisc.utils;

import android.os.Environment;
import android.util.Log;

import com.zhqz.wisc.BuildConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 打印log日志
 */
public class ELog {
    public static boolean isDebug = BuildConfig.JAVA_LOG_ENABLED;

    public static void i(Object msg) {
        if (isDebug) {
            Log.i("wisc", "" + msg);
        }
    }

    public static void d(Object msg) {
        if (isDebug) {
            Log.d("wisc", "" + msg);
        }
    }

    public static void e(Object msg) {
        if (isDebug) {
            Log.e("wisc", "" + msg);
        }
    }

    public static void getLogcat() {
        ELog.i("====================Logcat 初始化==================");
        try {
            ArrayList<String> cmdLine = new ArrayList<String>();   //设置命令   logcat -d 读取日志
            cmdLine.add("logcat");
            cmdLine.add("-d");
//            cmdLine.add( Environment.getExternalStorageDirectory()+"/log/logcat.txt");
            ArrayList<String> clearLog = new ArrayList<String>();  //设置命令  logcat -c 清除日志
            clearLog.add("logcat");
            clearLog.add("-c");

            Process process = Runtime.getRuntime().exec(cmdLine.toArray(new String[cmdLine.size()]));   //捕获日志
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));    //将捕获内容转换为BufferedReader

            String str = null;
            while ((str = bufferedReader.readLine()) != null)    //开始读取日志，每次读取一行
            {
                Runtime.getRuntime().exec(clearLog.toArray(new String[clearLog.size()]));  //清理日志....这里至关重要，不清理的话，任何操作都将产生新的日志，代码进入死循环，直到bufferreader满
//                System.out.println(str);    //输出，在logcat中查看效果，也可以是其他操作，比如发送给服务器..
                writeLogtoFile(str);
            }
            if (str == null) {
                ELog.i("==================== is null ==================");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String MYLOG_PATH_SDCARD_DIR = Environment.getExternalStorageDirectory() + "/班牌wisclog/"; // 日志文件在sdcard中的路径
    private static String MYLOGFILEName = "-Logcat.txt";// 本类输出的日志文件名称
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式

    private static void writeLogtoFile(String text) {
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + text;
        File file = new File(MYLOG_PATH_SDCARD_DIR, needWriteFiel + MYLOGFILEName);

        FileWriter filerWriter = null;
        BufferedWriter bufWriter = null;
        try {
            filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (filerWriter != null) {
                    filerWriter.close();//关闭缓冲流
                }
                if (bufWriter != null) {
                    bufWriter.close();//关闭缓冲流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}