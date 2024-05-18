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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.minseok.loginanddatabase.R;

import org.w3c.dom.Text;

import CommentAdapter.comment1;
import Myinformation.list1;

public class InformationFragment extends Fragment {
    Button button;
    Button button2;
    Boolean setting=false;//이건 xml파일과 상관없는거라 전역으로 선언해주는게 편함.
//Java의 익명 내부 클래스는 외부 클래스의 멤버 변수를 참조할 수 없다. 사용하려면 변수가 final로 선언되어야 합니다 값 변경 불가.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView= inflater.inflate(R.layout.information_fragment,container,false);

        //플로팅바 정의
ExtendedFloatingActionButton add1=rootView.findViewById(R.id.floatingActionButton1);
        FloatingActionButton add2=rootView.findViewById(R.id.floatingActionButton2);
        FloatingActionButton add3=rootView.findViewById(R.id.floatingActionButton3);

        FloatingActionButton add4=rootView.findViewById(R.id.floatingActionButton4);

        FloatingActionButton add5=rootView.findViewById(R.id.floatingActionButton5);
        FloatingActionButton add6=rootView.findViewById(R.id.floatingActionButton6);

        TextView textView1 = rootView.findViewById(R.id.textView1);
        TextView textView2=rootView.findViewById(R.id.textView2);
        TextView textView3=rootView.findViewById(R.id.textView3);
        TextView textView4=rootView.findViewById(R.id.textView4);
        TextView textView5=rootView.findViewById(R.id.textView5);




       button= rootView.findViewById(R.id.button4);//내가 쓴 글 보기
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                list1 a = new list1();
                FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
                fragmentTransaction1.replace(R.id.container, a).commit(); // commit() 호출을 빠뜨리지 마세요!
            }
        }); //프래그먼트에서 프래그먼트끼리 이동 가능. 결국 매니저, 트랜잭션 이용해서 하면 됨. 꼭 메인 액티비티에서만 해주는게 아님.
        //(근데 난 왜 실패한거지 ㅜㅜ 객체 뽑아서 하면 이론상 쌉가능인)

        button2= rootView.findViewById(R.id.button5);//내가 쓴 댓글 보기
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                comment1 a = new comment1();
                FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
                fragmentTransaction1.replace(R.id.container, a).commit(); // commit() 호출을 빠뜨리지 마세요!
            }
        });




        //플로팅바에 대한 값들 정의 . 불값은 이제 보이게 하려면 해야함
         add2.setVisibility(View.GONE);
        add3.setVisibility(View.GONE);
        add4.setVisibility(View.GONE);
        add5.setVisibility(View.GONE);
        add6.setVisibility(View.GONE);

        textView1.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        textView4.setVisibility(View.GONE);
        textView5.setVisibility(View.GONE);

        add1.shrink();

add1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if(!setting){
            add2.setVisibility(View.VISIBLE);
            add3.setVisibility(View.VISIBLE);
            add4.setVisibility(View.VISIBLE);
            add5.setVisibility(View.VISIBLE);
            add6.setVisibility(View.VISIBLE);

            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView3.setVisibility(View.VISIBLE);
            textView4.setVisibility(View.VISIBLE);
            textView5.setVisibility(View.VISIBLE);

            setting=true;
        add1.extend();
        }else{
            add2.setVisibility(View.INVISIBLE);
            add3.setVisibility(View.INVISIBLE);
            add4.setVisibility(View.INVISIBLE);
            add5.setVisibility(View.INVISIBLE);
            add6.setVisibility(View.INVISIBLE);

            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);
            textView4.setVisibility(View.GONE);
            textView5.setVisibility(View.GONE);


            add1.shrink();

            setting=false;

        }

//shrink extend 는 Android Material Design 라이브러리의 확장된 플로팅 액션 버튼에서만 사용 가능하다.
//extend는 확장시켜 주는 역할임. 기본적으로 작은 크기에서 속에 내용이 들어있는걸 확장시킬때 씀 즉
// , shrink 시스템상에 정해놓은 기본적인 작은 크기로+버튼에 텍스트 적으면 같이 사라짐..


    }
});



        return rootView;



    }
    }

