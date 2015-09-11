package com.inred.library.filehttputil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.IOException;
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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by inred on 2015/9/11.
 */
public class VelocityFilePostHttp extends Thread{

    private static final int CONNECTION_TIMEOUT = 2 * 1000;//设置请求超时2秒钟 根据业务调整
    private static final int SO_TIMEOUT = 2 * 1000;//设置等待数据超时时间2秒钟 根据业务调整
    private static final Long CONN_MANAGER_TIMEOUT = 500L; //该值就是连接不够用的时候等待超时时间，一定要设置，而且不能太大 ()

    private String url;
    private File[] files;
    private String filekey;

    public VelocityFilePostHttp(String url,String filekey, File... files){
        this.url = url;
        this.files = files;
        this.filekey = filekey;
    }

    /**
     * 步骤三:http上传文件执行
     * @param url
     * @param params
     * @param files
     * @return
     */
    private boolean postFile(String url, Map<String, String> params, File[] files) {
        try {
            HttpClient client = getHttpClient();//开启一个客户端http请求
            HttpPost post = new HttpPost(url);//创建 http post 请求
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName(HTTP.UTF_8));//设置请求编码格式;
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
            for (File file : files) {
                FileBody fileBody = new FileBody(file);//把文件转换成流对象filebody
                builder.addPart(filekey//文件接收key
                        , fileBody);
            }

            builder.addTextBody("method", params.get("method"));//设置请求参数
            builder.addTextBody("fileTypes", params.get("fileTypes"));//设置请求参数
            HttpEntity entity = builder.build();
            VelocityMultipartEntity velocityMultipartEntity = new VelocityMultipartEntity(entity, new VelocityMultipartEntity.ProgressListener() {
                @Override
                public void transferred(long num, int progress) {
                    currentProgress(num,progress);
                }
            });
            post.setEntity(velocityMultipartEntity);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            } else
                return false;
        } catch (ClientProtocolException cpe) {
            return false;
        } catch (IOException ioe) {
            return false;
        } catch (Exception ex) {
            return false;
        }

    }

    public void currentProgress(long num, int progress){

    }

    public void postCallBack(boolean isPostSuccess){

    }


    @Override
    public void run() {
        super.run();
       postCallBack(postFiles(url,files));
    }

    /**
     * 步骤二
     * 获取文件的类型
     *
     * @param fileName ：文件名
     * @return 文件类型
     */
    private String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    /**
     * 步骤一:解析文件名和类型
     * 上传文件
     * @param url
     * @param files
     * @return
     */
    public boolean postFiles(String url, File... files) {
        File[] postfiles = files;
        if (postfiles == null || postfiles.length == 0)
            return false;
        Map<String, String> params = new HashMap<>();
        StringBuffer sbFileTypes = new StringBuffer();
        for (File tempFile : postfiles) {
            String filename = tempFile.getName();
            sbFileTypes.append(getFileType(filename));
        }

        params.put("fileTypes", sbFileTypes.toString());
        params.put("method", "upload");

        return postFile(url, params, postfiles);

    }


    /**
     * 自定义HttpClient
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

}
