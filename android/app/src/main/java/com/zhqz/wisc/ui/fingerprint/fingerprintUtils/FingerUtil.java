package com.zhqz.wisc.ui.fingerprint.fingerprintUtils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.zhqz.wisc.data.WiscClient;
import com.zhqz.wisc.ui.fingerprint.FingerprintPresenter;
import com.zhqz.wisc.ui.main.MainActivity;
import com.zhqz.wisc.ui.main.MainPresenter;
import com.zhqz.wisc.utils.ELog;

/**
 * Created by guoxuezhu on 18-3-12.
 */

public class FingerUtil {

    public static DevComm m_devComm;
    private Handler  mainHandler,fingerHandler;
    private FingerprintPresenter mFingerprintPresenter;
    MainPresenter mainPresenter;
    private short m_dwCode;
    private byte[] m_TemplateData = new byte[DevComm.GD_MAX_RECORD_SIZE];
    private int m_nTemplateSize = 0;

    public FingerUtil(Activity parentActivity, Handler handler, MainPresenter mainPresenters) {
        if (m_devComm == null) {
            ELog.i("====FingerUtil====tjj=123=打开指纹设备成功=MainPresenter===");
            m_devComm = new DevComm(parentActivity, m_IConnectionHandler);
        }
        ELog.i("====FingerUtil====tjj=123=打开指纹设备成功=MainPresenter==1=");
        mainHandler = handler;
        mainPresenter = mainPresenters;
    }

    public FingerUtil(Activity parentActivity, FingerprintPresenter fingerprintPresenter,Handler handler) {
        if (m_devComm == null) {
            ELog.i("====FingerUtil====tjj=123=打开指纹设备成功===FingerprintPresenter=");
            m_devComm = new DevComm(parentActivity, m_IConnectionHandler);
        }
        fingerHandler = handler;
        ELog.i("====FingerUtil====tjj=234=打开指纹设备成功==FingerprintPresenter==");
        mFingerprintPresenter = fingerprintPresenter;
    }


    private final IUsbConnState m_IConnectionHandler = new IUsbConnState() {
        @Override
        public void onUsbConnected() {
            ELog.i("====FingerUtil====2222222222==打开指纹设备成功====");
            Message msg = new Message();
            msg.arg1 = 1;
            msg.what = WiscClient.FINGER_DEVICE_HANDLER;
            if (WiscClient.isFinger == false){
                ELog.i("====FingerUtil====2222222222==打开指纹设备成功==11111==");
                mainHandler.sendMessage(msg);
            }
//            mainHandler.sendMessage(msg);
        }

        @Override
        public void onUsbPermissionDenied() {
            ELog.i("====FingerUtil====2222222222==权限被拒绝!====");
            Message msg = new Message();
            msg.arg1 = 2;
            msg.what = WiscClient.FINGER_DEVICE_HANDLER;
            if (WiscClient.isFinger == false){
                mainHandler.sendMessage(msg);
            }
//            mainHandler.sendMessage(msg);
        }

        @Override
        public void onDeviceNotFound() {
            ELog.i("====FingerUtil======找不到USB设备!===");
            Message msg = new Message();
            msg.arg1 = 3;
            msg.what = WiscClient.FINGER_DEVICE_HANDLER;
            if (WiscClient.isFinger == false){
                mainHandler.sendMessage(msg);
            }
//            mainHandler.sendMessage(msg);
        }
    };


    public int OpenDevice() {
        ELog.i("====FingerUtil===1111111111===m_devComm是否为空===="+m_devComm);
        if (m_devComm != null) {
//            m_devComm.CloseComm();
            if (!m_devComm.IsInit()) {
                if (!m_devComm.OpenComm("USB", 9600)) {
                    ELog.i("====FingerUtil===1111111111===初始化设备失败!====");
                    Message msg = new Message();
                    msg.arg1 = 0;
                    msg.what = WiscClient.FINGER_DEVICE_HANDLER;
                    mainHandler.sendMessage(msg);
                    return 1;
                }
            }
            if (m_devComm.Run_TestConnection() == (short) DevComm.ERR_SUCCESS) {
                if (m_devComm.Run_GetDeviceInfo() == (short) DevComm.ERR_SUCCESS) {
                    ELog.i("====FingerUtil===333333333===打开指纹设备成功====");
                    Message msg = new Message();
                    msg.arg1 = 1;
                    msg.what = WiscClient.FINGER_DEVICE_HANDLER;
                    mainHandler.sendMessage(msg);
                    return 0;
                } else {
                    ELog.i("====FingerUtil===1==无法连接到设备!====");
                    Message msg = new Message();
                    msg.arg1 = 4;
                    msg.what = WiscClient.FINGER_DEVICE_HANDLER;
                    mainHandler.sendMessage(msg);
//                    m_devComm.CloseComm();
                    return 1;
                }
            } else {
                ELog.i("====FingerUtil===2===无法连接到设备!==");
                Message msg = new Message();
                msg.arg1 = 4;
                msg.what = WiscClient.FINGER_DEVICE_HANDLER;
                mainHandler.sendMessage(msg);
                m_devComm.CloseComm();
                return 1;
            }
        }

        return 1;
    }


    public int Run_CmdDeleteAll() {
        ELog.e("tjj删除所有的指纹" + m_devComm);
        //. Assemble command packet
        m_devComm.InitPacket((short) DevComm.CMD_CLEAR_ALLTEMPLATE_CODE, true);
        m_devComm.AddCheckSum(true);
        m_dwCode = (short) DevComm.CMD_CLEAR_ALLTEMPLATE_CODE;
        try {
            StartSendThread();
        }catch (Exception e){
            ELog.e("tjj删除所有的指纹" +e);
        }

        return 0;
    }

    public void StartSendThread() {
        new Thread(new Runnable() {
            public void run() {
                boolean w_blRet = false;
                short w_wPrefix = 0;

                w_wPrefix = (short) (((m_devComm.m_abyPacket[1] << 8) & 0x0000FF00) | (m_devComm.m_abyPacket[0] & 0x000000FF));
                if (w_wPrefix == (short) (DevComm.CMD_PREFIX_CODE)) {
                    if (m_dwCode != (short) (DevComm.CMD_FP_CANCEL_CODE)) {
                        ELog.i("==========StartSendThread=======11111111111=====0000000=====11111111111======" + m_dwCode);
                        try {
                            w_blRet = m_devComm.USB_SendPacket(m_dwCode);
                        }catch (Exception e){
//                            w_blRet = true;
                            ELog.e("tjj删除所有的指纹" +e);
                        }

                    } else {
                        ELog.i("==========StartSendThread=======11111111111=====11111111=====11111111111======" + m_dwCode);
                        w_blRet = m_devComm.USB_ReceiveAck(m_dwCode);
                    }
                } else if (w_wPrefix == (short) (DevComm.CMD_DATA_PREFIX_CODE)) {
                    ELog.i("==========StartSendThread=======11111111111=====22222222=====11111111111======" + m_dwCode);
                    w_blRet = m_devComm.USB_SendDataPacket(m_dwCode);
                } else {
                    if (m_dwCode != (short) (DevComm.CMD_FEATURE_OF_CAPTURED_FP_CODE)) {
                        ELog.i("==========StartSendThread=======11111111111=====33333333=====11111111111======" + m_dwCode);
                        w_blRet = m_devComm.USB_ReceiveAck(m_dwCode);
                    } else {
                        ELog.i("==========StartSendThread=======11111111111=====44444444=====11111111111======" + m_dwCode);
                        w_blRet = m_devComm.USB_ReceiveDataPacket((short) DevComm.CMD_FEATURE_OF_CAPTURED_FP_CODE);
                    }
                }
                if (w_blRet == false) {
                    Message msg = new Message();
                    msg.arg1 = 66;
                    msg.what = WiscClient.FINGER_DEVICE_HANDLER;
                    if (WiscClient.isFinger == true){
                        fingerHandler.sendMessage(msg);
                    } else {
                        mainHandler.sendMessage(msg);
                    }
//                    Run_CmdCancel();
//                    CloseDevice();
                    ELog.i("====FingerUtil======Fail to receive response! ======== Please check the connection to target.============");
                    return;
                }
                //. Display response packet
                short w_wCmd = (short) (((m_devComm.m_abyPacket[3] << 8) & 0x0000FF00) | (m_devComm.m_abyPacket[2] & 0x000000FF));
                displayResult(w_wCmd);
                ELog.i("==========StartSendThread====222======" + w_wCmd);
            }
        }).start();
    }


    public int Run_CmdWriteTemplate(int p_nTmpNo, String dataString) {
        boolean w_blRet = false;

        String[] dataslist = dataString.split(",");
        int length = dataslist == null ? 0 : dataslist.length;
        byte[] byteData = new byte[length];
        for (int j = 0; j < length; j++) {
            byteData[j] = Integer.valueOf(dataslist[j]).byteValue();
        }

        m_TemplateData = byteData;
        m_nTemplateSize = 570;

        //. Assemble command packet
        m_devComm.InitPacket((short) DevComm.CMD_WRITE_TEMPLATE_CODE, true);
        m_devComm.SetDataLen((short) 0x0002);
        m_devComm.SetCmdData((short) m_nTemplateSize, true);
        m_devComm.AddCheckSum(true);

        //. Send command packet to target
        w_blRet = m_devComm.Send_Command((short) DevComm.CMD_WRITE_TEMPLATE_CODE);
        if (w_blRet == false) {
            //CloseDevice();
            return 1;
        }

        if (m_devComm.GetRetCode() != (short) DevComm.ERR_SUCCESS) {
            displayResult((short) DevComm.CMD_WRITE_TEMPLATE_CODE);
            return 1;
        }

        //. Assemble data packet
        m_devComm.InitPacket((short) DevComm.CMD_WRITE_TEMPLATE_CODE, false);
        m_devComm.SetDataLen((short) (m_nTemplateSize + 2));
        ELog.e("==tjj==m_abyPacket=short="+(short) p_nTmpNo);
        m_devComm.SetCmdData((short) p_nTmpNo, true);
        System.arraycopy(m_TemplateData, 0, m_devComm.m_abyPacket, 8, m_nTemplateSize);
        m_devComm.AddCheckSum(false);

        //. Send data packet to target
        w_blRet = m_devComm.Send_DataPacket((short) DevComm.CMD_WRITE_TEMPLATE_CODE);
        if (w_blRet == false) {
            return 2;
        }

        //. Display response packet
        displayResult((short) DevComm.CMD_WRITE_TEMPLATE_CODE);
        return 0;
    }

    private void displayResult(short p_nCode) {
        short w_nRet, w_nData, w_nData2, w_nSize;

        w_nRet = m_devComm.MAKEWORD(m_devComm.m_abyPacket[6], m_devComm.m_abyPacket[7]);
        w_nData = m_devComm.MAKEWORD(m_devComm.m_abyPacket[8], m_devComm.m_abyPacket[9]);
        w_nData2 = m_devComm.MAKEWORD(m_devComm.m_abyPacket[10], m_devComm.m_abyPacket[11]);
        w_nSize = m_devComm.MAKEWORD(m_devComm.m_abyPacket[4], m_devComm.m_abyPacket[5]);

        switch (p_nCode) {
            case (short) DevComm.CMD_CLEAR_ALLTEMPLATE_CODE:
                if (w_nRet == (short) DevComm.ERR_SUCCESS) {
                    ELog.i("==========清空所有指纹=====成功======" + w_nData);
                } else {
                    ELog.i("==========清空所有指纹=====失败======");
                }
                break;
            case (short) DevComm.CMD_WRITE_TEMPLATE_CODE:
                ELog.i("==========写入指纹=====成功===id==w_nData=" + w_nRet + "===="+(short) DevComm.ERR_SUCCESS);
                if (w_nRet == (short) DevComm.ERR_SUCCESS) {
                    ELog.i("==========写入指纹=====成功===id===" + w_nData);
                } else {
                    ELog.i("==========写入指纹=====失败======");
                }
                break;
            case (short) DevComm.CMD_READ_TEMPLATE_CODE:
                if (w_nRet == (short) DevComm.ERR_SUCCESS) {
                    ELog.i("==========读取指纹=====成功===id===" + w_nData);
                    String str = "";
                    for (int ii = 0; ii < m_TemplateData.length; ii++) {
                        str += m_TemplateData[ii] + ",";
                    }
                    if (WiscClient.isFinger == true){
                        mFingerprintPresenter.sendZhiwenData(str);
                    } else {
                        mainPresenter.sendZhiwenData(str);
                    }
//                   mFingerprintPresenter.sendZhiwenData(str);
                } else {
                    ELog.i("==========读取指纹=====失败======" + w_nData);
                }
                break;
            case (short) DevComm.CMD_VERIFY_WITH_DOWN_TMPL_CODE:
            case (short) DevComm.CMD_IDENTIFY_WITH_DOWN_TMPL_CODE:
            case (short) DevComm.CMD_VERIFY_CODE:
            case (short) DevComm.CMD_IDENTIFY_CODE:
            case (short) DevComm.CMD_IDENTIFY_FREE_CODE:
            case (short) DevComm.CMD_ENROLL_CODE:
            case (short) DevComm.CMD_ENROLL_ONETIME_CODE:
            case (short) DevComm.CMD_CHANGE_TEMPLATE_CODE:
            case (short) DevComm.CMD_IDENTIFY_WITH_IMAGE_CODE:
            case (short) DevComm.CMD_VERIFY_WITH_IMAGE_CODE:
                if (w_nRet == (short) DevComm.ERR_SUCCESS) {
                    switch (w_nData) {
                        case (short) DevComm.GD_NEED_RELEASE_FINGER:
                            ELog.i("===tjj==识别====录入指纹=====请释放你的手指======");
//                            mFingerprintPresenter.showMessage(3, 0);
                            if (WiscClient.isFinger == true){
                                mFingerprintPresenter.showMessage(3, 0);
                            } else {
                                mainPresenter.showMessage(3, 0);
                            }
                            break;
                        case (short) DevComm.GD_NEED_FIRST_SWEEP:
                            ELog.i("==tjj==录入指纹=====请输入你的手指======");
                            if (WiscClient.isFinger == true){
                                mFingerprintPresenter.showMessage(4, 0);
                            } else {
                                mainPresenter.showMessage(4, 0);
                            }
//                            mFingerprintPresenter.showMessage(4, 0);
                            break;
                        case (short) DevComm.GD_NEED_SECOND_SWEEP:
                            ELog.i("====录入指纹=====请再输入两次你的手指======");
                            if (WiscClient.isFinger == true){
                                mFingerprintPresenter.showMessage(5, 0);
                            } else {
                                mainPresenter.showMessage(5, 0);
                            }
//                            mFingerprintPresenter.showMessage(5, 0);
                            break;
                        case (short) DevComm.GD_NEED_THIRD_SWEEP:
                            ELog.i("====录入指纹=====请再输入一次你的手指======");
//                            mFingerprintPresenter.showMessage(5, 0);
                            if (WiscClient.isFinger == true){
                                mFingerprintPresenter.showMessage(5, 0);
                            } else {
                                mainPresenter.showMessage(5, 0);
                            }
                            break;
                        default:
                            ELog.i("===tjj==识别====录入指纹=====成功==id====" + w_nData);
                            if (WiscClient.isEnter == true) {
//                                mFingerprintPresenter.showMessage(1, 0);
                                if (WiscClient.isFinger == true){
                                    mFingerprintPresenter.showMessage(1, 0);
                                } else {
                                    mainPresenter.showMessage(1, 0);
                                }
                            } else {
                                if (WiscClient.isFinger == true){
                                    mFingerprintPresenter.showMessage(2, w_nData);//识别指纹成功
                                } else {
                                    mainPresenter.showMessage(2, w_nData);
                                }
//                                mFingerprintPresenter.showMessage(2, w_nData);//识别指纹成功
                            }
                            break;
                    }
                } else {
                    ELog.i("====tjj====识别======录入指纹======失败======");
                    if (WiscClient.isEnter == true) {
//                        mFingerprintPresenter.showMessage(-1, 0);
                        if (WiscClient.isFinger == true){
                            mFingerprintPresenter.showMessage(-1, 0);
                        } else {
                            mainPresenter.showMessage(-1, 0);
                        }
                    } else {
                        if (WiscClient.isFinger == true){
                            mFingerprintPresenter.showMessage(2, 0);//识别指纹失败
                        } else {
                            mainPresenter.showMessage(2, 0);
                        }
//                        mFingerprintPresenter.showMessage(2, 0);
                    }
                }
                break;


            default:
                break;


        }


        if ((p_nCode == (short) DevComm.CMD_IDENTIFY_FREE_CODE)) {
            ELog.i("=========识别==============");
            if (w_nRet == (short) DevComm.ERR_SUCCESS ||
                    m_devComm.LOBYTE(w_nData) != DevComm.ERR_NOT_AUTHORIZED &&
                            m_devComm.LOBYTE(w_nData) != DevComm.ERR_FP_CANCEL &&
                            m_devComm.LOBYTE(w_nData) != DevComm.ERR_INVALID_OPERATION_MODE &&
                            m_devComm.LOBYTE(w_nData) != DevComm.ERR_ALL_TMPL_EMPTY) {
                ELog.i("=========识别====22222222222==========");
                m_devComm.memset(m_devComm.m_abyPacket, (byte) 0, 64 * 1024);
                StartSendThread();
                return;
            }
        }

        if ((p_nCode == (short) DevComm.CMD_ENROLL_CODE) || (p_nCode == (short) DevComm.CMD_CHANGE_TEMPLATE_CODE)) {
            ELog.i("=========录入指纹=======11=======");
            switch (w_nData) {
                case (short) DevComm.GD_NEED_RELEASE_FINGER:
                case (short) DevComm.GD_NEED_FIRST_SWEEP:
                case (short) DevComm.GD_NEED_SECOND_SWEEP:
                case (short) DevComm.GD_NEED_THIRD_SWEEP:
                case (short) DevComm.ERR_BAD_QUALITY:
                    ELog.i("=========录入指纹=====再次录入=========");
                    m_devComm.memset(m_devComm.m_abyPacket, (byte) 0, 64 * 1024);
                    StartSendThread();
                    return;
                default:
                    break;
            }
        }

        m_devComm.memset(m_devComm.m_abyPacket, (byte) 0, 64 * 1024);
    }


    public int CloseDevice() {
        m_devComm.CloseComm();
        return 0;
    }


    public int Run_CmdEnroll(int p_nTmpNo) {
        ELog.e("tjj删除所有的指纹333333");
        //. Assemble command packet
        m_devComm.InitPacket((short) DevComm.CMD_ENROLL_CODE, true);
        m_devComm.SetDataLen((short) 0x0002);
        m_devComm.SetCmdData((short) p_nTmpNo, true);
        m_devComm.AddCheckSum(true);

        m_dwCode = (short) DevComm.CMD_ENROLL_CODE;
        try {
            StartSendThread();
        }catch (Exception e){
            ELog.e("tjj删除所有的指纹333333" +e);
        }


        return 0;
    }


    public int Run_CmdIdentifyFree() {
        ELog.i("========tjj==Run_CmdIdentifyFree=====");
        //. Assemble command packet
        m_devComm.InitPacket((short) DevComm.CMD_IDENTIFY_FREE_CODE, true);
        m_devComm.AddCheckSum(true);

        m_dwCode = (short) DevComm.CMD_IDENTIFY_FREE_CODE;
        if (WiscClient.isFinger){
            mFingerprintPresenter.showMessage(4, 0);//"请按下你的手指"
        } else {
            mainPresenter.showMessage(4, 0);//"请按下你的手指"
        }
        StartSendThread();
        return 0;
    }


    public int Run_CmdCancel() {

        boolean w_bRet;

        //. Init Packet
        m_devComm.InitPacket2((short) DevComm.CMD_FP_CANCEL_CODE, true);
        m_devComm.SetDataLen2((short) 0x00);
        m_devComm.AddCheckSum2(true);

        //. Send Packet
        w_bRet = false;
        if ((m_devComm.m_nConnected == 1) || (m_devComm.m_nConnected == 3)) {
            w_bRet = m_devComm.UART_SendCommand2((short) DevComm.CMD_FP_CANCEL_CODE);
        } else if (m_devComm.m_nConnected == 2) {
            w_bRet = m_devComm.USB_SendPacket2((short) DevComm.CMD_FP_CANCEL_CODE);
        }
        if (w_bRet != true) {
            ELog.i("==========取消===失败==１===");
            return -1;
        }

        if ((m_devComm.m_nConnected == 1) || (m_devComm.m_nConnected == 3)) {
            w_bRet = m_devComm.UART_ReceiveAck2((short) DevComm.CMD_FP_CANCEL_CODE);
        } else if (m_devComm.m_nConnected == 2) {
            w_bRet = m_devComm.USB_ReceiveAck2((short) DevComm.CMD_FP_CANCEL_CODE);
        }
        if (w_bRet == true) {
            ELog.i("==========取消===成功=====");
//            CloseDevice();
        } else {
            ELog.i("==========取消===失败==２===");
            return -1;
        }

        return 0;
    }

    public int Run_CmdReadTemplate(int p_nTmpNo) {
        boolean w_blRet = false;
        int w_nTemplateNo = 0;
        int w_nLen = 0;
        int w_nBufOffset = 0;

        w_nTemplateNo = p_nTmpNo;
        m_devComm.memset(m_TemplateData, (byte) 0, DevComm.GD_MAX_RECORD_SIZE);

        //. Assemble command packet
        m_devComm.InitPacket((short) DevComm.CMD_READ_TEMPLATE_CODE, true);
        m_devComm.SetDataLen((short) 0x0002);
        m_devComm.SetCmdData((short) w_nTemplateNo, true);
        m_devComm.AddCheckSum(true);

        m_dwCode = DevComm.CMD_READ_TEMPLATE_CODE;
        w_blRet = m_devComm.Send_Command((short) DevComm.CMD_READ_TEMPLATE_CODE);
        if (w_blRet == false) {
//            CloseDevice();
            return 1;
        }
        if (m_devComm.GetRetCode() != (short) DevComm.ERR_SUCCESS) {
            displayResult((short) DevComm.CMD_READ_TEMPLATE_CODE);
            return 1;
        }

        if (m_devComm.GetCmdData(false) == DevComm.GD_TEMPLATE_SIZE) {
            w_blRet = m_devComm.Receive_DataPacket((short) DevComm.CMD_READ_TEMPLATE_CODE);
            w_nLen = DevComm.GD_TEMPLATE_SIZE;
            System.arraycopy(m_devComm.m_abyPacket, 10, m_TemplateData, 0, DevComm.GD_TEMPLATE_SIZE);
        } else {
            w_nLen = m_devComm.GetCmdData(false);
            w_nBufOffset = 0;

            while (true) {
                w_blRet = m_devComm.Receive_DataPacket((short) DevComm.CMD_READ_TEMPLATE_CODE);

                if (w_blRet == false) {
                    break;
                } else {
                    if (m_devComm.GetRetCode() == DevComm.ERR_SUCCESS) {
                        if (m_devComm.GetDataLen() > (DevComm.DATA_SPLIT_UNIT + 4)) {
                            m_devComm.SetCmdData((short) DevComm.ERR_FAIL, true);
                            m_devComm.SetCmdData((short) DevComm.ERR_INVALID_PARAM, false);
                            w_blRet = false;
                            break;
                        } else {
                            System.arraycopy(m_devComm.m_abyPacket, 10, m_TemplateData, w_nBufOffset, m_devComm.GetDataLen() - 4);
                            w_nBufOffset = w_nBufOffset + (m_devComm.GetDataLen() - 4);
                            if (w_nBufOffset == w_nLen) {
                                break;
                            }
                        }
                    } else {
                        w_blRet = false;
                        break;
                    }
                }
            }
        }

        if (w_blRet == false) {
            return 2;
        } else {
            m_nTemplateSize = w_nLen;
            displayResult((short) DevComm.CMD_READ_TEMPLATE_CODE);
        }

        return 0;
    }

}
