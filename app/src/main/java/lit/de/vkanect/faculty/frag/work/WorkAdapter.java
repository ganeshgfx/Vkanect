package lit.de.vkanect.faculty.frag.work;

import static lit.de.vkanect.data.CONSTANTS.Firebase.FACULTY;
import static lit.de.vkanect.data.CONSTANTS.Firebase.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.Massage;
import lit.de.vkanect.data.Work;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.MyViewHolder> {
    private List<Work> list;
    class MyViewHolder extends RecyclerView.ViewHolder {

        MyViewHolder(View view) {
            super(view);

        }
    }
    public WorkAdapter(List<Work> workList) {
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
        Work work = list.get(position);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}