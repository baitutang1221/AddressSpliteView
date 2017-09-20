package com.example.xiazhenjie.addressspliteview.splite;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiazhenjie.addressspliteview.R;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jeasy.analysis.MMAnalyzer;

/**
 * Created by xiazhenjie on 2017/8/29.
 */

public class SpliteActivity extends Activity {

    private FlowlayoutTags flt_elective = null;
    private Button dismissButton;
    private EditText nameET;
    private EditText phoneET;
    private EditText provET;
    private EditText cityET;
    private EditText streetET;
    private EditText addressET;

    private List<String> datas = new ArrayList<String>();
    public String str = "郭建斌 18678066322 北京市西城区德胜国际中心，B座11层2楼201，龙源数字传媒集团";
    private ClipboardManager clipboard = null;

    //词库分词
    Analyzer analyzerMM = new MMAnalyzer();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splite_layout);

        clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyThread thread = new MyThread();
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initViews(){

        flt_elective = (FlowlayoutTags) findViewById(R.id.flt_elective);
        dismissButton = (Button) findViewById(R.id.dismissButton);
        nameET = (EditText) findViewById(R.id.nameET);
        phoneET = (EditText) findViewById(R.id.phoneET);
        provET = (EditText) findViewById(R.id.provET);
        cityET = (EditText) findViewById(R.id.cityET);
        streetET = (EditText) findViewById(R.id.streetET);
        addressET = (EditText) findViewById(R.id.addressET);

        dismissButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String[] address = new String[5];
                address[0] = nameET.getText().toString();
                address[1] = phoneET.getText().toString();
                address[2] = streetET.getText().toString();
                address[3] = provET.getText().toString();
                address[4] = cityET.getText().toString();

                intent.putExtra("address",address);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_splite_backg);
//        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Drawable drawable = new BitmapDrawable(FastBlurUtil.doBlur(bitmap, 40, true));
        Drawable drawable = new BitmapDrawable(bitmap);
        getWindow().getDecorView().setBackground(drawable);

        flt_elective.setOnTagClickListener(new FlowlayoutTags.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {

                flt_elective.setFocusable(false);
                View rootview = getWindow().getDecorView();
                View focusView = rootview.findFocus();

                if(focusView != null){
                    int focusId = focusView.getId();
                    switch (focusId){
                        case R.id.nameET:
                            if(nameET.getText().toString().contains(tag)){
                                String text = nameET.getText().toString();
                                nameET.setText(text.replace(tag,""));
                            }else{
                                nameET.setText(nameET.getText().toString()+tag);
                            }
                            break;
                        case R.id.phoneET:
                            if(phoneET.getText().toString().contains(tag)){
                                String text = phoneET.getText().toString();
                                phoneET.setText(text.replace(tag,""));
                            }else{
                                phoneET.setText(phoneET.getText().toString()+tag);
                            }
                            break;
                        case R.id.provET:
                            if(provET.getText().toString().contains(tag)){
                                String text = provET.getText().toString();
                                provET.setText(text.replace(tag,""));
                                addressET.setText(text.replace(tag,""));
                            }else{
                                provET.setText(provET.getText().toString()+tag);
                                addressET.setText(addressET.getText().toString()+tag);
                            }
                            break;
                        case R.id.cityET:
                            if(cityET.getText().toString().contains(tag)){
                                String text = cityET.getText().toString();
                                cityET.setText(text.replace(tag,""));
                                addressET.setText(text.replace(tag,""));
                            }else{
                                cityET.setText(cityET.getText().toString()+tag);
                                addressET.setText(addressET.getText().toString()+tag);
                            }
                            break;
                        case R.id.streetET:
                            if(streetET.getText().toString().contains(tag)){
                                String text = streetET.getText().toString();
                                streetET.setText(text.replace(tag,""));
                                addressET.setText(text.replace(tag,""));
                            }else{
                                streetET.setText(streetET.getText().toString()+tag);
                                addressET.setText(addressET.getText().toString()+tag);
                            }
                            break;
                        default:
                            break;
                    }

                }
            }
        });
    }

    /**
     * 分词
     *
     */
    public void analyze(Analyzer analyzer, String text) throws Exception {
        TokenStream tokensStream = analyzer.tokenStream("content",new StringReader(text));
        datas.clear();
        for(Token token = new Token(); (token=tokensStream.next(token)) !=null;) {
            datas.add(token.termText());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flt_elective = (FlowlayoutTags) findViewById(R.id.flt_elective);
                refreshCategorys(flt_elective,datas);
            }
        });
    }

    public void refreshCategorys(FlowlayoutTags flowlayoutTags,List<String> list) {
        flowlayoutTags.removeAllViews();
        flowlayoutTags.setTags(list);
        flowlayoutTags.setTagsUncheckedColorAnimal(false);
    }

    public class MyThread extends Thread {
        public String _String;

        public void run(){
            Looper.prepare();
            SharedPreferences pref = SpliteActivity.this.getSharedPreferences("addressShared",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            String lastStr = pref.getString("address","");

            ClipData.Item item = null;
            if(!clipboard.hasPrimaryClip()){
                Toast.makeText(SpliteActivity.this, "剪贴板中无数据", Toast.LENGTH_SHORT).show();
                return ;
            }

            //如果是文本信息
            String type = clipboard.getPrimaryClipDescription().getMimeType(0);
            if (type.equals(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    || type.equals(ClipDescription.MIMETYPE_TEXT_HTML)) {

                ClipData cdText = clipboard.getPrimaryClip();
                item = cdText.getItemAt(0);
                //此处是TEXT文本信息
                if(item.getText() == null){
                    Toast.makeText(SpliteActivity.this, "剪贴板中无内容", Toast.LENGTH_SHORT).show();
                    _String = "剪贴板中无内容";
                    return ;
                }else{
                    _String = item.getText().toString();
                    if(!lastStr.contains(_String)){
                        _String = lastStr+_String;
                    }else{
                        _String=lastStr;
                    }

                    editor.putString("address",_String);
                    editor.commit();
                }
            }

            try {
                if(_String != null){
                    analyze(analyzerMM,_String);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Looper.loop();
        }
    }
}
