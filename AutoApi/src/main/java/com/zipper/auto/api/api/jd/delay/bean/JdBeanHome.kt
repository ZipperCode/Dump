package com.zipper.auto.api.api.jd.delay.bean

data class JdBeanHome(
    val code: String,
    val `data`: Data
){

    data class Data(
        val awardType: String,
        val beanUserType: Int,
        val conductionBtn: ConductionBtn,
        val continuityAward: ContinuityAward,
        val msgGuideSwitch: String,
        val recommend: Recommend,
        val signCalendar: SignCalendar,
        val signRemind: SignRemind,
        val signedRan: String,
        val sourceTips: String,
        val status: String,
        val tomorrowSendBeans: Int
    )

    data class ConductionBtn(
        val btnText: String,
        val linkUrl: String
    )

    data class ContinuityAward(
        val beanAward: BeanAward,
        val title: String
    )

    data class BeanAward(
        val beanCount: String,
        val beanImgUrl: String
    )

    class Recommend


    data class SignCalendar(
        val currentDate: String,
        val signRecordList: List<SignRecord>
    )

    data class SignRecord(
        val awardImgUrl: String,
        val awardState: String,
        val awardType: String,
        val day: String
    )


    data class SignRemind(
        val beanHomeLink: String,
        val content: String,
        val popImgUrl: String,
        val title: String
    )
}