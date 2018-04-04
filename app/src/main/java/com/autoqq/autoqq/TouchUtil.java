package com.autoqq.autoqq;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.view.InputDeviceCompat;
import android.view.IWindowManager;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zenghui on 2017/7/6.
 */

public class TouchUtil {

    private static InputManager im;
    private static Method injectInputEventMethod;
    private static long downTime;
    

    public static TouchUtil touchUtil;
    public static Context mContext;

    public static TouchUtil getInstance(Context context){
        mContext = context;
        if (touchUtil == null) {
            synchronized (TouchUtil.class) {
                if (touchUtil == null) {
                    touchUtil = new TouchUtil();
                    try {
                        touchUtil.initEvent();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return touchUtil;
    }



    public void touchUp(float clientX, float clientY) throws InvocationTargetException, IllegalAccessException {
        injectMotionEvent(im, injectInputEventMethod, InputDeviceCompat.SOURCE_TOUCHSCREEN, 1, downTime, SystemClock.uptimeMillis() - downTime, clientX, clientY, 1.0f);
    }

    public void touchMove(float clientX, float clientY) throws InvocationTargetException, IllegalAccessException {
        injectMotionEvent(im, injectInputEventMethod, InputDeviceCompat.SOURCE_TOUCHSCREEN, 2, downTime, SystemClock.uptimeMillis() - downTime, clientX, clientY, 1.0f);
    }

    public void touchDown(float clientX, float clientY) throws InvocationTargetException, IllegalAccessException {
        downTime = SystemClock.uptimeMillis();
        injectMotionEvent(im, injectInputEventMethod, InputDeviceCompat.SOURCE_TOUCHSCREEN, 0, downTime, downTime, clientX, clientY, 1.0f);

    }


    public void pressHome() throws InvocationTargetException, IllegalAccessException {
        sendKeyEvent(im, injectInputEventMethod, InputDeviceCompat.SOURCE_KEYBOARD, KeyEvent.KEYCODE_SEARCH, false);

    }

    public void initEvent() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        im = (InputManager) InputManager.class.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        MotionEvent.class.getDeclaredMethod("obtain", new Class[0]).setAccessible(true);
        injectInputEventMethod = InputManager.class.getMethod("injectInputEvent", new Class[]{InputEvent.class, Integer.TYPE});

    }

    public MotionEvent getUp(float x,float y){
        return MotionEvent.obtain(downTime, SystemClock.uptimeMillis() - downTime, 1, x, y, 1.0f, 1.0f, 0, 1.0f, 1.0f, 0, 0);
    }

    public MotionEvent getDown(float x,float y){
        return MotionEvent.obtain(downTime, SystemClock.uptimeMillis() - downTime, 0, x, y, 1.0f, 1.0f, 0, 1.0f, 1.0f, 0, 0);
    }

    public MotionEvent getMove(float x,float y){
        return MotionEvent.obtain(downTime, SystemClock.uptimeMillis() - downTime, 2, x, y, 1.0f, 1.0f, 0, 1.0f, 1.0f, 0, 0);
    }

    private void injectMotionEvent(InputManager im, Method injectInputEventMethod, int inputSource, int action, long downTime, long eventTime, float x, float y, float pressure) throws InvocationTargetException, IllegalAccessException {
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, action, x, y, pressure, 1.0f, 0, 1.0f, 1.0f, 0, 0);
        event.setSource(inputSource);
        injectInputEventMethod.invoke(im, new Object[]{event, Integer.valueOf(0)});
    }

    private void injectKeyEvent(InputManager im, Method injectInputEventMethod, KeyEvent event) throws InvocationTargetException, IllegalAccessException {
        injectInputEventMethod.invoke(im, new Object[]{event, Integer.valueOf(0)});
    }


    private void sendKeyEvent(InputManager im, Method injectInputEventMethod, int inputSource, int keyCode, boolean shift) throws InvocationTargetException, IllegalAccessException {
        long now = SystemClock.uptimeMillis();
        int meta = shift ? 1 : 0;
        injectKeyEvent(im, injectInputEventMethod, new KeyEvent(now, now, 0, keyCode, 0, meta, -1, 0, 0, inputSource));
        injectKeyEvent(im, injectInputEventMethod, new KeyEvent(now, now, 1, keyCode, 0, meta, -1, 0, 0, inputSource));
    }

    public void sendEvent(int keyCode){
        try{
    Object object = new Object();
    Method getService = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
    Object obj = getService.invoke(object, new Object[]{new String("window")});
    //System.out.println(obj.toString());
            long now = SystemClock.uptimeMillis();
            IWindowManager windowMger = IWindowManager.Stub.asInterface((IBinder)obj);
            windowMger.injectKeyEvent(new KeyEvent(now, now, 1, keyCode, 0, 0, -1, 0, 0, InputDeviceCompat.SOURCE_KEYBOARD),false);
    }catch(Exception ex){
            ex.printStackTrace();
   }
    }

}