package nwafu.edu.app;

import android.content.Context;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    //使用Get方式获取服务器上数据
   /* public static void sendOkHttpRequest(final String address, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }*/
    //使用Post方式向服务器上提交数据并获取返回提示数据
    public static void sendOkHttpResponse(final String address, final RequestBody requestBody, final okhttp3.Callback callback) {
        try
        {
            OkHttpClient client = new OkHttpClient();
//        JSONObject object;
            Request request = new Request.Builder()
                    .url(address).post(requestBody).addHeader("Accept", "application/json;charset=UTF-8")
                    .addHeader("Content-type", "application/json;charset=UTF-8").build();
            client.newCall(request).enqueue(callback);
        }
        catch (Exception a)
        {

        }
    }
    public static void sendOkHttpResponse(Context context, final String address, final RequestBody requestBody, final okhttp3.Callback callback) {
        try
        {
            OkHttpClient client = new OkHttpClient();
//        JSONObject object;
            Request request = new Request.Builder()
                    .url(address).post(requestBody).addHeader("Accept", "application/json;charset=UTF-8")
                    .addHeader("Content-type", "application/json;charset=UTF-8").build();
            client.newCall(request).enqueue(callback);
        }
        catch (Exception a)
        {
            Toast.makeText(context, "网络失败，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }
}