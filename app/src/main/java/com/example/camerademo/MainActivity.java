package com.example.camerademo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    FrameLayout cameraLayout;
    private static final int CAM_PERMISSION = 1000;
    private ImageSurface mImageSurface;
    Camera camera;
    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap == null) {
                Toast.makeText(MainActivity.this, "Captured Image is empty", Toast.LENGTH_LONG);
                return;
            }
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageBitmap(bitmap);
            imageView.setRotation(90f);

            ((LinearLayout) findViewById(R.id.captured_images)).addView(imageView);
            camera.startPreview();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraLayout = findViewById(R.id.Frame_Image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAM_PERMISSION);


        }
        Button captureButton = findViewById(R.id.capture_button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, pictureCallback);
            }
        });


    }

    protected void onResume() {
        super.onResume();
        camera = initDeviceCamera();
        mImageSurface = new ImageSurface(MainActivity.this, camera);
        cameraLayout.addView(mImageSurface);

    }

    private Camera initDeviceCamera() {
        Camera mcamera = null;
        try {
            mcamera = mcamera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mcamera;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAM_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG);
            }
            else
            {
                Toast.makeText(this,"camera permission not granted",Toast.LENGTH_LONG);
            }
        }

    }

}