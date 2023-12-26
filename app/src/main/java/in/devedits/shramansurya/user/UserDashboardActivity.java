package in.devedits.shramansurya.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import in.devedits.shramansurya.R;
import in.devedits.shramansurya.auth.RegisterActivity;
import in.devedits.shramansurya.user.gridviews.AboutUsActivity;
import in.devedits.shramansurya.user.dashboardhelper.DashboardRecyclerAdapter;
import in.devedits.shramansurya.user.dashboardhelper.GridModels;
import in.devedits.shramansurya.user.gridviews.ProfileActivity;

public class UserDashboardActivity extends AppCompatActivity {

    Context context;

    RecyclerView gridItems;
    TextView location, address, sunrise, sunset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        context= getBaseContext();

        gridItems= findViewById(R.id.dash_recycler_items);

        location= findViewById(R.id.dash_text_location);
        address= findViewById(R.id.dash_text_info);
        sunrise= findViewById(R.id.dash_text_rise_time);
        sunset= findViewById(R.id.dash_text_set_time);


        ArrayList<GridModels> items = new ArrayList<>();

        items.add(new GridModels("00", "Jain PDF",R.drawable.baseline_book_24, "#FAA250"));
        items.add(new GridModels("01", "Jain Songs",R.drawable.baseline_library_music_24, "#87CE8E"));

        items.add(new GridModels("02", "Posts",R.drawable.baseline_local_post_office_24, "#8EBAE3"));
        items.add(new GridModels("03", "Blogs",R.drawable.baseline_blog_feed_24, "#E284F5"));

        items.add(new GridModels("04", "Daily Updates",R.drawable.baseline_update_24, "#DFABC4"));
        items.add(new GridModels("05", "Jain Calendar",R.drawable.baseline_calendar_today_24, "#B7DB93"));

        items.add(new GridModels("06", "Hindu Punchag",R.drawable.baseline_calendar_punchag_24, "#8FD4E4"));
        items.add(new GridModels("07", "Pachkhan",R.drawable.baseline_notes_24, "#F08181"));

        items.add(new GridModels("08", "Time of Sunrise & Sunset",R.drawable.baseline_rise_set_4_24, "#81AAF0"));
        items.add(new GridModels("09", "Birthday & Anniversaries",R.drawable.baseline_birthday_24, "#FA5060"));

        items.add(new GridModels("10", "Profile",R.drawable.baseline_person_pin_24, "#E284F5"));
        items.add(new GridModels("11", "About US",R.drawable.baseline_info_24, "#FFB132"));

        DashboardRecyclerAdapter ad= new DashboardRecyclerAdapter(items,context );

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        gridItems.setLayoutManager(staggeredGridLayoutManager);
        gridItems.setAdapter(ad);


        ad.setOnItemClickListener(new DashboardRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(String someVariable) {
                //Toast.makeText(context,"From DASHBOARD: "+model,Toast.LENGTH_SHORT).show();

                switch (someVariable) {
                    case "00":
                        startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
                        break;
                    case "01":
                        startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
                        break;
                    case "02":
                        startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
                        break;
                    case "03":
                        startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
                        break;
                    case "04":
                        startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
                        break;
                    case "05":
                        startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
                        break;
                    case "06":
                        startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
                        break;
                    case "07":
                        startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
                        break;
                    case "08":
                        startActivity(new Intent(UserDashboardActivity.this, RegisterActivity.class));
                        break;
                    case "09":
                        startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
                        break;
                    case "10":
                        startActivity(new Intent(UserDashboardActivity.this, RegisterActivity.class));
                        break;
                    case "11":
                        startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
                        break;
                    // Add more cases as needed
                }

            }
        });



    }
}