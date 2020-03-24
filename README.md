# 权限管理系统

## 使用技术：后端SSM、MySql、Redis；前端Jquery

## 代码食用须知：
1、需要预先创建Mysql数据库，数据库名permission，数据集utf8mb4，排序规则utf8mb4_general_ci，结构和数据sql语句在permission.sql文件中
2、数据库配置和Redis配置在src/main/resources/datasource中
3、管理员账号`Admin@qq.com`，密码12345678，目前不支持注册账号，所有账号在sys_user表中，密码MD5加密过了，原密码均为12345678
4、登录页/signin.jsp

## 自行扩展内容：
1、SysCoreService.java类里的isSuperAdmin()设计超级管理员
2、新增管理员的密码处理SysUserService.java里的save()方法里需要移除password = "12345678";
3、同时，MailUtil里的发信参数要补全，并在SysUserService.java里的save()里sysUserMapper.insert(user)之前调用

## 后续待开发内容：
1、带有需要的各项指标的首页页面
2、管理员配置页面
3、用户的个人信息页面和个人设置页面
4、目前“小红旗”权限点的展示只能通过F12获取
5、权限操作的显示日志角色与权限、角色与用户的修改显示不够详细