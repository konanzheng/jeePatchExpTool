package jeePatchExp;

public class MainClass {

	/**
	 * 两种模式：判断资源名称是否为为patch开头.txt结尾,如果是 则调用批量导出否则导出单个
	 * @param args
	 */
	public static void main(String[] args) {
		//判断参数个数及内容，
		if(args.length<5){
		    System.out.println("参数错误！");
		    System.exit(0);
		}else{
			String project_loc=args[0];
			String project_name=args[1];
			String resource_loc=args[2];
			String resource_name=args[3];
			String expFolder=args[4];
			ExpUtils expUtils=new ExpUtils(project_loc,project_name, resource_loc, resource_name, expFolder);
			if(resource_name!=null&&resource_name.startsWith("patch")&&resource_name.endsWith(".txt")){
				expUtils.expPatch();
			}else{
				expUtils.expResource();
			}
		}
	}

}
