package lit.de.vkanect.HOME;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lit.de.vkanect.R;

public class TtAdapter extends RecyclerView.Adapter<TtAdapter.MyViewHolder> {

    private List<Tdata> list;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subject;
        TextView sr;
        TextView time;
        MyViewHolder(View view) {
            super(view);
            subject = view.findViewById(R.id.subject);
            time = view.findViewById(R.id.time);
            sr = view.findViewById(R.id.sr);
        }
    }
    public  TtAdapter(List<Tdata> workList) {
        this.list = workList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tt_row, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tdata work = list.get(position);
        holder.subject.setText(work.subject);
        holder.sr.setText((position+1)+"");
        holder.time.setText(work.time);

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
