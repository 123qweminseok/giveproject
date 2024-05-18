package Myinformation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minseok.loginanddatabase.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import activity.ApplyAdapter;
import activity.ApplyItem;

//해당 액티비티는 내가 쓴 글 불러오는것
public class list1 extends Fragment {
    RecyclerView recyclerView_apply;
    private FirebaseAuth mFirebaseauth;//Firebase 인증 객체
    ApplyAdapter applyAdapter;
    ArrayList<ApplyItem> applyItems;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.list1, container, false);
        mFirebaseauth=FirebaseAuth.getInstance();

        applyItems = new ArrayList<>();
        recyclerView_apply = rootView.findViewById(R.id.recyclerView_apply2);
        applyAdapter = new ApplyAdapter(applyItems, getActivity());
        recyclerView_apply.setAdapter(applyAdapter);

        addChildEvent();

        return rootView;
    }

    public void addChildEvent() {
        databaseReference.child("apply list").child(mFirebaseauth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("확인하기",snapshot.toString()); //하윗값 전부 가지고 있고
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    ApplyItem item=dataSnapshot.getValue(ApplyItem.class);
Log.d("1",dataSnapshot.toString()); //키에 고유번호, 값에 키,값 뭉치로 있음.
                    Log.d("2",item.toString());
                    applyItems.add(0,item);
                }
                applyAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
  //  여기부터 새로 만들어야 함 리사이클러뷰에 띄울거임 아래 다 주석처리
  /*  private void addChildEvent() {
//       사용자 하위에 있는값들 다 가져오고
        databaseReference.child("apply list").child(mFirebaseauth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("보여줘",snapshot.toString());

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
            //하위 항목들에 대해 순차적으로 접근함 키,값 형태로
                    Log.d("순차접근",dataSnapshot.toString());
                    arrayList.add(dataSnapshot.getValue());
                    //각각 값을 저장시켜줬잖아. 근데 그 값이 지금  스트링 형태잖아 tostring필요없음 ㅇㅇ
                    Log.d("리스트 갑 확인",arrayList.toString());
System.out.println(arrayList);
                }

                textView1.setText(arrayList.get(0).toString());
                textView2.setText(arrayList.get(2).toString());

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
*/




