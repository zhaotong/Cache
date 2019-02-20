package cn.thecover.www.cache.dataprovider.http;


import cn.thecover.www.cache.dataprovider.DataResult;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class CallBackObserver<T> implements Observer<DataResult<T>> {

    @Override
    public void onNext(DataResult<T> result) {
        try {
            if (result != null) {
                onSuccess(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Throwable e) {
        try {
            onFailure(e.getMessage() + "");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        CompositeDisposableHelper.add(d);
    }

    protected abstract void onSuccess(DataResult<T> t) throws Exception;

    protected abstract void onFailure(String error) throws Exception;


}
