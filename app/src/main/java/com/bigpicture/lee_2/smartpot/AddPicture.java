package com.bigpicture.lee_2.smartpot;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class AddPicture extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;
    private int id_view;
    private String absoultePath = null;
    private String filePath = "";
    private Uri mImageCaptureUri;
    private Bitmap photo;
    private ImageButton myEmtyBtn;
    String imageString =" ";
    String pythonURL = "http://110.46.204.146:8000/?base64=";
    String imagePlantName = " ";
    EditText plantNameEdit;
    ProgressDialog dialog;
    String jsonString;
    Boolean isPlantSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);

        plantNameEdit = (EditText) findViewById(R.id.plantNameEdit);
        myEmtyBtn = (ImageButton) findViewById(R.id.emtyImage);
    }

    public void onNextClicked(View view) {
        if(isPlantSet) {
            Intent retrunIntent = new Intent();
            String stringPhoto = imageString;
            String stringName = "";
            if (plantNameEdit.getText() != null) {
                stringName = plantNameEdit.getText().toString();
            }
            retrunIntent.putExtra("photo", stringPhoto);
            retrunIntent.putExtra("name", stringName);
            setResult(RESULT_OK, retrunIntent);
            finish();
        }else{
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
            new AlertDialog.Builder(this)
                    .setTitle("유효한 식물명이 아닙니다.")
                    .setPositiveButton("확인",cancelListener)
                    .show();
        }

    }

    public void onSendBtnClicked(View view){
        if(imageString.length()==0){
            imageString = "error";
        }
        dialog = new ProgressDialog(AddPicture.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("이미지를 분석하는 중입니다.");

        Log.d("입력","입력");
        if(imageString.length() !=0){
            Log.d("imageString",imageString);
        }
        imageString = imageString.replace("\n","");
        imageString = imageString.replace(" ","");
        String imageServerUrl = pythonURL.concat(imageString);
        BackGroundTask backGroundTask = new BackGroundTask(dialog, imageServerUrl,plantNameEdit);
        backGroundTask.execute();
        try {
            jsonString = backGroundTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            JsonParser jp = new JsonParser(jsonString);
            TransLang tl = new TransLang(jp.DoJsonPasing("0"));
            imagePlantName = tl.GetKorName();
            plantNameEdit.setText(imagePlantName);
        }catch (Exception e){
            e.printStackTrace();
        }

//        GetParsingData gpd = new GetParsingData(pythonURL+imageString);
//        gpd.start();
//        Log.d("tempUrl",tempUrl);
//        try {
//            gpd.join();
//            dialog.dismiss();
//            JsonParser jp = new JsonParser(gpd.GetResult());
//            Log.d("결과",gpd.GetResult());
//            imagePlantName = jp.DoJsonPasing("0");
//            plantNameEdit.setText(imagePlantName);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void onSearchClicked(View view) {
        String contentsNum = "0";
        GetParsingData gpd = new GetParsingData("http://api.nongsaro.go.kr/service/garden/gardenList?apiKey=20171010HECT2JBCAWKELEKZWLHA&sType=sCntntsSj&sText=" + plantNameEdit.getText());
        gpd.start();
        try {
            gpd.join();
            XmlParser xp = new XmlParser(gpd.GetResult());
            if (xp.DoXmlParser("cntntsNo").length() != 0)
                contentsNum = xp.DoXmlParser("cntntsNo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!contentsNum.equals("0")) {
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
            isPlantSet =true;
            new AlertDialog.Builder(this)
                    .setTitle("본식물명은 유효합니다.")
                    .setPositiveButton("확인", cancelListener)
                    .show();
        } else if (contentsNum.equals("0")) {
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
            isPlantSet =false;
            new AlertDialog.Builder(this)
                    .setTitle("검색결과없습니다.")
                    .setPositiveButton("확인", cancelListener)
                    .show();
        }
    }

    public void onCancelClicked(View view) {
        finish();
    }

    public void onPictureClicked(View v) {
        id_view = v.getId();
        if (v.getId() == R.id.emtyImage) {
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakePhotoAction();
                }
            };
            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakeAlbumAction();
                }
            };
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("사진촬영", cameraListener)
                    .setNeutralButton("앨범선택", albumListener)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        }

    }

    public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행
                mImageCaptureUri = data.getData();
                Log.d("SmartWheel", mImageCaptureUri.getPath().toString());
            }

            case PICK_FROM_CAMERA: {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
                intent.putExtra("outputX", 400); //  x축 크기
                intent.putExtra("outputY", 400);
                intent.putExtra("aspectX", 1); // 비율
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_iMAGE); // CROP_FROM_CAMERA case문 이동
                break;
            }
            case CROP_FROM_iMAGE: {
                if (resultCode != RESULT_OK) { return; }
                final Bundle extras = data.getExtras();
                ChangeStrBitmap changeStrBitmap = new ChangeStrBitmap();
                filePath = "tmp_" + System.currentTimeMillis() + ".jpg";
                if (extras != null) {
                    photo = extras.getParcelable("data"); // CROP된 BITMAP
                    myEmtyBtn.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌
                    imageString = changeStrBitmap.BitmapToString(photo);
                    Log.d("이미지스트링",imageString);
                    storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.
                    absoultePath = filePath;
                    break;
                } else if (extras == null) {
                    Bitmap bit = BitmapFactory.decodeResource(getResources(), R.id.emtyImage);
                    imageString = changeStrBitmap.BitmapToString(bit);
                }
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
            }
        }
    }

    public void storeCropImage(Bitmap bitmap, String filePath) {
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;
        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}