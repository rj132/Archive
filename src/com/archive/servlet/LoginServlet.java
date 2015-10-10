package com.archive.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.archive.dao.DAOTUsers;
import com.archive.jpa.TUsers;
import com.archive.utility.HibernateUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LoginServlet.class.getName());
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		System.out.println("dfdsafdsaf");
		//获取前端传过来的参数－请求
		String action = request.getParameter("action");
		if("checkUser".equals(action)){
			checkUser(request,response);
		}else{
			logger.log(Level.INFO, "action type parameter error");
			Exception e = new Exception("不支持操作  " + action);
			HibernateUtility.exceptionOutput(e, request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}
	
	
	public void checkUser(HttpServletRequest request, HttpServletResponse response) {
		//读取前端传来的参数－对象
		String data = request.getParameter("params");	
		
	    if(data==null){
	    	Exception e = new Exception("请求参数有误！");
	    	HibernateUtility.exceptionOutput(e,request,response);
	    }
		//创建GSON对象
	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	    //将GSON对象映射成JAVA对象
		TUsers emp = gson.fromJson(data, TUsers.class);
		
		try{
			if(emp.getUserName()!=""&&emp.getUserName()!=null){
				emp.setUserName(java.net.URLDecoder.decode(emp.getUserName(), "utf-8"));//对于编码后字符串进行解码
			}
		}catch(UnsupportedEncodingException e){
			HibernateUtility.exceptionOutput(e,request,response);
			e.printStackTrace();
		}		
		
		//访问数据库，验证用户的合法性
		List<TUsers> userlist = null;
		DAOTUsers daouser = new DAOTUsers();
		String cond = "username='" + emp.getUserName() + "' and userpwd='" + emp.getUserPwd() + "'";
		
		try{
			userlist = daouser.getUsers(cond, 0, 0);
		}catch(Exception e){
			HibernateUtility.exceptionOutput(e,request,response);
			e.printStackTrace();			
		}finally{
			daouser.closeSession();
		}
		
		String responseResult = null;
		//创建jsonobject，封装处理结果状态和结果字符串
		JsonObject jobj = new JsonObject();		
		if(userlist!=null&&userlist.size()>0){
			jobj.addProperty("success", true);			
			jobj.addProperty("message", "exist");
			System.out.println(userlist.size());					
		}else
		{
			jobj.addProperty("success", true);			
			jobj.addProperty("message", "noexist");			
		}
		//将json对象转字符串，用于传回前端	
		responseResult = gson.toJson(jobj);
		//写回前端
		response.setContentType("text/html;charset=utf-8");	
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.print(responseResult);
			pw.close();	
		} catch (IOException e) {
			HibernateUtility.exceptionOutput(e,request,response);			
			e.printStackTrace();
		}
		
	}
	
	

}


