package com.v.neteasepeeling;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void changeSkin(View view) {
//        File file=new File(Environment.getExternalStorageDirectory(),"skin.apk");
//        SkinManager.getInstance().loadSkinApk(file.getAbsolutePath());
    }
}
