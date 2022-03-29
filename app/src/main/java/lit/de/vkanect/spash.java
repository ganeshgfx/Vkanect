package lit.de.vkanect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import lit.de.vkanect.data.User;
import lit.de.vkanect.faculty.faculty_activity;
import lit.de.vkanect.student.student_activity;

public class spash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        //getActionBar().hide();

        ImageView imageView = (ImageView)findViewById(R.id.image_spash);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(700);
        imageView.startAnimation(animation);
        //imageView.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null) {
                    Intent myIntent = new Intent(spash.this, MainActivity.class);
                    spash.this.startActivity(myIntent);
                    finish();
                }else{

                    FirebaseFirestore
                            .getInstance()
                            .collection("users")
                            .document(user.getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
//                        for(DocumentSnapshot doc:task.getResult().){
//
//                        }
                                    User user = task.getResult().toObject(User.class);

                                    if(user.getType().equals("Student")) {
                                        Log.d("TAG", "onComplete: "+user.getType());
                                        startActivity(new Intent(spash.this, student_activity.class));
                                        finish();
                                    }else{
                                        Log.d("TAG", "onComplete: "+user.getType());
                                        startActivity(new Intent(spash.this, faculty_activity.class));
                                        finish();
                                    }
                                    //Log.d("TAG", user.getType());
                                }
                            });

                }
            }
        }, 700);
    }
}