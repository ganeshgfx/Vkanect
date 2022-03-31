package lit.de.vkanect.data.CONSTANTS;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import lit.de.vkanect.data.Institute;

public class Firebase {
    public static FirebaseUser USER = FirebaseAuth.getInstance().getCurrentUser();
    public static Institute institute;

    public static DatabaseReference instituteDB = FirebaseDatabase.getInstance("https://vkanect" +
            "-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(
                    "institutes");

    public static void FAC_getInstitude(){
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
                        if(data.owner.equals(USER.getUid())) {
                            Log.d("TAG",data.id);
                            institute = data;
                        }
                    }
                    //Log.d("TAG", list.toString());
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
