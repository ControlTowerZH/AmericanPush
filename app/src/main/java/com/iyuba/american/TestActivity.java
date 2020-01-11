package com.iyuba.american;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iyuba.american.push.LogUtil;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent =getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("push_init","1");
        String uri=intent.toUri(Intent.URI_INTENT_SCHEME);
        LogUtil.e("uri:"+uri);
    }
}
