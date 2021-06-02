package com.example.demo.util;



import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.entity.ContentType;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 统一的io处理工具类
 *
 * @author liting
 */
public class IoUtil {
  /** 默认的字节读取大小 */
  private static final int SIZE = 4 * 1024;

  public static File inputStream2File(
      InputStream inputStream, String targetFile, boolean closeStream) throws IOException {
    File file = new File(targetFile);
    OutputStream outStream = null;
    try {
      outStream = new FileOutputStream(file);
      File parentFile = file.getParentFile();
      if (!parentFile.exists()) {
        parentFile.mkdirs();
      }
      byte[] buffer = new byte[SIZE];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outStream.write(buffer, 0, bytesRead);
      }
      outStream.flush();
    } finally {
      if (outStream != null) {
        outStream.close();
      }
      if (closeStream && inputStream != null) {
        inputStream.close();
      }
    }
    return file;
  }

  public static String inputStream2String(InputStream is, String charsetName, boolean closeStream)
      throws IOException {
    ByteArrayOutputStream result = null;
    try {
      result = new ByteArrayOutputStream();
      byte[] buffer = new byte[SIZE];
      int length;
      while ((length = is.read(buffer)) != -1) {
        result.write(buffer, 0, length);
      }
      String str = result.toString(charsetName);
      return str;
    } finally {
      if (closeStream && is != null) {
        is.close();
      }
      if (result != null) {
        result.close();
      }
    }
  }

  public static ByteArrayInputStream string2InputStream(String s, String charsetName)
      throws UnsupportedEncodingException {
    return new ByteArrayInputStream(s.getBytes(charsetName));
  }

  public static FileInputStream file2InputStream(String path) throws FileNotFoundException {
    return new FileInputStream(path);
  }



  /** 目的是检查目录存不存在，不存在则创建. */
  public static void checkPath(String path) {
    File file = new File(path);
    if (file != null && file.isDirectory() && !file.exists()) {
      file.mkdirs();
    }
    if (file == null || !file.exists()) {
      file.mkdirs();
    }
  }

  public static File multipartFileToFile(MultipartFile multipartFile, String filePath)
      throws IOException {
    InputStream in = multipartFile.getInputStream();
    return inputStream2File(in, filePath, true);
  }

  /** 输出流转字符串 */
  public static String outputStream2String(OutputStream out, boolean closeStream)
      throws IOException {
    try {
      ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
      ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
      return swapStream.toString();
    } finally {
      if (closeStream == true && out != null) {
        out.close();
      }
    }
  }

  public static final InputStream byte2InputStream(byte[] buf) {
    return new ByteArrayInputStream(buf);
  }

  public static final byte[] inputStream2byte(InputStream inStream) throws IOException {
    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
    byte[] buff = new byte[SIZE];
    int rc = 0;
    while ((rc = inStream.read(buff)) != -1) {
      swapStream.write(buff, 0, rc);
    }
    byte[] in2b = swapStream.toByteArray();
    return in2b;
  }

  /** inputStream转输出流 */
  public static ByteArrayOutputStream inputStream2OutputStream(InputStream in) throws Exception {
    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
    int ch;
    while ((ch = in.read()) != -1) {
      swapStream.write(ch);
    }
    return swapStream;
  }

  /** string转输出流 */
  public static ByteArrayOutputStream string2OutputStream(String in) throws Exception {
    return inputStream2OutputStream(string2InputStream(in));
  }

  /** String转inputStream */
  public static ByteArrayInputStream string2InputStream(String in) {
    ByteArrayInputStream input = new ByteArrayInputStream(in.getBytes());
    return input;
  }

  /** byte数组转文件 */
  public static byte[] file2Byte(File file) throws IOException {
    byte[] buffer;
    FileInputStream fis = null;
    ByteArrayOutputStream bos = null;
    try {
      checkPath(file.getParent());
      fis = new FileInputStream(file);
      bos = new ByteArrayOutputStream();
      byte[] b = new byte[SIZE];
      int n;
      while ((n = fis.read(b)) != -1) {
        bos.write(b, 0, n);
      }
    } finally {
      if (fis != null) {
        fis.close();
      }
      if (bos != null) {
        bos.close();
      }
    }
    buffer = bos.toByteArray();
    return buffer;
  }

  /** 文件转字节数组 */
  public static void byte2File(byte[] bytes, String fileName) throws IOException {
    BufferedOutputStream bos = null;
    FileOutputStream fos = null;
    File file = null;
    try {
      file = new File(fileName);
      checkPath(file.getParent());
      fos = new FileOutputStream(file);
      bos = new BufferedOutputStream(fos);
      bos.write(bytes);
      bos.flush();
    } finally {
      if (bos != null) {
        bos.close();
      }
      if (fos != null) {
        fos.close();
      }
    }
  }

  public static byte[] readAsBytes(HttpServletRequest request) {

    int len = request.getContentLength();
    byte[] buffer = new byte[len];
    ServletInputStream in = null;

    try {
      in = request.getInputStream();
      in.read(buffer, 0, len);
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (null != in) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return buffer;
  }

}
