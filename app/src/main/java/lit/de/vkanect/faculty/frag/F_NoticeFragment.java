package lit.de.vkanect.faculty.frag;

import static lit.de.vkanect.data.CONSTANTS.Firebase.FAC_getInstitude;
import static lit.de.vkanect.data.CONSTANTS.Firebase.STUD_getInstitude;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.CONSTANTS.Firebase;
import lit.de.vkanect.data.Institute;
import lit.de.vkanect.data.Massage;
import lit.de.vkanect.student.frag.notice.NoticeBoardAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link F_NoticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_NoticeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "VKT";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public F_NoticeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoticeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static F_NoticeFragment newInstance(String param1, String param2) {
        F_NoticeFragment fragment = new F_NoticeFragment();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // FIXME: 3/31/22 Fix loading...
        FAC_getInstitude();
        //Toast.makeText(getContext(), "Loading please wait", Toast.LENGTH_LONG).show();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.faculty_fragment_notice, container, false);
        Button fac_notice_send = (Button)rootView.findViewById(R.id.fac_notice_send);
        EditText fac_notice_text = (EditText) rootView.findViewById(R.id.fac_notice_text);

        recyclerView = rootView.findViewById(R.id.handle_notice_fac);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayout linearLayout = rootView.findViewById(R.id.linearLayout);

        com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton btnSendNotice = rootView.findViewById(R.id.extended_fab_send_notice);

        recyclerView.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);

        loadInstitute();

        btnSendNotice.setOnClickListener(V->{
            if(recyleViewVisiblity){
                recyclerView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                btnSendNotice.setIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
            }else{
                recyclerView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                btnSendNotice.setIcon(getResources().getDrawable(R.drawable.ic_notifications));
            }
            recyleViewVisiblity = !recyleViewVisiblity;
            btnSendNotice.setExtended(recyleViewVisiblity);
        });

        fac_notice_send.setOnClickListener(V->{

            //Log.d(TAG, "Send notice : "+fac_notice_text.getText());
            String text = fac_notice_text.getText().toString();

            if(!text.equals("")){
                sendNotice(text);
            }



        });
        return rootView;
    }
    void sendNotice( String xText){
        try {
            FAC_getInstitude();
            Massage value = new Massage(xText, FirebaseAuth.getInstance().getUid(), Massage.TYPE_NOTICE);

            value
                    .setSender(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
            ;

            Firebase.instituteDB.child(institute.getId()).child("notice").push().setValue(value);

        }catch (Exception e){

            Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendNotice(xText);
                }
            }, 2500);

        }
    }
    Institute mInstitute = studInstitute;
    void loadInstitute(){
        try {
            FAC_getInstitude();
            mInstitute = institute;
            loadNotices();
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

    void loadNotices(){
        instituteDB.child(mInstitute.getId()).child("notice").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Log.d(TAG, "onDataChange: "+snapshot.toString());

                List<Massage> list = new ArrayList<>();

                movieList.clear();

                for (DataSnapshot data:snapshot.getChildren()) {

                    Massage massage = data.getValue(Massage.class);
                    list.add(massage);
                    movieList.add(massage);

                }
                mAdapter = new NoticeBoardAdapter(movieList);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    RecyclerView recyclerView;
    private List<Massage> movieList = new ArrayList<>();
    private NoticeBoardAdapter mAdapter;
}