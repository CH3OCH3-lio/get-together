package nwafu.edu.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nwafu.edu.app.utils.ImgUtils;

import static nwafu.edu.app.mainpage.list;

public class camera extends AppCompatActivity {

    private SurfaceView sfv_preview;
    private Button btn_take;
    private Button btn_cancel;
    private Button btn_next;
    private Camera camera = null;


    private RecyclerView mrv_img2;
    private TextView mtv_msg2;
    private String workingpath = Environment.getExternalStorageDirectory() + "/" + "Pictures" + "/" +"ges";
    imgAdapter adapter2;
    private SurfaceHolder.Callback cpHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            startPreview();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopPreview();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        File newdir = new File(workingpath);
        if (!newdir.exists()) {
            newdir.mkdir();
            System.out.println("创建文件夹路径为：" + workingpath);
        }
        mrv_img2=findViewById(R.id.rv_img2);
        mtv_msg2=findViewById(R.id.tv_msg2);
        btn_cancel=findViewById(R.id.button7);
        btn_next=findViewById(R.id.button8);
        System.out.println("!!!!!!"+list);
        //list =new ArrayList<>();
        loadUi();
        bindViews();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(camera.this,mainpage.class);
                startActivity(intent);
                finish();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(camera.this,Imageview.class);
                intent.putExtra("mode",1);
                startActivity(intent);
            }
        });
    }
    private void loadUi() {
            GridLayoutManager layoutManager;
                layoutManager = new GridLayoutManager(camera.this, 1);//spancount 每行个数
                layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            mrv_img2.setLayoutManager(layoutManager);
            adapter2 = new imgAdapter(list);
            mrv_img2.setAdapter(adapter2);
    }

    class imgAdapter extends RecyclerView.Adapter<imgAdapter.ViewHolder> {
        private List<String> list;

        public imgAdapter(List<String> list) {
            this.list = list;
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
            View v = LayoutInflater.from(camera.this).inflate(R.layout.img_item2, parent, false);

            return new ViewHolder(v);
        }
        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            final String img = list.get(position);
            Uri imguri= Uri.fromFile(new File(img));
            if (imguri != null) {
                Glide.with(camera.this)
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
                        adapter2.notifyDataSetChanged();
                        System.out.println("!!!!!!"+list);
                    }
                });

                holder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View ve) {
                        Intent showbigger =new Intent(camera.this,showdetail.class);
                        String img = list.get(position);
                        Uri imguri= Uri.fromFile(new File(img));
                        showbigger.putExtra("BiggerUri",String.valueOf(imguri));
                        startActivity(showbigger);
                    }
                });
            } else return;
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }


    private void bindViews() {
        sfv_preview = (SurfaceView) findViewById(R.id.surfaceView2);
        btn_take = (Button) findViewById(R.id.button6);
        sfv_preview.getHolder().addCallback(cpHolderCallback);

        btn_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        String path = "";
                        if ((path = saveFile(data)) != null) {
                            list.add(path);
                            adapter2.notifyDataSetChanged();
//                            Toast.makeText(camera.this, path , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(camera.this, "保存照片失败", Toast.LENGTH_SHORT).show();
                        }
                        //stopPreview();
                        camera.startPreview();
                    }
                });
            }
        });
    }
    private String saveFile(byte[] bytes){
        try {
            Date now = new Date(System.currentTimeMillis());
            String nowtime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(now);
            File file = new File(workingpath,nowtime+".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            Bitmap bitmap = BitmapFactory.decodeFile(workingpath+"/"+nowtime+".jpg");
            if (bitmap == null)
                return null;

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            // Setting post rotate to 90
            Matrix mtx = new Matrix();
            mtx.postRotate(270);
            Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90,baos);
            file = new File(workingpath,nowtime+".jpg");
            fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();

            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    private void startPreview(){
        camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        params.setPictureSize(4000, 3000);
        System.out.println(params.getSupportedPictureSizes());
        camera.setParameters(params);
        try {
            camera.setPreviewDisplay(sfv_preview.getHolder());
            camera.setDisplayOrientation(90);   //让相机旋转90度
            camera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void stopPreview() {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

}
