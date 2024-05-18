package CommentAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.minseok.loginanddatabase.R;

import java.util.ArrayList;


//해당 액티비티는 어댑터 즉 RecruimenwtActivity에 있는
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<CommentItem> commentItems;

    public CommentAdapter(ArrayList<CommentItem> commentItems) {
        this.commentItems = commentItems;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list,parent,false);
        return new CommentAdapter.ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        holder.text_comment_user.setText("유저");
        holder.text_comment_content.setText(commentItems.get(position).getComment_content());
        holder.text_comment_date.setText(commentItems.get(position).getComment_date());
    }

    @Override
    public int getItemCount() {
        if (commentItems != null) {
            return commentItems.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_comment_user,text_comment_content,text_comment_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_comment_user=itemView.findViewById(R.id.text_comment_user);
            text_comment_content=itemView.findViewById(R.id.text_comment_content);
            text_comment_date=itemView.findViewById(R.id.text_comment_date);
        }
    }
}