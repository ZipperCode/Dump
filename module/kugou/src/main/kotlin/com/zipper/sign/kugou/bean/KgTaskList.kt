package com.zipper.sign.kugou.bean

data class KgTaskList(
    val `data`: Data = Data(),
    val errcode: Int = 0,
    val error: String = "",
    val status: Int = 0
){
    data class Data(
        val list: List<Type> = listOf()
    )

    data class Type(
        val classname: String = "",
        val gift_list: List<Gift> = listOf()
    )

    data class Gift(
        val can_excharge: Boolean = false,
        val coins: Int = 0,
        val gift_id: Int = 0,
        val gift_type: Int = 0,
        val icon: String = "",
        val name: String = "",
        val order_no: String = "",
        val remain_num: Int = 0,
        val sort: Int = 0,
        val total_num: Int = 0
    )
}