## FastAdmin-exp
**运行需要Java>=11**
### 目前支持任意文件读取漏洞的检测
单个目标检测： 
`java -jar FastAdmin-exp.jar -u http://target.com/`
![image](https://github.com/user-attachments/assets/d333cb67-a379-4f41-87b2-944c531b0494)
![image](https://github.com/user-attachments/assets/c26e1855-022b-4f65-a0c5-5d4422085959)

自动对存在漏洞的目标进行一键Shell,在网站根路径下生成api.php文件,连接密码随机生成
![image](https://github.com/user-attachments/assets/26711f47-6965-4c5b-a9f0-0a9c6d684cef)
![Snipaste_2024-09-19_21-29-27](https://github.com/user-attachments/assets/2b672fcc-2cb3-4a29-a1be-f289da4239c9)

进行批量检测：
`java -jar FastAdmin-exp.jar -r FastAdmin.csv`
![image](https://github.com/user-attachments/assets/d7fa9ff7-66fa-4544-ae60-861ef031319a)
