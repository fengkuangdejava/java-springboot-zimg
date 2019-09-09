package com.example.demo.model;

/**
 * 返回信息枚举类 用于统一管理返回状态码和异常提示信息
 *
 *@author 李挺 【fengkuangdejava@outlook.com】
 *@date 2018/10/8 10:20
 */
public enum ResultEnum {
    /**
     * 成功返回
     * */
    SUCCESS(200,"success"),
    UNKNOWN_ERROR(4000,"未知错误"),

    UN_FIND_TOKEN_HEADER(6001,"token解析失败，请求头无token"),
    REQUST_METHOD_ERROR(6003,"请求方法不正确"),
    VALID_AUTHORIZED(6004,"权限非法"),
    TOKEN_ERROR(6005,"token处理失败"),
    GET_TOKEN_ERROR(6006,"申请token失败"),
    TOKEN_INVALID(6007,"token过期"),
    TOKEN_DISABLED(6008,"token无效"),
    USER_UNLOGIN(6009,"用户未登录，请重新登录"),
    USER_NO_AUTH(6010,"用户没有权限"),
    TOCKEN_REFRESH_ERROR(6011,"tocken刷新失败"),
    REGIST_ERROR(6012,"注册失败"),
    VALIDATE_CODE_ERROR(6013,"验证码错误"),
    USER_NAME_REPEAT(6014,"用户名重复"),
    LOGOUT_ERROR(6015,"退出失败"),
    NO_GUESTS_ACCOUNT_CAN_USE(6016,"没有可用游客账户"),
    SYSTEM_ERROR(6017,"系统认证异常"),
    USER_NOT_FOUND(6018,"用户名密码错误"),
    LOGIN_PASSWORD_ERROR(6018,"用户名密码错误"),
    UN_MOBILE_AUTHORIZED(6019,"手机登录失败"),

    CREATE_IM_GROUP_ERROR(7001,"创建环信群组失败"),
    SMS_SEND_ERROR(7002,"短信发送失败"),
    SMS_QUERY_ERROR(7003,"短信获取失败"),
    IM_REGISTER_ERROR(7004,"注册环信账号失败"),
    IM_INFO_NOT_FOUND(7005,"注册环信账号失败"),
    SMS_CLIENT_ERROR(7006,"获取阿里云短信服务客户端失败"),


    INNER_REQUEST_ERROR(8001,"内部请求失败"),
    DATABASE_INSERT_ERROR(8002,"数据库写入失败"),
    DATABASE_UPDATE_ERROR(8003,"数据库更新失败"),
    DATABASE_DELETE_ERROR(8004,"数据库删除失败"),
    DATABASE_QUERY_ERROR(8005,"数据库查询失败"),
    FILE_DELETE_ERROR(8006,"文件删除失败"),
    PARAM_VALIDATE_ERROR(8007,"参数不对"),
    IO_CLOSE_ERROR(8008,"IO关闭异常"),
    IO_ERROR(8009,"IO异常"),
    SERVICE_CONNECT_ERROR(8010,"连接第三方服务失败"),
    DEAL_RESPONSE_ERROR(8011,"处理响应结果失败"),
    CLOSE_RESPONSE_ERROR(8012,"关闭response失败"),
    REQUEST_SERVICE_ERROR(8013,"调用第三方服务失败"),
    UPLOAD_ZIMG_ERROR(8014,"上传图片失败"),


    ALREADY_LIKE(1001,"您已经点赞过"),
    FILE_IS_NULL(1002,"文件为空"),
    FILE_UPLOAD_FAILD(1003,"文件上传失败"),
    NOT_FRIEND(1004,"不是好友关系"),
    GROUPORCOMPANY_NONEOF_RELATION(1005,"用户不在该企业或者群组"),
    DATABASE_NO_RECORD(1006,"后台未找到记录"),
    FILE_NOT_FOUND(1007,"文件未找到"),
    AREADY_JOIN(1008,"您已经加入了"),
    PASSWORD_ERROR(1009,"原密码不正确"),
    DECRYPT_ERROR(1010,"解密失败"),
    ENCRYPTION_ERROR(1011,"加签异常");
    private  int code;
    private  String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
