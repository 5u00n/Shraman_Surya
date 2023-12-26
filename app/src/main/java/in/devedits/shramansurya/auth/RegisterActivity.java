package in.devedits.shramansurya.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import in.devedits.shramansurya.R;
import in.devedits.shramansurya.model.Profiles;
import in.devedits.shramansurya.user.UserDashboardActivity;

public class RegisterActivity extends AppCompatActivity {


    Context context;

    DatePickerDialog picker;

    Button saveButton;
    ImageView profile,back_button;
    TextInputEditText name,email;
    TextView birthday,anniversary,title;
    RadioGroup gender, isMarried;
    ImageButton bProfile;
    TextView imgUrl;


    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    private Uri imagepath;
    private static final int PICK_IMAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = getBaseContext();

        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        reference= database.getReference("profiles").child(auth.getUid().toString());


        saveButton= findViewById(R.id.reg_save);
        back_button= findViewById(R.id.reg_back);

        profile= findViewById(R.id.reg_profile_image);
        name= findViewById(R.id.reg_name);
        email= findViewById(R.id.reg_email);
        birthday= findViewById(R.id.reg_dob);
        anniversary= findViewById(R.id.reg_anniversary);
        title= findViewById(R.id.reg_title);

        gender= findViewById(R.id.reg_gender);
        isMarried= findViewById(R.id.reg_isMarried);

        bProfile= findViewById(R.id.reg_edit_profile);
        imgUrl= findViewById(R.id.reg_profilr_url);


        getPermission();
        
        chekdata();


        birthday.setInputType(InputType.TYPE_NULL);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        anniversary.setInputType(InputType.TYPE_NULL);
        anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                anniversary.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        isMarried.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.married)
                    anniversary.setVisibility(View.VISIBLE);
                else
                    anniversary.setVisibility(View.GONE);
            }
        });


        bProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setUserData()==0){
                    startActivity(new Intent(context, UserDashboardActivity.class));
                    finish();
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }

    private void chekdata() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    back_button.setVisibility(View.VISIBLE);
                    title.setText("Profile ");

                    Picasso.get().load(snapshot.child("img_url").getValue().toString()).into(profile);

                    name.setText(snapshot.child("name").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                    birthday.setText(snapshot.child("birthday").getValue().toString());
                    if(snapshot.child("anniversary").getValue().toString()!=""){
                        anniversary.setText(snapshot.child("anniversary").getValue().toString());
                    }

                    if(snapshot.child("is_married").getValue().toString().equals("Yes")){
                        isMarried.check(R.id.married);
                    }

                    if(snapshot.child("gender").getValue().toString().equals("Female")){
                        gender.check(R.id.female);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private int setUserData() {

        if(name.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please Enter Name !", Toast.LENGTH_SHORT).show();
            return 1;
        }
        if(email.getText().toString().isEmpty()){
            Toast.makeText(context, "Please Enter a Valid Email !", Toast.LENGTH_SHORT).show();
            return 2;
        }
        if (birthday.getText().toString().isEmpty()){
            Toast.makeText(context, "PLease Selecta Birthdate !", Toast.LENGTH_SHORT).show();
            return 3;
        }
        if (imgUrl.getText().toString().isEmpty()){
            Toast.makeText(context, "Please Select a Profile Image !", Toast.LENGTH_SHORT).show();
            return 4;
        }

        String is_married="No",gen="Male";
        if (isMarried.getCheckedRadioButtonId()==R.id.married)
            is_married="Yes";
        else
            is_married="No";

        if (gender.getCheckedRadioButtonId()==R.id.male)
            gen="Male";
        else
            gen="Female";

        Profiles p = new Profiles(auth.getUid(),name.getText().toString(),imgUrl.getText().toString(),is_married,birthday.getText().toString(),anniversary.getText().toString(),gen,email.getText().toString(),"None");

        reference.setValue(p);

        return 0;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imagepath = data.getData();
            profile.setImageURI(imagepath);

            sendImagetoStorage();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else {

        }
    }


    private int sendImagetoStorage() {

        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference imageref = storage.getReference().child("profiles").child(auth.getUid()).child("Profile Pic.jpg");

        //Image compresesion

        Bitmap bitmap = null,newbitmap=null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(0);
        Bitmap imageAfterRotation = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);



        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageAfterRotation.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        ///putting image to storage

        UploadTask uploadTask = imageref.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //ImageUriAcessToken = uri.toString();
                        Picasso.get().load(uri.toString()).into(profile);
                        imgUrl.setText(uri.toString());


                       // sendDataToRealTimeDatabase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "URI get Failed", Toast.LENGTH_SHORT).show();
                    }


                });
                Toast.makeText(getApplicationContext(), "Image is uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Image Not Uploaded", Toast.LENGTH_SHORT).show();

            }
        });

        return 0;

    }

}