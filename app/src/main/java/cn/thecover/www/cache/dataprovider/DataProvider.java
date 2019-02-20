package cn.thecover.www.cache.dataprovider;


import java.util.Map;

import cn.thecover.www.cache.dataprovider.cache.DiskCacheManager;
import cn.thecover.www.cache.dataprovider.http.HttpUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

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
            final DataResult<T> result = DiskCacheManager.getInstance().getSerializable(key);

            resultObservable = Observable
                    .concat(Observable.just(result), httpObservable)
                    .flatMap(new Function<DataResult<T>, ObservableSource<DataResult<T>>>() {
                        @Override
                        public ObservableSource<DataResult<T>> apply(DataResult<T> dataResult) throws Exception {
                            DiskCacheManager.getInstance().put(key,dataResult);
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
