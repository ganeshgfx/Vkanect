package lit.de.vkanect.student.frag;

import static lit.de.vkanect.data.CONSTANTS.Firebase.STUD_getInstitude;
import static lit.de.vkanect.data.CONSTANTS.Firebase.instituteDB;
import static lit.de.vkanect.data.CONSTANTS.Firebase.studInstitute;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.Institute;
import lit.de.vkanect.data.Massage;
import lit.de.vkanect.student.frag.notice.NoticeBoardAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NoticeFragment() {
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
    public static NoticeFragment newInstance(String param1, String param2) {
        NoticeFragment fragment = new NoticeFragment();
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

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notice, container, false);
        //noticeText = (TextView) root.findViewById(R.id.noticeText);
        recyclerView = root.findViewById(R.id.noticeRecycleView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadInstitute();
        //Log.d(TAG, "onCreateView: "+studInstitute.toString());

        return root;
    }
    void loadInstitute(){
        try {
            STUD_getInstitude();
            mInstitute = studInstitute;
          //  Log.d(TAG, "loadInstitute: Loaded");
            loadNotices();

        }catch (Exception e){

            Toast toast = Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

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
    Institute mInstitute;
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
    private List<Massage> movieList = new ArrayList<>();
    private NoticeBoardAdapter mAdapter;
}