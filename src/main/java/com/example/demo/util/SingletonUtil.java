package com.example.demo.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 单例工具类
 *
 * @author LiTing
 * @date 2021/3/26 18:22
 */
public class SingletonUtil {

  private static volatile PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
  private static volatile HttpClientBuilder httpClientBuilder;
  private static RequestConfig globalConfig =
      RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
  private static Lock lock1 = new ReentrantLock();
  private static Lock lock2 = new ReentrantLock();
  public static ThreadFactory namedThreadFactory =
      new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
  public static ExecutorService executorService =
      new ThreadPoolExecutor(
          5, 100, 200L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), namedThreadFactory);
  /** 保证全局只使用提个连接池管理对象 */
  public static PoolingHttpClientConnectionManager
      getSingletonPoolingHttpClientConnectionManager() {
    if (poolingHttpClientConnectionManager == null) {
      lock1.lock();
      try {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(800);
        cm.setMaxTotal(1000);
        poolingHttpClientConnectionManager = cm;
      } finally {
        lock1.unlock();
      }
    }
    return poolingHttpClientConnectionManager;
  }

  /** 保证全局只通过一个builder去创建client */
  public static HttpClientBuilder getSingletonHttpClientBuilder() {
    if (httpClientBuilder == null) {
      lock2.lock();
      try {
        httpClientBuilder =
            HttpClients.custom()
                .setConnectionManager(
                    SingletonUtil.getSingletonPoolingHttpClientConnectionManager())
                .setDefaultRequestConfig(globalConfig);
      } finally {
        lock2.unlock();
      }
    }
    return httpClientBuilder;
  }
}
