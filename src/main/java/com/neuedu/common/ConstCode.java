package com.neuedu.common;

public class ConstCode {
    //用户名与密码的
    public static final String CURRENTUSER="currentuser";

    public enum ReponseCodeEnum{

        NEED_LOGIN(100,"需要登录"),
        NO_PRIVILEGE(101,"没有权限操作")
        ;
        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        ReponseCodeEnum(int code, String desc) {

            this.code = code;
            this.desc = desc;
        }
    }






    //这是角色定值   用的是枚举
    public  enum RoleEnum{ //一个变量他是有限的你就可以定义一个枚举
        ROLE_ADMIN(0,"管理员"),
        ROLE_CUSTOMER(1,"普通用户")
        ; //这个分号是枚举有的
        private int code; //这个是0或1
        private String desc;//这个是描述：管理员  或  普通用户
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private RoleEnum(int code, String desc){
            this.code=code;
            this.desc=desc;

        }
    }
}
