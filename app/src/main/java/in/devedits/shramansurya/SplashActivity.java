package in.devedits.shramansurya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.devedits.shramansurya.admin.AdminActivity;
import in.devedits.shramansurya.auth.LoginActivity;
import in.devedits.shramansurya.auth.RegisterActivity;
import in.devedits.shramansurya.user.UserDashboardActivity;

public class SplashActivity extends AppCompatActivity {

    Context context;

    ImageView spl_image;

    FirebaseAuth auth;

    FirebaseDatabase database;
    DatabaseReference ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(getBaseContext());
        auth= FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        ref= database.getReference();

        context= getBaseContext();

        spl_image = findViewById(R.id.splash_image);

        AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(1000);
        animation1.setFillAfter(true);
        spl_image.startAnimation(animation1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                changeImage();
            }
        }, 1500);


    }

    private void changeImage() {

        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(1000);
        animation1.setFillAfter(true);
        spl_image.startAnimation(animation1);
        spl_image.setImageResource(R.drawable.spash2_img);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                goToMain();
                //startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            }
        }, 1500);
    }

    private void goToMain() {

        final Intent[] intent = {new Intent()};
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d("Auth ---------","Logged in!");
            ref.child("profiles").child(auth.getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        Log.d("Snapshot ---------","Profile Exists");
                        if(snapshot.child("uType").exists() && snapshot.child("uType").getValue().toString().equals("Admin")){
                            //Go to admin
                            intent[0] = new Intent(context, AdminActivity.class);
                        }else{
                            //Go to Users
                            intent[0] = new Intent(context, UserDashboardActivity.class);
                        }
                    }else{
                        //Go to Set Users Details
                        Log.d("Snapshot ---------","Profile Doesn't Exists");
                        intent[0] = new Intent(context, RegisterActivity.class);
                    }
                    startActivity(intent[0]);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            //Logged out User-----
            SharedPreferences sharedPreferences = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
            boolean isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true);

            if (isFirstLaunch) {
                isFirstLaunch = false;
                sharedPreferences.edit().putBoolean("isFirstLaunch", isFirstLaunch).apply();
                //First launch Welcome page then to Login
                intent[0] = new Intent(context, WelcomeActivity.class);

            } else {
                //Go to Login Page
                intent[0] = new Intent(context, LoginActivity.class);
            }
            startActivity(intent[0]);
            finish();
        }



    }
}