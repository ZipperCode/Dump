package com.zipper.auto.api.api.jd.delay.bean

data class JdKillingQuery(
    val code: Int,
    val result: Result?
){

    data class Result(
        val assignment: Assignment,
        val banner: Banner,
        val beginTime: Long,
        val button: String,
        val clockFace: Int,
        val continuousCheckIn: Int,
        val current: Boolean,
        val endTime: Long,
        val endTimeRemain: Int,
        val feedTitle: String,
        val head: Head,
        val noticeBoard: String,
        val otherGetDiscount: List<String>,
        val popup: Popup,
        val projectId: String,
        val share: Share,
        val taskId: String,
        val title: String,
        val transferButtonDoAssignmentShow: Boolean,
        val transferButtonShow: Boolean,
        val transferButtonText: String,
        val transferShow: Boolean
    )

    data class Assignment(
        val assignmentLowGrade: Int,
        val assignmentPoints: Int,
        val assignmentTransferRedRatio: Int,
        val encryptAssignmentId: String,
        val encryptProjectId: String,
        val isNewUserMark: Boolean,
        val transferSubTitle: String
    )

    class Banner

    class Popup

    class Head

    data class Share(
        val sharePicUrl: String,
        val shareText: String,
        val shareTitle: String
    )
}