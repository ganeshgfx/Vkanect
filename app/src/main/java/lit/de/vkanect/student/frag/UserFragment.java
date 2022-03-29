package lit.de.vkanect.student.frag;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import lit.de.vkanect.MainActivity;
import lit.de.vkanect.R;
import lit.de.vkanect.data.Institute;
import lit.de.vkanect.data.User;
import lit.de.vkanect.faculty.faculty_activity;
import lit.de.vkanect.student.student_activity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;
    FirebaseUser user;
    String collID="";

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user, container, false);
        Button logOut = (Button)rootView.findViewById(R.id.logOut);
        Button user_info = (Button)rootView.findViewById(R.id.user_info);
        Button back_top_bar = (Button)rootView.findViewById(R.id.back_top_bar);
        back_top_bar.setOnClickListener(view -> {
            BackForth(View.GONE,View.VISIBLE);
        });
        user_info.setOnClickListener(v ->{
            BackForth(View.VISIBLE,View.GONE);
            rootView.
                    findViewById(R.id.user_info_layout)
                    .setVisibility(View.VISIBLE);
        });
        logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
            //Log.d("TAG", "onClick: log");
        });

        /////////////MANAGE COLLAGE
        TextView disp_collage = (TextView)rootView.findViewById(R.id.disp_collage);
        Button manage_collage = (Button)rootView.findViewById(R.id.manage_collage);
        manage_collage.setOnClickListener(v ->{
            BackForth(View.VISIBLE,View.GONE);
            if(collID.equals("")){
                rootView.
                        findViewById(R.id.make_institute_layout)
                        .setVisibility(View.VISIBLE);
            }else{
                disp_collage.setText(disp_collage.getText().toString()+" : "+collID);
                rootView.
                        findViewById(R.id.manage_institute_layout)
                        .setVisibility(View.VISIBLE);
            }
        });
        EditText join_code = (EditText)rootView.findViewById(R.id.join_code);
        Button ijoin = (Button)rootView.findViewById(R.id.ijoin);
        ijoin.setOnClickListener(view -> {
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            DocumentReference docIdRef = rootRef.collection("institutes").document(join_code.getText().toString());
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String,String> me = new HashMap<>();
                            Map<String,String> inst = new HashMap<>();
                            me.put("sid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Log.d("TAG", "Document exists!");
                            FirebaseFirestore.getInstance()
                                    .collection("institutes")
                                    .document(join_code.getText().toString())
                                    .collection("studentList")
                                    .document(user.getUid())
                                    .set(me);
                            inst.put("cid",join_code.getText().toString());
                            FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(user.getUid())
                                    .collection("myCollage")
                                    .document(join_code.getText().toString())
                                    .set(me);

                            disp_collage.setText(disp_collage.getText().toString()+" : "+join_code.getText().toString());
                            rootView.
                                    findViewById(R.id.make_institute_layout)
                                    .setVisibility(View.GONE);
                            rootView.
                                    findViewById(R.id.manage_institute_layout)
                                    .setVisibility(View.VISIBLE);
                        } else {
                            Log.d("TAG", "Document does not exist!");
                            Toast.makeText(getContext(),"Does not exist!",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("TAG", "Failed with: ", task.getException());
                    }
                }
            });
        });

        FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid())
                .collection("myCollage")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if(task.getResult().size() > 0) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    collID = document.getId();
                                    //Log.d("TAG", "Room already exists, start the chat");
                                    //Log.d("TAG", ""+document.getId());
                                }
                            } else {
                                Log.d("TAG", "room doesn't exist create a new room");

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

//        CollectionReference loc = FirebaseFirestore.getInstance()
//                .collection("users")
//                .document(user.getUid())
//                .collection("myCollage");
//        loc
//                .get()
//                .addOnCompleteListener(task -> {
//                    for (QueryDocumentSnapshot document:task.getResult()) {
//                        Log.d("TAG", "onCreateView: "+document.getId().toString());
//                        if(document.getId().toString().equals("")){
//                            Log.d("TAG", "onCreateView: EMTY");
//                        }
//                    }
//                });

//        Button ijoin = (Button)rootView.findViewById(R.id.ijoin);

//        ijoin.setOnClickListener(view -> {
//
//        });

        ///////////////////////////


        TextView info = (TextView)rootView.findViewById(R.id.userDataTxt);
        DocumentReference userData = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userData
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        User user = task.getResult().toObject(User.class);
                        info.setText(
                                task.getResult().getData().toString().replace(",",",\n")
                        );
                    }
                });

        return rootView;
    }
    void BackForth(int TOPBAR,int BODY){
        rootView.
                findViewById(R.id.user_section_top_bar)
                .setVisibility(TOPBAR);
        rootView.
                findViewById(R.id.user_main_page_options)
                .setVisibility(BODY);
        //Hide all Views
        if(TOPBAR==View.GONE){
            rootView.
                    findViewById(R.id.user_info_layout)
                    .setVisibility(View.GONE);
            rootView.
                    findViewById(R.id.make_institute_layout)
                    .setVisibility(View.GONE);
        }
    }
}