package com.example.demo.util;

import com.example.demo.model.UrlEntity;
import java.util.HashMap;
import org.springframework.util.StringUtils;

/**
 * url解析工具
 *
 * @author LiTing
 * @date 2021/2/26 11:12
 */
public class UrlUtil {

  /**
   * 解析url
   *
   * @param url
   * @return
   */
  public static UrlEntity parse(String url) {
    UrlEntity entity = new UrlEntity();
    if (StringUtils.isEmpty(url)) {
      return entity;
    }
    String[] urlParts = url.split("\\?");
    entity.baseUrl = urlParts[0];
    // 没有参数
    if (urlParts.length == 1) {
      return entity;
    }
    // 有参数
    String[] params = urlParts[1].split("&");
    entity.params = new HashMap<>(8);
    for (String param : params) {
      String[] keyValue = param.split("=");
      entity.params.put(keyValue[0], keyValue[1]);
    }

    return entity;
  }

  /**
   * 测试
   *
   * @param args
   */
  public static void main(String[] args) {
    UrlEntity entity = parse(null);
    System.out.println(entity.baseUrl + "\n" + entity.params);
    entity = parse("http://www.123.com");
    System.out.println(entity.baseUrl + "\n" + entity.params);
    entity = parse("http://www.123.com?id=1");
    System.out.println(entity.baseUrl + "\n" + entity.params);
    entity = parse("http://www.123.com?id=1&name=小明");
    System.out.println(entity.baseUrl + "\n" + entity.params);
  }
}
