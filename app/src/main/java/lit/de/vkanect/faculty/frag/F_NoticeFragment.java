package lit.de.vkanect.faculty.frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lit.de.vkanect.R;
import lit.de.vkanect.data.CONSTANTS.Firebase;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.faculty_fragment_notice, container, false);
        Button fac_notice_send = (Button)rootView.findViewById(R.id.fac_notice_send);
        fac_notice_send.setOnClickListener(V->{
            //Log.d("TAG", "onCreateView: OK");
            DatabaseReference myRef =   FirebaseDatabase.getInstance().getReference();
            myRef.child("notice").setValue(Firebase.institute.getId()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("TAG", "onComplete: "+task.getResult().toString());
                }
            });
        });
        return rootView;
    }
}