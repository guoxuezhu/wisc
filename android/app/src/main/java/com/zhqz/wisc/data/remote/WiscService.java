package com.zhqz.wisc.data.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhqz.wisc.BuildConfig;
import com.zhqz.wisc.canteenui.todaymenu.TodayMenu;
import com.zhqz.wisc.data.Datas;
import com.zhqz.wisc.data.HttpResult;
import com.zhqz.wisc.data.model.BindCanTing;
import com.zhqz.wisc.data.model.CanTing;
import com.zhqz.wisc.data.model.Canteen;
import com.zhqz.wisc.data.model.DakaInfo;
import com.zhqz.wisc.data.model.DakaRequest;
import com.zhqz.wisc.data.model.AttendanceDataTwo;
import com.zhqz.wisc.data.model.AttendanceDetails;
import com.zhqz.wisc.data.model.CabinetInfo;
import com.zhqz.wisc.data.model.CabinetPerson;
import com.zhqz.wisc.data.model.Classrooms;
import com.zhqz.wisc.data.model.Course;
import com.zhqz.wisc.data.model.Courses;
import com.zhqz.wisc.data.model.EnterStudent;
import com.zhqz.wisc.data.model.FingerEnterStudent;
import com.zhqz.wisc.data.model.FingerTeacherUsers;
import com.zhqz.wisc.data.model.FingerUsers;
import com.zhqz.wisc.data.model.Leave;
import com.zhqz.wisc.data.model.Meals;
import com.zhqz.wisc.data.model.NoticeList;
import com.zhqz.wisc.data.model.Room;
import com.zhqz.wisc.data.model.StudentLeaveReason;
import com.zhqz.wisc.data.model.StudentLeaveSpainner;
import com.zhqz.wisc.data.model.TeacherIntroduction;
import com.zhqz.wisc.data.model.ThisClassStudents;
import com.zhqz.wisc.data.model.VersionCheck;
import com.zhqz.wisc.data.model.ZuoPin;
import com.zhqz.wisc.libraryui.main.LibraryModel;
import com.zhqz.wisc.libraryui.main.LibraryReshuModel;
import com.zhqz.wisc.ui.bind.Person;
import com.zhqz.wisc.ui.scene.SecenList;
import com.zhqz.wisc.utils.Coder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface WiscService {

    /**
     * www.lark.ink
     * 192.168.31.180
     * smart.rovemaker.com
     * 192.168.10.105
     * 192.168.2.173
     */
    final String URLIP = "192.168.31.180";
    final String ENDPOINT = "http://" + URLIP + "/api/";

    final String TIME_STAMP_HEADER = "timestamp";
    final String CLIENT_ID_HEADER = "clientId";
    final String CLIENT_ID_KEY = "client_id";
    final String CLIENT_ID = "SWQxcGJxM2RrRkoyOTAxNGU";
    final String APPKEY_TOKEN = "S2V5MzY3MDg5YTBkMWRiNDlmZmI0NzY4Yzg3MzdjMzlkOWU";
    final String ACCEPT_HEADER = "Accept: application/json";
    final String UA_HEADER = "User-Agent: Retrofit-ecampus-App";
    final String SIGNSEAT_HEADER = "signSeat: HEADER";
    final String SIGN_HEADER = "sign";


    final String CONTENT_CHECKSUM_HEADER = "Content-Checksum";
    final String SKIP_SIGN_HEADER_NAME = "X-SkipSign";
    final String SKIP_SIGN_HEADER = "X-SkipSign: true";
    static final String JSON_CONTENT_TYPE = "application/json";


    /*班主任获取本班学生录入信息*/
    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/getAllStudent")//班主任获取本班学生录入信息
    Observable<HttpResult<List<EnterStudent>>> getStudents(@Query("cardNumber") String cardNumber,
                                                           @Query("schoolId") int schoolId);


    /*班主任获取本班学生录入信息*/
    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/fingerprint/getUser")
    Observable<HttpResult<List<FingerEnterStudent>>> getfingerStudents(@Query("cardNumber") String cardNumber,
                                                                       @Query("schoolId") int schoolId);


    /*上传图片*/
    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @Multipart
    @POST("cb/save/face")
    Observable<HttpResult> updataImage(@Query("stId") int stId, @Query("psType") String psType,@Query("status") int status,
                                       @Query("schoolId") int schoolId,
                                       @PartMap Map<String, RequestBody> params);

    /*打卡签到*/
    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @Multipart
    @POST("cb/faceidentify/face")
    Observable<HttpResult> FaceIdentify(@Query("courseRoomTimeId") int courseRoomTimeId,
                                        @Query("schoolId") int schoolId,
                                        @PartMap Map<String, RequestBody> params);
    /*打人脸检测*/
    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @Multipart
    @POST("cb/face/detectface")
    Observable<HttpResult> detectface(@PartMap Map<String, RequestBody> params);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/classBrand/init/")//绑定教室
    Observable<HttpResult<Room>> bindRoom(@Body JsonRequest.BindRoomRequest bindRoomRequest);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/library/classBrand/init ")//绑定教室
    Observable<HttpResult<Room>> bindlibrary(@Body JsonRequest.BindRoomRequest bindRoomRequest);


    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/attendance/sign")//刷卡签到(学生/老师)一起提交
    Observable<HttpResult<List<AttendanceDataTwo>>> uploadAttendence(@Body JsonRequest.RollCallSubmitRequest req);


    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/moJi/STAS/")//天气:
    Observable<HttpResult> getTianqi();


    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/moJi/SAQI/")//天气　空气质量:
    Observable<HttpResult> getTianqiAQI();

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/room/syllabus/")//这天本教室课程列表:
    Observable<HttpResult<List<Course>>> courseList(@Query("deviceId") String deviceId,
                                                    @Query("date") String date, @Query("schoolId") int schoolId);
    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/classBrand/room/list")//教室列表   startNumber开始行号 eachPageNumber 每页行数
    Observable<HttpResult<List<Classrooms>>> classrooms(@Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/library/menu")//图书馆列表   startNumber开始行号 eachPageNumber 每页行数
    Observable<HttpResult<List<Classrooms>>> librarymenu(@Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/librarybookRecommend/list")//推荐书籍   startNumber开始行号 eachPageNumber 每页行数
    Observable<HttpResult<List<LibraryReshuModel>>> LibraryReshuList(@Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("school/menu")//学校列表
    Observable<HttpResult<List<Person>>> schoolList();

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/syllabus/myList/")//查询个人课程表:
    Observable<HttpResult<Courses>> myCourseList(@Query("cardNumber") String cardNumber, @Query("date") String date,
                                                 @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/attendance/list")//这天本教室考勤:
    Observable<HttpResult<AttendanceDetails>> studentList(@Query("courseId") int courseId,
                                                          @Query("periodId") int periodId,
                                                          @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/classBadge/badge")//班徽:
    Observable<HttpResult> banhui(@Query("deviceId") String deviceId,@Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/library/classBadge/badge")//班徽:
    Observable<HttpResult> banhuilibrary(@Query("deviceId") String deviceId,@Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/library/classBrand/getLibrary")//图书馆简介
    Observable<HttpResult<LibraryModel>> getLibraryIntroduction(@Query("deviceId") String deviceId, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/material/list/")//课程作品:
    Observable<HttpResult<List<ZuoPin>>> zuopin(@Query("courseId") int courseId,
                                                @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/schoolStyle/app/list")//没课作品:
    Observable<HttpResult<List<ZuoPin>>> meikezuopin(@Query("deviceId") String deviceId,@Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/notifi/app/List/")//通知
    Observable<HttpResult<List<NoticeList>>> noticeList(@Query("uuid") String deviceId, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/library/notifi/app/List/")//通知
    Observable<HttpResult<List<NoticeList>>> noticeListlibrary(@Query("uuid") String deviceId, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/attendance/byTeacher/")//本班学生
    Observable<HttpResult<List<ThisClassStudents>>> benbanstudent(@Query("cardNumber") String cardNumber, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/transaction/byCardNumber")//余额
    Observable<HttpResult<Meals>> getMeals(@Query("cardNumber") String cardNumber, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/leave/get/list")//请假
    Observable<HttpResult<Leave>> getLeave(@Query("number") String cardNumber, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/leave/get/ask")//提交请假
    Observable<HttpResult<Boolean>> getStudentLeaveSubmit(@Query("stid") int stid,@Query("psType") String psType,
                                                          @Query("startDate") String startDate,@Query("endDate") String endDate,
                                                          @Query("beginNum") int beginNum,@Query("endNum") int endNum,
                                                          @Query("beginLessonNum") String beginLessonNum,@Query("endLessonNum") String endLessonNum,
                                                          @Query("typeId") int typeId, @Query("leaveName") String leaveName, @Query("reason") String reason,@Query("schoolId") int schoolId);


    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/leave/get/time") // 节课
    Observable<HttpResult<List<StudentLeaveSpainner>>> getStudentLeaveSpainner(@Query("time") String time, @Query("userId") int userId, @Query("schoolId") int schoolId);


    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/leave/type/list")
    Observable<HttpResult<List<StudentLeaveReason>>> getStudentLeaveReasonSpainner(@Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/classBrand/")//查看绑定教室
    Observable<HttpResult<Room>> getbindRoom(@Query("uuid") String deviceId, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/library/classBrand")//查看绑定教室
    Observable<HttpResult<Room>> getbindlibrary(@Query("uuid") String deviceId, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/version/check/")//通知:
    Observable<HttpResult<VersionCheck>> versionCheck(@Query("code") String code , @Query("name") String name, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/course/courseProfile/")//课程简介
    Observable<HttpResult<String>> courseIntroduction(@Query("courseId") String courseId,
                                                      @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/course/teacherProfile/")//老师简介
    Observable<HttpResult<TeacherIntroduction>> teacherIntroduction(@Query("courseId") String courseId,
                                                                    @Query("schoolId") int schoolId);


    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/logout")//调出设置界面
    Observable<HttpResult> getSeting(@Query("cardNumber") String cardNumber, @Query("deviceId") int deviceId,
                                     @Query("schoolId") int schoolId);


    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/lockerDetail/getOne")
    Observable<HttpResult<CabinetPerson>> getCabinetPerson(@Query("scardNum") String scardNum, @Query("page") String page,
                                                           @Query("pageSize") String pageSize, @Query("schoolId") int schoolId);


    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/lockerDetail/class/list")
    Observable<HttpResult<Datas<List<CabinetInfo>>>> getCabinetClass(@Query("scardNum") String scardNum, @Query("page") String page,
                                                  @Query("pageSize") String pageSize, @Query("schoolId") int schoolId);


    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/teacherAttendance/add")
    Observable<HttpResult<String>> teacherKaoqin(@Query("cardNumber") String cardNumber, @Query("schoolId") int schoolId,
                                                 @Query("stid") int stid, @Query("psType") int psType);


    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/loceer/force/add")
    Observable<HttpResult> getUnlockClicks(@Query("scardNum") String scardNum, @Query("number") int number,
                                           @Query("reason") String reason, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/lockerDetail/class/lock/useOrNotUse")
    Observable<HttpResult> getlocking(@Query("schoolId") int schoolId, @Query("status") int status,
                                      @Query("id") int id,@Query("scardNumber") String scardNumber);

    /*
    *
    * 场景
    * */
    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/spectacle/list")//场景列表   startNumber开始行号 eachPageNumber 每页行数
    Observable<HttpResult<List<SecenList>>> GetSecen(@Query("schoolId") int schoolId);


    /*
    * 食堂接口
    * */
    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/fingerprint/save")
    Observable<HttpResult> sendTemplateData(@Query("schoolId") int schoolId, @Query("psType") int psType,
                                            @Query("stid") int stid, @Query("context") String context);
    @GET("canTingPost/getCanteen/")//查询餐厅的接口:
    Observable<HttpResult<Canteen>> getCanteen(@Query("canteenPostId") int canteenPostId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("transaction/save/")//打卡
    Observable<HttpResult<DakaRequest>> sendTransactionDetail(@Body JsonRequest.SendSubmitRequest sendSubmitRequest);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("consumer/list/")//同步数据:
    Observable<HttpResult<List<DakaInfo>>> syndata(@Query("schoolId") int schoolId);

//    @Headers({
//            SIGNSEAT_HEADER,
//            ACCEPT_HEADER,
//            UA_HEADER
//    })
//    @POST("cb/logout")//调出设置界面
//    Observable<HttpResult> getSeting(@Query("cardNumber") String cardNumber, @Query("deviceId") int deviceId,
//                                     @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("canTing/listApp/")//餐厅列表:
    Observable<HttpResult<List<CanTing>>> getCantings(@Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("canTingPost/save/")//绑定餐厅
    Observable<HttpResult<BindCanTing>> bindCanTing(@Body JsonRequest.DeviceRequest deviceRequest);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cd/canteenMode/find")//食堂模式界面
    Observable<HttpResult> getMode(@Query("schoolId") int schoolId);


    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/billOfFare/list")//今日菜单
    Observable<HttpResult<List<TodayMenu>>> getMenu(@Query("number") String number, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @GET("cb/billOfFareToday/list")//今日菜单
    Observable<HttpResult<List<TodayMenu>>> getTodayMenu(@Query("canteentId") int canteentId, @Query("schoolId") int schoolId);

    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/billOfFare/save")//今日菜单提交
    Observable<HttpResult> submit(@Body JsonRequest.SendSubmitMenu sendSubmitMenu);
    /*
    * 餐厅接口结束
    * */
    //这天本教室fingerprint
    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/fingerprint/list/dayAndRoom")
    Observable<HttpResult<List<FingerUsers>>> getFingerUserList(@Query("roomId") int roomId,
                                                                @Query("day") String day, @Query("schoolId") int schoolId);

    //教职工指纹数据
    @Headers({
            SIGNSEAT_HEADER,
            ACCEPT_HEADER,
            UA_HEADER
    })
    @POST("cb/teacherFingerprint/list")
    Observable<HttpResult<List<FingerTeacherUsers>>> getFingerTeacherUserList( @Query("schoolId") int schoolId);

    /********
     * Factory class that sets up a new ribot services
     *******/
    class Factory {
        static final int CONNECTION_TIMEOUT_MS = 60 * 1000 * 2;
        static final int SOCKET_READ_TIMEOUT_MS = 60 * 1000 * 2;

        public static void setDeviceId(int deviceId) {
            WiscService.Factory.HeaderSignagureInterceptor.setDeviceId(deviceId);
        }

        public static WiscService makeRibotService(Context context) {
                    OkHttpClient.Builder builer = new OkHttpClient.Builder()
                    .connectTimeout(CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                    .readTimeout(SOCKET_READ_TIMEOUT_MS, TimeUnit.MILLISECONDS);

            // add auth header
            builer.interceptors().add(new HeaderSignagureInterceptor());

            // add log interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                    : HttpLoggingInterceptor.Level.NONE);
            builer.interceptors().add(logging);

            OkHttpClient okHttpClient = builer.build();


            Gson gson = new GsonBuilder()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WiscService.ENDPOINT)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(WiscService.class);
        }

        static final class HeaderSignagureInterceptor implements Interceptor {
            static int deviceId;

            public static void setDeviceId(int dvId) {
                deviceId = dvId;
            }

            private static String generateQueryString(Map<String, String> params) {
                if (params == null || params.isEmpty()) {
                    return null;
                }
                StringBuffer buffer = new StringBuffer();
                List<String> keys = new ArrayList<String>(params.keySet());
                Collections.sort(keys);
                for (String key : keys) {
                    String value = params.get(key);
                    if (null == value) {
                        value = "";
                    }
                    value = getUtf8EscapedString(value);
                    buffer.append("&").append(key).append("=").append(value);
                }
                return buffer.substring(1);
            }

            public static String getUtf8EscapedString(String input) {
                String result = null;
                try {
                    result = URLEncoder.encode(input, "utf8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
                return result;
            }

            private static String mapToCheckSum(Map<String, String> querys) {
                String queryString = generateQueryString(querys) + APPKEY_TOKEN;
                try {
                    return Coder.hashMD5(queryString.getBytes("utf8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }

            private static String bodyCheckSum(final Request request) {
                String returnVal = "";
                final Buffer buffer = new Buffer();
                try {
                    request.body().writeTo(buffer);
                    InputStream input = buffer.inputStream();
                    MessageDigest md5Hash = MessageDigest.getInstance("MD5");
                    int numRead = 0;
                    byte[] buf = new byte[1024];
                    while (numRead != -1) {
                        numRead = input.read(buf);
                        if (numRead > 0) {
                            md5Hash.update(buf, 0, numRead);
                        }
                    }
                    input.close();

                    byte[] md5Bytes = md5Hash.digest();
                    for (int i = 0; i < md5Bytes.length; i++) {
                        returnVal += Integer.toString((md5Bytes[i] & 0xff) + 0x100, 16).substring(1);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                return returnVal;
            }

            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder newBuilder = chain.request().newBuilder();

                final Request req = chain.request();
                final String timeStamp = String.valueOf(System.currentTimeMillis() / 1000l);

                Map<String, String> queryParameters = new HashMap<>();
                queryParameters.put(TIME_STAMP_HEADER, timeStamp);
                newBuilder.addHeader(TIME_STAMP_HEADER, timeStamp);

                queryParameters.put(CLIENT_ID_KEY, CLIENT_ID);
                newBuilder.addHeader(CLIENT_ID_HEADER, CLIENT_ID);

                if (null == req.header(SKIP_SIGN_HEADER_NAME)) {
                    final HttpUrl httpUrl = req.url();
                    // put query parameter in map
                    final int queryParameterCounts = httpUrl.querySize();
                    for (int i = 0; i < queryParameterCounts; i++) {
                        final String key = httpUrl.queryParameterName(i);
                        final String value = httpUrl.queryParameterValue(i);
                        queryParameters.put(key, value);
                    }

                    // add Content-CheckSum header
//                    if (req.body() != null && req.body().contentLength() > 0 && req.body().contentType().toString().startsWith(JSON_CONTENT_TYPE)) {
//                        String checkSum = bodyCheckSum(req);
//                        newBuilder.addHeader(CONTENT_CHECKSUM_HEADER, checkSum);
//                        queryParameters.put(CONTENT_CHECKSUM_HEADER, checkSum);
//                    }
                    // calculate X-Sign
                    newBuilder.addHeader(SIGN_HEADER, mapToCheckSum(queryParameters));
                } else {
                    newBuilder.removeHeader(SKIP_SIGN_HEADER_NAME);
                }

                return chain.proceed(newBuilder.build());
            }


        }


    }

}
