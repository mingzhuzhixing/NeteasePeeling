package com.v.neteasepeeling;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends BaseActivity {

    private String[] PERMISSION_STORAGE={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    private final int REQUET_PERMISSION_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,PERMISSION_STORAGE,REQUET_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUET_PERMISSION_CODE){
            for (int i=0;i<permissions.length;i++){
                Toast.makeText(MainActivity.this,"申请权限为："+permissions[i]+",申请结果为："+grantResults[i],Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void jumpActivity(View view) {
        File file=new File(Environment.getExternalStorageDirectory(),"skin.apk");
        SkinManager.getInstance().loadSkinApk(file.getAbsolutePath());
        Intent intent=new Intent(this,SecondActivity.class);
        startActivity(intent);
    }
}
