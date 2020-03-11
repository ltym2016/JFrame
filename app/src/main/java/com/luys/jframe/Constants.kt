package com.luys.jframe

/**
 * @author luys
 * @describe
 * @date 2019/5/8
 * @email samluys@foxmail.com
 */
object Constants {
    /**
     * 跳转到话题详情页区分
     */
    const val FROM = "from"
    /**
     * 跳转到话题详情页区分: 点击item
     */
    const val FROM_ITEM = 1
    /**
     * 跳转到话题详情页区分: 点击评论
     */
    const val FROM_COMMENT = 2
    /**
     * 跳转到登录页区分: 从系统设置的退出登录到登录页
     */
    const val FROM_SETTING_LOGOUT = 3
    /**
     * 分页一页请求多少条
     */
    const val PAGE_NUM = 10
    /**
     * 文件前缀
     */
    const val FILE_PREX = "file://"
    /**
     * position 常量
     */
    const val POSITION = "position"
    /**
     * ID常量
     */
    const val ID = "id"
    /**
     * 图片集合
     */
    const val IMAGES = "images"
    /**
     * 话题的作者ID
     */
    const val POSTER_USER_ID = "poster_user_id"
    /**
     * 实体类传递参数
     */
    const val ENTITY = "entity"
    /**
     * webview Url
     */
    const val URL = "url"
    /**
     * 实体类传递参数
     */
    const val ALL_REPLT_ENTITY = "all_reply_entity"
    /**
     * 用户ID传递参数
     */
    const val USER_ID = "user_id"
    /**
     * 用户状态
     */
    const val USER_STATUS = "user_status"
    /**
     * 话题编辑传递参数
     */
    const val TOPIC_EDIT = "topic_edit"
    /**
     * 用户昵称传递参数
     */
    const val USER_NAME = "user_name"
    /**
     * 名称传递参数
     */
    const val NAME = "name"
    /**
     * 话题ID
     */
    const val TOPIC_ID = "topic_id"
    /**
     * 楼层ID
     */
    const val FLOOR_ID = "floor_id"
    /****** 友盟统计事件ID START  */ // 退出登录事件
    const val EVENT_LOGOUT_ID = "9"
    // 举报事件
    const val EVENT_REPORT_ID = "8"
    // 屏蔽事件
    const val EVENT_SHEILD_ID = "7"
    // 分享事件
    const val EVENT_SHARE_ID = "6"
    // 发表评论事件
    const val EVENT_COMMENT_ID = "5"
    // 发布事件
    const val EVENT_PUBLISH_ID = "4"
    // 搜索事件
    const val EVENT_SEARCH_ID = "3"
    // 加关注事件
    const val EVENT_FOLLOW_ID = "2"
    // 点赞事件
    const val EVENT_ZAN_ID = "1"
    /****** 友盟统计事件ID END  */
    /****** 接口请求结果状态 START */ // 请求成功
    const val REQUEST_OK = 0
    // 无数据
    const val REQUEST_NO_DATA = 1
    // 无网络
    const val REQUEST_NO_NET = 2
    // 服务器挂了
    const val REQUEST_FAIL = 3
    // 没有更多数据
    const val REQUEST_NO_MORE = 4
    // 只是提示用
    const val REQUEST_REMIND = 5
    // 数据异常、空指针等
    const val REQUEST_DATA_ERROR = 6
    // 内容被删除
    const val REQUEST_DELETE = 7
    /****** 接口请求结果状态 END */
    /****** 举报场景 START */ // 话题
    const val REPORT_SCENE_TOPIC = 2
    // 评论
    const val REPORT_SCENE_COMMENT = 3
    // 个人
    const val REPORT_SCENE_PERSON = 5
    /******举报场景 END */ // 微信平台
    const val PLATFORM_WECHAT = "wechat"
    // QQ平台
    const val PLATFORM_QQ = "qq"
    const val PLATFORM = "platform"
    const val THIRD_INFO = "third_info"
    // 登录时传的event区分 第三方登录
    const val THIRD_REGISTER = "thirdregister"
    // 登录时传的event区分 手机登录
    const val MOBILE_LOGIN = "mobilelogin"
    // 1=评论点赞
    const val ZAN_TYPE_COMMENT = "1"
    // ,2=回复点赞
    const val ZAN_TYPE_REPLY = "2"
    /**
     * 头像
     */
    const val AVATAR = "avatar"
    /**
     * 内容
     */
    const val CONTENT = "content"
}