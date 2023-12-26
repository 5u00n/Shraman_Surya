package in.devedits.shramansurya.user.gridviews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import in.devedits.shramansurya.R;

public class ProfileActivity extends AppCompatActivity {

    Context context;

    ImageView back_button, profile_image;
    TextView name_text, email_text, birthday_text, gender_text,is_married, anniversary;

    ImageButton bProfile,bName,bBirth,bGender,bIsMarried,bAnniversary;



    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context= getBaseContext();

        auth= FirebaseAuth.getInstance();

        database= FirebaseDatabase.getInstance();
        reference= database.getReference("profiles").child(auth.getUid().toString());

         back_button= findViewById(R.id.profile_backButton);
         profile_image= findViewById(R.id.profile_image);

         name_text= findViewById(R.id.profile_name);
         email_text= findViewById(R.id.profile_email);
         birthday_text= findViewById(R.id.profile_dob);
         gender_text= findViewById(R.id.profile_gender);
         is_married= findViewById(R.id.profile_isMarried);
         anniversary= findViewById(R.id.profile_anniversary);


         bProfile= findViewById(R.id.profile_edit_profile);
         bName= findViewById(R.id.profile_edit_name);
         bBirth= findViewById(R.id.profile_edit_birthday);
         bGender= findViewById(R.id.profile_edit_gender);
         bIsMarried= findViewById(R.id.profile_edit_isMarried);
         bAnniversary= findViewById(R.id.profile_edit_anniversary);


         back_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 finish();
             }
         });


         bProfile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

             }
         });



         fillDataFromFireBase();





    }

    private void fillDataFromFireBase() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Log.d("My data", new Gson().toJson(snapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}