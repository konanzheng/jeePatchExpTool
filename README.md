java web工程补丁导出工具 1.0
=========================
  利用eclipse的外部工具 和svn插件将补丁文件导出  
  目前版本适用于java web工程。
  手动选择导出位置，导出资源目录层级会保持一致。   
  类文件导出到 WebRoot/WEB-INF/classes ，  
  资源文件导出到工程目录下。  

- - -
  
  配置方式：
    
1. 给jeePatchExp.jar 执行权限
2. 配置到eclipse的外部工具中   
external Tools Conflgurations 工具配置  
Main:
location :指定可执行jeePatchExp.jar包位置
 
：Auguments:
${project_loc}    ${project_name}		${resource_loc}		${resource_name} 	${folder_prompt}  
 参数含义：
      
1. ${project_loc} 工程路径 
2. ${project_name} 工程名称
3. ${resource_loc} 资源路径（绝对）
4. ${resource_name} 资源文件名
5.  导出目录  对话框

假如使用windows系统可能遇到不能直接调用jar文件 

1. 可以修改jar文件默认打开方式为java.exe
2. 可以编写批处理脚本 调用
 

例如：  

	c:    
	cd c:\buding  
	java -jar jeePatchExp.jar %1 %2 %3 %4 %5  
	
**注意：**eclipse的外部工具location此时要设置为批处理的路径  
(感谢海丹同学的帮助在windows下的测试和完善)
- - -
使用方式有两种： 

1. 选中单个文件点击 外部工具执行
2. 通过svn的teamSynchronizing 模式选中多个补丁资源 然后创建补丁"create patch..."   
   命名成pathc20141210201418.txt （“patch”开头“.txt”结束）模式的文件名  
   然后选中这个补丁文件 点击外部工具执行
   
