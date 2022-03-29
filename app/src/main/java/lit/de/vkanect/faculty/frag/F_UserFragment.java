package lit.de.vkanect.faculty.frag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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

import lit.de.vkanect.MainActivity;
import lit.de.vkanect.R;
import lit.de.vkanect.data.Institute;
import lit.de.vkanect.data.User;
import lit.de.vkanect.faculty.manageCollage.ManageCollage;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link F_UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View rootView;
    FirebaseUser user;
    FirebaseFirestore db;

    Institute institute;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public F_UserFragment() {
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
    public static F_UserFragment newInstance(String param1, String param2) {
        F_UserFragment fragment = new F_UserFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //fireBase
        db = FirebaseFirestore.getInstance();
        user =  FirebaseAuth.getInstance().getCurrentUser();

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.faculty_fragment_user, container, false);
        //user
        Button logOut = (Button)rootView.findViewById(R.id.logOut);
        Button user_info = (Button)rootView.findViewById(R.id.user_info);
        //
        Button back_top_bar = (Button)rootView.findViewById(R.id.back_top_bar);

        //manage/////////////////////////////
        Button intitude_code = (Button)rootView.findViewById(R.id.intitude_code);
        Button manage_collage = (Button)rootView.findViewById(R.id.manage_collage);
        Button make_institute = (Button)rootView.findViewById(R.id.make_institute);
        EditText collage_name_box = (EditText)rootView.findViewById(R.id.collage_name_box);
        manage_collage.setOnClickListener(v->{


            this.startActivity(new Intent(getActivity(), ManageCollage.class));

            //Manage(v);
//            rootView.findViewById(R.id.make_institute_layout).setVisibility(View.VISIBLE);
//            BackForth(View.VISIBLE,View.GONE);

            //Log.d("TAG", "OVA : HERE");
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
                           }
                        }
                        //Log.d("TAG", list.toString());
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
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
        //////////////////////////////////
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

        TextView info = (TextView)rootView.findViewById(R.id.userDataTxt);
        DocumentReference userData = FirebaseFirestore.getInstance()
                .collection("users")
                .document(
                        FirebaseAuth
                                .getInstance()
                                .getCurrentUser()
                                .getUid()
                );
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
    void Manage(View view){
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.loading, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        new PopupWindow(popupView, width, height, focusable).showAtLocation(view, Gravity.CENTER, 0, 0);
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