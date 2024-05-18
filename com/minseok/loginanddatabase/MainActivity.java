package com.minseok.loginanddatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.integrity.IntegrityTokenRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button logoutButton; //로그아웃 버튼
    private FirebaseAuth mFirebaseauth;//Firebase 인증 객체
    private DatabaseReference mDatabaseRef;//실시간 데이터베이스 참소 객체
    private EditText datasave,dataread;
    private Button savebutton;

    public Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        next=findViewById(R.id.next1);

        logoutButton = findViewById(R.id.logoutButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), activity.MainActivity.class);
                startActivity(intent);
            }
        });
        //로그아웃 관련된 코드들
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            // 로그아웃 성공
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                                    finish();
                                    Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            // 로그아웃 후에 추가 작업을 수행할 수 있습니다.
                        } else {
                            // 로그아웃 실패
                            Toast.makeText(MainActivity.this, "로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        /////////////////////////////////////////////////////////////여기까지 로그아웃 버튼.

        mFirebaseauth=FirebaseAuth.getInstance();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference();

        datasave=findViewById(R.id.editTextText2);
                dataread=findViewById(R.id.editTextText3);

                savebutton=findViewById(R.id.button);

                //저장버튼 누르면 db에 저장하게끔
        datasave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = datasave.getText().toString(); //입력한 텍스트를 불러와 문자열로 변환함.

                // Firebase 데이터베이스에 데이터 저장하기 setValue메서드로 내부 매개값을 변수에 저장. getCurrentUser메서드로 현재 사용자 아이디에 맞게 저장함.
                mDatabaseRef.child("dataset").child(mFirebaseauth.getCurrentUser().getUid()).setValue(strEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "데이터 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        mDatabaseRef.child("dataset").child(mFirebaseauth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    if (snapshot.exists()) {

                        // 데이터가 존재하는 경우 해당 데이터를 읽어와 EditText에 표시
                        String userData = snapshot.getValue(String.class);
                        dataread.setText(userData);
                        Toast.makeText(MainActivity.this, "데이터가 존재한다.", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "해당 사용자의 데이터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "데이터 읽기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
