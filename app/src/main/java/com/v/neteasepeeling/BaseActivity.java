package com.v.neteasepeeling;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;

public class BaseActivity extends Activity {
    private SkinFactory skinFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SkinManager.getInstance().setContext(this);

        skinFactory=new SkinFactory();

        //监听到xml的生成过程
        LayoutInflaterCompat.setFactory2(getLayoutInflater(),skinFactory);
    }

    @Override
    protected void onResume() {
        super.onResume();

        skinFactory.apply();
    }
}
