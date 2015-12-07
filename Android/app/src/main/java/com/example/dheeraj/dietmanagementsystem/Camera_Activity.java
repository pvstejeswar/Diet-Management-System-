package com.example.dheeraj.dietmanagementsystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
//import android.provider.SyncStateContract;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.photoalbum.Constants;
//import com.cloudinary.photoalbum.PhotoAlbumApplication;
import com.cloudinary.utils.ObjectUtils;
//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.nostra13.universalimageloader.utils.L;
//import com.parse.ParseCloud;
//import com.parse.ParseException;
//import com.parse.ParseObject;

//import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import org.apache.commons.codec.binary.Base64;

public class Camera_Activity extends Activity implements View.OnClickListener {
    private final Activity current = this;
    Spinner spinner_hgt;
    private static String logtag = "CameraApp";
    private static int TAKE_PICTURE = 1;
    public Uri imageUri;
    Intent temp;
    String imgPath;
    String filename = "";
    /*Uploading image to server*/
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String SERVER_ADDRESS = "";

    Button button_upload;
    ImageView image_camera;
    ImageView imageToUpload;
    TextView personname;

    //progressbar
    private ProgressDialog progress;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_);

        //displaying the username after welcome
        personname = new TextView(this);
        personname = (TextView) findViewById(R.id.user_name_cam);
        personname.setText(Global.getusername);

        image_camera = (ImageView) findViewById(R.id.image_camera);
        Button cameraButton = (Button) findViewById(R.id.button_camera);
        //cameraButton.setOnClickListener(cameraListener);

        spinner_hgt = (Spinner) findViewById(R.id.spinner_height);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.height, android.R.layout.simple_spinner_item);
        spinner_hgt.setAdapter(adapter);
        //spinner_hgt.setOnItemSelectedListener(this);

        /*Image and server*/
        button_upload = (Button) findViewById(R.id.button_upload);
        button_upload.setOnClickListener(this);

        image_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery(1);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void download(View view){
        progress=new ProgressDialog(this);
        progress.setMessage("Downloading Music");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        final int totalProgressTime = 100;
        final Thread tp = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(jumpTime < totalProgressTime) {
                    try {
                        sleep(200);
                        jumpTime += 5;
                        progress.setProgress(jumpTime);
                    }
                    catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        tp.start();
    }



    public void openGallery(int req_code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
        //startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (requestCode == 0) {
                filename = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                image_camera.setImageURI(selectedImageUri);
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//                                "cloud_name", "dl6fi0f2o",
//                                "api_key", "172322623958866",
//                                "api_secret", "Z-GZmxmje-eAqW1BLeuglpv9AeU"));
//
//                        //Cloudinary cloudinary = new Cloudinary(config);
//
//
//                        try {
//                            Map result = cloudinary.uploader().upload(new File(filename),
//                                    ObjectUtils.asMap("public_id", "sample_remote"));
//
////                    Thread.sleep(2000);
////                    String ur = cloudinary.url().generate("sample_remote.jpg");
////                    Android_Error("filename" + ur);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                t.start();

                //try {

                //  t.join();
                //} catch (InterruptedException e) {
                //   e.printStackTrace();
                //}


                // System.out.println("selectedPath1 : " + selectedPath1);

            }


//            if (requestCode == SELECT_FILE2)
//
//            {
//
//                selectedPath2 = getPath(selectedImageUri);
//
//                System.out.println("selectedPath2 : " + selectedPath2);
//
//            }

            //tv.setText("Selected File paths : " + selectedPath1 + "," + selectedPath2);

        }


//    public String getPath(Uri uri) {
//
//        String[] projection = { MediaStore.Images.Media.DATA };
//
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//        cursor.moveToFirst();
//
//        return cursor.getString(column_index);
//
//    }
//
//
//
//    private View.OnClickListener cameraListener = new View.OnClickListener() {
//        public void onClick(View v) {
//            takePhoto(v);
//        }
//    };
//
//    private void takePhoto(View v) {
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
//        imageUri = Uri.fromFile(photo);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, TAKE_PICTURE);
//    }
//
//    private void startUpload(String filePath) {
//        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
//            protected String doInBackground(String... paths) {
//                // L.d("Running upload task");
//
//                // sign request
//                Map<String, String> uploadParams;
//                try {
//                    // Parse+Cloudinary: retrieves a Cloudinary signature and upload params using the Parse cloud function.
//                    //   see https://github.com/cloudinary/cloudinary_parse
//                    HashMap<String, String> args = new HashMap<String, String>();
//                    uploadParams = ParseCloud.callFunction(Constants.PARSE_SIGN_CLOUD_FUNCTION, args);
//                    // L.i("Signed request: %s", uploadParams.toString());
//                } catch (ParseException e) {
//                    // L.e(e, "Error signing request");
//                    return "Error signing request: " + e.toString();
//                }
//
//                // Upload to cloudinary
//                Cloudinary cloudinary = PhotoAlbumApplication.getInstance(current).getCloudinary();
//                File file = new File(paths[0]);
//                @SuppressWarnings("rawtypes")
//                Map cloudinaryResult;
//                try {
//                    // Cloudinary: Upload file using the retrieved signature and upload params
//                    cloudinaryResult = cloudinary.uploader().upload(file, uploadParams);
//                    //L.i("Uploaded file: %s", cloudinaryResult.toString());
//                } catch (RuntimeException e) {
//                    //L.e(e, "Error uploading file");
//                    return "Error uploading file: " + e.toString();
//                } catch (IOException e) {
//                    //L.e(e, "Error uploading file");
//                    return "Error uploading file: " + e.toString();
//                }
//
//                // update parse
//                ParseObject photo = new ParseObject("Photo");
//                try {
//                    // Parse+Cloudinary: Save a reference to the uploaded image in Parse backend. The
//                    //   field may be verified using the beforeSave filter demonstrated in:
//                    //   https://github.com/cloudinary/cloudinary_parse
//                    photo.put(Constants.PARSE_CLOUDINARY_FIELD, cloudinary.signedPreloadedImage(cloudinaryResult));
//                    photo.save();
//                   // L.i("Saved object");
//                } catch (Exception e) {
//                   // L.e(e, "Error saving object");
//                    return "Error saving object: " + e.toString();
//                }
//                return null;
//            }
//        };
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        Uri selectedImage;
////        if (resultCode == Activity.RESULT_OK) {
////            selectedImage = imageUri;
////            getContentResolver().notifyChange(selectedImage, null);
////
////            ImageView imageView = (ImageView) findViewById(R.id.image_camera);
////            ContentResolver cr = getContentResolver();
////            Bitmap bitmap;
////
////            try {
////                bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
////                imageView.setImageBitmap(bitmap);
////                Toast.makeText(Camera_Activity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
////            } catch (Exception e) {
////                Log.e(logtag, e.toString());
////            }
////        }
//        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && intent!= null)
//        {
//            Uri selectedImage1 = intent.getData();
////            String[] filePathColumn = { MediaStore.Images.Media.DATA };
////
////            // Get the cursor
////            Cursor cursor = getContentResolver().query(selectedImage1,
////                    filePathColumn, null, null, null);
////            // Move to first row
////            cursor.moveToFirst();
////
////            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////            imgPath = cursor.getString(columnIndex);
////            cursor.close();
////            ImageView imgView = (ImageView) findViewById(R.id.image_camera);
////            // Set the Image in ImageView
////            imgView.setImageBitmap(BitmapFactory
////                    .decodeFile(imgPath));
////            // Get the Image's file name
////            String fileNameSegments[] = imgPath.split("/");
////            fileName = fileNameSegments[fileNameSegments.length - 1];
//            // Put file name in Async Http Post Param which will used in Java web app
//
//
//            image_camera.setImageURI(selectedImage1);
//        }
//
//    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera_, menu);
        return true;
    }

//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                TextView myText = (TextView) view;
//                Toast.makeText(this, "You selected " + myText.getText(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }

    //          @Override
    //           public void onClick(View v) {
    //   switch (v.getId()) {
//            case R.id.image_camera:
//
//                imgPath= "";
//                //Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                temp = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                Uri selectedImage = temp.getData();
//               // String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//                // Get the cursor
//                Cursor cursor = getContentResolver().query(selectedImage,
//                        null, null, null, null);
//                // Move to first row
//                if (cursor == null)
//                {
//                    imgPath = selectedImage.getPath();
//                }
//                else
//                {
//                    cursor.moveToFirst();
//                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                    imgPath = cursor.getString(idx);
//                    cursor.close();
//                }
//
//
////                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////                imgPath = cursor.getString(columnIndex);
//
//                startActivityForResult(temp, RESULT_LOAD_IMAGE);
//                Android_Error(imgPath);
//                break;
//            case R.id.button_upload:
//                Android_Error(imgPath);

//                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//                        "cloud_name", "dl6fi0f2o",
//                        "api_key", "172322623958866",
//                        "api_secret", "Z-GZmxmje-eAqW1BLeuglpv9AeU"));
//
//                //Cloudinary cloudinary = new Cloudinary(config);
//
//
//                try {
//                    Map result = cloudinary.uploader().upload(new File(imgPath),
//                            ObjectUtils.asMap("public_id", "sample_remote"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


//                Map result = cloudinary.uploader().upload(new File("C:\\Users\\iarora\\Desktop\\ccna\\tiger1.jpg"), ObjectUtils.asMap(
//                        "public_id", "sample_remote",
//                        "transformation", new Transformation().crop("limit").width(40).height(40),
//                        "eager", Arrays.asList(
//                                new Transformation().width(200).height(200)
//                                        .crop("thumb").gravity("face").radius(20)
//                                        .effect("sepia"),
//                                new Transformation().width(100).height(150)
//                                        .crop("fit").fetchFormat("jpg")
//                        )));


//                Bitmap image;
//                BitmapFactory.Options options = null;
//                options = new BitmapFactory.Options();
//                options.inSampleSize = 3;
//                image = BitmapFactory.decodeFile(imgPath,
//                        options);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                // Must compress the Image to reduce image size to make upload easy
//                image.compress(Bitmap.CompressFormat.JPEG, 50, stream);
//                String k = stream.toString();
//                byte[] byte_arr = new byte[0];
//                try {
//                    byte_arr = k.getBytes("UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                // Encode Image to String
//                final String encodedImage = Base64.encodeToString(byte_arr, Base64.DEFAULT);
//            // Get the Image's file name
//
//
//
//
//               //Bitmap image = ((BitmapDrawable) image_camera.getDrawable()).getBitmap();
////               // int bytes = image.getByteCount();
//////or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
//////int bytes = b.getWidth()*b.getHeight()*4;
////
////              //  ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
////              //  image.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
////
////              //  byte[] array = buffer.array();
////                ByteArrayOutputStream bytesArrayOutputStream = new ByteArrayOutputStream();
////                image.compress(Bitmap.CompressFormat.JPEG, 100, bytesArrayOutputStream);
////                final String encodedImage = Base64.encodeToString(bytesArrayOutputStream.toByteArray(),Base64.DEFAULT);
//
//                Android_Error("clicked");
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //new UploadImage().execute();
//                        System.out.println("hello");
//
//
//                        String string = "{" + "Image :" + "\"" + encodedImage + "\"" + "}";
//                        System.out.println("String is" + string);
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(string);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println("Json Object " + jsonObject);
//
//                        // Step2: Now pass JSON File Data to REST Service
//                        try {
//                            URL url = new URL("http://192.168.43.4:8080/cmpe220/webapi/crunchifyService");
//                            URLConnection connection = url.openConnection();
//                            connection.setDoOutput(true);
//                            connection.setRequestProperty("Content-Type", "application/json");
//                            connection.setConnectTimeout(5000);
//                            connection.setReadTimeout(5000);
//                            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
//                            out.write(jsonObject.toString());
//                            out.close();
//
//                            Thread.sleep(2000);
//                            String temp = null;
//
//                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//                            while((temp = in.readLine()) != null) {
//                                System.out.println(temp);
//
//                            }
//                            System.out.println("\nCrunchify REST Service Invoked Successfully..");
//                            in.close();
//                        } catch (Exception e) {
//                            System.out.println("\nError while calling Crunchify REST Service");
//                            System.out.println(e);
//                        }
//
//                    }
//                });
//                t.start();
//                try {
//                    t.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
    //      break;
    //  }


    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }

    private void Android_Error(String error) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Camera_Activity.this);
        dialogBuilder.setMessage(error);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_upload:
                //parse total string here and pass that to the hashmap in nutrible.java file
                //how to create a global hashmap?
                //

//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//                                "cloud_name", "dl6fi0f2o",
//                                "api_key", "172322623958866",
//                                "api_secret", "Z-GZmxmje-eAqW1BLeuglpv9AeU"));
//                        try {
//                            cloudinary.uploader().destroy("sample_remote", ObjectUtils.emptyMap());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        //Cloudinary cloudinary = new Cloudinary(config);
//
//
//                        try {
//                            if (filename == "") {
//                                Android_Error("Please seect image");
//                            } else {
//                                //  Android_Error(filename);
//                                Map result = cloudinary.uploader().upload(new File(filename),
//                                        ObjectUtils.asMap("public_id", "sample_remote"));
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

                Thread t =new Thread(new Runnable() {
                    @Override
                    public void run() {

                       // FileInputStream imageInFile = null;
                        Bitmap bm = BitmapFactory.decodeFile(filename);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
//                        try {
//                            imageInFile = new FileInputStream(filename);
//                        } catch (FileNotFoundException e1) {
//                            e1.printStackTrace();
//                        }
//                        byte imageData[] = new byte[(int) filename.length()];
//                        try {
//                            imageInFile.read(imageData);
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
                        String string;
//                        // Converting Image byte array into Base64 String
//                        String imageDataString = Base64.encodeToString(imageData, Base64.DEFAULT);
                       // String imageDataString = Base64.encodeBase64URLSafeString(imageData);
                        System.out.println(imageEncoded);
                        string = "{" + "Image :" + "\"" + imageEncoded + "\"" + "}";
                        System.out.println("String is" + string);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(string);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("Json Object " + jsonObject);

                        // Step2: Now pass JSON File Data to REST Service
                        try {
                            URL url = new URL("http://192.168.0.39:8080/DMSserver/webapi/xyz/upload");
                            URLConnection connection = url.openConnection();
                            connection.setDoOutput(true);
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setConnectTimeout(5000);
                            connection.setReadTimeout(5000);
                            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                            out.write(jsonObject.toString());
                            out.close();
                           // imageInFile.close();
                            Thread.sleep(2000);
                            String temp = null;
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                            while((temp = in.readLine()) != null) {
                                System.out.println(temp);
                            }
                            System.out.println("\nCrunchify REST Service Invoked Successfully..");
                            in.close();
                           // HttpResponse response = client.execute(request);
                            System.out.println("2");
                           // System.out.println(response);
//                            BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                            StringBuilder total = new StringBuilder();
//                            String line = null;
//                            while ((line = r.readLine()) != null) {
//                                total.append(line);
//                            }
//                            System.out.println(total);
//                            //storing total in a global string
//                            Global.nutri_info = total.toString();

                      //      r.close();
                        } catch (Exception e) {
                            System.out.println("\nError while calling Crunchify REST Service");
                            System.out.println(e);
                        }

                    }




                });

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Your code goes here
                            System.out.println("1");
                            HttpClient client = new DefaultHttpClient();
                            System.out.println("1");
                            HttpGet request = new HttpGet("http://192.168.0.39:8080/DMSserver/webapi/products/xyz");
                            System.out.println("1");
                            HttpResponse response = client.execute(request);
                            System.out.println("2");
                            System.out.println(response);
                            BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                            StringBuilder total = new StringBuilder();
                            String line = null;
                            while ((line = r.readLine()) != null) {
                                total.append(line);
                            }
                            System.out.println(total);
                            //storing total in a global string
                            Global.nutri_info = total.toString();
                            //start


                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                        }
                    }
                });


                t.start();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                thread.start();
//                final ProgressDialog progress;
//                //loading screen
//                progress=new ProgressDialog(this);
//                progress.setMessage("Downloading Music");
//                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                progress.setIndeterminate(true);
//                progress.setProgress(0);
//                progress.show();
//
//                final int totalProgressTime = 100000;
//                final Thread t2 = new Thread() {
//                    @Override
//                    public void run() {
//                        int jumpTime = 0;
//
//                        while(jumpTime < totalProgressTime) {
//                            try {
//                                sleep(200);
//                                jumpTime += 5;
//                                progress.setProgress(jumpTime);
//                            }
//                            catch (InterruptedException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                };
//                t2.start();
//                /***************/
//                int a= 0;
                try {
                    t.join();
                    thread.join();
                    //t2.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                startActivity(new Intent(this, nutritable.class));
                break;

        }
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Camera_ Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.example.dheeraj.dietmanagementsystem/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Camera_ Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.example.dheeraj.dietmanagementsystem/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
}