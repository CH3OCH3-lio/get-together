package nwafu.edu.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;

public class photoview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        Intent intent =getIntent();
        Uri imguri= Uri.parse(intent.getStringExtra("BiggerUri"));
        photoView.setImageURI(imguri);

    }
}
