package lit.de.vkanect.student.frag.notice;

import static lit.de.vkanect.data.CONSTANTS.Firebase.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lit.de.vkanect.R;
import lit.de.vkanect.data.Massage;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeAdapterViewHolder> {
    public class NoticeAdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView noticeText;

        public NoticeAdapterViewHolder(View itemView) {
            super(itemView);
            noticeText = itemView.findViewById(R.id.motion_base);
        }
    }

    List<Massage> list = new ArrayList<>();
    public NoticeAdapter(List<Massage> l){
        this.list = l;
       //Log.d(TAG, "onBindViewHolder: "+list.toString());
    }

    @NonNull
    @Override
    public NoticeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.student_notice_recycler_row,parent,false);

        return new NoticeAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapterViewHolder holder, int position) {
        Log.d(TAG, "Here");
        Massage massage = list.get(position);
        holder.noticeText.setText(massage.text);
        //Log.d(TAG, "onBindViewHolder: "+massage.toString());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
