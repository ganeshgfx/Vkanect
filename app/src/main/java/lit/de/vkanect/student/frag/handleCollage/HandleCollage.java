package lit.de.vkanect.student.frag.handleCollage;

import static lit.de.vkanect.data.CONSTANTS.Firebase.FAC_getInstitude;
import static lit.de.vkanect.data.CONSTANTS.Firebase.USER;
import static lit.de.vkanect.data.CONSTANTS.Firebase.databaseReference;
import static lit.de.vkanect.data.CONSTANTS.Firebase.institute;
import static lit.de.vkanect.data.CONSTANTS.Firebase.instituteDB;
import static lit.de.vkanect.data.CONSTANTS.Firebase.instituteStudentsDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lit.de.vkanect.R;
import lit.de.vkanect.data.CONSTANTS.Firebase;
import lit.de.vkanect.data.Institute;

public class HandleCollage extends AppCompatActivity {

    String TAG = "VKT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_handle_collage);

        // FIXME: 3/31/22 Handle loading
        databaseReference.child("Student").child(USER.getUid()).child("institute").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue()!=null){

                    instituteDB.child(snapshot.getValue()+"").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot s) {

                            //Log.d(TAG, "onDataChange: "+snapshot.getValue());

                            if(s.getValue()!=null){
                                LinearLayout make_institute_layout = findViewById(R.id.make_institute_layout);
                                make_institute_layout.setVisibility(View.GONE);
                                LinearLayout linearLayout = findViewById(R.id.show_institute_layout);
                                linearLayout.setVisibility(View.VISIBLE);

                                TextView textView = findViewById(R.id.myInstitute);
                                textView.setText("Your institute code is "+snapshot.getValue()+"");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onDataChange: "+error.getMessage());
            }
        });

        EditText join_code = (EditText) findViewById(R.id.join_code);
        Button ijoin = (Button) findViewById(R.id.ijoin);
        ijoin.setOnClickListener(view -> {

            String inputCode = join_code.getText().toString();
            if (!inputCode.equals("")) {


                instituteDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Log.d(TAG, "onDataChange: "+snapshot.getValue());
                        boolean found = false;
                        for (DataSnapshot ss : snapshot.getChildren()) {

                            //Log.d(TAG, "onDataChange: "+ss.getValue());
                            Institute is = ss.child("instituteData").getValue(Institute.class);
                            try {
                                if (is.id.equals(inputCode)) {
                                    Log.d(TAG, "Instiyude Found: ");
                                    found = true;

                                    //instituteDB.child(inputCode).child("students").child(USER
                                    // .getUid()).setValue(USER.getUid());

                                    databaseReference.child("Student").child(USER.getUid()).child("institute").setValue(inputCode).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: "+"done student added");
                                        }
                                    });

                                    break;
                                }
                            } catch (Exception e) {
                            }
                        }
                        if (!found) {
                            Toast.makeText(HandleCollage.this, "There is no such institute",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}

