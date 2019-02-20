package cn.thecover.www.cache.dataprovider.http;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CompositeDisposableHelper {
    private static CompositeDisposable compositeDisposable =new CompositeDisposable();


    public static void add(Disposable disposable){
        compositeDisposable.add(disposable);
    }

    public static void dispose(){
        compositeDisposable.dispose();
    }
}
