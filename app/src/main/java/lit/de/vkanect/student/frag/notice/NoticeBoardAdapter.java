package lit.de.vkanect.student.frag.notice;

import static lit.de.vkanect.data.CONSTANTS.Firebase.FACULTY;
import static lit.de.vkanect.data.CONSTANTS.Firebase.TAG;
import static lit.de.vkanect.data.CONSTANTS.Firebase.institute;
import static lit.de.vkanect.data.CONSTANTS.Firebase.instituteDB;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.CONSTANTS.Firebase;
import lit.de.vkanect.data.Institute;
import lit.de.vkanect.data.Massage;

public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.MyViewHolder> {
    private List<Massage> list;
    Institute mInstitute;
    Context context;
    ConstraintLayout layout;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        TextView sender_name;
        Button delete;
        MaterialCardView card;
        ImageView fireImageForNotice;

        MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.message_text);
            delete = view.findViewById(R.id.button_delete_msg);
            card =  view.findViewById(R.id.card);
            sender_name = view.findViewById(R.id.sender_name);
            fireImageForNotice = view.findViewById(R.id.fireImageForNotice);
        }
    }
    public NoticeBoardAdapter(List<Massage> moviesList,Institute mInstitute,Context context, ConstraintLayout layout) {
        this.list = moviesList;
        this.context = context;
        this.mInstitute = mInstitute;
        this.layout = layout;
        Log.d(TAG, "MoviesAdapter: "+this.list.toString());
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_notice_recycler_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Massage msg = list.get(position);

        //Log.d(TAG, "onBindViewHolder: "+msg.sender);
        holder.text.setText(msg.text);
        holder.sender_name.setText("Sent by - "+ msg.sender);
        holder.delete.setOnClickListener(c->{
            //Log.d(TAG, "onBindViewHolder:DELEYTE "+msg.key);
            //Firebase.instituteDB.child(institute.getId()).child("notice");


           try{
               new MaterialAlertDialogBuilder(context)
                       .setTitle("Are you sure...?")
                       .setMessage("do you want to delete notice")
                       .setIcon(R.drawable.ic_baseline_warning_24)
                       .setPositiveButton("Yes", (dialog, whichButton) -> {

                           Firebase.instituteDB.child(institute.getId()).child("notice").child(msg.key).removeValue();

//                        instituteDB.child(mInstitute.id).removeValue()
//                                .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: DELETD"))
//                                .addOnFailureListener(e -> Log.d(TAG, "ERROr: DELET"));


                           Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
                       })
                       .setNegativeButton("No", null)
                       .show();
           }catch (Exception e){
               Log.d(TAG, "onBindViewHolder: "+e.getMessage());
               // Create a snackbar
               Snackbar snackbar
                       = Snackbar
                       .make(
                               layout,
                               "Confirm delete...?!",
                               Snackbar.LENGTH_LONG)
                       .setAction(
                               "DELETE",

                               // If the Undo button
                               // is pressed, show
                               // the message using Toast
                               new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view)
                                   {
                                       Firebase.instituteDB.child(institute.getId()).child("notice").child(msg.key).removeValue();
                                   }
                               });

               snackbar.show();
           }

        });
        if(FACULTY){
            holder.delete.setVisibility(View.VISIBLE);
        }else{
            holder.delete.setVisibility(View.GONE);
        }
        if(msg.img!=null){
            holder.fireImageForNotice.setVisibility(View.VISIBLE);
        }
        //if(position==list.size()){ }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}