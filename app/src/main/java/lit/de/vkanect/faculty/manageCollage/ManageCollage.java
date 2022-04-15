package lit.de.vkanect.faculty.manageCollage;

import static lit.de.vkanect.data.CONSTANTS.Firebase.USER;
import static lit.de.vkanect.data.CONSTANTS.Firebase.databaseReference;
import static lit.de.vkanect.data.CONSTANTS.Firebase.institute;
import static lit.de.vkanect.data.CONSTANTS.Firebase.instituteDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Random;

import lit.de.vkanect.FacultyForm;
import lit.de.vkanect.R;
import lit.de.vkanect.data.CONSTANTS.Firebase;
import lit.de.vkanect.data.Institute;
import lit.de.vkanect.faculty.faculty_activity;
import lit.de.vkanect.spash;
import lit.de.vkanect.student.frag.handleCollage.HandleCollage;

public class ManageCollage extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore db;

    Institute mInstitute;

    String TAG = "VKT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty_manage_collage);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Button intitude_code = (Button) findViewById(R.id.intitude_code);
        Button manage_collage = (Button) findViewById(R.id.manage_collage);
        Button make_institute = (Button) findViewById(R.id.make_institute);
        Button delete_institute = (Button) findViewById(R.id.delete_institute);
        EditText collage_name_box = (EditText) findViewById(R.id.collage_name_box);
        MaterialCardView intitude_code_card = (MaterialCardView) findViewById(R.id.intitude_code_card);
        MaterialCardView intitude_make_card_layout = (MaterialCardView) findViewById(R.id.intitude_make_card_layout);

        Button join_as_FAC_bttn = (Button) findViewById(R.id.join_as_FAC_bttn);
        EditText join_as_FAC_txt = (EditText) findViewById(R.id.join_as_FAC_txt);
        TextView or = (TextView) findViewById(R.id.textView4OR);

        join_as_FAC_bttn.setOnClickListener(v->{
            String inputCode = join_as_FAC_txt.getText().toString();
            if (!inputCode.equals("")) {
                ValueEventListener eventListener = new ValueEventListener() {
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

                                    databaseReference.child("Faculty").child(USER.getUid()).child(
                                            "institute").setValue(inputCode).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: " + "done Faculty added");

                                            saveLocally(inputCode);


                                        }
                                    });

                                    break;
                                }
                            } catch (Exception e) {
                            }
                        }
                        if (!found) {
                            Toast.makeText(getApplicationContext(), "There is no such institute",
                                    Toast.LENGTH_LONG).show();
                        }
                        removeListner(instituteDB,this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                instituteDB.addValueEventListener(eventListener);
            }
        });

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d(TAG, "onDataChange: "+snapshot.getValue());
                boolean found = false;
                for (DataSnapshot ss : snapshot.getChildren()) {
                    mInstitute = ss.child("instituteData").getValue(Institute.class);

                    if (mInstitute.owner.equals(USER.getUid())) {
                        showInsData(intitude_code, intitude_code_card, make_institute, collage_name_box, join_as_FAC_bttn, join_as_FAC_txt, or);

                        found = true;
                        break;
                    }

                }
                if (!found) {

                    ValueEventListener listenerForSubFAC = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.getValue() != null) {

                                instituteDB.child(snapshot.getValue() + "").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot s) {

                                       // Log.d(TAG, "onDataChange: "+s.getValue());

                                        if (s.getValue() != null) {
                                          // Log.d(TAG,"onDataChange: You in..."+s.getValue());
                                            mInstitute = s.child("instituteData").getValue(Institute.class);

                                            showInsData(intitude_code, intitude_code_card, make_institute, collage_name_box, join_as_FAC_bttn, join_as_FAC_txt, or);

                                            delete_institute.setVisibility(View.GONE);

                                            //Log.d(TAG, "onDataChange: "+is.toString());
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
                            Log.d(TAG, "onDataChange: " + error.getMessage());
                        }
                    };
                    databaseReference.child("Faculty").child(USER.getUid()).child("institute").addValueEventListener(listenerForSubFAC);

                    intitude_code.setVisibility(View.GONE);
                    intitude_code_card.setVisibility(View.GONE);
                    make_institute.setEnabled(true);
                    collage_name_box.setVisibility(View.VISIBLE);
                    make_institute.setVisibility(View.VISIBLE);
                    join_as_FAC_bttn.setVisibility(View.VISIBLE);
                    join_as_FAC_txt.setVisibility(View.VISIBLE);
                    or.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        instituteDB.addValueEventListener(eventListener);

        intitude_code.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mInstitute.getId());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        });

        delete_institute.setOnClickListener(v->{

            new AlertDialog.Builder(this)
                    .setTitle("Are you sure...?")
                    .setMessage("If you delete the institute all data will be lost.?")
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .setPositiveButton("Yes", (dialog, whichButton) -> {
                        instituteDB.child(mInstitute.id).removeValue()
                                .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: DELETD"))
                                .addOnFailureListener(e -> Log.d(TAG, "ERROr: DELET"));

                        databaseReference.child("Faculty").child(USER.getUid()).child(
                                "institute").removeValue();

                        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null).show();
        });

        make_institute.setOnClickListener(view -> {
            String collage_name_box_str = collage_name_box.getText().toString();

            if (!collage_name_box_str.isEmpty()) {
                mInstitute = new Institute(user.getUid(), getSaltString(), collage_name_box_str);

                intitude_code.setText("Your institute code : " + mInstitute.getId());

                //Log.d(TAG, collage_name_box_str);

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        boolean created = false;

                        for (DataSnapshot ss:snapshot.getChildren()) {
                            Institute nstitute = ss.child("instituteData").getValue(Institute.class);
                            Log.d(TAG, "onDataChange: "+ nstitute.owner);
                            if(nstitute.owner.equals(Firebase.USER.getUid())){
                                created=true;
                            }
                        }

                        if (!created)
                            instituteDB.child(mInstitute.id).child("instituteData").setValue(mInstitute)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // Log.d(TAG, "onDataChange: "+mInstitute.id);
                                            Log.d(TAG, "onSuccess: institude created");
                                            databaseReference.child("Faculty").child(USER.getUid()).child(
                                                    "institute").setValue(mInstitute.id);
                                            saveLocally(mInstitute.id);
                                            startActivity(new Intent(ManageCollage.this,
                                                    FacultyForm.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: institude not crrated");
                                }
                            });
                        removeListner(instituteDB,this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };

                instituteDB.addValueEventListener(valueEventListener);

                //RT
                //


//                DocumentReference institutes = FirebaseFirestore.getInstance()
//                        .collection("institutes")
//                        .document(
//                                institute.id
//                        );
//                institutes
//                        .get()
//                        .addOnCompleteListener(task -> {
//                            Institute temp = task.getResult().toObject(Institute.class);
//                            if(temp==null){
//                                db
//                                        .collection("institutes")
//                                        .document(institute.getId())
//                                        .set(institute);
//                                intitude_code.setVisibility(View.VISIBLE);
//                                make_institute.setEnabled(false);
//
////                                SharedPreferences sharedPref = getActivity().getPreferences(getContext().MODE_PRIVATE);
////                                SharedPreferences.Editor editor = sharedPref.edit();
////                                editor.putString("institute",institute.getId());
////                                editor.apply();
//                            }else{
//                                //Log.d("TAG", "Matched");
//                                make_institute.performClick();
//                            }
//                        });
            } else {
                Log.d("TAG", "EMPT");
            }
        });

    }

    private void saveLocally(String inputCode) {

        SharedPreferences sharedPreferences =
                getSharedPreferences("institute", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("code", inputCode);
        editor.commit();

    }

    private void showInsData(Button intitude_code, MaterialCardView intitude_code_card, Button make_institute, EditText collage_name_box, Button join_as_FAC_bttn, EditText join_as_FAC_txt, TextView or) {
        intitude_code.setText("Your intitude code : " + mInstitute.getId());
        intitude_code.setVisibility(View.VISIBLE);
        intitude_code_card.setVisibility(View.VISIBLE);
        make_institute.setEnabled(false);
        collage_name_box.setVisibility(View.GONE);
        make_institute.setVisibility(View.GONE);
        join_as_FAC_bttn.setVisibility(View.GONE);
        join_as_FAC_txt.setVisibility(View.GONE);
        or.setVisibility(View.GONE);
    }

    void removeListner(DatabaseReference documentReference, ValueEventListener valueEventListener){
    documentReference.removeEventListener(valueEventListener);
}
    String getSaltString() {
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