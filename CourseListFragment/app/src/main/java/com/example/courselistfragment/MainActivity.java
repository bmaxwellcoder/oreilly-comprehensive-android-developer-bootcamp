package com.example.courselistfragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import data.Course;

public class MainActivity extends AppCompatActivity
        implements CourseListFragment.Callbacks {
    private boolean isTwoPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detailContainer) != null) {
            isTwoPage = true;
        }

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.myContainer);
//
//        if (fragment == null) {
//            fragment = new CourseListFragment();
//            fragmentManager.beginTransaction()
//                    .add(R.id.myContainer, fragment)
//                    .commit();
//        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.myContainer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onItemSelected(Course course, int position) {
        if (isTwoPage) {
            Bundle bundle = new Bundle();
            bundle.putInt("course_id", position);

            FragmentManager fragmentManager = getSupportFragmentManager();
            CourseDetailFragment courseDetailFragment = new CourseDetailFragment();
           courseDetailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.detailContainer, courseDetailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(MainActivity.this, CourseDetailActivity.class);
            intent.putExtra("course_id", position);
            startActivity(intent);
        }
//        Toast.makeText(this, "Hello", Toast.LENGTH_LONG)
//                .show();
    }
}
