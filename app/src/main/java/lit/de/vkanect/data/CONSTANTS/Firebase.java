package lit.de.vkanect.data.CONSTANTS;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import lit.de.vkanect.data.Institute;

public class Firebase {
    private final static String LOC = "https://vkanect" +
            "-default-rtdb.asia-southeast1.firebasedatabase.app/";
    public static final String TAG = "VKT";
    public static FirebaseUser USER = FirebaseAuth.getInstance().getCurrentUser();

    public static FirebaseUser USER_ME() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    public static Institute institute;
    public static Institute studInstitute;

    public static boolean FACULTY = false;

    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance(LOC).getReference();
    public static DatabaseReference instituteDB = FirebaseDatabase.getInstance(LOC).getReference().child("institutes");
    public static DatabaseReference instituteStudentsDB = FirebaseDatabase.getInstance(LOC).getReference().child("students");

    public static void FAC_getInstitude() {
        instituteDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Log.d(TAG, "onDataChange: "+snapshot.getValue());
                for (DataSnapshot ss : snapshot.getChildren()) {

                    //Log.d(TAG, "onDataChange: "+ss.getValue());
                    Institute is = ss.child("instituteData").getValue(Institute.class);
                    try {
                        if (is.owner.equals(Firebase.USER.getUid())) {
                            institute = is;
                            break;
                        }
                    } catch (Exception e) {
                    }
                    ;

                }
                ValueEventListener listenerForSubFAC = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.getValue() != null)

                            instituteDB.child(snapshot.getValue() + "").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot s) {
                                    if (s.getValue() != null)
                                        institute = s.child("instituteData").getValue(Institute.class);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onDataChange: " + error.getMessage());
                    }
                };
                try {
                    databaseReference.child("Faculty").child(USER.getUid()).child("institute").addValueEventListener(listenerForSubFAC);
                }catch (Exception e){
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void STUD_getInstitude() {
        instituteDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d(TAG, "onDataChange: "+snapshot.getValue());
                for (DataSnapshot ss : snapshot.getChildren()) {

                    studInstitute = ss.child("instituteData").getValue(Institute.class);

                    if (studInstitute.owner.equals(Firebase.USER.getUid())) {
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
