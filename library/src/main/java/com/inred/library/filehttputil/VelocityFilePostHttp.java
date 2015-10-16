package com.inred.library.filehttputil;

import android.text.TextUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by inred on 2015/9/11.
 */
public class VelocityFilePostHttp extends Thread {

    private static final int CONNECTION_TIMEOUT = 2 * 1000;//设置请求超时2秒钟 根据业务调整
    private static final int SO_TIMEOUT = 2 * 1000;//设置等待数据超时时间2秒钟 根据业务调整
    private static final Long CONN_MANAGER_TIMEOUT = 500L; //该值就是连接不够用的时候等待超时时间，一定要设置，而且不能太大 ()

    private String url;
    private List<NameValuePair> params;
    private Map<String, File> fileMap;

    /**
     * 文件上传
     *
     * @param url
     * @param fileMap
     */
    public VelocityFilePostHttp(String url, Map<String, File> fileMap) {
        this.url = url;
        this.fileMap = fileMap;
    }

    /**
     * 参数与文件上传
     *
     * @param url
     * @param params
     * @param fileMap
     */
    public VelocityFilePostHttp(String url, List<NameValuePair> params, Map<String, File> fileMap) {
        this.url = url;
        this.params = params;
        this.fileMap = fileMap;
    }

    /**
     * 步骤三:http上传文件执行
     *
     * @param url
     * @param params
     * @param fileMap
     * @return
     */
    private String postFile(String url, List<NameValuePair> params, Map<String, File> fileMap) {
        try {
            HttpClient client = getHttpClient();//开启一个客户端http请求
            HttpPost post = new HttpPost(url);//创建 http post 请求
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.setCharset(Charset.forName(HTTP.UTF_8));//设置请求编码格式;
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式

            if (params != null && !params.isEmpty())
                for (NameValuePair p : params) {
                    builder.addTextBody(p.getName(), p.getValue(),
                            ContentType.TEXT_PLAIN.withCharset(Charset.forName(HTTP.UTF_8)));
                }

            if (fileMap != null && !fileMap.isEmpty()) {
                Set<Map.Entry<String, File>> entries = fileMap.entrySet();
                for (Map.Entry<String, File> entry : entries) {
                    builder.addPart(entry.getKey(), new FileBody(entry.getValue()));
                }
            }

            HttpEntity entity = builder.build();
            VelocityMultipartEntity velocityMultipartEntity = new VelocityMultipartEntity(entity, new VelocityMultipartEntity.ProgressListener() {
                @Override
                public void transferred(long num, int progress) {
                    currentProgress(num, progress);
                }
            });
            post.setEntity(velocityMultipartEntity);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                return read(response);
            } else
                return null;
        } catch (ClientProtocolException cpe) {
            return null;
        } catch (IOException ioe) {
            return null;
        } catch (Exception ex) {
            return null;
        }

    }

    /**
     * 返回进度数
     *
     * @param num      传入文件long数
     * @param progress 百分比progress
     */
    public void currentProgress(long num, int progress) {

    }

    /**
     * 上传完成与否回调
     *
     * @param isPostSuccess
     */
    public void postCallBack(boolean isPostSuccess, String string) {

    }


    @Override
    public void run() {
        super.run();
        String backString = postFile(url, params, fileMap);
        postCallBack(!TextUtils.isEmpty(backString), backString);
    }

//    /**
//     * 步骤二
//     * 获取文件的类型
//     *
//     * @param fileName ：文件名
//     * @return 文件类型
//     */
//    private String getFileType(String fileName) {
//        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
//    }

//    /**
//     * 步骤一:解析文件名和类型
//     * 上传文件
//     * @param url
//     * @param files
//     * @return
//     */
//    public boolean postFiles(String url, File... files) {
//        File[] postfiles = files;
//        if (postfiles == null || postfiles.length == 0)
//            return false;
//        Map<String, String> params = new HashMap<>();
//        StringBuffer sbFileTypes = new StringBuffer();
//        for (File tempFile : postfiles) {
//            String filename = tempFile.getName();
//            sbFileTypes.append(getFileType(filename));
//        }
//
//        params.put("fileTypes", sbFileTypes.toString());
//        params.put("method", "upload");
//
//        return postFile(url, params, postfiles);
//
//    }


    /**
     * 自定义HttpClient
     *
     * @return
     */
    private HttpClient getHttpClient() {
        try {

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
            params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
            params.setLongParameter(ClientPNames.CONNECTION_MANAGER_FACTORY, CONN_MANAGER_TIMEOUT);
            //在提交请求之前 测试连接是否可用
            params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);


            HttpClient client = new DefaultHttpClient(ccm, params);
            return client;

        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    /**
     * Http 安全套字节
     */
    private class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    /**
     * Read http requests result from response .
     *
     * @param response : http response by executing httpclient
     * @return String : http response content
     */
    public static String read(HttpResponse response) {
        String result = "";
        HttpEntity entity = response.getEntity();
        InputStream inputStream;
        try {
            inputStream = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();

            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                inputStream = new GZIPInputStream(inputStream);
            }

            // Read response into a buffered stream
            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }
            // Return result from buffered stream
            result = new String(content.toByteArray(), "UTF-8");
            return result;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
