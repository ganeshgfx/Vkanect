package lit.de.vkanect.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import lit.de.vkanect.MainActivity;
import lit.de.vkanect.R;

public class student_activity extends AppCompatActivity {

    BottomNavigationView stud_bottomNavigationView;
    NavController stud_navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //Log.d("TAG", "STUD");
        //Nav Stufff
//        stud_navController = Navigation.findNavController(this,R.id.student_navigation);
//        stud_bottomNavigationView = findViewById(R.id.stud_bottomNavigationView);
//        NavigationUI.setupWithNavController(stud_bottomNavigationView, stud_navController);
        //Initialize Bottom Navigation View.
        BottomNavigationView navView = findViewById(R.id.stud_bottomNavigationView);

        //Pass the ID's of Different destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.chatFragment, R.id.homeFragment, R.id.noticeFragment, R.id.userFragment,R.id.workFragment )
                .build();

        //Initialize NavController.
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.stud_fragmentContainerView);
        NavController navController = navHostFragment.getNavController();

        //NavController navController = Navigation.findNavController(this, R.id.stud_fragmentContainerView);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        getSupportActionBar().hide();
    }
}