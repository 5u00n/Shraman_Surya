package in.devedits.shramansurya.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import in.devedits.shramansurya.R;
import in.devedits.shramansurya.admin.AdminActivity;
import in.devedits.shramansurya.user.UserDashboardActivity;

public class LoginActivity extends AppCompatActivity {


    Context context;

    String number;

    String countrycode,phonenumber,codesent;

    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    FirebaseDatabase database;
    DatabaseReference ref;

    CountryCodePicker cc;
    TextInputEditText number_txt, otp_txt;
    MaterialButton otp_button, login_button;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getBaseContext();

        auth= FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        ref=database.getReference();

        cc= findViewById(R.id.login_countrycodepicker);
        number_txt= findViewById(R.id.login_textbox_phone);
        otp_txt= findViewById(R.id.login_textbox_otp);

       otp_button = findViewById(R.id.login_button_otp);
       login_button= findViewById(R.id.login_button_login);

       pb= findViewById(R.id.login_progress_bar);


        countrycode = cc.getSelectedCountryCodeWithPlus();
        cc.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode = cc.getSelectedCountryCodeWithPlus();
            }
        });


       otp_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               number = number_txt.getText().toString();
               if (number.isEmpty()) {
                   Toast.makeText(getApplicationContext(), "Please Enter Your number", Toast.LENGTH_SHORT).show();
               } else if (number.length() < 10) {
                   Toast.makeText(getApplicationContext(), "Please Enter correct number", Toast.LENGTH_SHORT).show();
               } else {
                   pb.setVisibility(View.VISIBLE);

                   if(countrycode==null){
                       countrycode="+91";
                   }
                   phonenumber = countrycode + number;


                   PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                           .setPhoneNumber(phonenumber)
                           .setTimeout(60L, TimeUnit.SECONDS)
                           .setActivity(LoginActivity.this)
                           .setCallbacks(mCallbacks)
                           .build();
                   PhoneAuthProvider.verifyPhoneNumber(options);
               }
           }
       });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //how to automatically fetch code here
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pb.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this,"Sending OTP failed, try again!",Toast.LENGTH_SHORT).show();
                otp_txt.setText("Try Again");
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(), "OTP is Sent", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
                codesent = s;

                otp_button.setText("Resend OTP");

                Toast.makeText(getApplicationContext(), "OTP Sent", Toast.LENGTH_SHORT).show();

                otp_txt.setVisibility(View.VISIBLE);
                login_button.setVisibility(View.VISIBLE);
                //Intent intent = new Intent(LoginActivity.this, OTPAuthenticationActivity.class);
                //intent.putExtra("otp", codesent);
                //intent.putExtra("phone", phonenumber);

                //startActivity(intent);
            }
        };


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredotp= otp_txt.getText().toString();
                if (enteredotp.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter your OTP First ", Toast.LENGTH_SHORT).show();
                } else {
                    pb.setVisibility(View.VISIBLE);

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, enteredotp);
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                    goToNextActivity();

                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        pb.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void goToNextActivity() {
        final Intent[] intent = {new Intent()};
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ref.child("profiles").child(auth.getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        if(snapshot.child("uType").exists() && snapshot.child("uType").getValue().toString().equals("Admin")){
                            //Go to admin
                            ref.child("profiles").child(auth.getUid()).child("mob_no").setValue(number);
                            intent[0] = new Intent(context, AdminActivity.class);
                        }else{
                            //Go to Users
                            ref.child("profiles").child(auth.getUid()).child("mob_no").setValue(number);
                            intent[0] = new Intent(context, UserDashboardActivity.class);
                        }
                    }else{
                        //Go to Set Users Details

                        ref.child("profiles").child(auth.getUid()).child("mob_no").setValue(number);
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


    }
}