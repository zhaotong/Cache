package com.tone.cache.dataprovider;


import java.util.Map;

import com.tone.cache.dataprovider.cache.DiskCacheManager;
import com.tone.cache.dataprovider.http.HttpUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class DataProvider {
    private static DataProvider instance;

    public static DataProvider getInstance() {
        if (instance == null) {
            synchronized (DataProvider.class) {
                if (instance == null) {
                    instance = new DataProvider();
                }
            }
        }
        return instance;
    }

    private DataProvider() {
    }


    /**
     * @param useCache 是否使用缓存
     * @param url      请求地址
     * @param map      请求参数
     * @param <T>
     * @return
     */
    public <T> Observable<DataResult<T>> getData(boolean useCache, String url, Map<String, String> map, Class<T> clazz) {
        Observable<DataResult<T>> resultObservable;
        final Observable<DataResult<T>> httpObservable = HttpUtil.getInstance().postData(url, map, clazz);
        if (useCache) {

            final String key = url + map.toString();
            DataResult<T> result = DiskCacheManager.getInstance().getSerializable(key);
            if (result == null) {
                result = new DataResult<>();
                result.setDataType(DataResult.CODE_CACHE_NULL);
            }
            resultObservable = Observable
                    .concat(Observable.just(result), httpObservable)
                    .filter(new Predicate<DataResult<T>>() {
                        @Override
                        public boolean test(DataResult<T> dataResult) throws Exception {
                            return dataResult.getDataType() != DataResult.CODE_CACHE_NULL;
                        }
                    })
                    .flatMap(new Function<DataResult<T>, ObservableSource<DataResult<T>>>() {
                        @Override
                        public ObservableSource<DataResult<T>> apply(DataResult<T> dataResult) throws Exception {
                            if (dataResult.getDataType() == DataResult.CODE_NETWORK_SUCCESS)
                                DiskCacheManager.getInstance().put(key, dataResult);
                            return Observable.just(dataResult);
                        }
                    });
        } else {
            resultObservable = httpObservable;

        }

        return resultObservable;
    }

    /**
     * @param url 请求地址
     * @param map 请求参数
     * @param <T>
     * @return
     */
    public <T> Observable<DataResult<T>> getData(String url, Map<String, String> map, Class<T> clazz) {
        return getData(false, url, map, clazz);
    }


}
