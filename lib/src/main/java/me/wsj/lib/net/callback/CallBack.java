package me.wsj.lib.net.callback;


import java.io.IOException;

public interface CallBack<T> {

    void onNext(String responseBody) throws IOException;
    
    void onSuccess(T result, String code, String msg);
}
