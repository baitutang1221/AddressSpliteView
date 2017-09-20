package com.example.xiazhenjie.addressspliteview.splite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.xiazhenjie.addressspliteview.R;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * 功能介绍： 获取剪切板内容，进行分词；
 *          点击分解后的词，填入输入框；
 *          点击叉号将地址拼接起来返回主界面
 *
 *  用途： 增加用户的体验效果，可以直接在微信上复制地址，然后通过此功能确认地址
 *
 */
public class MainActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SpliteActivity.class);
                startActivityForResult(intent,300);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 300:
                if(data != null){
                    String[] address = data.getStringArrayExtra("address");
                    Map<String,String> maps3 = new HashMap<>();
                    maps3.put("consignee",address[0]);
                    maps3.put("mobile",address[1]);
                    maps3.put("address",address[2]);
                    maps3.put("province",address[3]);
                    maps3.put("city",address[4]);

                    //获取到了返回的数据
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
