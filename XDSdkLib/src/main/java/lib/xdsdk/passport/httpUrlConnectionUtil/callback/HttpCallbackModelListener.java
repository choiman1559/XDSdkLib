package lib.xdsdk.passport.httpUrlConnectionUtil.callback;

public interface HttpCallbackModelListener<T> {
    void onError(Exception exc);

    void onFinish(T t);
}
