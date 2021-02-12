package lib.xdsdk.passport.httpUrlConnectionUtil.callback;

public interface HttpCallbackBytesListener {
    void onError(Exception exc);

    void onFinish(byte[] bArr);
}
