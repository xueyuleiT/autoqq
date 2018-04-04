package android.view;


import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by zenghui on 2018/3/22.
 */

public interface IWindowManager {
    public static class Stub {
        public static IWindowManager asInterface(IBinder binder) {
            return null;
        }
    }

    // 注入自己的窗口事件
    public boolean injectKeyEvent(KeyEvent ev, boolean sync);

    public boolean injectPointerEvent(MotionEvent ev, boolean sync);

    public boolean injectTrackballEvent(MotionEvent ev, boolean sync);

    // public boolean injectInputEventNoWait(InputEvent ev);
}
