package cn.thecover.www.cache;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

import cn.thecover.www.cache.dataprovider.DataProvider;
import cn.thecover.www.cache.dataprovider.DataResult;
import cn.thecover.www.cache.dataprovider.http.CallBackObserver;
import cn.thecover.www.cache.dataprovider.http.CompositeDisposableHelper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                1111);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap map = new HashMap<String, String>();
                map.put("type", 2);
                DataProvider
                        .getInstance()
                        .getData(true, "http://121.43.179.175:8080/canteen/app/get", map, Entity.class)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CallBackObserver<Entity>() {
                            @Override
                            protected void onSuccess(DataResult<Entity> t) throws Exception {
                                Log.d("MainActivity", "onSuccess: " + t.toString());
//                                CompositeDisposableHelper.dispose();
                            }

                            @Override
                            protected void onFailure(String error) throws Exception {
                                Log.d("MainActivity", "onFailure: " + error);

                            }
                        });
            }
        });

    }
}
