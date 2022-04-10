package lit.de.vkanect.faculty.frag.work;

import static lit.de.vkanect.data.CONSTANTS.Firebase.FACULTY;
import static lit.de.vkanect.data.CONSTANTS.Firebase.TAG;
import static lit.de.vkanect.data.CONSTANTS.Firebase.institute;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.CONSTANTS.Firebase;
import lit.de.vkanect.data.Massage;
import lit.de.vkanect.data.Work;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.MyViewHolder> {
    private List<Massage> list;
    Context context;
    View v;
    class MyViewHolder extends RecyclerView.ViewHolder {
        Button text;
        Button delete_work;
        Button viewStatus;
        MyViewHolder(View view) {
            super(view);
            v = view;
            context = view.getContext();
            text = view.findViewById(R.id.workInfoText);
            delete_work = view.findViewById(R.id.delete_work);
            viewStatus = view.findViewById(R.id.viewStatus);

        }
    }
    public WorkAdapter(List<Massage> workList) {
        this.list = workList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.work_view_row, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Massage work = list.get(position);
        holder.text.setText(work.text);
        if(FACULTY){
            holder.delete_work.setVisibility(View.VISIBLE);
            holder.viewStatus.setVisibility(View.VISIBLE);
            holder.delete_work.setOnClickListener(delete->{
                try{
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Are you sure...?")
                            .setMessage("do you want to delete notice")
                            .setIcon(R.drawable.ic_baseline_warning_24)
                            .setPositiveButton("Yes", (dialog, whichButton) -> {

                                Log.d(TAG, "onBindViewHolder: "+work.key);
                                Firebase.instituteDB.child(institute.getId()).child("work").child(work.key).removeValue();

//                        instituteDB.child(mInstitute.id).removeValue()
//                                .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: DELETD"))
//                                .addOnFailureListener(e -> Log.d(TAG, "ERROr: DELET"));


                                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("No", null)
                            .show();
                }catch (Exception e){
                    Log.e(TAG, "onBindViewHolder: "+e.getMessage(),e);
                    // Create a snackbar
//                    Snackbar snackbar
//                            = Snackbar
//                            .make(
//
//                                    "Confirm delete...?!",
//                                    Snackbar.LENGTH_LONG)
//                            .setAction(
//                                    "DELETE",
//
//                                    // If the Undo button
//                                    // is pressed, show
//                                    // the message using Toast
//                                    new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view)
//                                        {
//                                            Firebase.instituteDB.child(institute.getId()).child(
//                                                    "notice").child(work.key).removeValue();
//                                        }
//                                    });
//
//                    snackbar.show();
                }

            });
        }else{
            holder.delete_work.setVisibility(View.GONE);
            holder.viewStatus.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}