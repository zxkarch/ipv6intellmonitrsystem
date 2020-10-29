package com.example.saierclient.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings("all")
public class MyHttpClientUtils {
    private static Logger logger = LoggerFactory.getLogger(MyHttpClientUtils.class);
    private static String JSON = "application/json; charset=utf-8";
    //private static String MEDIA_TYPE_JSON= "application/x-www-form-urlencoded; charset=utf-8";
    /**使用volatile双重校验锁**/
    private static volatile Semaphore semaphore = null;
    private static volatile OkHttpClient okHttpClient = null;

    /**建立单例模式*/
    public static  Semaphore getSemaphoreInstance(){
        //只能0个线程同时访问
        synchronized (MyHttpClientUtils.class) {
            if (semaphore == null) {
                semaphore = new Semaphore(0);
            }
        }
        return semaphore;
    }


    /**建立单例模式*/
    public static  OkHttpClient getInstance(){
        synchronized (MyHttpClientUtils.class) {
            if (okHttpClient == null) {
                //这里是以毫秒为单位，1000 毫秒 = 1秒
                okHttpClient = new OkHttpClient().newBuilder()
                        .connectTimeout(5000, TimeUnit.SECONDS)// 设置超时时间
                        .readTimeout(5000, TimeUnit.SECONDS)// 设置读取超时时间
                        .writeTimeout(5000, TimeUnit.SECONDS)// 设置写入超时时间
                        .build();
            }
        }
        return okHttpClient;
    }



    /**
     * @Description 求在子线程发起网络请求
     * @param url 请求url地址
     * @param params  请求body参数
     * @param okHttpClientCall 回调接口
     * @throws IOException 参数
     * @return void 返回类型
     */
    public  static void createAsycHttpGet(String url,Map<String,Object> params,String contentType,final IOkHttpClientCallBack okHttpClientCall)  {
        // 创建请求对象
        Call call = createCall(url, params);

        //发起异步的请求
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response!=null && response.isSuccessful()) {
                    String string = response.body().string();
                    okHttpClientCall.onSuccessful(string);
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                String errorLog = getCurrentClassName()+"#createHttpGet,请求异常，异常信息为:"+e.getMessage();
                logger.error("@see "+errorLog);
            }
        });
    }

    /**
     * @Description 求在子线程发起网络请求
     * @param url 请求url地址
     * @param params  请求body参数
     * @param okHttpClientCall 回调接口
     * @throws IOException 参数
     * @return void 返回类型
     */
    public static String createAsycHttpGet(String url,Map<String,Object> params,String contentType)  {
        final StringBuilder buffer = new StringBuilder("");
        try {
            // 创建请求对象
            Call call = createCall(url, params);

            //发起异步的请求
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if (response!=null && response.isSuccessful()) {
                        String string = response.body().string();
                        buffer.append(string);
                        getSemaphoreInstance().release();
                    }
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    String errorLog = getCurrentClassName()+"#createHttpGet,请求异常，异常信息为:"+e.getMessage();
                    logger.error("@see "+errorLog);
                }
            });
            getSemaphoreInstance().acquire();//获取许可
            return buffer.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * @Description 求在子线程发起网络请求
     * @param url 请求url地址
     * @param params  请求body参数
     * @param okHttpClientCall 回调接口
     * @throws IOException 参数
     * @return void 返回类型
     */
    public static String createHttpGet(String url,Map<String,Object> params,String contentType)  {
        try {
            // 创建请求对象
            Call call = createCall(url, params);

            Response response = call.execute();
//            if (response!=null && response.isSuccessful() && ObjectUtil.isNotEmpty(response.body())) {
            if (response!=null && response.isSuccessful()) {
                //Collection<String> readLines = IOUtil.readLines(byteStream);
                //System.out.println(readLines);
                return convertToString(response.body().byteStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * @Description convertStreamToString
     * @param is
     * @return 参数
     * @return String 返回类型
     * @throws
     */
    public static String convertToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder buffer = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\r");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

    /**
     * @Description convertStr
     * @param is
     * @return
     * @throws IOException 参数
     * @return String 返回类型
     * @throws
     */
    public static String convertStr(InputStream is) throws IOException {
        OutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }
    /**
     * @Description 创建异步表单Body参数的post请求处理
     * @param url   请求链接
     * @param params 请求表单body参数
     * @param okHttpClientCall 参数  回调接口
     * @return void 返回类型
     */
    public static void createPostByAsynWithForm(String url,Map<String,Object> params,final IOkHttpClientCallBack okHttpClientCall)  {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key).toString());
        }
        RequestBody formBody = builder.build();
        logger.info("@see"+getCurrentClassName()+"请求url"+url+",请求参数:"+formBody);

        Request request = new Request.Builder().url(url).post(formBody).build();
        // 创建请求对象
        Call call = getInstance().newCall(request);
        //发起异步的请求
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response!=null && response.isSuccessful()) {
                    String string = response.body().string();
                    okHttpClientCall.onSuccessful(string);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String errorLog = getCurrentClassName()+"#createPostByAsynWithForm,请求异常，异常信息为:"+e.getMessage();
                //okHttpClientCall.onFailure(errorLog);
                logger.error("@see "+errorLog);
            }
        });
    }


    /**
     * @Description 创建异步表单Body参数的post请求处理
     * @param url   请求链接
     * @param params 请求表单body参数
     * @param okHttpClientCall 参数  回调接口
     * @return
     * @return void 返回类型
     */
    public static String createPostByAsynWithForm(String url,Map<String,Object> params)  {
        final StringBuilder buffer = new StringBuilder("");
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : params.keySet()) {
                builder.add(key, params.get(key).toString());
            }
            RequestBody formBody = builder.build();
            logger.info("@see"+getCurrentClassName()+"请求url"+url+",请求参数:"+formBody);

            Request request = new Request.Builder().url(url).post(formBody).build();
            // 创建请求对象
            Call call = getInstance().newCall(request);
            //发起异步的请求
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if (response!=null && response.isSuccessful()) {
                        String string = response.body().string();
                        buffer.append(string);
                        getSemaphoreInstance().release();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    String errorLog = getCurrentClassName()+"#createPostByAsynWithForm,请求异常，异常信息为:"+e.getMessage();
                    logger.error("@see "+errorLog);
                }
            });
            getSemaphoreInstance().acquire();
            return buffer.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }



    /**
     * okHttp createCall
     * @param url  接口地址
     * @param params   请求参数
     */
    private static Call createCall(String url, Map<String, Object> params) {
        //补全请求地址,【%s?%s或者%s/%s的使用】第一个%s代表第一个参数，第二个?代表是请求地址的?后面%s代表是组装戳参数,如：
        //http://localhost:8080/api/test.do?userId=1212&deviceInfo=PC
        String requestUrl = String.format("%s?%s", url, concatParams(params).toString());
        //创建一个请求
        Request request = new Request.Builder().url(requestUrl).build();
        return  getInstance().newCall(request);
    }


    /**
     * @param url
     * @param reqMap
     * @param contentType
     * @return 参数
     * @return String 返回类型
     */
    public static String createHttpPost(String url,Map<String,Object> reqMap,String contentType) {
        try {
            RequestBody body = createRequestBody(contentType, reqMap);
            //logger.info("@see"+getCurrentClassName()+"#createHttpPost,请求url"+url+",请求参数:"+body.toString());
            final Request request = new Request.Builder().url(url).post(body).build();
            // 创建请求对象
            final Call call = getInstance().newCall(request);
            Response response = call.execute();
            if (response!=null && response.isSuccessful()) {
                return convertStr(response.body().byteStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * @param url 参数
     * @return void 返回类型
     */
    public static String createAsycHttpPost(String url,Map<String,Object> reqMap,String contentType) {
        final StringBuilder buffer = new StringBuilder("");
        try {
            final RequestBody body = createRequestBody(contentType, reqMap);

            //logger.info("@see"+getCurrentClassName()+"#createHttpPost,请求url"+url+",请求参数:"+body.toString());
            final Request request = new Request.Builder().url(url).post(body).build();
            // 创建请求对象
            final Call call = getInstance().newCall(request);

            // 发起请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String errorLog = getCurrentClassName()+"#createHttpPost,请求异常，异常信息为:"+e.getMessage();
                    logger.error("@see "+errorLog);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response!=null && response.isSuccessful()) {
//                        if(ObjectUtil.isNotEmpty(response.body())){
                            String string = response.body().string();
                            buffer.append(string);
                            getSemaphoreInstance().release();//释放
//                        }
                    }
                }
            });
            getSemaphoreInstance().acquire();//接受
            return  buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * @param url 参数
     * @return void 返回类型
     */
    public static void createAsycHttpPost(String url,Map<String,Object> reqMap,String contentType,final IOkHttpClientCallBack okHttpClientCall) {
        final RequestBody body = createRequestBody(contentType, reqMap);

        //logger.info("@see"+getCurrentClassName()+"#createHttpPost,请求url"+url+",请求参数:"+body.toString());
        final Request request = new Request.Builder().url(url).post(body).build();
        // 创建请求对象
        final Call call = getInstance().newCall(request);

        // 发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String errorLog = getCurrentClassName()+"#createHttpPost,请求异常，异常信息为:"+e.getMessage();
                logger.error("@see "+errorLog);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response!=null && response.isSuccessful()) {
//                    if(ObjectUtil.isNotEmpty(response.body())){
                        String retBody = response.body().string();
                        okHttpClientCall.onSuccessful(retBody);
//                    }
                }
            }
        });
    }

    /**
     *
     * @param contentType  请求头header属性
     * @param params       请求参数
     * @return 参数
     * @return RequestBody 返回类型
     */
    private static RequestBody createRequestBody(String contentType,Map<String,Object> params){
        MediaType type = MediaType.parse(contentType);
        String paramStrs = concatParams(params).toString();
        return RequestBody.create(type, paramStrs);
    }



    /**
     * @param params
     * @return 参数
     * @return StringBuilder 返回类型
     */
    private static StringBuilder concatParams(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder("");//请求参数为空给一个默认值空字符串
        //判断是空
//        if (ObjectUtil.isNotEmptyMap(params)) {
            int i = 0;
            for (String key : params.keySet()) {
                Object value = params.get(key);
                builder.append(i != 0 ? "&" : "");
                builder.append(key + "=" + value);
                i++;
            }
//        }
        return builder;
    }


    /**
     * @param url
     * @param reqMap
     * @param contentType 参数
     * @return void 返回类型
     * @throws
     */
    public static String createHttpsPost(String url,Map<String,Object> reqMap,String contentType) {
        final StringBuilder buffer = new StringBuilder("");
        /**忽略SSL协议证书*/

        OkHttpClient build = new OkHttpClient.Builder().sslSocketFactory(createSSLSocketFactory()).hostnameVerifier(new TrustAllHostnameVerifier()).build();

        final RequestBody body = createRequestBody(contentType, reqMap);

        final Request request  = new Request.Builder().url(url).post(body).build();
        final Call    call     = build.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String res = response.body().string();
                buffer.append(res);
                getSemaphoreInstance().release();//释放
            }
        });

        try {
            getSemaphoreInstance().acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return  buffer.toString();
    }

    /**
     * @param url
     * @param json
     * @param contentType 参数
     * @return void 返回类型
     * @throws
     */
    public static String createHttpsPostByjson(String url,String postjson,String contentType) {
        final StringBuilder buffer = new StringBuilder("");
        /**忽略SSL协议证书*/

        OkHttpClient build = new OkHttpClient.Builder().sslSocketFactory(createSSLSocketFactory()).hostnameVerifier(new TrustAllHostnameVerifier()).build();
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        final RequestBody requestBody = RequestBody.create(mediaType, postjson);;

        final Request request  = new Request.Builder().url(url).post(requestBody).build();
        final Call    call     = build.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String res = response.body().string();
                buffer.append(res);
                getSemaphoreInstance().release();//释放
            }
        });

        try {
            getSemaphoreInstance().acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return  buffer.toString();
    }


    /**
     * @Author    liangjl
     * @Copyright (c) All Rights Reserved, 2018.
     */
    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * @Description    验证所有主机
     * @ClassName  TrustAllHostnameVerifier
     * @Copyright (c) All Rights Reserved, 2018.
     */
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * @Description createSSLSocketFactory
     * @return 参数
     * @return SSLSocketFactory 返回类型
     * @throws
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[] { new TrustAllCerts() }, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    /**
     *
     * @Description 获取当前类名包含有包名路径
     * @param @return 参数
     * @return String 返回类型
     * @throws
     */
    public static String getCurrentClassName(){
        return MyHttpClientUtils.class.getName();
    }


    /**
     * @Description    定义一个回调成功的接口.
     * @ClassName  IOkHttpClientCallBack
     * @Copyright (c) All Rights Reserved, 2018.
     */
    public interface IOkHttpClientCallBack {

        void onSuccessful(String retBody);

    }
}
