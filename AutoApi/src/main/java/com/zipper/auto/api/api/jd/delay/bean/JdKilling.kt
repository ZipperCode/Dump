package com.zipper.auto.api.api.jd.delay.bean

data class JdKilling(
    val assignmentInfo: AssignmentInfo,
    val code: String,
    val msg: String,
    val rewardsInfo: RewardsInfo?,
    val subCode: String
){

    data class RewardsInfo(
        val failRewards: List<FailReward>,
        val poolRewardZeroStock: Int,
        val successRewards: SuccessRewards?
    )

    class SuccessRewards

    data class FailReward(
        val assignmentEndTime: String,
        val assignmentName: String,
        val assignmentType: Int,
        val encryptAssignmentId: String,
        val encryptProjectId: String,
        val encryptProjectPoolId: String,
        val ext: String,
        val limitType: Int,
        val msg: String,
        val pin: String,
        val poolId: Int,
        val prizeName: String,
        val rewardCode: String,
        val rewardDesc: String,
        val rewardExt: RewardExt,
        val rewardId: Int,
        val rewardImg: String,
        val rewardName: String,
        val rewardTime: Long,
        val rewardType: Int,
        val rewardValue: String,
        val type: Int,
        val version: String
    )

    data class AssignmentInfo(
        val completionCnt: Int,
        val maxTimes: Int,
        val signList: List<String>
    )

    class RewardExt

}