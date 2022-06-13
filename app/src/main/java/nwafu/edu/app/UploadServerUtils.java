package nwafu.edu.app;

import android.util.Log;

import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;

import org.apache.http.HttpStatus;

import org.apache.http.HttpVersion;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.mime.MultipartEntity;

import org.apache.http.entity.mime.content.ContentBody;

import org.apache.http.entity.mime.content.FileBody;

import org.apache.http.entity.mime.content.StringBody;

import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.params.CoreProtocolPNames;

import org.apache.http.protocol.HTTP;

import org.apache.http.util.EntityUtils;

import java.io.File;

import java.io.IOException;
import java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UploadServerUtils {

    public static String uploadLogFile(String uploadUrl, String filePath, String folderPath) {

        String result = null;

        try {

            HttpClient hc = new DefaultHttpClient();

            hc.getParams().setParameter(

                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            HttpPost hp = new HttpPost(uploadUrl);

            File file = new File(filePath);

            final MultipartEntity entity = new MultipartEntity();

            ContentBody contentBody = new FileBody(file);

            entity.addPart("file", contentBody);

            entity.addPart("folderPath", new StringBody(folderPath, Charset.forName("UTF-8")));

            hp.setEntity(entity);

            HttpResponse hr = hc.execute(hp);

            HttpEntity he = hr.getEntity();

            int statusCode = hr.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK)

                System.out.println("error");
                //throw new ServiceRulesException(Common.MSG_SERVER_ERROR);

            result = EntityUtils.toString(he, HTTP.UTF_8);

        } catch (Exception e) {

            e.printStackTrace();

            Log.e("TAG", "文件上传失败！上传文件为：" + filePath);

            Log.e("TAG", "报错信息toString：" + e.toString());

        }

        return result;

    }

    public static String uploadLogFiles(String uploadUrl, List<String> filePaths, String folderPath) {

        String result = null;

        try {

            HttpClient hc = new DefaultHttpClient();

            hc.getParams().setParameter(

                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            HttpPost hp = new HttpPost(uploadUrl);

            final MultipartEntity entity = new MultipartEntity();

            for (String filePath : filePaths){

                File file = new File(filePath);

                ContentBody contentBody = new FileBody(file);

                entity.addPart("files", contentBody);

            }

            entity.addPart("folderPath", new StringBody(folderPath, Charset.forName("UTF-8")));

            hp.setEntity(entity);

            HttpResponse hr = hc.execute(hp);

            HttpEntity he = hr.getEntity();

            int statusCode = hr.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK)

                System.out.println("error");
                //throw new ServiceRulesException(Common.MSG_SERVER_ERROR);

            result = EntityUtils.toString(he, HTTP.UTF_8);
            Log.e("TAG", result);

        } catch (Exception e) {

            e.printStackTrace();

            Log.e("TAG", "文件上传失败！上传文件为：" + filePaths.toString());

            Log.e("TAG", "报错信息toString：" + e.toString());

        }

        return result;

    }

    /**
     * 上传文件及参数
     */
//    public static void sendMultipart(String uploadUrl,List<File> fileList){
//        File sdcache = getExternalCacheDir();
//        int cacheSize = 10 * 1024 * 1024;
//        //设置超时时间及缓存
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
//                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
//
//
//        OkHttpClient mOkHttpClient=builder.build();
//
//        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);
//
//        int i=0;
//        for(File file:fileList){
//            if(file.exists()){
//                Log.i("imageName:",file.getName());//经过测试，此处的名称不能相同，如果相同，只能保存最后一个图片，不知道那些同名的大神是怎么成功保存图片的。
//                mbody.addFormDataPart("image"+i,file.getName(), RequestBody.create(MEDIA_TYPE_PNG,file));
//                i++;
//            }
//        }
//
//        RequestBody requestBody =mbody.build();
//        Request request = new Request.Builder()
//                .header("Authorization", "Client-ID " + "...")
//                .url(uploadUrl)
//                .post(requestBody)
//                .build();
//
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("InfoMSG", response.body().string());
//            }
//        });
//    }
}
