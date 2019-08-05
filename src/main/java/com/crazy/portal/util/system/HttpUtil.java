package com.crazy.portal.util.system;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.Map.Entry;

/**
 * http get和post请求
 *
 * @Title:
 * @Description:
 * @Author:xiaozhou.zhou
 * @Since:2015年12月17日
 * @Copyright:Copyright (c) 2015
 * @ModifyDate:
 * @Version:1.1.0
 */
public class HttpUtil{

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final String CHARSET_UTF_8 = "UTF-8";

    public static final String PROTOCOL="TLS";

    public static final String CONTENT_TYPE_TEXTHTML = "text/xml; charset=utf-8";

    static RequestConfig defaultRequestConfig = null;

    private static final int defautTimeoutSecond = 20;
    static{
        defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
    }



    private static RequestConfig makeLocalRequestConfig(int second){
        RequestConfig requestConfig = null;
        if (second > 0){
            requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(second * 1000).setConnectTimeout(second * 1000).setConnectionRequestTimeout(second * 1000).build();
        }else{
            requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(defautTimeoutSecond * 1000).setConnectTimeout(defautTimeoutSecond * 1000).setConnectionRequestTimeout(defautTimeoutSecond * 1000).build();
        }
        return requestConfig;

    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String doGet(String url,String param){
        String result = "";
        BufferedReader in = null;
        try{
            String urlNameString = null == param ? url : url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null){
                result += line;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally{
            try{
                if (in != null){
                    in.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * get请求map参数内容
     *
     * @param url
     * @param params
     * @return
     * @Description:
     */
    public static String getMap(String url,Map<String, String> params){
        // http客户端创建
        HttpClientBuilder httpClient = HttpClientBuilder.create();
        // 可关闭httpclient获取
        CloseableHttpClient closeableHttpClient = httpClient.build();
        String body = null;
        // 装载post请求数据NameValuePair类型
        HttpGet get = getForm(url, params);
        // post请求http
        body = invoke(closeableHttpClient, get);
        try{
            // 关闭http客户端
            closeableHttpClient.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return body;
    }

    /**
     * 执行http请求
     *
     * @param httpclient
     * @param httpost
     * @return
     * @Description:
     */
    private static String invoke(HttpClient httpclient,HttpUriRequest httpost){
        HttpResponse response = sendRequest(httpclient, httpost);
        String body = paseResponse(response);
        return body;
    }

    /**
     * 获取http响应内容
     *
     * @param response
     * @return
     * @Description:
     */
    private static String paseResponse(HttpResponse response){
        HttpEntity entity = response.getEntity();
        String body = null;
        try{
            body = EntityUtils.toString(entity, "UTF-8");
        }catch (ParseException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return body;
    }

    private static HttpResponse sendRequest(HttpClient httpclient,HttpUriRequest httpost){
        HttpResponse response = null;
        try{
            response = httpclient.execute(httpost);
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return response;
    }

    /**
     *
     * @param url
     * @param params
     * @return
     * @Description:
     */
    private static HttpPost postForm(String url,Map<String, String> params){

        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet){
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        try{
            // log.info("set utf-8 form entity to httppost");
            httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return httpost;
    }

    /**
     *
     * @param url
     * @param params
     * @return
     * @Description:
     */
    private static HttpGet getForm(String url,Map<String, String> params){
        HttpGet get = new HttpGet(url);
        Iterator<Entry<String, String>> ite = params.entrySet().iterator();
        while (ite.hasNext()){
            Entry<String, String> entry = ite.next();
            get.setHeader(entry.getKey(), entry.getValue());
        }
        return get;
    }

    /**
     * post请求,现在圆通使用该方法，无法使用上面的post请求方式，原因未知
     */
    public static String post(Object param,String url){
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try{
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null){
                result += line;
            }
        }catch (Exception e){
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally{
            try{
                if (out != null){
                    out.close();
                }
                if (in != null){
                    in.close();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * post请求,LF使用,设置超时时间30S
     */
    public static String postLF(Object param,String url, int timeout){
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try{
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 设置超时时间30S
            conn.setConnectTimeout(timeout);// 建立连接的超时时间
            conn.setReadTimeout(timeout);// 传递数据的超时时间
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null){
                result += line;
            }
        }catch (Exception e){
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally{
            try{
                if (out != null){
                    out.close();
                }
                if (in != null){
                    in.close();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }




    /**
     * http协议接口请求,现用淘宝等
     *
     * @param url
     * @param body
     * @return
     * @throws Exception
     * @Description:
     * @deprecated 请多使用send(String url,Map<String, String> headMap,String
     *             body)方法，headMap为null即可
     */
    public static String send(String url,String body) throws Exception{

        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(url);

        HttpEntity entity = new StringEntity(body, "utf-8");
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200){
            String resEntityStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            client.close();
            return resEntityStr;
        }else if (response.getStatusLine().getStatusCode() == 404){
            client.close();
            throw new Exception("报错~~");
        }else{
            client.close();
            throw new Exception();
        }
    }

    /**
     *
     *
     * @param url
     * @param headMap,头信息
     * @param body
     *            body内容
     * @return
     * @throws Exception
     * @Description:
     */
    public static String send(String url,Map<String, String> headMap,String body) throws Exception{

        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(url);

        HttpEntity entity = new StringEntity(body, "utf-8");
        post.setEntity(entity);
        if (headMap != null && headMap.size() > 0){
            Iterator<Entry<String, String>> ite = headMap.entrySet().iterator();
            while (ite.hasNext()){
                Entry<String, String> entry = ite.next();
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200){
            String resEntityStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            client.close();
            return resEntityStr;
        }else if (response.getStatusLine().getStatusCode() == 202){
            client.close();
            return null;
        }else if (response.getStatusLine().getStatusCode() == 404){
            client.close();
            throw new Exception("报错~~");
        }else{
            client.close();
            throw new Exception();
        }
    }

    /**
     * webservice REST 协议接口请求，现用在香榭格雷
     *
     * @param url
     * @param body
     * @param contentType:(text/xml;charset=utf-8)(application/json)
     * @return
     * @throws Exception
     * @Description:
     */
    public static String sendWebservice(String urlStr,String body,String contentType) throws Exception{
        URL url = new URL(urlStr);
        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestProperty("Content-Length", Integer.toString(body.length()));
        conn.setRequestProperty("Content-Type", contentType);

        OutputStream os = conn.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_UTF_8);
        osw.write(body);
        osw.flush();
        osw.close();
        StringBuilder sTotalString = new StringBuilder();
        String sCurrentLine = "";
        InputStream is = conn.getInputStream();
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(is));
        while ((sCurrentLine = l_reader.readLine()) != null){
            sTotalString.append(sCurrentLine);
        }
        return sTotalString.toString();

    }

    /**
     * webservice REST 协议接口请求，现用在香榭格雷
     *
     *
     * now please use the method sendWebservice(String urlStr, String body, String
     * contentType) contentType,you will set "text/xml;charset=utf-8"
     *
     * @param url
     * @param body
     * @return
     * @throws Exception
     * @Description:
     * @deprecated
     */
    public static String sendWebservice(String urlStr,String body) throws Exception{
        URL url = new URL(urlStr);
        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestProperty("Content-Length", Integer.toString(body.length()));
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

        OutputStream os = conn.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_UTF_8);
        osw.write(body);
        osw.flush();
        osw.close();
        StringBuilder sTotalString = new StringBuilder();
        String sCurrentLine = "";
        InputStream is = conn.getInputStream();
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(is));
        while ((sCurrentLine = l_reader.readLine()) != null){
            sTotalString.append(sCurrentLine);
        }
        return sTotalString.toString();

    }

    /**
     * put请求
     *
     * @param url
     * @param params
     * @return
     * @Description:
     */
    public static String putMap(String url,Map<String, String> headers,String boby){
        CloseableHttpClient client = null;
        String body = null;
        try{
            // http客户端创建
            HttpClientBuilder httpClient = HttpClientBuilder.create();
            client = httpClient.build();
            HttpPut put = putForm(url, headers, boby);
            body = invoke(client, put);
        }finally{
            try{
                // 关闭http客户端
                if (null != client){
                    client.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return body;
    }

    private static HttpPut putForm(String url,Map<String, String> headers,String boby){
        HttpPut httput = new HttpPut(url);
        Set<String> keySet = headers.keySet();
        for (String key : keySet){
            httput.addHeader(key, headers.get(key));
        }
        StringEntity entity = new StringEntity(boby, "UTF-8");
        httput.setEntity(entity);
        return httput;
    }

    public static String sendWebserviceNew(String urlStr,String body) throws Exception{
        URL url = new URL(urlStr);
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
        trustAllHosts(httpsConn);
        httpsConn.setHostnameVerifier(DO_NOT_VERIFY);
        httpsConn.setUseCaches(false);
        httpsConn.setDoInput(true);
        httpsConn.setDoOutput(true);

        httpsConn.setRequestProperty("Content-Length", Integer.toString(body.length()));
        httpsConn.setRequestProperty("Content-Type", CONTENT_TYPE_TEXTHTML);

        OutputStream os = httpsConn.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(body);
        osw.flush();
        osw.close();
        StringBuilder sTotalString = new StringBuilder();
        String sCurrentLine = "";
        InputStream is = httpsConn.getInputStream();
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(is));
        while ((sCurrentLine = l_reader.readLine()) != null){
            sTotalString.append(sCurrentLine);
        }
        return sTotalString.toString();
    }

    /**
     * 设置不验证主机
     */
    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier(){

        public boolean verify(String hostname,SSLSession session){
            return true;
        }
    };

    /**
     * 覆盖java默认的证书验证
     */
    private static final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager(){

        public java.security.cert.X509Certificate[] getAcceptedIssuers(){
            return new java.security.cert.X509Certificate[] {};
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,String paramString) throws java.security.cert.CertificateException{
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,String paramString) throws java.security.cert.CertificateException{
            // TODO Auto-generated method stub

        }
    } };

    /**
     * 信任所有
     *
     * @param connection
     * @return
     */
    private static SSLSocketFactory trustAllHosts(HttpsURLConnection connection){
        SSLSocketFactory oldFactory = connection.getSSLSocketFactory();
        try{
            SSLContext sc = SSLContext.getInstance(PROTOCOL);
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory newFactory = sc.getSocketFactory();
            connection.setSSLSocketFactory(newFactory);
        }catch (Exception e){
        }
        return oldFactory;
    }

}
