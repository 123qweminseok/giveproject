package activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minseok.loginanddatabase.R;

import java.util.ArrayList;

public class ApplyFragment extends Fragment {
    //☆☆☆☆해당 프래그먼트가 RecruimentFragment에서 db로 저장한 값들을 불러와서 띄워줌!☆☆☆
    //여기서 아이템을 담아 어댑터로 반드시 보내야 한다!!
    // 즉 arraylist에 담긴것을 그대로 어댑터에 보내서 어댑터에서 갯수만큼 초기화 해주는거다!

    //1.db에있는값을 젤 아래쪽  arraylist1으로 저장시켜주고 순서대로. 그걸 ApplyAdapter의 생성자로 보냄
    //2.보낸 갯수만큼 이제 applayAdapter에서 갯수만큼 onBindViewHolder반복됨 그러면서 화면에 띄움 이게 핵심임.setAdapter을 하면 자동 발동 ㅇㅇ
    RecyclerView recyclerView_apply;
    ApplyAdapter applyAdapter;
    ArrayList<ApplyItem> applyItems1;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup) inflater.inflate(R.layout.apply_fragment,container,false);
        applyItems1=new ArrayList<>();
        recyclerView_apply=rootView.findViewById(R.id.recyclerView_apply);
//        //리사이클러뷰 특징 시작 1.ㅇㅇxml레이아웃  id가져옴
        //객체 빼와서 저장하는것도
        applyAdapter=new ApplyAdapter(applyItems1,getActivity());
        //어댑터 가져와서 아래에 연결함.
        //현재 담긴 내용들을 생성자를 통해 다 보내줌. ->여기서 이제 내용들을 다 보낸거임.(처음에는 암것도 없고 아래에 applyItems1값 추가하는거 있다)
        //또한 현재 프래그먼트의 참조를 얻기 위해 getActivity가 사용된다.
        recyclerView_apply.setAdapter(applyAdapter); //리사이클러뷰에 어댑터 연결시 어댑터 내용들이 뜬다.


//       ※여기가 이제 리사이클러뷰 띄워주는 액티비티임 어댑터 연결해서 띄우는거 ㅇㅇ.※

        addChildEvent();
        return rootView;
    }
    //그냥 해당 경로로 접근하고  리스너 추가시 addChildEventListener는 해당 위치의 데이터베이스에 새로운 자식 항목추가되면 자동으로 호출된다!
    // 각각의 콜백 메서드(onChildAdded(), onChildChanged(), onChildRemoved(), onChildMoved(), onCancelled())가 호출되어 해당 변경 사항을 처리합니다





    //☆☆☆이 값이 db에서 값을 가져옴!!!!!!!!! 어디에? applyItems1리스트에다가 추가☆☆☆
    // Recruitement 프래그먼트에서 추가한 값을 가져온다. 이제 큰 단락으로 보면 두번째 것이다.☆☆☆
    public void addChildEvent(){
        databaseReference.child("applyRecyclerView").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //해당 노드값에 접근시 자동으로 실행됨 ㅇㅇ
                //아래 for문은 db에 있는 자식들 순서대로 계속해서 반복시킴. 즉 한번 도는게 아니고 계속해서 반복함. apply list하위 노드갯수만큼. (아마 맞을거임)
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ApplyItem item=dataSnapshot.getValue(ApplyItem.class);
                    applyItems1.add(0,item);//추가시 리스트의 제일 앞쪽에 추가하는거고,item은 추가할 객체이다.
                        //현재 item에는 값이 들어있다.


                }
                //이제 이걸 순서대로 넣어주면 됨

                applyAdapter.notifyDataSetChanged();
              //   리사이클러뷰 갱신 =>야! 이제 리스트 크기도 변할 거고, 아이템도 새로운 게 들어올 거야. 다시 새로 그려 즉 아이템간에 삽입, 삭제, 이동이 일어났을 때!"
            //즉 처음부터 실행된다고 생각하면 된다. ※정확한 구조는 확인해봐야함
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
