package nwafu.edu.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import java.io.FileNotFoundException;

import nwafu.edu.app.utils.PhotoView;

public class showdetail extends AppCompatActivity {
    private PhotoView photoView;
    Intent intent;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdetail);
        intent =getIntent();
        photoView = findViewById(R.id.photoView);
        uri = Uri.parse(intent.getStringExtra("BiggerUri"));
        try {
            photoView.setImageBitmap(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        photoView.enable();
    }
}
