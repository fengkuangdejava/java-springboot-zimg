### 简述
    zimg是图像存储和处理服务器。您可以使用URL参数从zimg获取压缩和缩放的图像。
    zimg的并发I / O，分布式存储和时间处理能力非常出色。
    您不再需要在图像服务器中使用nginx。在基准测试中，zimg可以在高并发级别上处理每秒3000+图像下载任务和每秒90000+ HTTP回应请求。
    性能高于PHP或其他图像处理服务器。
 
    搭建前，所需要的依赖库也是很多的，除了官网文档上的依赖库，在构建时还会需要不少的依赖库。按照惯例，自己爬的坑，别人就不要爬了。
### 开始
#### 安装依赖库
    sudo yum install -y  wget openssl-devel cmake libevent-devel libjpeg-devel giflib-devel libpng-devel libwebp-devel ImageMagick-devel libmemcached-devel 
    sudo yum install -y glibc-headers gcc-c++
    sudo yum install -y build-essential nasm
#### 安装依赖
##### openssl
    mkdir /usr/local/zimg/openssl
    cd /usr/local/zimg/openssl
    wget http://www.openssl.org/source/openssl-1.0.1i.tar.gz
    tar zxvf openssl-1.0.1i.tar.gz
    cd openssl-1.0.1i
    ./config shared --prefix=/usr/local --openssldir=/usr/ssl
    make && make install
##### cmake
    mkdir /usr/local/zimg/cmake
    cd /usr/local/zimg/cmake
    wget http://www.cmake.org/files/v3.0/cmake-3.0.1.tar.gz
    tar xzvf cmake-3.0.1.tar.gz 
    cd cmake-3.0.1
    ./bootstrap --prefix=/usr/local 
    make && make install
##### libevent
    mkdir /usr/local/zimg/libevent
    cd /usr/local/zimg/libevent
    wget http://cloud.github.com/downloads/libevent/libevent/libevent-2.0.21-stable.tar.gz
    tar zxvf libevent-2.0.21-stable.tar.gz
    cd libevent-2.0.21-stable
    ./configure --prefix=/usr/local 
    make && make install 
##### libjpeg-turbo
    mkdir /usr/local/zimg/libjpeg-turbo
    cd /usr/local/zimg/libjpeg-turbo
    wget https://downloads.sourceforge.net/project/libjpeg-turbo/1.3.1/libjpeg-turbo-1.3.1.tar.gz
    tar zxvf libjpeg-turbo-1.3.1.tar.gz
    cd libjpeg-turbo-1.3.1
    ./configure --prefix=/usr/local --with-jpeg8
    make && make install
#### 下面这几个，如果采取源代码构建，不需要安装
    另外源码不要根据官方文档提供的链接去下载，自己去zimg github地址库区下载release 版；
    库连接：https://github.com/buaazp/zimg
##### webp
    mkdir /usr/local/zimg/webp
    cd /usr/local/zimg/
    wget http://downloads.webmproject.org/releases/webp/libwebp-0.4.1.tar.gz
    tar zxvf libwebp-0.4.1.tar.gz
    cd libwebp-0.4.1
    ./configure
    make
    sudo make install
##### jpegsrc
    mkdir /usr/local/zimg/jpegsrc
    cd /usr/local/zimg/
    wget http://www.ijg.org/files/jpegsrc.v8b.tar.gz
    tar -xf  jpegsrc.v8b.tar.gz
    cd jpeg-8b
    ./configure --prefix=/usr/local --enable-shared --enable-static
    make && make install
##### imageMagic
    mkdir /usr/local/zimg/imageMagick
    cd /usr/local/zimg/
    wget http://www.imagemagick.org/download/ImageMagick.tar.gz
    tar zxvf ImageMagick.tar.gz
    cd ImageMagick-6.9.1-10
    ./configure  --prefix=/usr/local 
    make && make install
##### libmemcached
    wget https://launchpad.net/libmemcached/1.0/1.0.18/+download/libmemcached-1.0.18.tar.gz
    tar zxvf libmemcached-1.0.18.tar.gz
    cd libmemcached-1.0.18
    ./configure -prefix=/usr/local 
    make &&　make install
### 构建zimg
    cd /usr/local
    ##此处不要根据下面链接去下载，自己去zimg github地址库区下载release 版；
    #git clone https://github.com/buaazp/zimg -b master --depth=1
    cd zimg   
    make 
### 可选的插件
#### memcached
    wget http://www.memcached.org/files/memcached-1.4.19.tar.gz
    tar zxvf memcached-1.4.19.tar.gz
    cd memcached-1.4.19
    ./configure --prefix=/usr/local
    make
    make install
#### beansdb
    git clone https://github.com/douban/beansdb
    cd beansdb
    ./configure --prefix=/usr/local
    make
#### benseye
    git clone git@github.com:douban/beanseye.git
    cd beanseye
    make
#### SSDB
    wget --no-check-certificate https://github.com/ideawu/ssdb/archive/master.zip
    unzip master
    cd ssdb-master
    make
#### twemproxy
    git clone git@github.com:twitter/twemproxy.git
    cd twemproxy
    autoreconf -fvi
    ./configure --enable-debug=log
    make
    src/nutcracker -h
### 安装成功后
    cd /usr/local/zimg/bin
    ./zimg conf/zimg.lua
    ctrl+z 退出
    crul http://localhost/4869 
    如果出现html代码  则说明安装启动成功。
    
### 如果嫌手动安装太麻烦,就直接使用docker镜像
    docker pull iknow0612/zimg
    docker run -it -d -p 4869:4869 -v /data/zimg/:/zimg/bin/img --name my_zimg iknow0612/zimg sh app.sh
    dokcerfile形式：
        zimg:
            image: iknow0612/zimg
            container_name: qx-zimg
            restart: always
            volumes:
              - /data/zimg:/zimg/bin/img
            ports:
              - 4869:4869
            command: [sh,app.sh]
### Java对接
    网上的zimg都不太好用，反正zimg是基于restful风格的，干脆自己封装。
#### 依赖
    <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>${fastjson.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mitre.dsmiley.httpproxy</groupId>
            <artifactId>smiley-http-proxy-servlet</artifactId>
            <version>1.7</version>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpmime</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-httpclient</groupId>
            <artifactId>commons-httpclient</artifactId>
            <version>${commons-httpclient.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
#### 封装httpClient
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
#### StringFile工具类
    package com.example.demo.util;
    import lombok.extern.slf4j.Slf4j;
    import org.apache.http.HttpEntity;
    import org.apache.http.HttpResponse;
    
    import javax.imageio.ImageIO;
    import java.awt.*;
    import java.io.*;
    
    @Slf4j
    public class StringFileUtil {
        /**
         * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
         *
         * @param res      原字符串
         * @param filePath 文件路径
         * @return 成功标记
         */
        public static boolean string2File(String res, String filePath) {
            boolean flag = true;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;
            try {
                File distFile = new File(filePath);
                if (!distFile.getParentFile().exists()) distFile.getParentFile().mkdirs();
                bufferedReader = new BufferedReader(new StringReader(res));
                bufferedWriter = new BufferedWriter(new FileWriter(distFile));
                char buf[] = new char[1024];         //字符缓冲区
                int len;
                while ((len = bufferedReader.read(buf)) != -1) {
                    bufferedWriter.write(buf, 0, len);
                }
                bufferedWriter.flush();
                bufferedReader.close();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
                return flag;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return flag;
        }
    
        /**
         * 文本文件转换为指定编码的字符串
         *
         * @param file     文本文件
         * @param encoding 编码类型
         * @return 转换后的字符串
         * @throws IOException
         */
        public static String file2String(File file, String encoding) {
            InputStreamReader reader = null;
            StringWriter writer = new StringWriter();
            try {
                if (encoding == null || "".equals(encoding.trim())) {
                    reader = new InputStreamReader(new FileInputStream(file), encoding);
                } else {
                    reader = new InputStreamReader(new FileInputStream(file));
                }
                //将输入流写入输出流
                char[] buffer = new char[1024];
                int n = 0;
                while (-1 != (n = reader.read(buffer))) {
                    writer.write(buffer, 0, n);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            //返回转换结果
            if (writer != null) {
                return writer.toString();
            } else {
                return null;
            }
        }
    
        public static void httpResponseToFile(HttpResponse httpResponse, String path){
            File file=new File(path);
            if(file.exists())file.delete();
            try {
                //使用file来写入本地数据
                file.createNewFile();
                FileOutputStream outStream = new FileOutputStream(path);
                log.info("[STATUS] Download : "+httpResponse.getStatusLine()+" [FROM] "+path);
                HttpEntity httpEntity=httpResponse.getEntity();
                InputStream inStream=httpEntity.getContent();
                while(true){//这个循环读取网络数据，写入本地文件
                    byte[] bytes=new byte[1024*1000];
                    int k=inStream.read(bytes);
                    if(k>=0){
                        outStream.write(bytes,0,k);
                        outStream.flush();
                    }
                    else break;
                }
                inStream.close();
                outStream.close();
            } catch (IOException e){
                e.printStackTrace();
                log.info("[ERROR] Download IOException : "+e.toString()+" [FROM] : "+path);
                //e.printStackTrace();
            }
        }
        public static byte[] file2byte(File tradeFile){
            byte[] buffer = null;
            try
            {
                FileInputStream fis = new FileInputStream(tradeFile);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1)
                {
                    bos.write(b, 0, n);
                }
                fis.close();
                bos.close();
                buffer = bos.toByteArray();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return buffer;
        }
    
        public static byte[] toByteArray(InputStream input) throws IOException {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            byte[] result =  output.toByteArray();
            output.close();
            return  result;
        }
    
        public static boolean isImage(InputStream inputStream) {
            if (inputStream == null) {
                return false;
            }
            Image img;
            try {
                img = ImageIO.read(inputStream);
                return !(img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0);
            } catch (Exception e) {
                return false;
            }
        }
    }

#### 返回结果封装
    //ZimgResultInfo
    import lombok.Data;

    @Data
    public class ZimgResultInfo {
        private String md5;
        private String size;
    }
    
    
    //ZimgResultError
    import lombok.Data;
    @Data
    public class ZimgResultError {
        private int code;
        private String message;
    }
    
    //ZimgResult
    import lombok.Data;
    @Data
    public class ZimgResult {
        private boolean ret;
        private ZimgResultInfo info;
        private ZimgResultError error;
    }
#### zimg配置类
    
    import lombok.Data;
    import org.mitre.dsmiley.httpproxy.ProxyServlet;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.boot.web.servlet.ServletRegistrationBean;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.annotation.Order;
    
    @Data
    @Configuration
    public class ZimgConfig {
        @Value("${app.zimg.server}")
        private String zimgServer;
        @Value("${app.zimg.router}")
        private String router;
    
        /**
         * 注册http代理 拦截前台/zimg/* 请求 转发到图片服务器。用于希望 “访问图片需要带上token或session” 的情况
         * */
        @Bean
        @Order(Integer.MAX_VALUE-1)
        public ServletRegistrationBean servletRegistrationBean(){
            ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new ProxyServlet(),getRouter()+"/*");
            //这个setName必须要设置，并且多个的时候，名字需要不一样
            servletRegistrationBean.setName("zimg");
            servletRegistrationBean.addInitParameter("targetUri",getZimgServer());
            servletRegistrationBean.addInitParameter(ProxyServlet.P_LOG, "false");
            return servletRegistrationBean;
        }
    }
#### zimgService接口
    public interface ZimgService {
        String uploadImage(MultipartFile multipartFile);
        List<String> uploadImage(List<MultipartFile> multipartFiles);
        String uploadImage(File file);
        boolean deleteImage(String md5);
    }
#### zimgService实现类
    package com.example.demo.service.impl;

    import com.alibaba.fastjson.JSONObject;
    import com.example.demo.config.ZimgConfig;
    import com.example.demo.exception.DefaultException;
    import com.example.demo.model.ResultEnum;
    import com.example.demo.model.ZimgResult;
    import com.example.demo.service.ZimgService;
    import com.example.demo.util.HttpClientUtil;
    import lombok.Data;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;
    
    import java.io.File;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;
    
    @Data
    @Service
    @Slf4j
    public class ZimgServiceImpl implements ZimgService {
        @Autowired
        ZimgConfig zimgConfig;
        private static final  String  uploadPath="/upload";
        private static final  String  deletePath ="/admin";
        @Override
        public String uploadImage(MultipartFile multipartFile) {
            String url = zimgConfig.getZimgServer()+uploadPath;
            String s = HttpClientUtil.postMultipartFileToImage(url,multipartFile);
            ZimgResult zimgResult = JSONObject.parseObject(s,ZimgResult.class);
            if(zimgResult.isRet()){
                String imgUrl = zimgConfig.getRouter().trim()+"/"+zimgResult.getInfo().getMd5();
                imgUrl= imgUrl.substring(1);
                log.info("imgUrl={}",imgUrl.substring(1));
                return imgUrl;
            }else {
                throw new DefaultException(ResultEnum.UPLOAD_ZIMG_ERROR.getCode(),zimgResult.getError().getMessage());
            }
        }
    
        @Override
        public List<String> uploadImage(List<MultipartFile> multipartFiles) {
            List<String> list = multipartFiles.parallelStream().map(multipartFile->uploadImage(multipartFile)).collect(Collectors.toList());
            return list;
        }
    
        @Override
        public String uploadImage(File file) {
            String url = zimgConfig.getZimgServer()+uploadPath;
            String s = HttpClientUtil.postFileToImage(url,file);
            ZimgResult zimgResult = JSONObject.parseObject(s,ZimgResult.class);
            if(zimgResult.isRet()){
                String imgUrl = zimgConfig.getRouter().trim()+"/"+zimgResult.getInfo().getMd5();
                imgUrl= imgUrl.substring(1);
                log.info("imgUrl={}",imgUrl);
                return imgUrl;
            }else {
                throw new DefaultException(ResultEnum.UPLOAD_ZIMG_ERROR.getCode(),zimgResult.getError().getMessage());
            }
        }
    
        /**
         * 需要服务器开启远程修改权限 按自己需要修改 admin_rule='allow 127.0.0.1' 这一项
         * */
        @Override
        public boolean deleteImage(String md5) {
            String url = zimgConfig.getZimgServer()+deletePath;
            Map<String,String> params = new HashMap<>(2);
            params.put("md5",md5);
            params.put("t","1");
            String s = HttpClientUtil.doGet(url,params,null);
            System.out.println(s);
            //这里因忘记ssh密码，暂时无法登陆服务器修改配置，所以不知道delete返回结果具体是什么。以下两行只是示意处理流程
            ZimgResult zimgResult = JSONObject.parseObject(s,ZimgResult.class);
            return zimgResult.isRet();
        }
    
        public static void main(String[] args) {
            String str1 = "abc";
            String str2 = str1.substring(1);
            System.out.println(str2);
        }
    }
    
### github链接：
    
