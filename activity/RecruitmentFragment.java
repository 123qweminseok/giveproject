package activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.minseok.loginanddatabase.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecruitmentFragment extends Fragment {
    //여기에는 ☆댓글, 그리고 apply쪽의 댓글갯수만 업데이트 해줌☆☆☆☆ apply쪽을 봐야함
    TextView text_recruitment_title,text_recruitment_content;
    Button button_recruitment;

    FirebaseAuth mFirebaseauth;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=(ViewGroup) inflater.inflate(R.layout.recruitment_fragment,container,false);
        mFirebaseauth=FirebaseAuth.getInstance();

        text_recruitment_title=rootView.findViewById(R.id.text_recruitment_title);
        text_recruitment_content=rootView.findViewById(R.id.text_recruitment_content);
        button_recruitment=rootView.findViewById(R.id.button_recruitment);

        clickbutton(button_recruitment);
        //버튼 객체를 넣은것과 같음 id로 받음->xml파일에서 객체화시켜서 연결다리 지어준것일뿐.
//※이게 제일 기본임!!!! 확인 버튼 눌러서 제목,내용 값 다 넣고, 아래에다가
        return rootView;
    }

    public void clickbutton(Button button){
        //버튼 객체가 들어가며 리스너 됨.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyItem item=new ApplyItem();
                SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd HH:mm",new Locale("ko","KR"));
                String currentDate=dateFormat.format(new Date());
                item.setTitle(text_recruitment_title.getText().toString());
                item.setContent(text_recruitment_content.getText().toString());
                item.setDate(currentDate);
                item.setNumber("0");
            //여기서 ApplyItem 에다가 적은 값들 다 집어넣어줌 (get,set메서드 사용해서)->그리고 아래 코드로 db에 값 집어넣게 됨.

//          ※※※※여기서 db값이 추가되는 것이다!!!!!!! uid별로 값이 저장되는 공간이다!※※※※

                DatabaseReference userRef=databaseReference.child("apply list").child(mFirebaseauth.getCurrentUser().getUid());
                //집어넣을 값의 경로설정 제일 위는 applylist-Uid값
                userRef.push().setValue(item);
                // 각 데이터에 대해 고유한 키를 생성하여 추가, db에 들어가졌음-> setValue로 item객체를 넣음으로 item필드값들이 다 db에 저장됨.
                //해당 객체 안에 필드에 대한 값까지 있을때 필드=변수=키, 변수에 대한 값= 값

//아래는 추가로 만들어준 값. 즉 uid값 없이 push로 랜덤한 값대로 만들어줌 . 즉 두개의 데이터베이스가 생김.
                DatabaseReference applyRecyclerView=databaseReference.child("applyRecyclerView");
                applyRecyclerView.push().setValue(item);
                text_recruitment_title.setText("");
                text_recruitment_content.setText("");

            }
        });
    }

}