package lib.xdsdk.passport.httpUrlConnectionUtil.callback;

public interface HttpCallbackStringListener {
    void onError(Exception exc);

    void onFinish(String str);
}
