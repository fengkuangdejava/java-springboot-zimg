package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.exception.DefaultException;
import com.example.demo.model.ResultEnum;
import com.example.demo.model.UrlEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 * http请求工具类
 *
 * @author LiTing
 * @date 2021/3/26 16:39
 */
@Slf4j
public class HttpClientUtil {

  private static final String DEFAULT_CHARSET = "UTF-8";

  public static void httpResponseToFile(CloseableHttpResponse httpResponse, String path)
      throws IOException {
    HttpEntity httpEntity = httpResponse.getEntity();
    InputStream inStream = httpEntity.getContent();
    IoUtil.inputStream2File(inStream, path, true);
  }

  /**
   * 发送Get请求
   *
   * @param url
   * @param header
   * @param tClass
   * @return T
   * @throws
   * @version V1.0.0
   * @date 2021/3/26 17:06
   */
  public static <T> T doGet(String url, Map<String, String> header, Class<T> tClass) {
    CloseableHttpClient httpClient = getHttpClient();
    HttpGet get = new HttpGet(url);
    setHeader(get, header);
    return getResult(httpClient, get, tClass);
  }

  /**
   * 发送Get请求
   *
   * @param url
   * @param header
   * @param tClass
   * @return T
   * @throws
   * @version V1.0.0
   * @date 2021/3/26 17:06
   */
  public static <T> T doGetWithProxy(
      String url, Map<String, String> header, Class<T> tClass, String ip, int port) {
    CloseableHttpClient httpClient = getHttpClient();
    HttpGet get = new HttpGet(url);
    RequestConfig c = get.getConfig();
    if (c == null) {
      c = RequestConfig.DEFAULT;
    }
    RequestConfig requestConfig = RequestConfig.copy(c).setProxy(new HttpHost(ip, port)).build();
    get.setConfig(requestConfig);
    setHeader(get, header);
    return getResult(httpClient, get, tClass);
  }

  /**
   * 发送get请求 带参
   *
   * @param url
   * @param params
   * @param header
   * @param tClass
   * @return T
   * @throws
   * @version V1.0.0
   * @date 2021/3/26 17:06
   */
  public static <T> T doGet(
      String url, Map<String, Object> params, Map<String, String> header, Class<T> tClass) {
    CloseableHttpClient httpClient = getHttpClient();
    HttpGet get = new HttpGet(dealUrl(url, params));
    setHeader(get, header);
    return getResult(httpClient, get, tClass);
  }

  /**
   * 下载文件
   *
   * @param url
   * @param header
   * @param filePath
   * @return void
   * @throws
   * @version V1.0.0
   * @date 2021/3/26 17:06
   */
  public static void doGetFile(String url, Map<String, String> header, String filePath) {
    CloseableHttpClient httpClient = getHttpClient();
    HttpGet get = new HttpGet(url);
    setHeader(get, header);
    try (CloseableHttpResponse res = doExecute(httpClient, get)) {
      httpResponseToFile(res, filePath);
    } catch (IOException e) {
      e.printStackTrace();
      throw new DefaultException(ResultEnum.IO_ERROR);
    }
  }

  /**
   * 获取文件输入流
   *
   * @param url
   * @param header
   * @return java.io.InputStream
   * @throws
   * @version V1.0.0
   * @date 2021/3/26 17:05
   */
  public static InputStream doGetFileInputStream(String url, Map<String, String> header) {
    CloseableHttpClient httpClient = getHttpClient();
    HttpGet get = new HttpGet(url);
    setHeader(get, header);
    try (CloseableHttpResponse res = doExecute(httpClient, get)) {
      return res.getEntity().getContent();
    } catch (IOException e) {
      e.printStackTrace();
      throw new DefaultException(ResultEnum.IO_ERROR);
    }
  }

  /**
   * 发送 application/x-www-form-urlencoded请求
   *
   * @param url
   * @param param
   * @param header
   * @param tClass
   * @return T
   * @throws
   * @version V1.0.0
   * @date 2021/3/26 17:05
   */
  public static <T> T doPost(
      String url, Map<String, Object> param, Map<String, String> header, Class<T> tClass) {
    CloseableHttpClient httpClient = getHttpClient();
    HttpPost httpPost = new HttpPost(url);
    setHeader(httpPost, header);
    try {
      setUrlFormEntity(httpPost, param);
      return getResult(httpClient, httpPost, tClass);
    } catch (IOException e) {
      e.printStackTrace();
      throw new DefaultException(ResultEnum.IO_ERROR);
    }
  }

  /**
   * 发送application/json请求
   *
   * @param url
   * @param json
   * @param header
   * @param t
   * @return T
   * @throws
   * @version V1.0.0
   * @date 2021/3/26 17:04
   */
  public static <T> T doPostJson(String url, String json, Map<String, String> header, Class<T> t) {
    try {
      CloseableHttpClient httpClient = getHttpClient();
      HttpPost httpPost = new HttpPost(url);
      setHeader(httpPost, header);
      setJsonEntity(httpPost, json);
      return getResult(httpClient, httpPost, t);
    } catch (IOException e) {
      e.printStackTrace();
      throw new DefaultException(ResultEnum.IO_ERROR);
    }
  }

  /**
   * form表单文件上传
   *
   * @param url
   * @param params
   * @param headers
   * @param t
   * @return T
   * @throws
   * @version V1.0.0
   * @date 2021/3/26 17:23
   */
  public static <T> T doPostFormData(
      String url, Map<String, Object> params, Map<String, String> headers, Class<T> t) {
    CloseableHttpClient httpClient = getHttpClient();
    HttpPost httpPost = new HttpPost(url);
    setHeader(httpPost, headers);
    setFormDataEntity(httpPost, params);
    return getResult(httpClient, httpPost, t);
  }

  /**
   * 仅上传文件
   *
   * @param url
   * @param file
   * @param headers
   * @param tClass
   * @return T
   * @throws
   * @version V1.0.0
   * @date 2021/3/26 17:30
   */
  public static <T> T doPostFile(
      String url, File file, Map<String, String> headers, Class<T> tClass) {
    CloseableHttpClient httpClient = getHttpClient();
    HttpPost httpPost = new HttpPost(url);
    setHeader(httpPost, headers);
    ByteArrayEntity byteArrayEntity = null;
    try {
      if (file != null) {
        byteArrayEntity = new ByteArrayEntity(IoUtil.file2Byte(file));
      }
      httpPost.setEntity(byteArrayEntity);
      return getResult(httpClient, httpPost, tClass);
    } catch (IOException e) {
      e.printStackTrace();
      throw new DefaultException(ResultEnum.IO_ERROR);
    }
  }

  public static CloseableHttpClient getHttpClient() {
    return SingletonUtil.getSingletonHttpClientBuilder().build();
  }

  public static void setHeader(HttpRequestBase hb, Map<String, String> headers) {
    if (headers != null) {
      for (String key : headers.keySet()) {
        hb.setHeader(key, headers.get(key));
      }
    }
  }

  private static CloseableHttpResponse doExecute(CloseableHttpClient hc, HttpRequestBase hb)
      throws IOException {
    CloseableHttpResponse ch = hc.execute(hb);
    return ch;
  }

  private static String dealUrl(String url, Map<String, Object> params) {
    UrlEntity urlEntity = UrlUtil.parse(url);
    urlEntity.addParam(params);
    return urlEntity.toUrl();
  }

  public static void setUrlFormEntity(HttpPost httpPost, Map<String, Object> param)
      throws UnsupportedEncodingException {
    if (param != null) {
      List<NameValuePair> paramList = new ArrayList<>();
      for (String key : param.keySet()) {
        paramList.add(new BasicNameValuePair(key, String.valueOf(param.get(key))));
      }
      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
      httpPost.setEntity(entity);
    }
  }

  public static void setJsonEntity(HttpPost httpPost, String json)
      throws UnsupportedEncodingException {
    StringEntity entity = new StringEntity(json, Charset.forName(DEFAULT_CHARSET));
    entity.setContentType("application/json;charset=utf-8");
    httpPost.setEntity(entity);
  }

  public static void setFormDataEntity(HttpPost httpPost, Map<String, Object> params) {
    MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
    mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    mEntityBuilder.setCharset(Charset.forName(DEFAULT_CHARSET));
    ContentType textContent = ContentType.create("text/plain", Charset.forName(DEFAULT_CHARSET));
    params.forEach(
        (k, v) -> {
          if (v instanceof File) {
            File file = (File) v;
            mEntityBuilder.addBinaryBody(k, file);
          } else if (v instanceof Collection) {
            Collection cl = (Collection) v;
            cl.stream()
                .forEach(
                    c -> {
                      File f = (File) c;
                      mEntityBuilder.addBinaryBody(k, f);
                    });
          } else {
            mEntityBuilder.addTextBody(k, v.toString(), textContent);
          }
        });
    httpPost.setEntity(mEntityBuilder.build());
  }

  public static <T> boolean checkStringResult(Class<T> t) {
    if (String.class.getName().equals(t.getName())) {
      return true;
    }
    return false;
  }

  public static <T> boolean checkJsonObjectResult(Class<T> t) {
    if (JSONObject.class.getName().equals(t.getName())) {
      return true;
    }
    return false;
  }

  public static <T> boolean checkJsonArrayResult(Class<T> t) {
    if (JSONArray.class.getName().equals(t.getName())) {
      return true;
    }
    return false;
  }

  public static <T> T getResult(CloseableHttpClient hc, HttpRequestBase hb, Class<T> t) {
    String s;
    try (CloseableHttpResponse res = doExecute(hc, hb)) {
      s = EntityUtils.toString(res.getEntity(), DEFAULT_CHARSET);
      log.info("resultString:{}", s);
      if (checkStringResult(t)) {
        return (T) s;
      } else if (checkJsonObjectResult(t)) {
        return (T) JSONObject.parse(s);
      } else if (checkJsonArrayResult(t)) {
        return (T) JSONArray.parse(s);
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new DefaultException(ResultEnum.IO_ERROR);
    }
    return JSON.parseObject(s, t);
  }
}
