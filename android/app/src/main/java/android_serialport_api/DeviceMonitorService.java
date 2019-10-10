package android_serialport_api;


import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Observable;

/**
 * Created by qingchengl on 17-7-7.
 */

public class DeviceMonitorService {
    private static final String DEVICE_PATH = "/dev/ttyS2";
    private static final int BAUDRATE = 9600;
    private static volatile boolean run = false;
    private static FlowableEmitter<String> myemitter;

    /* public static Flowable<String> flow() {
         return Flowable.fromCallable(()-> new SerialPort(new File(DEVICE_PATH), BAUDRATE, 0).getInputStream())
                 .flatMap(fileInputStream -> Flowable.create((emitter) -> {
                             final InputStream in = fileInputStream;
                             run = true;
                             new Thread() {
                                 @Override
                                 public void run() {
                                     int len;
                                     byte[] buffer = new byte[64];
                                     int i = 0;
                                     try {
                                         while (run && (len = in.read(buffer, 0, 64)) > 0) {
                                             if (len > 0) {
                                                 emitter.onNext(new String(buffer, 0, len));
                                                 sleep(500);
                                             } else {
                                             }
                                         }
                                     } catch (IOException e) {
                                         emitter.onError(e);
                                     } catch (InterruptedException e) {
                                         emitter.onError(e);
                                     } finally {
                                         closeQuietly(in);
                                         run = false;
                                         emitter.onComplete();
                                     }
                                 }
                             }.start();
                         }
                 , BackpressureStrategy.BUFFER));
     }*/
    public static Flowable<String> flow1() {
        return Flowable.fromCallable(() -> new SerialPort(new File(DEVICE_PATH), BAUDRATE, 0).getInputStream())
                .flatMap(fileInputStream -> Flowable.create((emitter) -> {
                            final InputStream in = fileInputStream;

                            run = true;
                            myemitter = emitter;
                            new Thread() {
                                @Override
                                public void run() {
                                    int len;
                                    byte[] buffer = new byte[64];
                                    int i = 0;
                                    try {
                                        while (run && (len = in.read(buffer, 0, 64)) > 0) {
                                            if (len > 0) {
                                                myemitter.onNext(new String(buffer, 0, len));
                                                sleep(500);
                                            } else {
                                            }
                                        }
                                    } catch (IOException e) {
                                        myemitter.onError(e);
                                    } catch (InterruptedException e) {
                                        myemitter.onError(e);
                                    } finally {
                                        closeQuietly(in);
                                        run = false;
                                        myemitter.onComplete();
                                    }
                                }
                            }.start();
                        }
                        , BackpressureStrategy.BUFFER));
    }

    public static Observable<Integer> create() {
        return Observable.fromCallable(() -> new FileInputStream(new File("/dev/zero")))
                .flatMap((input) -> Observable.create((emitter) ->
                        {
                            final InputStream in = input;
                            run = true;
                            new Thread() {
                                @Override
                                public void run() {
                                    int len;
                                    byte[] buffer = new byte[64];
                                    int i = 0;
                                    try {
                                        while (run && (len = in.read(buffer, 0, 1)) > 0) {
                                            if (len > 0) {
                                                emitter.onNext(i++);
                                                sleep(500);
                                            } else {
                                            }
                                        }

                                    } catch (IOException e) {
                                        emitter.onError(e);
                                    } catch (InterruptedException e) {
                                        emitter.onError(e);
                                    } finally {
                                        closeQuietly(in);
                                        run = false;
                                        emitter.onComplete();
                                    }
                                }
                            }.start();
                        }
                ));
    }


    /**
     * Close a stream without needing to check if the stream is null or if an
     * exception is thrown.
     *
     * @param stream Stream to close.
     */
    public static void closeQuietly(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // Ignore.
        }
    }

    public static void stop() {
        run = false;
        if (myemitter != null) {
            myemitter.onComplete();
        }

    }
}
