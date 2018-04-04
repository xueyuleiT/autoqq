package com.autoqq.autoqq;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.KeyEventCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zenghui on 2018/3/19.
 */

public class AutoService extends AccessibilityService {
    //没有找到相关结果
    PowerManager.WakeLock mWakeLock;
    private boolean isRun = false;
    private boolean isBreak = false;

    String[] params = new String[]{"技术","交流","上海","成都"};
    String keyWord;
    int index = 0;
    int findListIndex = 0;

    String[] steps = { "com.tencent.mobileqq:id/et_search_keyword",
            "com.tencent.mobileqq:id/ib_clear_text",
            "com.tencent.mobileqq:id/title",
            "com.tencent.mobileqq:id/info",
            "com.tencent.mobileqq:id/name",
            "com.tencent.mobileqq:id/ivTitleBtnRightText" ,
            "com.tencent.mobileqq:id/common_xlistview",
            "com.alipay.mobile.ui:id/ensure"};


    private String savePath;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (this.isRun)
            return;
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date localDate = new Date(System.currentTimeMillis());
        savePath = "/sdcard/autoqq_" + localSimpleDateFormat.format(localDate) + ".txt";
        findSearch(steps[0]);
//        findGroupDetail("本群创建");
//        findDetail(new MemberInfo());
//        findGroupEntry();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    void findSearch(String paramString){
        Log.d("index ====>",""+index);
        if (index == 4){
            stopSelf();
            return;
        }
        isBreak = false;
        isRun = true;
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> localList = nodeInfo.findAccessibilityNodeInfosByViewId(paramString);
        if (localList != null && localList.size() != 0) {
               AccessibilityNodeInfo accessibilityNodeInfo = localList.get(0);
            Bundle argumentsTest = new Bundle();
            argumentsTest.putCharSequence("ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE", params[index]);
            keyWord = params[index];
            index ++;

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    RootShellCmd rootShellCmd = new RootShellCmd();
//                    rootShellCmd.simulateKey(KeyEvent.KEYCODE_ENTER);
////                        TouchUtil.getInstance(getBaseContext()).pressHome();
//                }
//            }).start();
//            EditorInfo.IME_ACTION_SEARCH
            accessibilityNodeInfo.performAction(2097152, argumentsTest);
            try {
                Thread.sleep(1000);
                findGroup("找群");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    Thread t;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void findGroup(String paramString){
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> localList = nodeInfo
                .findAccessibilityNodeInfosByText(paramString);
        if (localList != null && localList.size() != 0) {
            AccessibilityNodeInfo accessibilityNodeInfo = localList.get(0);
            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);

            try {
                Thread.sleep(10000);
                nodeInfo = getRootInActiveWindow();
                localList = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/webview");
                if (localList != null && localList.size() != 0) {
                    AccessibilityNodeInfo result = localList.get(0);
                    Rect rect = new Rect();
                    result.getBoundsInParent(rect);
                    int startHeight =  MainActivity.SCREEN_HEIGHT - rect.height();
                    for (int i = findListIndex ; i < 5 && !isBreak; i++){
                        findListIndex ++;
                        final int y = i*rect.height() / 5+ rect.height() / 10 + startHeight;
                        t =  new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RootShellCmd rootShellCmd = new RootShellCmd();
                                try {
                                    Thread.sleep(1000);
                                    rootShellCmd.click(100,y);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        t.start();
                        Log.d("start ===>","start");

                        t.join();
                        Thread.sleep(5000);
                        if (!isBreak) {
                            findGroupDetail("本群创建");
                        }
                        Log.d("join ===>","join");

                    }

                    findListIndex = 0;
                    Thread.sleep(2000);
                    Log.d("isBreak ===>",""+isBreak+"  findListIndex = "+findListIndex);
                    if (!isBreak){
                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        Thread.sleep(1000);
                        findSearch(steps[0]);
                    }

                }else {//没有找到相关结果 下一次查找
                    findSearch(steps[0]);
                }
//                findGroupDetail("本群创建");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    GroupInfo groupInfo;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void findGroupDetail(String params){
        groupInfo = new GroupInfo();
        groupInfo.setKeyWord(keyWord);
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null){
            return;
        }
        List<AccessibilityNodeInfo> localList = nodeInfo
                .findAccessibilityNodeInfosByText(params);
        if (localList != null && localList.size() != 0) {
            AccessibilityNodeInfo accessibilityNodeInfo = localList.get(0);
            //获取创建时间
            groupInfo.setCreateTime(accessibilityNodeInfo.getText().toString());


            //群名
            AccessibilityNodeInfo rootView = accessibilityNodeInfo.getParent();
            groupInfo.setGroupName(rootView.getChild(1).getText().toString());//群昵称
            groupInfo.setGroupQQ(rootView.getChild(2).getText().toString());//群号码



            //获取群标签
            StringBuilder sb = new StringBuilder();
            localList = nodeInfo.findAccessibilityNodeInfosByText("群标签");
            if (localList != null && localList.size() != 0) {
                accessibilityNodeInfo = localList.get(0).getParent();
                for (int i = 1; i < accessibilityNodeInfo.getChildCount(); i ++){
                    sb.append(accessibilityNodeInfo.getChild(i).getText());
                    sb.append(";");
                }
            }
            groupInfo.setGroupTags(sb.toString());
            groupInfo.setMemberNum(Integer.parseInt(nodeInfo.findAccessibilityNodeInfosByText("成员概况")
                    .get(0).getParent().getChild(1).getText().toString().replace("人","")));

            //获取群员概况
            List<AccessibilityNodeInfo> manInfo = nodeInfo.findAccessibilityNodeInfosByText("男-");
            if (manInfo != null && manInfo.size() > 0) {
                groupInfo.setGroupMemberSurvey(manInfo.get(0).getParent().getContentDescription().toString());
            }else {
                manInfo = nodeInfo.findAccessibilityNodeInfosByText("女-");
                if (manInfo != null && manInfo.size() > 0) {
                    groupInfo.setGroupMemberSurvey(manInfo.get(0).getParent().getContentDescription().toString());
                }
            }
            //获取管理员信息
            manInfo = nodeInfo.findAccessibilityNodeInfosByText("管理员");
            if (manInfo != null && manInfo.size() > 0) {
                for (int i = 0 ; i < manInfo.size(); i ++) {
                    if (manInfo.get(i).getClassName().equals("android.widget.TextView")
                            && manInfo.get(i).getText().toString().equals("管理员") ) {
                        manInfo.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
                try {
                    Thread.sleep(2000);
                    findManager();
                    Thread.sleep(2000);
                    requestJoinGroup();
//                    findQuestion();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

            }


        }else {

            try {
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                Thread.sleep(1000);

                //开始下一次的寻找
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void findManager(){
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> localList = nodeInfo
                .findAccessibilityNodeInfosByViewId(steps[6]);
        if (localList != null && localList.size() != 0) {
            nodeInfo = localList.get(0);
            nodeInfo.getChildCount();
            List<MemberInfo> memberInfos = new ArrayList<>(nodeInfo.getChildCount());

            groupInfo.setGroupManagerNum(""+nodeInfo.getChildCount());
            for (int i = 0 ; i < nodeInfo.getChildCount(); i ++){
                MemberInfo memberInfo = new MemberInfo();
                AccessibilityNodeInfo temp = nodeInfo.getChild(i);
//                if (temp.isVisibleToUser()){
                    memberInfo.setNickName(temp.getChild(0).getText().toString());//管理员昵称
                    memberInfo.setLevel(1);//群管理
                    if (temp.getChildCount() > 1){//群主
                        memberInfo.setLevel(0);//群主
                    }

                    temp.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    try {
                        Thread.sleep(5000);
                        findDetail(memberInfo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    memberInfos.add(memberInfo);
//                }
            }
            groupInfo.setMemberInfos(memberInfos);
        }

        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void findDetail(MemberInfo memberInfo) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

        List<AccessibilityNodeInfo> localList = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/common_xlistview");
        if (localList != null && localList.size() > 0) {
            AccessibilityNodeInfo result = localList.get(0);
            if (result != null) {
                if (result.getClassName().equals("android.widget.FrameLayout")) {
                    int length = result.getChildCount();
                    for (int i = 0; i < length; i++){
                        if (result.getChild(i) != null
                                && result.getChild(i).getClassName().equals("android.widget.TextView")
                                && result.getChild(i).getText() != null){
                            if (result.getChild(i).getContentDescription() != null) {
                                if (result.getChild(i).getContentDescription().toString().startsWith("昵称:")) {
                                    memberInfo.setNickName(result.getChild(i).getText().toString());
                                } else if (result.getChild(i).getContentDescription().toString().startsWith("签名:")) {
                                    memberInfo.setSign(result.getChild(i).getText().toString());
                                } else if (result.getChild(i).getContentDescription().toString().startsWith("基本信息:")) {
                                    memberInfo.setPersonInfo(result.getChild(i).getText().toString());
                                }
                            }
                        }
                    }
//                    if (result.getChildCount() > 1
//                            && result.getChild(1).getClassName().equals("android.widget.TextView")
//                            && result.getChild(1).getText() != null) {
//                        memberInfo.setNickName(result.getChild(1).getText().toString());
//                    }
//                    if (result.getChildCount() > 3
//                            && result.getChild(3).getClassName().equals("android.widget.TextView")
//                            && result.getChild(3).getText() != null) {
//                        memberInfo.setSign(result.getChild(3).getText().toString());
//
//                    }
                } else {
                    if (result.getChildCount() > 0) {
                        int length = result.getChild(0).getChildCount();
                        result = result.getChild(0);
                        for (int i = 0; i < length; i++) {
                            if (result.getChild(i) != null
                                    && result.getChild(i).getClassName().equals("android.widget.TextView")
                                    && result.getChild(i).getText() != null) {
                                if (result.getChild(i).getContentDescription() != null) {
                                    if (result.getChild(i).getContentDescription().toString().startsWith("昵称:")) {
                                        memberInfo.setNickName(result.getChild(i).getText().toString());
                                    } else if (result.getChild(i).getContentDescription().toString().startsWith("签名:")) {
                                        memberInfo.setSign(result.getChild(i).getText().toString());
                                    } else if (result.getChild(i).getContentDescription().toString().startsWith("基本信息:")) {
                                        memberInfo.setPersonInfo(result.getChild(i).getText().toString());
                                    }
                                }
                            }
                        }

                    }


//                    if (result.getChildCount() > 0 && result.getChild(0).getChildCount() > 2
//                            && result.getChild(0).getChild(2).getClassName().equals("android.widget.TextView")
//                            &&result.getChild(0).getChild(2).getText() != null) {
//                        memberInfo.setNickName(result.getChild(0).getChild(2).getText().toString());
//                    } else if (result.getChildCount() > 0 && result.getChild(0).getChildCount() > 3
//                            && result.getChild(0).getChild(3).getClassName().equals("android.widget.TextView")
//                            && result.getChild(0).getChild(3).getText() != null) {
//                        memberInfo.setNickName(result.getChild(0).getChild(3).getText().toString());
//
//                    }
//                    if (result.getChild(0).getChildCount() > 3
//                            && result.getChild(0).getChild(3).getClassName().equals("android.widget.TextView")
//                            && result.getChild(0).getChild(3).getText() != null)
//                        memberInfo.setSign(result.getChild(0).getChild(3).getText().toString());
                }
            }
        }


        List<AccessibilityNodeInfo> tempInfo = nodeInfo.findAccessibilityNodeInfosByText("个性标签");
        if (tempInfo != null && tempInfo.size() > 0) {
            localList = tempInfo;
            AccessibilityNodeInfo result = localList.get(0);
            result = result.getParent();
            int index = 1;
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < result.getChildCount(); i++) {
                AccessibilityNodeInfo temp = result.getChild(i);
                if (temp.getClassName().equals("android.widget.TextView")) {
                    //个性标签
                    sb.append(temp.getText());
                    sb.append(";");
                } else {
                    index = i;
                    break;
                }
            }

            memberInfo.setTags(sb.toString());

            //
            if (result.getChildCount() > index + 1) {
                if (result.getChild(index + 1) != null && result.getChild(index + 1).getContentDescription() != null) {
                    memberInfo.setQqLevel(result.getChild(index + 1).getContentDescription().toString());//qq等级
                    result = result.getChild(index);
                    memberInfo.setQq(result.getChild(0).getText().toString());//qq号

                    if (result.getChildCount() > index + 2 && result.getChild(index + 2).getContentDescription() != null) {
                        if (result.getChild(index + 2) != null) {
                            memberInfo.setSign(result.getChild(index + 2).getContentDescription().toString());//签名
                        }
                    }

                    if (result.getChildCount() > index + 3) {
                        if (result.getChild(index + 3) != null && result.getChild(index + 3).getContentDescription() != null) {
                            memberInfo.setSpecialPower(result.getChild(index + 3).getContentDescription().toString());//特权
                        }
                    }


                    if (result.getChildCount() == 1 || TextUtils.isEmpty(result.getChild(1).getText())) {//企业号
                        memberInfo.setQqType("企业");
                    } else {
                        if (result.getChild(1).getText() != null && !result.getChild(1).getText().toString().endsWith("的空间")) {
                            memberInfo.setPersonInfo(result.getChild(1).getText().toString());//姓名 年龄 星座 地址
                        }
                    }
                    if (result.getChildCount() > 2) {
                        if (result.getChild(2) != null)
                            memberInfo.setCompanyOrOccupation(result.getChild(2).getText().toString());//公司

                    }


                } else {
                    List<AccessibilityNodeInfo> levelList = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/info");
                    if (levelList != null && levelList.size() > 0 ) {
                        if ( levelList.get(0).getClassName().equals("android.widget.TextView")  && levelList.get(0).getText() != null) {
                            memberInfo.setQq(levelList.get(0).getText().toString());
                        }
                        if (levelList.size() > 1 && levelList.get(1).getClassName().equals("android.widget.TextView")  && levelList.get(1).getText() != null) {
                          if (!levelList.get(1).getText().toString().endsWith("的空间")) {
                              memberInfo.setPersonInfo(levelList.get(1).getText().toString());
                          }
                        }

                        if (levelList.size() > 2 && levelList.get(2).getClassName().equals("android.widget.TextView")  && levelList.get(2).getText() != null) {
                            memberInfo.setCompanyOrOccupation(levelList.get(2).getText().toString());
                        }


                        if (levelList.size() > 3) {

                            List<AccessibilityNodeInfo> temp = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/common_xlistview");
                            if (temp != null && temp.get(0).getChildCount() > 1 && temp.get(0).getChild(1).getChildCount() > 1) {
                                memberInfo.setQqLevel(temp.get(0).getChild(1).getChild(1).getContentDescription().toString());
                            }
                        }

                    }
                }
            }//com.tencent.mobileqq:id/info


//            加入的群
//            localList = nodeInfo.findAccessibilityNodeInfosByText("加入的群");

            try {
                Thread.sleep(500);
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {

            if (localList != null && localList.size() > 0 && localList.get(0).getClassName().equals("android.widget.FrameLayout")) {
                if (localList.get(0).getChildCount() > 4 && localList.get(0).getChild(4).getChildCount() > 0
                        && localList.get(0).getChild(4).getChild(0).getText() != null)
                    memberInfo.setQq(localList.get(0).getChild(4).getChild(0).getText().toString());
                if (localList.get(0).getChildCount() > 5 && localList.get(0).getChild(5).getChildCount() > 0
                        && localList.get(0).getChild(5).getChild(0).getText() != null) {
                    memberInfo.setCompanyOrOccupation(localList.get(0).getChild(5).getChild(0).getText().toString());
                }

                memberInfo.setQqType("企业");

            } else {

                List<AccessibilityNodeInfo> levelList = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/info");
                if (levelList != null && levelList.size() > 0) {
                    if (levelList.get(0).getClassName().equals("android.widget.TextView") && levelList.get(0).getText() != null) {
                        memberInfo.setQq(levelList.get(0).getText().toString());
                    }

                    if (levelList.size() > 1) {
                        if (levelList.get(1).getClassName().equals("android.widget.TextView") && levelList.get(1).getText() != null) {
                            if (!levelList.get(1).getText().toString().endsWith("的空间")) {
                                memberInfo.setPersonInfo(levelList.get(1).getText().toString());
                            }
                        }
                    }

                    if (levelList.size() > 2) {
                        if (levelList.get(2).getClassName().equals("android.widget.TextView") && levelList.get(2).getText() != null) {
                            memberInfo.setCompanyOrOccupation(levelList.get(2).getText().toString());
                        }
                    }


                    if (levelList.size() > 3) {

                        List<AccessibilityNodeInfo> temp = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/common_xlistview");
                        if (temp != null && temp.get(0).getChildCount() > 1 && temp.get(0).getChild(1).getChildCount() > 1) {
                            memberInfo.setQqLevel(temp.get(0).getChild(1).getChild(1).getContentDescription().toString());
                        }
                    }

//                if (localList.get(0).getChild(1).getChild(0).getChildCount() > 0 && localList.get(0).getChild(1).getChild(0).getChild(0).getText() != null)
//                    memberInfo.setQq(localList.get(0).getChild(1).getChild(0).getChild(0).getText().toString());
//
//                if (localList.get(0).getChild(1).getChild(0).getChildCount() > 1 && localList.get(0).getChild(1).getChild(0).getChild(1).getText()!= null)
//                    memberInfo.setPersonInfo(localList.get(0).getChild(1).getChild(0).getChild(1).getText().toString());
//
//                if (localList.get(0).getChild(1).getChild(0).getChildCount() > 2 && localList.get(0).getChild(1).getChild(0).getChild(2).getText()!= null)
//                    memberInfo.setCompanyOrOccupation(localList.get(0).getChild(1).getChild(0).getChild(2).getText().toString());
//
//                if (localList.get(0).getChild(1).getChildCount() > 1) {
//                    memberInfo.setQqLevel(localList.get(0).getChild(1).getChild(1).getContentDescription().toString());//qq等级
//                    if (localList.get(0).getChild(1).getChildCount() > 2) {
//                        memberInfo.setSpecialPower(localList.get(0).getChild(1).getChild(2).getContentDescription().toString());//qq特权
//                    }
//                } else {
//                    memberInfo.setQqType("企业");
//                }
                }
            }
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void requestJoinGroup(){
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> localList = nodeInfo
                .findAccessibilityNodeInfosByText("申请加群");
        if (localList != null && localList.size() != 0) {
            localList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            try {
                Thread.sleep(5000);
                findQuestion();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {//可能已经入群了
            writeFileSdcard(savePath);
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void findQuestion(){
        int tempIndex = findListIndex;
        writeFileSdcard(savePath);

        try {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> localList = nodeInfo.findAccessibilityNodeInfosByText("问题:");
        if (localList != null && localList.size() != 0) {
            Bundle argumentsTest = new Bundle();
            argumentsTest.putCharSequence("ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE", "麻烦通过下 一起交流 谢谢");
            localList.get(0).getParent().getChild(1).performAction(2097152,argumentsTest);
        }else {
            localList = nodeInfo.findAccessibilityNodeInfosByText("个人介绍");
            if (localList != null && localList.size() != 0) {
                Bundle argumentsTest = new Bundle();
                argumentsTest.putCharSequence("ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE", "麻烦通过下 一起交流 谢谢");
                localList.get(0).getParent().getChild(1).performAction(2097152,argumentsTest);
            }else {
                Thread.sleep(3000);
                List<AccessibilityNodeInfo> sendList = nodeInfo.findAccessibilityNodeInfosByText("本群创建于");
                if (sendList != null && sendList.size() != 0) {//加群失败
                    Thread.sleep(1000);
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    return;
                }else {
                    Thread.sleep(1000);


                    nodeInfo = getRootInActiveWindow();
                    List<AccessibilityNodeInfo> studengts = nodeInfo.findAccessibilityNodeInfosByText("填写身份信息");//家长群
                    if (studengts != null && studengts.size()>0){
                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        Thread.sleep(1000);
                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        Thread.sleep(1000);

                        return;
                    }

                    //直接入群了
                    findListIndex = tempIndex;
                    index --;
                    isBreak = true;
                   List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByText("群须知");//进群须知弹框
                    if (infos != null && infos.size() > 0){
                        infos = nodeInfo.findAccessibilityNodeInfosByText("我知道了");//进群须知弹框
                        if(infos != null && infos.size() > 0){
                            infos.get(infos.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            Thread.sleep(1000);
                        }
                    }

                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

                        Thread.sleep(1000);
                        findGroupEntry();
                        return;
                }
            }
        }
        List<AccessibilityNodeInfo> sendList = nodeInfo.findAccessibilityNodeInfosByViewId(steps[5]);
        if (sendList != null && sendList.size() > 0) {
            sendList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Thread.sleep(4000);
                nodeInfo = getRootInActiveWindow();
                localList = nodeInfo.findAccessibilityNodeInfosByText("问题:");
                if (localList != null && localList.size() != 0) {//需要回答正确的问题
//                    groupInfo.setQuestion(localList.get(0).getText().toString());
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    Thread.sleep(1000);
                }else{
                    localList = nodeInfo.findAccessibilityNodeInfosByViewId(steps[5]);
                    Log.d("localList",""+localList.size());

                    if (localList != null && localList.size() > 0) {//加群失败
                        if (localList.get(0).getClassName().equals("android.widget.TextView")
                                && localList.get(0).getText().equals("发送")){
                        Log.d("加群失败", "加群失败");

                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        Thread.sleep(1000);
                    }
                    }

                }

            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

        }else {//加群失败

                Thread.sleep(1000);
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void findGroupEntry(){

        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> localList = nodeInfo
                .findAccessibilityNodeInfosByText("消息");
        if (localList != null && localList.size() > 0){
            localList.get(0).getParent().getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nodeInfo = getRootInActiveWindow();
            localList = nodeInfo.findAccessibilityNodeInfosByText("加好友/群");
            if (localList != null && localList.size() > 0){
                localList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);

                try {
                    Thread.sleep(2000);
                    nodeInfo = getRootInActiveWindow();
                    localList = nodeInfo.findAccessibilityNodeInfosByText("找群");
                    if (localList != null && localList.size() > 0){
                        localList.get(0).getParent().getParent().getChild(5).getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        Thread.sleep(1000);
                        findSearch(steps[0]);

//                        nodeInfo = getRootInActiveWindow();
//                        localList = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/content");
//                        if (localList != null && localList.size() > 0){
//                            localList.get(0).getChild(1).getChild(0).getChild(0)
//                                    .getChild(0).getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            Thread.sleep(1000);
//                            findSearch(steps[0]);
//                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void findTag(String params){
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> localList = nodeInfo
                .findAccessibilityNodeInfosByText(params);
        if (localList != null && localList.size() != 0) {

        }
    }

    @Override
    public void onInterrupt() {

    }

    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        this.isRun = false;
        this.mWakeLock = ((PowerManager) getSystemService("power"))
                .newWakeLock(6, "My Tag");
        this.mWakeLock.acquire();
        return super.onStartCommand(paramIntent, paramInt1, paramInt2);
    }
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }


    public void writeFileSdcard(String paramString1) {
        if (groupInfo == null){
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        groupInfo.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
        try {
            File localFile = new File(paramString1);
            if (!localFile.exists())
                localFile.createNewFile();
            FileOutputStream localFileOutputStream = new FileOutputStream(paramString1, true);
            localFileOutputStream.write(objectMapper.writeValueAsString(groupInfo).getBytes());
            localFileOutputStream.close();
            groupInfo = null;
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
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
