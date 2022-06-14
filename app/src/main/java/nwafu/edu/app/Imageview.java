package nwafu.edu.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.soundcloud.android.crop.Crop;

import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.Luban;

import static java.lang.Thread.sleep;
import static nwafu.edu.app.mainpage.list;


public class Imageview extends AppCompatActivity {
    private RecyclerView mrv_img;
    private TextView mtv_msg;
    private Button backback;
    private Button cancel;
    private Button start;
    imgAdapter adapter;
    String result;
    public int mode;
    String resultpath;

    static File originfile;
    static File cropfile;

    String serverip = "192.168.43.112";
    private boolean backstatus=false;
    public ProgressDialog pd;
    String workingpath = Environment.getExternalStorageDirectory() + "/" + "Pictures" + "/" +"ges";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        mtv_msg = findViewById(R.id.tv_msg);
        mrv_img = findViewById(R.id.rv_img);
        backback =findViewById(R.id.button2);
        cancel=findViewById(R.id.button3);
        start=findViewById(R.id.button4);
        EventBus.getDefault().register(this);
        loadUi();

        Intent back =getIntent();
        mode = back.getIntExtra("mode",0);
        backstatus=back.getBooleanExtra("back",false);
        backback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == 2)
                {
                    Intent intent = new Intent();
                    setResult(101,intent);
                    finish();
                }
                else if(mode==1)
                {
                    finish();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == 2)
                {
                    Intent intent = new Intent();
                    setResult(101,intent);
                    finish();
                }
                else if(mode==1)
                {
                    finish();
                }
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(Imageview.this);
                pd.setMessage("正在拼接中...");
                pd.setCancelable(false);
                pd.show();


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String uploadUrl = "http://172.20.10.12:8081/aic/func/upFile";
                        List<String> filePaths =list;
                        String folderPath = "aaa";

                        List<File> filelist=new ArrayList<File>();
                        for(String path : list)
                        {
                            File img=new File(path);
                            filelist.add(img);
                        }

                        for(File f :filelist)
                        {
                            System.out.println(f.length()+"\n");
                        }
                        try {
                            Luban.with(Imageview.this).load(filelist).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        for(File f :filelist)
                        {
                            System.out.println(f.length()+"\n");
                        }

                        result = UploadServerUtils.uploadLogFiles(uploadUrl,filePaths,folderPath);
                        //UploadServerUtils.sendMultipart(uploadUrl,filelist);

                        //result="{'coordinate':coordinate,'path':path,'info':info}";
                        System.out.println(result);
                        Map<String,String> resultmap= new Gson().fromJson(result, new TypeToken<Map<String,String>>(){}.getType());

                        //resultpath = "http://172.20.10.12:8081/aic/uploadTemp/2742743caec64b8d9f1ca06da9ced316/ec8c2ca04a4f4b51a0bb92633ffa74e5/b107a48da89c4acc912ce1411418ba03.png";
                        resultpath = resultmap.get("path");
                        System.out.println("resultpath"+resultpath);
                        try {
                            download(resultpath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new MessageEvent("拼接完成！！！！"));
                        pd.cancel();
                    }

                }).start();

            }
        });
    }


    private void download(final String resultpath) throws Exception{
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
        downloada(resultpath,originfile);
        cropfile = new File(workingpath,nowtime+"C.png");
    }
    public static void downloada(String urlString ,File file) throws Exception {
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

    private void loadUi() {
        if (list != null && list.size() > 0) {
            GridLayoutManager layoutManager;
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                layoutManager = new GridLayoutManager(Imageview.this, 2);//spancount 每行个数
            }
            else
            {
                layoutManager = new GridLayoutManager(Imageview.this, 4);//spancount 每行个数
                //横屏
            }
            mrv_img.setLayoutManager(layoutManager);
            List<String> showlist=new ArrayList<>();
            for(String path : list)
            {
                showlist.add(path);
            }
            adapter = new imgAdapter(showlist);
            mrv_img.setAdapter(adapter);
        } else {
            mtv_msg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (list != null && list.size() > 0) {
            GridLayoutManager layoutManager;
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                layoutManager = new GridLayoutManager(Imageview.this, 2);//spancount 每行个数
            }
            else
            {
                layoutManager = new GridLayoutManager(Imageview.this, 4);//spancount 每行个数
                //横屏
            }
            mrv_img.setLayoutManager(layoutManager);
            adapter = new imgAdapter(list);
            mrv_img.setAdapter(adapter);
        } else {
            mtv_msg.setVisibility(View.VISIBLE);
        }
    }
        class imgAdapter extends RecyclerView.Adapter<imgAdapter.ViewHolder> {
            private List<String> list;

            public imgAdapter(List<String> list) {
                this.list=list;
                this.list.add("plus");
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                View myView;
                TextView tv_name;
                ImageView iv_img;
                ViewGroup item_bg;
                ImageButton ib_delete;

                public ViewHolder(View itemView) {
                    super(itemView);
                    myView = itemView;
                    tv_name = itemView.findViewById(R.id.tv_name);
                    iv_img = itemView.findViewById(R.id.iv_img);
                    item_bg = itemView.findViewById(R.id.item_bg);
                    ib_delete =itemView.findViewById(R.id.imageButton);
                }
            }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(Imageview.this).inflate(R.layout.img_item, parent, false);

            return new ViewHolder(v);
        }
    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String img = list.get(position);
        if(img.equals("plus"))
        {
            holder.iv_img.setImageResource(R.drawable.selector_item_add);
            holder.item_bg.setBackgroundColor(Color.TRANSPARENT);
            holder.ib_delete.setVisibility(View.GONE);
            holder.myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mode == 2)
                    {
                        Intent intent = new Intent();
                        setResult(101,intent);
                        finish();
                    }
                    else if(mode==1)
                    {
                        finish();
                    }
                }
            });
        }
        else
        {
            Uri imguri= Uri.fromFile(new File(img));
            if (imguri != null) {
                Glide.with(Imageview.this)
                        .load(imguri)
                        .fitCenter()
                        //.centerCrop()
                        .placeholder(R.mipmap.place)
                        .into(holder.iv_img);
                holder.tv_name.setVisibility(View.VISIBLE);
                holder.item_bg.setBackground(getDrawable(R.drawable.selector_btn_item));
                holder.ib_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                holder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View ve) {
                        Intent showbigger =new Intent(Imageview.this,showdetail.class);
                        String img = list.get(position);
                        Uri imguri= Uri.fromFile(new File(img));
                        showbigger.putExtra("BiggerUri",String.valueOf(imguri));
                        startActivity(showbigger);
                    }
                });
            } else return;
        }

    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void Onconnected(MessageEvent messageEvent) {
        System.out.println(messageEvent.getMessage());
        if(messageEvent.getMessage().equals("拼接完成！！！！")) {
            Intent deal = new Intent(Imageview.this, result.class);
            deal.putExtra("result", result);
//            originfile = new File(workingpath,"20220614163252643O.png");
//            cropfile = new File(workingpath,"20220614163252643C.png");
            deal.putExtra("originfile",String.valueOf(Uri.fromFile(originfile)));
            deal.putExtra("cropfile",String.valueOf(Uri.fromFile(cropfile)));
           // deal.putExtra("originfile",sb);

            System.out.println(originfile);
            startActivity(deal);
        }
    }
}
