package com.example.demo.util;

/**
 * @author liting fengkuangdejava@outlook.com
 * @version V1.0
 * @description TODO
 * @date 2019/7/23
 **/
import com.example.demo.exception.DefaultException;
import com.example.demo.model.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
public class HttpClientUtil {

    public static String doGet(String url, Map<String, String> param, Map<String, String> header) {
        String resultString ;
        CloseableHttpResponse response;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet = (HttpGet) setHeader(httpGet, header);
            response = httpClient.execute(httpGet);
            resultString = dealResponse(response);
        } catch (IOException | URISyntaxException e) {
            log.error(e.getMessage());
            throw new DefaultException(ResultEnum.REQUEST_SERVICE_ERROR);
        }
        return resultString;
    }

    //form表单
    public static String doPost(String url, Map<String, Object> param, Map<String, String> header) {
        CloseableHttpResponse response;
        String resultString;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost = (HttpPost) setHeader(httpPost, header);
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, (String) param.get(key)));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            response = httpClient.execute(httpPost);
            resultString = dealResponse(response);
        } catch (IOException  e) {
            log.error(e.getMessage());
            throw new DefaultException(ResultEnum.REQUEST_SERVICE_ERROR);
        }
        return resultString;
    }
    //json
    public static String doPostJson(String url, String json, Map<String, String> header){
        CloseableHttpResponse response;
        String resultString;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
            HttpPost httpPost = new HttpPost(url);
            httpPost = (HttpPost) setHeader(httpPost, header);
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json;charset=utf-8");
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            resultString = dealResponse(response);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new DefaultException(ResultEnum.REQUEST_SERVICE_ERROR);
        }
        return resultString;
    }
    //多文件
    public static String httpPostFormMultipartFiles(String url, Map<String,String> params, List<File> files, Map<String,String> headers){
        String encode = "utf-8";
        String resultString;
        CloseableHttpResponse  httpResponse;
        try(CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            //设置header
            httpPost = (HttpPost) setHeader(httpPost, headers);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
            mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            mEntityBuilder.setCharset(Charset.forName(encode));
            // 普通参数
            ContentType contentType = ContentType.create("text/plain", Charset.forName(encode));//解决中文乱码
            if (params != null && params.size() > 0) {
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    mEntityBuilder.addTextBody(key, params.get(key), contentType);
                }
            }
            //二进制参数
            if (files != null && files.size() > 0) {  //多文件处理
                for (File file : files) {
                    mEntityBuilder.addBinaryBody("file", file);
                }
            }
            httpPost.setEntity(mEntityBuilder.build());
            httpResponse = closeableHttpClient.execute(httpPost);
            resultString = dealResponse(httpResponse);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new DefaultException(ResultEnum.REQUEST_SERVICE_ERROR);
        }
          return resultString;
    }
    //单文件
    public static String httpPostFormMultipartFile(String url, Map<String,String> params, File file, Map<String,String> headers){
        String encode = "utf-8";
        String resultString;
        CloseableHttpResponse  httpResponse;
        try(CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            //设置header
            httpPost = (HttpPost) setHeader(httpPost, headers);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
            mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            mEntityBuilder.setCharset(Charset.forName(encode));
            // 普通参数
            ContentType contentType = ContentType.create("text/plain", Charset.forName(encode));//解决中文乱码
            if (params != null && params.size() > 0) {
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    mEntityBuilder.addTextBody(key, params.get(key), contentType);
                }
            }
            //二进制参数
            if (file != null) {
                    mEntityBuilder.addBinaryBody("file", file);
            }
            httpPost.setEntity(mEntityBuilder.build());
            httpResponse = closeableHttpClient.execute(httpPost);
            resultString = dealResponse(httpResponse);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new DefaultException(ResultEnum.REQUEST_SERVICE_ERROR);
        }
        return resultString;
    }




    //zimg单文件上传
    public static String postFileToImage(String url,File file){
        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String resultString;
        Map<String,String> headers = new HashMap<>();
        headers.put("Connection", "Keep-Alive");
        headers.put("Cache-Control", "no-cache");
        headers.put("Content-Type", ext.toLowerCase());
        headers.put("COOKIE", "qixun");
        CloseableHttpResponse  httpResponse;
        try(CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            //设置header
            httpPost = (HttpPost) setHeader(httpPost, headers);
            byte[] bytes = StringFileUtil.file2byte(file);
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes);
            httpPost.setEntity(byteArrayEntity);
            httpResponse = closeableHttpClient.execute(httpPost);
            resultString = dealResponse(httpResponse);
        }catch (Exception e){
            e.printStackTrace();
            throw new DefaultException(ResultEnum.REQUEST_SERVICE_ERROR);
        }
        return resultString;
    }

    public static String postMultipartFileToImage(String url, MultipartFile file){
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String resultString;
        Map<String,String> headers = new HashMap<>();
        headers.put("Connection", "Keep-Alive");
        headers.put("Cache-Control", "no-cache");
        headers.put("Content-Type", ext.toLowerCase());
        headers.put("COOKIE", "qixun");
        CloseableHttpResponse  httpResponse;
        try(CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            //设置header
            httpPost = (HttpPost) setHeader(httpPost, headers);
            byte[] bytes = StringFileUtil.toByteArray(file.getInputStream());
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes);
            httpPost.setEntity(byteArrayEntity);
            httpResponse = closeableHttpClient.execute(httpPost);
            resultString = dealResponse(httpResponse);
        }catch (Exception e){
            e.printStackTrace();
            throw new DefaultException(ResultEnum.REQUEST_SERVICE_ERROR);
        }
        return resultString;
    }

    private static HttpRequestBase setHeader(HttpRequestBase httpRequestBase, Map<String, String> header) {
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpRequestBase.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpRequestBase;
    }

    private static String dealResponse(CloseableHttpResponse response) {
        String resultString = null;
        String successStatus = "20";
        try {
            if (response!=null&&String.valueOf(response.getStatusLine().getStatusCode()).contains(successStatus)) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.info(resultString);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new DefaultException(ResultEnum.DEAL_RESPONSE_ERROR);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                    throw new DefaultException(ResultEnum.CLOSE_RESPONSE_ERROR);
                }
            }
        }
        return resultString;
    }

    public static void main(String[] args) {
        File file = new File("D:\\pic4.jpg");
        String s = postFileToImage("http://39.100.94.39:4869/upload",file);
     /*   ZimgResult zimgResult = JSONObject.parseObject(s,ZimgResult.class);*/
        System.out.println(s);
        System.out.println("99006d850f8daf696eede6d5070d0934".length());
    }

}
