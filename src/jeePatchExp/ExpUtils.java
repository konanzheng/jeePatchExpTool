package jeePatchExp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author ruizhao
 *
 */
public class ExpUtils {
	private String project_loc;//工程路径
	private String project_name;//工程名称
	private String resource_loc;//资源路径
	private String resource_name;//资源名称
	private String expFolder;//导出目录
	private static String constants_WEBCONTENT="WebRoot";//web内容目录 根据具体情况修改
	private static String constants_CLASSES=File.separatorChar+"WEB-INF"+File.separatorChar+"classes"+File.separatorChar;//web内容目录 根据具体情况修改
	private static String constants_JAVAFILE=".java";//java文件后缀
	private static String constants_CLASSFILE=".class";//class文件后缀
	
	public ExpUtils(String project_loc,String project_name,String resource_loc,String resource_name,String expFolder){
		this.project_loc=project_loc;
		this.project_name=project_name;
		this.resource_loc=resource_loc;
		this.resource_name=resource_name;
		this.expFolder=expFolder;
	}
	
	/**
	 * 导出资源
	 * 目前只考虑代码，配置或其他资源文件暂不考虑
	 * 且只能每次导出一个文件,
	 * 判断资源路径中是否包含constants_WEBCONTENT ，包含则直接复制
	 * 判断资源文件以.java结尾，拷贝class文件到对应目录,window下涉及到文件路径分割替换要用四个“\”
	 */
	public void expResource(){
		String src=resource_loc;//原文件
		String desc="";//导出文件
		String rName=resource_name;
		
		if(resource_loc!=null&&resource_loc.contains(constants_WEBCONTENT)){
			String desc_temp=resource_loc.substring(resource_loc.indexOf(constants_WEBCONTENT));
			desc=desc_temp.replace(constants_WEBCONTENT, expFolder+File.separatorChar+project_name);
			makFIleDirs(desc.replace(rName, "")); 
			copyResource(src,desc);
		}
		if(resource_name!=null&&resource_name.endsWith(constants_JAVAFILE)){
			String[] src_strs=resource_loc.split(project_name);
			String separator = getSeparator();
			String ydmMl=src_strs[1].split(separator)[1];
			String src_temp=constants_CLASSES+src_strs[1].replaceFirst(separator+ydmMl+separator, "").replace(constants_JAVAFILE, constants_CLASSFILE);
			src=project_loc+File.separatorChar+constants_WEBCONTENT+src_temp;
			desc=expFolder+File.separatorChar+project_name+src_temp;
			rName=resource_name.replace(constants_JAVAFILE, constants_CLASSFILE);
			makFIleDirs(desc.replace(rName, "")); 
			expInnerClass(src, desc, rName);
			copyResource(src,desc);
		}
		
	}
	/**
	 * 导出内部类
	 * @param src
	 * @param desc
	 * @param rName
	 */
	public void expInnerClass(String src,String desc,String rName){
		File classFileFolder=new File(src.replace(rName, ""));
		if(classFileFolder.exists()){
			for(String filename:classFileFolder.list()){
				if(filename!=null&&filename.startsWith(rName.replace(constants_CLASSFILE, "$"))&&filename.endsWith(constants_CLASSFILE)){
					copyResource(src.replace(rName, filename),desc.replace(rName, filename));
				}
			}
		}
	}
	/**
	 * 导出补丁（根据svn diff 命令生成的txt内容确定补丁文件，批量导出）
	 */
	public void expPatch(){
	        FileInputStream f;
			try {
				f = new FileInputStream(resource_loc);
				BufferedReader dr=new BufferedReader(new InputStreamReader(f));  
				String line;  
				while((line=dr.readLine())!=null){   
					if(line.indexOf("Index:")!=-1){  
						line=line.replaceAll(" ","");  
						line=line.substring(line.indexOf(":")+1,line.length());  
						String[] line_strs=line.split("/");
						String rn=line_strs[line_strs.length-1];
						String separator = getSeparator();
						line=line.replaceAll("/", separator);
						String rl=project_loc+File.separatorChar+line;
						if(rn!=null&&rl!=null){
							this.setResource_loc(rl);
							this.setResource_name(rn);
							this.expResource();
						}
					}
				}   
				dr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}   
		
	}

	/**
	 * 根据平台特性，区分windows linux 路径分割符
	 * @return
	 */
	private String getSeparator() {
		String separator="\\";
		if(separator.equals(String.valueOf(File.separatorChar))){
			separator="\\\\";
		}else{
			separator=String.valueOf(File.separatorChar);
		}
		return separator;
	}
	/**
	 * 拷贝资源
	 * @param src
	 * @param desc
	 */
	private  void copyResource(String src,String desc){ 
		try { 
			    System.out.println("copy resource from : "+src+" \nto :"+desc);
		        int byteread = 0; 
		        File oldfile = new File(src); 
		        if (oldfile.exists()) { 
		            InputStream inStream = new FileInputStream(src);   
		            FileOutputStream fs = new FileOutputStream(desc); 
		            byte[] buffer = new byte[1444]; 
		            while ( (byteread = inStream.read(buffer)) != -1) { 
		                fs.write(buffer, 0, byteread); 
		            } 
		            fs.flush();
		            inStream.close(); 
		            fs.close();
		        } 
	    } catch (Exception e) { 
	        System.out.println("复制单个文件操作出错"); 
	        e.printStackTrace(); 
	    } 
	
	}
	
	/**
	 * 创建目录
	 * @param filepath
	 */
	public void makFIleDirs(String filepath){
		File file=new File(filepath);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getResource_loc() {
		return resource_loc;
	}

	public void setResource_loc(String resource_loc) {
		this.resource_loc = resource_loc;
	}

	public String getResource_name() {
		return resource_name;
	}

	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}

	public String getExpFolder() {
		return expFolder;
	}

	public void setExpFolder(String expFolder) {
		this.expFolder = expFolder;
	}
	
}
