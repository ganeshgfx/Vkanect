package lit.de.vkanect.student.frag;

import static lit.de.vkanect.data.CONSTANTS.Firebase.STUD_getInstitude;
import static lit.de.vkanect.data.CONSTANTS.Firebase.TAG;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.Institute;
import lit.de.vkanect.data.Massage;
import lit.de.vkanect.faculty.frag.work.WorkAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkFragment() {
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
    public static WorkFragment newInstance(String param1, String param2) {
        WorkFragment fragment = new WorkFragment();
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
    RecyclerView workRowRecycler;
    WorkAdapter workAdapter;
    LinearLayout linearLayout;
    List<Massage> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_work, container, false);

        workRowRecycler = inflate.findViewById(R.id.work_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        workRowRecycler.setLayoutManager(mLayoutManager);
        workRowRecycler.setItemAnimator(new DefaultItemAnimator());

        list = new ArrayList<>();

        loadInstitute();


        return inflate;
    }
    void loadInstitute(){
        try {
            STUD_getInstitude();
            mInstitute = studInstitute;
            loadWorks();

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
    void loadWorks(){
        instituteDB.child(mInstitute.getId()).child("work").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Log.d(TAG, "onDataChange: "+snapshot.toString());
                list.clear();
                for (DataSnapshot data:snapshot.getChildren()) {
                    Massage massage = data.getValue(Massage.class);
                    massage.setKey(data.getKey());

                    //Log.d(TAG, "onDataChange: "+data.getKey());

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
}