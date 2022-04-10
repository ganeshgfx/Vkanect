package lit.de.vkanect.faculty.frag;

import static lit.de.vkanect.data.CONSTANTS.Firebase.FAC_getInstitude;
import static lit.de.vkanect.data.CONSTANTS.Firebase.TAG;
import static lit.de.vkanect.data.CONSTANTS.Firebase.institute;
import static lit.de.vkanect.data.CONSTANTS.Firebase.instituteDB;
import static lit.de.vkanect.data.CONSTANTS.Firebase.studInstitute;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.CONSTANTS.Firebase;
import lit.de.vkanect.data.Institute;
import lit.de.vkanect.data.Massage;
import lit.de.vkanect.data.Work;
import lit.de.vkanect.faculty.frag.work.WorkAdapter;
import lit.de.vkanect.student.frag.notice.NoticeBoardAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link F_WorkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_WorkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public F_WorkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static F_WorkFragment newInstance(String param1, String param2) {
        F_WorkFragment fragment = new F_WorkFragment();
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

    boolean recyleViewVisiblity = true;
    com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton makeWorkBttn ;
    Button send_new_button;
    RecyclerView workRowRecycler;
    WorkAdapter workAdapter;
    LinearLayout linearLayout;
    List<Massage> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.faculty_fragment_work, container, false);
        send_new_button = view.findViewById(R.id.fac_work_send);
        workRowRecycler = view.findViewById(R.id.work_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        workRowRecycler.setLayoutManager(mLayoutManager);
        workRowRecycler.setItemAnimator(new DefaultItemAnimator());

        list = new ArrayList<>();

        linearLayout = view.findViewById(R.id.make_work_layout);
        makeWorkBttn = view.findViewById(R.id.fab_make_Work);

        makeWorkBttn.setOnClickListener(V->{
            switchView();
        });

//        list.add(new Work());
//        list.add(new Work());

//        workAdapter = new WorkAdapter(list);
//        workRowRecycler.setAdapter(workAdapter);



        EditText fac_work_text = view.findViewById(R.id.fac_work_text);
        fac_work_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    makeWorkBttn.setVisibility(View.GONE);
                else
                    makeWorkBttn.setVisibility(View.VISIBLE);
            }
        });


        send_new_button.setOnClickListener(click->{
            EditText work_text = view.findViewById(R.id.fac_work_text);
            String text = work_text.getText().toString();
            if(!text.equals("")){
               // Log.d(TAG, "HERE"+text);
                sendWork(text);

            }
        });

        loadInstitute();

        return view;
    }

    private void switchView() {
        if(recyleViewVisiblity){
            workRowRecycler.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            makeWorkBttn.setIcon(getResources().getDrawable(R.drawable.ic_arrow_back));

        }else{
            workRowRecycler.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            makeWorkBttn.setIcon(getResources().getDrawable(R.drawable.ic_book));

        }
        recyleViewVisiblity = !recyleViewVisiblity;
        makeWorkBttn.setExtended(recyleViewVisiblity);
    }
    void loadWorks(){
        instituteDB.child(mInstitute.getId()).child("work").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Log.d(TAG, "onDataChange: "+snapshot.toString());
                list.clear();
                for (DataSnapshot data:snapshot.getChildren()) {
                    Massage massage = data.getValue(Massage.class);
                    massage.setKey(data.getKey());
                    list.add(massage);

                }
                workAdapter = new WorkAdapter(list);
workRowRecycler.setAdapter(workAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    Institute mInstitute = studInstitute;
    void loadInstitute(){

        try {
            FAC_getInstitude();
            mInstitute = institute;
            makeWorkBttn.setVisibility(View.VISIBLE);
            loadWorks();
        }catch (Exception e){

            Log.d(TAG, "loadInstitute: "+e.getMessage());

            try {
                Toast toast = Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }catch (Exception er){}

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    loadInstitute();
                }
            }, 2500);

        }
    }
    void sendWork(String xText){
        Toast.makeText(getActivity(), "Sending", Toast.LENGTH_SHORT).show();
            Firebase.instituteDB.child(institute.getId()).child("work").push().setValue(new Massage(xText, FirebaseAuth.getInstance().getUid(),Massage.TYPE_WORK)).addOnSuccessListener(unused -> {
                switchView();
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Failed to send work", Toast.LENGTH_SHORT).show();
                }
            });
    }
}