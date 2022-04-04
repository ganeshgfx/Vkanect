package lit.de.vkanect.faculty.frag.work;

import static lit.de.vkanect.data.CONSTANTS.Firebase.FACULTY;
import static lit.de.vkanect.data.CONSTANTS.Firebase.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.Massage;
import lit.de.vkanect.data.Work;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.MyViewHolder> {
    private List<Massage> list;
    class MyViewHolder extends RecyclerView.ViewHolder {
        Button text;
        Button delete_work;
        Button viewStatus;
        MyViewHolder(View view) {
            super(view);
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