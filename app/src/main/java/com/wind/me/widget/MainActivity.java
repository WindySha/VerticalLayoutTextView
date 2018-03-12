package com.wind.me.widget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    VerticalLayoutTextView verticalLayoutTextView1;
    VerticalLayoutTextView verticalLayoutTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verticalLayoutTextView1 = findViewById(R.id.vertical_text1);
        verticalLayoutTextView1.setText("你还是要了解HTML的标识的含义。特别在Unix下的软件编译，你就不能不自己写makefile了，" +
                "会不会写makefile，从一个侧面说明了一个人是否具备完成大型工程的能力。" +
                "因为，makefile关系到了整个工程的编译规则。" );

        verticalLayoutTextView2 = findViewById(R.id.vertical_text2);
        verticalLayoutTextView2.setText("要坚持法治、反对人治，对宪法法律始终保持敬畏之心，" +
                "带头在宪法法律范围内活动，严格依照法定权限、规则、程序行使权力、履行职责，" +
                "做到心中高悬法纪明镜、手中紧握法纪戒尺，知晓为官做事尺度。");
    }
}
