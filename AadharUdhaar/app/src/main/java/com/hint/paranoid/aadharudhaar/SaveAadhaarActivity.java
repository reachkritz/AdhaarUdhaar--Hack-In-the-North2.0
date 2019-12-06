package com.hint.paranoid.aadharudhaar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;

public class SaveAadhaarActivity extends AppCompatActivity {

    private FancyButton getImageGallery;
    private int RESULT_LOAD_IMAGE=123;
    private int MY_PERMISSIONS_REQUEST_MEDIA=124;
    private int CAMERA_REQUEST=125;
    ImageView adharimage;
    Uri outPutfileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_aadhaar);

        getImageGallery=(FancyButton)findViewById(R.id.get_image_gallery);
        getImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        if (ContextCompat.checkSelfPermission(SaveAadhaarActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SaveAadhaarActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(SaveAadhaarActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_MEDIA);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            adharimage = (ImageView) findViewById(R.id.adharimage);
            adharimage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        /*if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data!=null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            adharimage = (ImageView) findViewById(R.id.adharimage);
            adharimage.setImageBitmap(photo);
        }*/

        /*if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data!=null) {
            String uri = outPutfileUri.toString();
            //Log.e("uri-:", uri);
            Toast.makeText(this, outPutfileUri.toString(), Toast.LENGTH_LONG).show();

            //Bitmap myBitmap = BitmapFactory.decodeFile(uri);
            // mImageView.setImageURI(Uri.parse(uri));   OR drawable make image strechable so try bleow also

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                adharimage = (ImageView) findViewById(R.id.adharimage);
                adharimage.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if(requestCode==MY_PERMISSIONS_REQUEST_MEDIA) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getBaseContext(),"PERMISSION GRANTED",Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(getBaseContext(),"PERMISSION NOT GRANTED",Toast.LENGTH_SHORT).show();
            }
            return;
        }

    }
}
