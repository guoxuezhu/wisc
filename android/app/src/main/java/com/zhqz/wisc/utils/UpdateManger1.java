package com.zhqz.wisc.utils;

/**
 * Created by jingjingtan on 2/6/17.
 */

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhqz.wisc.R;
import com.zhqz.wisc.WiscApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateManger1 {
    // 应用程序Context
    public static Context mContext = null;
    // 提示消息
    private String updateMsg = "有最新的软件包，请下载！";
    // 下载安装包的网络路径
    public static String apkUrl = WiscApplication.prefs.getVersionUrl();
    private Dialog noticeDialog;// 提示有软件更新的对话框
    private Dialog downloadDialog;// 下载对话框


    // 进度条与通知UI刷新的handler和msg常量
    private ProgressBar mProgress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_FAIL = 3;
    private int progress;// 当前进度
    private Thread downLoadThread; // 下载线程
    private boolean interceptFlag = false;// 用户取消下载
    // 通知处理刷新界面的handler
    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    downloadDialog.cancel();
                    installApk();
                    break;
                case DOWN_FAIL:
                    Toast.makeText(mContext, "下载出错", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public UpdateManger1(Context context) {
        this.mContext = context;
    }

    // 显示更新程序对话框，供主程序调用
    public void checkUpdateInfo() {
        showDownloadDialog();
    }

    protected void showDownloadDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                mContext);
        builder.setTitle("正在更新");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        builder.setView(v);// 设置对话框的内容为一个View
//        builder.setNegativeButton("取消", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                interceptFlag = true;
//            }
//        });
        downloadDialog = builder.create();
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.show();
        downloadApk();
    }

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }


    protected void installApk() {
        File dir = StorageUtils.getCacheDirectory(mContext);
        String apkName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1,
                apkUrl.length());
        File ApkFile = new File(dir, apkName);
        if (!ApkFile.exists()) {
            return;
        }
        ELog.i("File.toString()" + cmd_install + ApkFile.toString());
//        excuteSuCMD(ApkFile.toString());
        onClick_install(ApkFile.toString());
        ELog.i("跳转主界面完成");

    }

    /*
    * 来源http://www.jianshu.com/p/326ea728e5d3
    * 
    * */
    public void onClick_install(String path) {
        File apkFile = new File(path);

        try {
            Class<?> clazz = Class.forName("android.os.ServiceManager");
            Method method_getService = clazz.getMethod("getService",
                    String.class);
            IBinder bind = (IBinder) method_getService.invoke(null, "package");

            IPackageManager iPm = IPackageManager.Stub.asInterface(bind);
            iPm.installPackage(Uri.fromFile(apkFile), null, 2,
                    apkFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable mdownApkRunnable = new Runnable() {

        @Override
        public void run() {
            URL url;
            try {
                url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream ins = conn.getInputStream();

                File dir = StorageUtils.getCacheDirectory(mContext);
                String apkName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1,
                        apkUrl.length());
                File ApkFile = new File(dir, apkName);

                FileOutputStream outStream = new FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = ins.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
// 下载进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
// 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    outStream.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消停止下载
                outStream.close();
                ins.close();
            } catch (Exception e) {
                ELog.i("Exception" + e.getMessage().toString());
                mHandler.sendEmptyMessage(DOWN_FAIL);
                e.printStackTrace();
            }
        }
    };

    private String cmd_install = "pm install -r ";
    private String cmd_uninstall = "pm uninstall ";

    protected int excuteSuCMD(String cmd) {
//        try {
//            //手机必须root
//            Process process = Runtime.getRuntime().exec("su");// (这里执行是系统已经开放了root权限，而不是说通过执行这句来获得root权限)
//            DataOutputStream dos = new DataOutputStream(
//                    (OutputStream) process.getOutputStream());
//            // 部分手机Root之后Library path 丢失，导入path可解决该问题
//            dos.writeBytes((String) "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
//            cmd = String.valueOf(cmd);
//            dos.writeBytes((String) (cmd + "\n"));
//            dos.flush();
//            dos.writeBytes("exit\n");
//            dos.flush();
//            process.waitFor();
//            int result = process.exitValue();
//            System.out.println("静默安装命令1：" + result);
//            return (Integer) result;
//        } catch (Exception localException) {
//            localException.printStackTrace();
//            return -1;
//        }


        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
//            如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
            PrintWriter.println("chmod 777 " + cmd);
            PrintWriter
                    .println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r " + cmd);
            // PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            ELog.i("File.toString()====value=静默安装返回值===" + value);
            return (Integer) value;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

}
