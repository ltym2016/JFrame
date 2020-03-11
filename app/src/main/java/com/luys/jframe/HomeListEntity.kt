package com.luys.jframe

import com.luys.library.base.BaseBindingItem

/**
 * @author luys
 * @describe
 * @date 2020/3/11
 * @email samluys@foxmail.com
 */
data class HomeListEntity(
    val id: Int = 0,
    val job_id: Int = 0,
    val salary: String = "",
    val status: Int = 0,
    val status_text: String = "",
    val title: String = "",
    val valid_flag: Int = 0,
    val valid_flag_text: String = "",
    val validtime_text: String = ""
): BaseBindingItem {

    var type:Int = 0

    override fun isHeader(): Boolean = false

    override fun isFooter(): Boolean = false

    override fun getViewType(): Int = R.layout.item_home_list
}