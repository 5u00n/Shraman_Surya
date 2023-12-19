package in.devedits.shramansurya.user.gridviews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import in.devedits.shramansurya.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        findViewById(R.id.about_backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}