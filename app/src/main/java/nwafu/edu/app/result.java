package nwafu.edu.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.draggable.library.extension.ImageViewerHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static nwafu.edu.app.Imageview.cropfile;
import static nwafu.edu.app.Imageview.originfile;

public class result extends AppCompatActivity {
    String result;
    private Button crop;
    private ImageView showresult;
    String picpath;
    String workingpath = Environment.getExternalStorageDirectory() + "/" + "Pictures" + "/" +"ges";
    String originuri;
    String cropuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

            StrictMode.setVmPolicy(builder.build());

        }
        Intent back =getIntent();
        result= back.getStringExtra("result");
        originuri=back.getStringExtra("originfile");
        cropuri=back.getStringExtra("cropfile");

        result=result.replace("{","");
        result=result.replace("}","");
        System.out.println(result+"\n!!!!!1");
        //System.out.println(getStringToMap(result).get("path"));
        crop=findViewById(R.id.button9);
        showresult =findViewById(R.id.showresult);
        Glide.with(this).load(Uri.parse(originuri)).into(showresult);
        //newfile();
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageUri(result.this,Uri.parse(originuri),Uri.parse(cropuri),3, 4, 3000, 4000, 250);
            }
        });
        showresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setDataAndType(Uri.parse(originuri), "image/*");
                startActivity(intent);
            }
        });
    }
    public static void download(String urlString ,File file) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        FileOutputStream os = new FileOutputStream(file, true);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }
    private void newfile() {
        File newdir = new File(workingpath);
        if (!newdir.exists()) {
            newdir.mkdir();
            System.out.println("创建文件夹路径为：" + workingpath);
        }
        workingpath=workingpath+ "/" +"result";
        newdir = new File(workingpath);
        if (!newdir.exists()) {
            newdir.mkdir();
            System.out.println("创建文件夹路径为：" + workingpath);
        }
        Date now = new Date(System.currentTimeMillis());
        String nowtime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(now);
        originfile =new File(workingpath,nowtime+"O.png");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        download(picpath,originfile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        cropfile = new File(workingpath,nowtime+"C.png");
    }

    public static Map<String, String> getStringToMap(String str) {
        // 判断str是否有值
        if (null == str || "".equals(str)) {
            return null;
        }
        // 根据&截取
        String[] strings = str.split(",");
        // 设置HashMap长度
        int mapLength = strings.length;
        Map<String, String> map = new HashMap<>(mapLength);
        // 循环加入map集合
        for (String string : strings) {
            // 截取一组字符串
            String[] strArray = string.split(":");
            // strArray[0]为KEY strArray[1]为值
            strArray[0]=strArray[0].replace("","");
            map.put(strArray[0], strArray[1]);
        }
        return map;
    }
    public void cropImageUri(Activity activity, Uri orgUri, Uri desUri, int aspectX, int aspectY, int width, int height, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        System.out.println("croppppp!!!!!");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(orgUri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        //intent.putExtra("aspectX",aspectX);
        //intent.putExtra("aspectY", aspectY);
        // outputX outputY 是裁剪图片宽高
        //intent.putExtra("outputX", width);
        //intent.putExtra("outputY", height);
        intent.putExtra("scale", true);
        //将剪切的图片保存到目标Uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        //裁减小图返回bitmap,设置为true，裁剪大图使用false，返回uri
        intent.putExtra("return-data", false);
        //去除默认的人脸识别，否则和剪裁匡重叠
        intent.putExtra("noFaceDetection", true);
        //intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        startActivityForResult(intent,200);//这里就将裁剪后的图片的Uri返回了
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            showresult.setImageURI(Uri.fromFile(cropfile));
        }
    }
}
