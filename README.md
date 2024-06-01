# 开发者工具箱

> 前后端分离
>
> 基于Spring Boot、Spring Data JPA、Spring Security、H2 Database
>
> 支持用户名密码登录、权限控制



## 功能列表

- [x] 用户管理
- [x] 角色管理
- [x] 权限管理
- [x] 增删改查代码生成
- [x] JSON工具
- [ ] 正则工具
- [x] 代办事项
- [x] 局域网扫描
- [x] 本机网络信息查看
- [ ] 查杀系统进程
- [ ] 测试数据生成
- [x] 扩展(书签)



## 核心依赖

| 依赖        | 版本    |
| ----------- | ------- |
| JDK         | 1.8     |
| Spring Boot | 2.7.6   |
| Node        | 18.20.3 |
| Vue         | 3.3.4   |



## 本地开发运行

```shell
#1.下载服务端代码
git clone https://github.com/cloudlandboy/clkit.git
#2. 启动服务端代码,运行 ClkitApplication
#3. 下载前端UI代码
git clone https://github.com/cloudlandboy/clkit-ui.git
#启动前端UI代码
cd clkit-ui/
npm i && npm run dev
```



## 仅后端模式运行

```shell
#1. 下载服务端代码
git clone https://github.com/cloudlandboy/clkit.git
#2. 下载前端UI代码
git clone https://github.com/cloudlandboy/clkit-ui.git
#3. 构建前端代码
cd clkit-ui/
npm i && npm run build
#4.将构建后dist目录下的文件复制到src/main/resources/public
mkdir ../clkit/src/main/resources/public
cp -r dist/* ../clkit/src/main/resources/public/
#5. 构建服务端代码
cd ../clkit
mvn clean package
#6. 后台运行
nohup java -jar target/clkit-0.0.5.jar > clkit.log 2>&1 &
```

