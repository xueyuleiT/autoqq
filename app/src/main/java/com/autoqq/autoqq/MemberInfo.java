package com.autoqq.autoqq;

/**
 * Created by zenghui on 2018/3/20.
 */

public class MemberInfo {

    int level;//0 群主 1 管理 2群员
    String qq;
    String qqType = "个人";
    String nickName;//昵称
    String companyOrOccupation;//公司或者职业
    String school;//学校
    String sign;//个性签名
    String tags;//好友印象标签
    String groupChatLevel;//群聊等级
    String qqLevel;//qq等级
    String personInfo;//性别 年龄 星座 地址
    String joinGroupTime;//入群时间
    String specialPower;//账号特权
    Object extas;//其它

    public String getQqLevel() {
        return qqLevel;
    }

    public void setQqLevel(String qqLevel) {
        this.qqLevel = qqLevel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getGroupChatLevel() {
        return groupChatLevel;
    }

    public void setGroupChatLevel(String groupChatLevel) {
        this.groupChatLevel = groupChatLevel;
    }

    public String getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(String personInfo) {
        this.personInfo = personInfo;
    }

    public String getJoinGroupTime() {
        return joinGroupTime;
    }

    public void setJoinGroupTime(String joinGroupTime) {
        this.joinGroupTime = joinGroupTime;
    }

    public String getSpecialPower() {
        return specialPower;
    }

    public void setSpecialPower(String specialPower) {
        this.specialPower = specialPower;
    }

    public Object getExtas() {
        return extas;
    }

    public void setExtas(Object extas) {
        this.extas = extas;
    }

    public String getQqType() {
        return qqType;
    }

    public void setQqType(String qqType) {
        this.qqType = qqType;
    }

    public String getCompanyOrOccupation() {
        return companyOrOccupation;
    }

    public void setCompanyOrOccupation(String companyOrOccupation) {
        this.companyOrOccupation = companyOrOccupation;
    }
}
