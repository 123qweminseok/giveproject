package CommentAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minseok.loginanddatabase.R;

import java.util.ArrayList;

import CommentAdapter.CommentAdapter;
import activity.ApplyAdapter;
import activity.ApplyItem;

public class comment1 extends Fragment {
    RecyclerView recyclerView_apply;
    private FirebaseAuth mFirebaseauth;//Firebase 인증 객체
    CommentAdapter commentAdapter;
    ArrayList<CommentItem> commentItems;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.comment1, container, false);
        mFirebaseauth=FirebaseAuth.getInstance();

        commentItems = new ArrayList<>();
        recyclerView_apply = rootView.findViewById(R.id.recyclerView_apply3);
        commentAdapter = new CommentAdapter(commentItems);
        recyclerView_apply.setAdapter(commentAdapter);
//여기서 리사이클러뷰 설정해준것.
        addChildEvent();

        return rootView;
    }
//이건 값을 가져와서 리사이클러뷰형태로 띄우는것임.순서대로.
    public void addChildEvent() {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("comment list").orderByChild("uid").equalTo(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("11111111111111111111",snapshot.toString()); //하윗값 전부 가지고 있고
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    CommentItem item=dataSnapshot.getValue(CommentItem.class);
                    Log.d("1",dataSnapshot.toString()); //키에 고유번호, 값에 키,값 뭉치로 있음.
                    Log.d("2",item.toString());
                    commentItems.add(0,item);
                }
                commentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
