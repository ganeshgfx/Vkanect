package lit.de.vkanect.faculty.frag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.Work;
import lit.de.vkanect.faculty.frag.work.WorkAdapter;

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
RecyclerView workRowRecycler;
    WorkAdapter workAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.faculty_fragment_work, container, false);

        workRowRecycler = view.findViewById(R.id.work_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        workRowRecycler.setLayoutManager(mLayoutManager);
        workRowRecycler.setItemAnimator(new DefaultItemAnimator());

        List<Work> list = new ArrayList<>();

        list.add(new Work());
        list.add(new Work());
        list.add(new Work());
        list.add(new Work());

        workAdapter = new WorkAdapter(list);
        workRowRecycler.setAdapter(workAdapter);

        return view;
    }
}