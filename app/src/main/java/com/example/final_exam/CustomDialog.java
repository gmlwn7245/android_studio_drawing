package com.example.final_exam;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class CustomDialog {
    private Context context;

    public CustomDialog(Context context) {
        this.context = context;
    }
     public void callFunction(String str){
        //커스텀 다이얼로그 정의를 위해 클래스 생성
      Dialog dig = new Dialog(context);

        //타이틀 바 숨김김
         dig.requestWindowFeature(Window.FEATURE_NO_TITLE);

         //레이아웃 설정
         if(str == "color") {
             dig.setContentView(R.layout.color_dialog);
         }else if(str =="width"){
             dig.setContentView(R.layout.width_dialog);
         }
       dig.show();

     }
}
