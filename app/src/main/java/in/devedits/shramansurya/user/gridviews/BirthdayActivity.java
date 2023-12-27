package in.devedits.shramansurya.user.gridviews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.devedits.shramansurya.R;

public class BirthdayActivity extends AppCompatActivity {

    Context context;

    ImageView backButton;
    TextView birthTab, anniTab,title;
    ListView birthList,anniList;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        context= getBaseContext();

        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        ref= database.getReference("profiles");

        birthTab= findViewById(R.id.birth_tabText_birth);
        anniTab= findViewById(R.id.birth_tabText_anni);
        title=findViewById(R.id.birth_title);

        birthList= findViewById(R.id.birth_list_birth);
        anniList= findViewById(R.id.birth_list_anni);

        findViewById(R.id.birth_backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        birthTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] attrs = new int[]{android.R.attr.selectableItemBackground};
                TypedArray typedArray = context.obtainStyledAttributes(attrs);
                int backgroundResource = typedArray.getResourceId(0, 0);
                birthTab.setBackgroundResource(backgroundResource);
                typedArray.recycle();
                birthList.setVisibility(View.VISIBLE);


                anniTab.setBackgroundResource(R.color.md_theme_dark_onSurfaceVariant);
                anniList.setVisibility(View.GONE);

                title.setText("Birthday");

            }
        });



        anniTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] attrs = new int[]{android.R.attr.selectableItemBackground};
                TypedArray typedArray = context.obtainStyledAttributes(attrs);
                int backgroundResource = typedArray.getResourceId(0, 0);
                anniTab.setBackgroundResource(backgroundResource);
                typedArray.recycle();
                anniList.setVisibility(View.VISIBLE);



                birthTab.setBackgroundResource(R.color.md_theme_dark_onSurfaceVariant);
                birthList.setVisibility(View.GONE);

                title.setText("Anniversary");
            }
        });

        fillDataIntoList();


    }

    private void fillDataIntoList() {
        ArrayList<String> valuesBirth =new ArrayList<>();
        ArrayList<String> valuesAnni = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()) {
                        if(ds.exists()){

                            SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
                            Date birthObj;
                            Date anniObj;
                            try {
                                birthObj = curFormater.parse(ds.child("birthday").getValue().toString());
                                anniObj = curFormater.parse(ds.child("anniversary").getValue().toString());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

                            Date c = Calendar.getInstance().getTime();
                            if(c.getDate()==birthObj.getTime() ) {
                                String birth = ds.child("name").getValue().toString() + " : " + ds.child("birthday").getValue().toString();
                                valuesBirth.add(birth);
                            }

                            if(c.getDate()==anniObj.getTime() ) {
                                String anni = ds.child("name").getValue().toString() + " : " + ds.child("anniversary").getValue().toString();
                                valuesAnni.add(anni);
                            }

                            ArrayAdapter<String> adapterBirth = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, android.R.id.text1, valuesBirth);
                            ArrayAdapter<String> adapterAnni = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, android.R.id.text1, valuesAnni);

                            birthList.setAdapter(adapterBirth);
                            anniList.setAdapter(adapterAnni);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}