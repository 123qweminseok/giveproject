package activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minseok.loginanddatabase.R;
import com.minseok.loginanddatabase.RegisterActivity;
import com.minseok.loginanddatabase.UserAccount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import CommentAdapter.CommentAdapter;
import CommentAdapter.CommentItem;

public class  RecruitmentActivity extends AppCompatActivity {

//※※※※※모집하기 프래그먼트에서 db에 저장시킨 값을 끌고온다. 1.현재 프래그먼트,2.현재 액티비티 순으로※※※※※


            RecyclerView recyclerView_comment;
    CommentAdapter commentAdapter;
    ArrayList<CommentItem> commentItems;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    TextView textview_title,textView_content,recruitment_number,recruitment_bubble_speech;
    EditText editText_comment;
    Button button_comment;
    String id,title,content,date;
    ApplyAdapter applyAdapter;
    ArrayList<ApplyItem> applyItems;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    //현재 로그인중인 사용자 Uid가져옴.

    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitmemt);

        commentItems=new ArrayList<>();
        textview_title=findViewById(R.id.textview_titie);
        textView_content=findViewById(R.id.textview_content);
        recruitment_number=findViewById(R.id.recruitment_number);
        recruitment_bubble_speech=findViewById(R.id.recruitment_bubble_speech);
        recyclerView_comment=findViewById(R.id.recyclerView_comment);

        commentAdapter=new CommentAdapter(commentItems);
        recyclerView_comment.setAdapter(commentAdapter);//어댑터 연결시킴

        //모집하기 프래그먼트에서 적은 글들이 해당 화면으로 들어온다.

        button_comment=findViewById(R.id.button_comment);
        editText_comment=findViewById(R.id.editText_comment);

        Intent intent=getIntent();//현재 사용하는 화면 가져오고
        id=intent.getStringExtra("id"); //해당 리사이클러뷰를 클릭하면 고유 id를 가져온다
        title=intent.getStringExtra("title"); //해당 리사이클러뷰를 클릭하면 제목을 가져온다
        content=intent.getStringExtra("content"); //해당 리사이클러뷰를 클릭하면 내용을 가져온다
        date=intent.getStringExtra("date"); //해당 리사이클러뷰를 클릭하면 날짜를 가져온다


        textview_title.setText(title);
        textView_content.setText(content);

        clickCommentButton(button_comment);
        addChildEvent();
         uid=currentUser.getUid(); //uid값 저장시킴
        //※uid값 저장시키는거임 코드 단 세줄!  현재 로그인중인 값 불러와서 저장시킴

    }

    public void clickCommentButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // 댓글 작성 버튼을 누르면 유저,해당 글id, 쓴 시간, 제목, 내용 등이 파이어베이스에 저장
                CommentItem item=new CommentItem();
                SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd HH:mm",new Locale("ko","KR"));
                String currentDate=dateFormat.format(new Date());
                item.setComment_user("유저");
                item.setComment_content(editText_comment.getText().toString());
                item.setComment_date(currentDate);
                item.setCheck_id(id);
                item.setCheck_title(title);
                item.setCheck_content(content);
                item.setCheck_date(date);
                RegisterActivity a= new RegisterActivity();
                item.setUid(uid);

                databaseReference.child("comment list").push().setValue(item);
                //이건 댓글 기능임.아래에 값들 다 넣어줌. db에 댓글 기능 저장시키는거임
                editText_comment.setText("");

            }
        });
    }

    public void addChildEvent(){
        databaseReference.child("comment list").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CommentItem item=snapshot.getValue(CommentItem.class);
                //db에있는 값들 가져오는것임.

                if(item.getCheck_id().equals(id)) {  //글 목록 id가 같은 댓글만 보이게
                    commentItems.add(item);    // 댓글 ui 추가
                    commentAdapter.notifyDataSetChanged(); // 새로고침
                    if(commentAdapter.getItemCount()==0){  // 댓글이 증가할때마다 ui변경
                        recruitment_bubble_speech.setVisibility(View.INVISIBLE);
                    }
                    else {
                        recruitment_number.setText(String.valueOf(commentAdapter.getItemCount()));
                        recruitment_number.setVisibility(View.VISIBLE);
                        recruitment_bubble_speech.setVisibility(View.VISIBLE);
                        updateApplyListNumber();
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //이건 뭐노 ->이건 작동 안하는것같음 ㅇㅇ 댓글이 추가될 때마다 Firebase의 데이터베이스에서 "apply list"에 있는 해당 항목의 댓글 수를 업데이트하는 역할을 합니다
    private void updateApplyListNumber() { // 댓글수가 증가할때마다 파이어베이스 넘버 값 수정
        DatabaseReference applyListRef = databaseReference.child("apply list");
        applyListRef.addListenerForSingleValueEvent(new ValueEventListener() {
        //apply list 하위노드에 저장함 값을 추가하는거임. 뽑아오는게 아니고
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //접근시에도 자동 호출됨
                for (DataSnapshot applySnapshot : snapshot.getChildren()) { //snapshot에 있는 각각의 자식(applySnapshot)에 대해 반복합니다. 각 자식은 "apply list" 위치에 있는 데이터를 나타냅니다.
                  Log.d("어디한번 보자",applySnapshot.toString());
                    ApplyItem applyItem = applySnapshot.getValue(ApplyItem.class);//ApplyItem 클래스 형식으로 변환해 값을 뽑아냄 특정값을
                    if(applyItem.getId().equals(id)) {
                        applySnapshot.getRef().child("number").setValue(String.valueOf(commentAdapter.getItemCount()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay,R.anim.slide_out_right);
    }
}