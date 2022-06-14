package nwafu.edu.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class mainpage extends AppCompatActivity {

    private Button shot;
    private Button photo;
    private ImageView showicon;
    static List<String> list =new ArrayList<>();
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(contentURI, null, null, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        shot=findViewById(R.id.bt_shot);
        photo=findViewById(R.id.bt_photo);
        showicon=findViewById(R.id.imageView);
        list.clear();

        shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mainpage.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mainpage.this, new String[]{Manifest.permission.CAMERA}, 1);
                }
                else
                {
                    Intent intent =new Intent(mainpage.this,camera.class);
                    startActivity(intent);
                }

            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mainpage.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mainpage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                }
                else
                {
                    MultiImageSelector selector = MultiImageSelector.create(mainpage.this);
                    selector.count(100);//z最大选择数量
                    selector.showCamera(false);
                    ArrayList<String> nothing=new ArrayList<>();
                    selector.origin(nothing);
                    selector.multi();//多选
                    selector.start(mainpage.this, 10);
                }

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(resultCode == RESULT_OK){
                list = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                System.out.println(list);
                Intent intent =new Intent(mainpage.this,Imageview.class);
                intent.putExtra("mode", 2);
                startActivityForResult(intent,15);

//                Bitmap bitmap = BitmapFactory.decodeFile(path.get(0));
//                showicon.setImageBitmap(bitmap);
            }
        }
        if(requestCode == 15)
        {
            if(resultCode==101)
            {
                MultiImageSelector selector = MultiImageSelector.create(mainpage.this);
                selector.count(100);//z最大选择数量
                selector.showCamera(false);
                ArrayList<String> save=new ArrayList<>();
                    for(String path : list)
                    {
                        save.add(path);
                    }
                selector.origin(save);
                selector.multi();//多选
                selector.start(mainpage.this, 10);
            }
        }

    }

}
