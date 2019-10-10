package com.zhqz.wisc.ui.fingerprint.fingerprintUtils;

public interface IUsbConnState {
    void onUsbConnected();

	void onUsbPermissionDenied();

	void onDeviceNotFound();
}
