package com.example.canvasui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.canvasui.api.USUCanvasAPI;
import com.example.canvasui.api.models.Course;
import com.example.canvasui.api.models.UpcomingEvent;
import com.example.canvasui.api.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        USUCanvasAPI api = USUCanvasAPI.getInstance(this);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, CourseFragment.class, null)
            .commit();
            api.getCourses(new USUCanvasAPI.OnRequestCompleteListener<Course>() {
                @Override
                public void onComplete(Course[] result, String errorMessage) {
                    System.out.println();
                    RecyclerView coursesRecycler = findViewById(R.id.recycler_courses);

                    coursesRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    coursesRecycler.setAdapter(new CoursesAdapter(result));
                    fab.setVisibility(View.VISIBLE);
                }
            });
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        api.getUser(new USUCanvasAPI.OnRequestCompleteListener<User>() {
            @Override
            public void onComplete(User[] result, String errorMessage) {
                System.out.println();
                TextView username = findViewById(R.id.user_name);
                username.setText(result[0].name);
            }
        });

        ////////////////////////////////////////////////////////////////////////

        toolbar.setNavigationOnClickListener((view)->{
            drawerLayout.open();
        });
         navigationView.setNavigationItemSelectedListener((menuItem)-> {
            menuItem.setChecked(true);
            if(menuItem.getItemId() == R.id.menu_item_courses){
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, CourseFragment.class, null)
                        .addToBackStack(null)
                        .commit();
                api.getCourses(new USUCanvasAPI.OnRequestCompleteListener<Course>() {
                    @Override
                    public void onComplete(Course[] result, String errorMessage) {
                        System.out.println();
                        RecyclerView coursesRecycler = findViewById(R.id.recycler_courses);

                        coursesRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        coursesRecycler.setAdapter(new CoursesAdapter(result));
                        fab.setVisibility(View.VISIBLE);
                    }
                });
            }

//////////////////////////////////////////////////////////////////////////////////////////
             if(menuItem.getItemId() == R.id.menu_item_upcoming_events){
                 //navigate upcoming events
                 getSupportFragmentManager().beginTransaction()
                         .setReorderingAllowed(true)
                         .replace(R.id.fragment_container, UpcomingEventsFragment.class, null)
                         .addToBackStack(null)
                         .commit();
                 api.getUpcomingEvents(new USUCanvasAPI.OnRequestCompleteListener<UpcomingEvent>() {
                     @Override
                     public void onComplete(UpcomingEvent[] result, String errorMessage) {
                         System.out.println();
                         RecyclerView eventsRecycler = findViewById(R.id.recycler_upcomingEvents);

                         eventsRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                         eventsRecycler.setAdapter(new UpcomingEventsAdapter(result));
                         fab.setVisibility(View.GONE);
                     }
                 });
             }
////////////////////////////////////////////////////////////////////////////////////////////
             if(menuItem.getItemId() == R.id.menu_item_profile){
                 //navigate profile
                 getSupportFragmentManager().beginTransaction()
                         .setReorderingAllowed(true)
                         .replace(R.id.fragment_container, ProfileFragment.class, null)
                         .addToBackStack(null)
                         .commit();
                 api.getUser(new USUCanvasAPI.OnRequestCompleteListener<User>() {
                     @Override
                     public void onComplete(User[] result, String errorMessage) {
                         System.out.println();

                         TextView profileName = findViewById(R.id.user_name_profile);
                         profileName.setText(result[0].name);
                         TextView profileDescription = findViewById(R.id.profile_description);
                         profileDescription.setText(R.string.lorem_ipsum);
                         ImageView profileImage = findViewById(R.id.profile_image);
                         profileImage.setImageResource(R.drawable.ic_baseline_person_24);
                         fab.setVisibility(View.GONE);
                     }
                 });
             }
             drawerLayout.close();
             return true;
         });







    }
}