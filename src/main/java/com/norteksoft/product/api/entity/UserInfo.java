package com.norteksoft.product.api.entity;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String telephone;//电话号码
	private String birthday;//出生日期
	private String nativePlace;//籍贯
	private String nation;//民族
	private String politicalStatus;//政治面貌
	private String height;//身高
	private String bodyWeight;//体重
	private String idCardNumber;//身份证号
	private String hireDate;//入职日期
	private String treatment;//享受待遇
	private String maritalStatus;//婚姻状态
	private String educationGrade;//学历
	private String graduatedSchool;//毕业学校
	private String major;//专业
	private String degree;//学位
	private String graduatedDate;//毕业时间
	private String firstForeignLanguage;//第一外语
	private String skilledDegree;//熟练程度
	private String secondForeignLanguage;//第二外语
	private String bloodGroup;//血型
	private String homeAddress;//家庭地址
	private String homePostCode;//邮编
	private String cityArea;//所属城管区
	private String interest;//个人爱好
	private String marriageDate;//结婚日期
	private String mateName;//配偶姓名
	private String mateBirthday;//配偶出生日期
	private String mateNation;//配偶民族
	private String mateWorkPlace;//配偶工作单位
	private String mateAddress;//配偶地址
	private String matePostCode;//配偶邮编
	private String mateTelephone;//配偶电话
	private String fatherName;//父亲姓名
	private String motherName;//母亲姓名
	private String parentAddress;//父母地址
	private String parentPostCode;//父母邮编
	private String photoPath;//头像路径
	private String nickName;//昵称
	private String age;//年龄
	private Integer dr;//删除标志
	private Date passwordUpdatedTime;//密码修改时间
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getPoliticalStatus() {
		return politicalStatus;
	}
	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getBodyWeight() {
		return bodyWeight;
	}
	public void setBodyWeight(String bodyWeight) {
		this.bodyWeight = bodyWeight;
	}
	public String getIdCardNumber() {
		return idCardNumber;
	}
	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}
	public String getHireDate() {
		return hireDate;
	}
	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}
	public String getTreatment() {
		return treatment;
	}
	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getEducationGrade() {
		return educationGrade;
	}
	public void setEducationGrade(String educationGrade) {
		this.educationGrade = educationGrade;
	}
	public String getGraduatedSchool() {
		return graduatedSchool;
	}
	public void setGraduatedSchool(String graduatedSchool) {
		this.graduatedSchool = graduatedSchool;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getGraduatedDate() {
		return graduatedDate;
	}
	public void setGraduatedDate(String graduatedDate) {
		this.graduatedDate = graduatedDate;
	}
	public String getFirstForeignLanguage() {
		return firstForeignLanguage;
	}
	public void setFirstForeignLanguage(String firstForeignLanguage) {
		this.firstForeignLanguage = firstForeignLanguage;
	}
	public String getSkilledDegree() {
		return skilledDegree;
	}
	public void setSkilledDegree(String skilledDegree) {
		this.skilledDegree = skilledDegree;
	}
	public String getSecondForeignLanguage() {
		return secondForeignLanguage;
	}
	public void setSecondForeignLanguage(String secondForeignLanguage) {
		this.secondForeignLanguage = secondForeignLanguage;
	}
	public String getBloodGroup() {
		return bloodGroup;
	}
	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getHomePostCode() {
		return homePostCode;
	}
	public void setHomePostCode(String homePostCode) {
		this.homePostCode = homePostCode;
	}
	public String getCityArea() {
		return cityArea;
	}
	public void setCityArea(String cityArea) {
		this.cityArea = cityArea;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getMarriageDate() {
		return marriageDate;
	}
	public void setMarriageDate(String marriageDate) {
		this.marriageDate = marriageDate;
	}
	public String getMateName() {
		return mateName;
	}
	public void setMateName(String mateName) {
		this.mateName = mateName;
	}
	public String getMateBirthday() {
		return mateBirthday;
	}
	public void setMateBirthday(String mateBirthday) {
		this.mateBirthday = mateBirthday;
	}
	public String getMateNation() {
		return mateNation;
	}
	public void setMateNation(String mateNation) {
		this.mateNation = mateNation;
	}
	public String getMateWorkPlace() {
		return mateWorkPlace;
	}
	public void setMateWorkPlace(String mateWorkPlace) {
		this.mateWorkPlace = mateWorkPlace;
	}
	public String getMateAddress() {
		return mateAddress;
	}
	public void setMateAddress(String mateAddress) {
		this.mateAddress = mateAddress;
	}
	public String getMatePostCode() {
		return matePostCode;
	}
	public void setMatePostCode(String matePostCode) {
		this.matePostCode = matePostCode;
	}
	public String getMateTelephone() {
		return mateTelephone;
	}
	public void setMateTelephone(String mateTelephone) {
		this.mateTelephone = mateTelephone;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getParentAddress() {
		return parentAddress;
	}
	public void setParentAddress(String parentAddress) {
		this.parentAddress = parentAddress;
	}
	public String getParentPostCode() {
		return parentPostCode;
	}
	public void setParentPostCode(String parentPostCode) {
		this.parentPostCode = parentPostCode;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public Date getPasswordUpdatedTime() {
		return passwordUpdatedTime;
	}
	public void setPasswordUpdatedTime(Date passwordUpdatedTime) {
		this.passwordUpdatedTime = passwordUpdatedTime;
	}
	
}
