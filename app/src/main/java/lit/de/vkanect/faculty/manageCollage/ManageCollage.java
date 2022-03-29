package lit.de.vkanect.faculty.manageCollage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lit.de.vkanect.R;
import lit.de.vkanect.data.Institute;

public class ManageCollage extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore db;

    Institute institute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty_manage_collage);

        db = FirebaseFirestore.getInstance();
        user =  FirebaseAuth.getInstance().getCurrentUser();

        Button intitude_code = (Button)findViewById(R.id.intitude_code);
        Button manage_collage = (Button)findViewById(R.id.manage_collage);
        Button make_institute = (Button)findViewById(R.id.make_institute );
        EditText collage_name_box = (EditText)findViewById(R.id.collage_name_box);

        FirebaseFirestore.getInstance().collection("institutes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task){
                if (task.isSuccessful()){
                    List<Institute> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()){
                        list.add(document.toObject(Institute.class));
                        //Log.d("TAG", document.toString());
                    }
                    for(Institute data : list){
                        if(data.owner.equals(user.getUid())) {
                            Log.d("TAG",data.id);
                            institute = data;
                            intitude_code.setText("Your intitude code : "+institute.getId());
                            intitude_code.setVisibility(View.VISIBLE);
                            make_institute.setEnabled(false);
                            collage_name_box.setVisibility(View.GONE);
                            make_institute.setVisibility(View.GONE);
                        }
                    }
                    //Log.d("TAG", list.toString());
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

        intitude_code.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, institute.getId());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        });

        make_institute.setOnClickListener(view->{
            String collage_name_box_str = collage_name_box.getText().toString();
            if(!collage_name_box_str.isEmpty()){
                institute = new Institute(user.getUid(),getSaltString(),"NULL");
                intitude_code.setText("Your intitude code : "+institute.getId());

                DocumentReference institutes = FirebaseFirestore.getInstance()
                        .collection("institutes")
                        .document(
                                institute.id
                        );
                institutes
                        .get()
                        .addOnCompleteListener(task -> {
                            Institute temp = task.getResult().toObject(Institute.class);
                            if(temp==null){
                                db
                                        .collection("institutes")
                                        .document(institute.getId())
                                        .set(institute);
                                intitude_code.setVisibility(View.VISIBLE);
                                make_institute.setEnabled(false);

//                                SharedPreferences sharedPref = getActivity().getPreferences(getContext().MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPref.edit();
//                                editor.putString("institute",institute.getId());
//                                editor.apply();
                            }else{
                                //Log.d("TAG", "Matched");
                                make_institute.performClick();
                            }
                        });
            }
            else{
                Log.d("TAG", "EMPT");
            }
        });

    }
    String getSaltString(){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}