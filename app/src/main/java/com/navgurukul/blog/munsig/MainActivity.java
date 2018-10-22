package com.navgurukul.blog.munsig;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    Uri imageUri;
    Button uploadButton, buttonCompile;
    boolean aBoolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = findViewById(R.id.displayOcrText);
        buttonCompile = findViewById(R.id.compileButton);
        imageView = findViewById(R.id.NewPost_Image);
        uploadButton = findViewById(R.id.uploadImage);


       buttonCompile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //conditions for if image is not upload this action is not work..
               if (aBoolean == true){
                   //initializing getTextFromImage into Compile Button
                   getTextFromImage(v);
                   //resetting the image and setting original image ...
                   imageView.setImageResource(R.drawable.upload_image);
                   //Showing Toast...
                   Toast.makeText(getApplicationContext(),"Compile Success",Toast.LENGTH_LONG).show();
               }else{
//                   Alert Dialog Box.................
                   AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                           MainActivity.this);

                   // Setting Dialog Title
                   alertDialog2.setTitle("Alert...");

                   // Setting Dialog Message
                   alertDialog2.setMessage("Please upload Image ...");

                   // Setting Icon to Dialog
                   alertDialog2.setIcon(R.drawable.upload_image);


                   // Setting Negative "NO" Btn
                   alertDialog2.setNegativeButton("OK",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   // Write your code here to execute after dialog
                                   dialog.cancel();
                               }
                           });

                   // Showing Alert Dialog
                   alertDialog2.show();
               }
           }
       });

        //on click uploadButton action
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when i open my gallery and select any picture it ask for croping image
                CropImage.activity()
                        //Image Cropping Guidelines...
                        .setGuidelines(CropImageView.Guidelines.ON)
                        //setting minimum with and height cropping Guideline...
                        .setMinCropResultSize(512,512)
                        //Starting image cropping process...
                        .start(MainActivity.this);
                        //set boolean as true it help me to identify whether image is upload or not..
                        aBoolean = true;

            }
        });
    }

//ending on create method here.................................

    //On Activity Result for getting image from gallery and set it into the Image View
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();

                //get image from imageUri and set into the image View...
                imageView.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
    //Ending onActivityResult method here....................

//    Getting text from image and set it into text view
    public void getTextFromImage(View v){


        //use for digital image compose of dots...
        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());

        //use for recognize text from the image......
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        //i conditions for checking if image can be operational or not...
        if (!textRecognizer.isOperational()){
            Toast.makeText(getApplicationContext(),"Image Text Is Not Operational",Toast.LENGTH_LONG).show();
        }else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> item = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();

            //loop for getting all text from the image and add into the StringBuilder...
            for (int i = 0 ; i < item.size(); ++i){
                TextBlock textBlock = item.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            //setting image which i get from image into the image view...
            textView.setText(stringBuilder.toString());
        }
    }
    }
