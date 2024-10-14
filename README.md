# FastAdmin-exp
**运行需要Java>=11,建议Java17**
### 目前支持任意文件读取漏洞的检测,FastAdmin版本 < 1.3.4.20220530
### 单个目标检测：
```shell
java -jar FastAdmin-exp.jar -u http://target.com/
```
![image](https://github.com/user-attachments/assets/6a153f17-3897-429f-9c8b-1b86ddf8f8a2)

![image](https://github.com/user-attachments/assets/c26e1855-022b-4f65-a0c5-5d4422085959)
### 目标一键Shell
**对单个目标Fuzz时，自动对存在漏洞的目标进行一键Shell，在网站根路径下生成api.php文件，连接密码随机生成**
![image](https://github.com/user-attachments/assets/26711f47-6965-4c5b-a9f0-0a9c6d684cef)
![Snipaste_2024-09-19_21-29-27](https://github.com/user-attachments/assets/2b672fcc-2cb3-4a29-a1be-f289da4239c9)
### 进行批量检测
**读取csv文件中以url为开头的列**
```shell
java -jar FastAdmin-exp.jar -r FastAdmin.csv
```
![image](https://github.com/user-attachments/assets/63b8117a-0349-4efe-be7b-36bcdf69e9f4)


### Fuzz效果
注：下图均为合法渗透测试内容,已修复
![image](https://github.com/user-attachments/assets/4ed3c562-0f96-4c6a-8c44-825a7a76b965)


# 免责申明
该工具仅用于安全自查检测

由于传播、利用此工具所提供的信息而造成的任何直接或者间接的后果及损失，均由使用者本人负责，作者不为此承担任何责任。

本人拥有对此工具的修改和解释权。未经网络安全部门及相关部门允许，不得善自使用本工具进行任何攻击活动，不得以任何方式将其用于商业目的。
