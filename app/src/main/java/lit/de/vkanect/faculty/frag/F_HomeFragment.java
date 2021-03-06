package lit.de.vkanect.faculty.frag;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lit.de.vkanect.HOME.Attendees;
import lit.de.vkanect.HOME.Fees;
import lit.de.vkanect.HOME.Result;
import lit.de.vkanect.HOME.TimeTable;
import lit.de.vkanect.R;
import lit.de.vkanect.faculty.manageCollage.ManageCollage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link F_HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class F_HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static F_HomeFragment newInstance(String param1, String param2) {
        F_HomeFragment fragment = new F_HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public F_HomeFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.faculty_fragment_home, container, false);

        view.findViewById(R.id.ttBtn).setOnClickListener(v->this.startActivity(new Intent(getActivity(), TimeTable.class)));
        view.findViewById(R.id.atdBtn).setOnClickListener(v->this.startActivity(new Intent(getActivity(), Attendees.class)));
        view.findViewById(R.id.passBtn).setOnClickListener(v->this.startActivity(new Intent(getActivity(), Result.class)));
        view.findViewById(R.id.paisaBtn).setOnClickListener(v->this.startActivity(new Intent(getActivity(), Fees.class)));

        return view;
    }

}