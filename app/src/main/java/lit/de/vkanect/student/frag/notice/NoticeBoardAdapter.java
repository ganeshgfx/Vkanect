package lit.de.vkanect.student.frag.notice;

import static lit.de.vkanect.data.CONSTANTS.Firebase.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.Massage;

public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.MyViewHolder> {
    private List<Massage> list;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.message_text);
        }
    }
    public NoticeBoardAdapter(List<Massage> moviesList) {
        this.list = moviesList;
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
        holder.text.setText(msg.text);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}