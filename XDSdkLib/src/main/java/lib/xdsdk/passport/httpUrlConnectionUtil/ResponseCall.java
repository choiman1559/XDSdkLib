package lib.xdsdk.passport.httpUrlConnectionUtil;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import lib.xdsdk.passport.httpUrlConnectionUtil.callback.HttpCallbackBytesListener;
import lib.xdsdk.passport.httpUrlConnectionUtil.callback.HttpCallbackModelListener;
import lib.xdsdk.passport.httpUrlConnectionUtil.callback.HttpCallbackStringListener;

public class ResponseCall<T> {
    private final int WHAT_FAIL = 1;
    private final int WHAT_SUCCESS = 0;
    Handler mHandler;

    public ResponseCall(Activity activity, final HttpCallbackStringListener httpCallbackStringListener) {
        this.mHandler = new Handler(activity.getMainLooper()) {

            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 0) {
                    httpCallbackStringListener.onFinish(message.obj.toString());
                } else if (message.what == 1) {
                    httpCallbackStringListener.onError((Exception) message.obj);
                }
            }
        };
    }

    public ResponseCall(Activity activity, final HttpCallbackModelListener<Object> httpCallbackModelListener) {
        this.mHandler = new Handler(activity.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 0) {
                    httpCallbackModelListener.onFinish(message.obj);
                } else if (message.what == 1) {
                    httpCallbackModelListener.onError((Exception) message.obj);
                }
            }
        };
    }

    public ResponseCall(Activity activity, final HttpCallbackBytesListener httpCallbackBytesListener) {
        this.mHandler = new Handler(activity.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 0) {
                    httpCallbackBytesListener.onFinish((byte[]) message.obj);
                } else if (message.what == 1) {
                    httpCallbackBytesListener.onError((Exception) message.obj);
                }
            }
        };
    }

    public void doScuccess(T t) {
        Message r0 = Message.obtain();
        r0.obj = t;
        r0.what = 0;
        this.mHandler.sendMessage(r0);
    }

    public void doFail(Exception exc) {
        Message r0 = Message.obtain();
        r0.obj = exc;
        r0.what = 1;
        this.mHandler.sendMessage(r0);
    }
}
