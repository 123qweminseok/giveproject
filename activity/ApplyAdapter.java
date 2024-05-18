package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minseok.loginanddatabase.R;

import java.util.ArrayList;


//걍 어댑터임 나중에 연결만 시키면 됨 -여기서 이제 모든것이 연결된다. 화면에 띄워주는 역할을 하는거지
public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.ViewHolder> {
//ApplyAdapter 클래스는 RecyclerView의 어댑터를 정의하는 것이며,
// 어떤 화면에도 직접적으로 연결되어 있지 않습니다. 따라서 getActivity()와 같은 메서드를 사용할 수 없습니다.
    //※그래서 생성자를 통해 값을 받아와 저장시켜준다 ※모든건 어댑터를 연결시키는 액티비티에서 해당 어댑터로 화면에
    // 각각의 뷰들이 생성되는거다! setAdapter 메서드시에 해당 내부 메서드들이 자동으로 실행된다!!※

    private ArrayList<ApplyItem> applyItems;
    private Context context;

    public ApplyAdapter(ArrayList<ApplyItem> applyItems, Context context) {
        this.applyItems = applyItems;
        this.context = context;
        //여기로 ApplyFragment클래스에 대한 화면이 제공된다.
        //ApplyFragment클래스에서 생성자를 통해 값들을 db에서 뺀 값들을 계속해서 넣어서 리스트에 차곡차곡 쌓음.

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder= LayoutInflater.from(parent.getContext()).inflate(R.layout.apply_list,parent,false);
        return new ViewHolder(holder);
        //리턴을 onBindViewHolder의 매개변수로 들어간다. 즉 아래에서 뷰홀더 객체가 만들어지며 값들이 리턴되는거임!
//그리고 안에 저장된 값들을 뽑음


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text_title.setText(applyItems.get(position).getTitle());
        holder.text_content.setText(applyItems.get(position).getContent());
        holder.bubble_number.setText(applyItems.get(position).getNumber());
        holder.text_date.setText(applyItems.get(position).getDate());
    }//포지션 즉 인덱스 값에 맞게끔 세팅함 화면에.
//어댑터 연결시 onCreateViewHolder 이것이 각각의 뷰홀더 객체가 다르게 다 저장되고
// onBindViewHolder이 메서드와 onCreateViewHolder 이 두개가 getItemCount갯수만큼 반복되서 나타나는것이다.

    //이건 화면에 나타낼 뷰홀더 갯수 이다. 해당 갯수만큼 위 두개의 메서드들이 반복한다. (ViewHolder의 객체를 ㅇㅇ)
    @Override
    public int getItemCount() {
        if (applyItems != null) {
            return applyItems.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_title,text_content,bubble_number,text_date,speech_bubble,line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_title=itemView.findViewById(R.id.text_title);
            text_content=itemView.findViewById(R.id.text_content);
            bubble_number=itemView.findViewById(R.id.bubble_number);
            text_date=itemView.findViewById(R.id.text_date);
            speech_bubble=itemView.findViewById(R.id.speech_bubble);
            line=itemView.findViewById(R.id.line);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positon=getAdapterPosition(); //해당 리사이클러뷰에 위치를 받아온다 각각의 리사이클러뷰 위치임.(인덱스)
                    Intent intent=new Intent(context, RecruitmentActivity.class);
                    //해당 액티비티로 클릭하면 넘어가는데 이건 댓글 달수있게 하는 액티비티이다.
                    intent.putExtra("id",applyItems.get(positon).getId()); // 해당 고유 id를 모집하기 엑티비티로
                    intent.putExtra("title",applyItems.get(positon).getTitle()); //해당 리사이클러뷰에 제목을 모집하기 엑티비티로
                    intent.putExtra("content",applyItems.get(positon).getContent()); //해당 리사이클러뷰에 내용을 모집하기 엑티비티로
                    intent.putExtra("date",applyItems.get(positon).getDate()); //해당 리사이클러뷰에 날짜를 모집하기 엑티비티로
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                }            });
//            윗부분은 화면이 넘어갔을때 값을 전달해주는 역할을 한다. 즉 클릭시 다른 액티비티로 정보를 넘겨준다.
        }
    }
}
