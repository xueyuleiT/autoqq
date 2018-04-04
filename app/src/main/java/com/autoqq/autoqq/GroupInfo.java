package com.autoqq.autoqq;

import java.util.List;

/**
 * Created by zenghui on 2018/3/21.
 */

public class GroupInfo {

    private String time;
    List<MemberInfo> memberInfos;//群员详细信息
    String question;
    int memberNum;//群用户数量
    String groupName;//群昵称
    String groupQQ;//群号
    String createTime;//创建时间
    String groupTags;//群标签  健康  心理
    String groupMemberSurvey;//群员概况 例如 男-5人 女-5人 福建-3人 单身-2人 90后-10人
    String groupManagerNum;//管理员个数
    String keyWord;//关键字
    Object extas;//其它

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGroupTags() {
        return groupTags;
    }

    public void setGroupTags(String groupTags) {
        this.groupTags = groupTags;
    }

    public String getGroupMemberSurvey() {
        return groupMemberSurvey;
    }

    public void setGroupMemberSurvey(String groupMemberSurvey) {
        this.groupMemberSurvey = groupMemberSurvey;
    }

    public String getGroupManagerNum() {
        return groupManagerNum;
    }

    public void setGroupManagerNum(String groupManagerNum) {
        this.groupManagerNum = groupManagerNum;
    }

    public List<MemberInfo> getMemberInfos() {
        return memberInfos;
    }

    public void setMemberInfos(List<MemberInfo> memberInfos) {
        this.memberInfos = memberInfos;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupQQ() {
        return groupQQ;
    }

    public void setGroupQQ(String groupQQ) {
        this.groupQQ = groupQQ;
    }

    public Object getExtas() {
        return extas;
    }

    public void setExtas(Object extas) {
        this.extas = extas;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
