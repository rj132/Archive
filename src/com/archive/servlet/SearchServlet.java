package com.archive.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.archive.dao.DAOTStaff;
import com.archive.jpa.TStaff;
import com.archive.jpa.TUsers;
import com.archive.utility.HibernateUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(SearchServlet.class.getName());
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//设置request编码格式，可以避免post请求的中文乱码，但get不行
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		
		if(action.equals("getAll")){
			getAll(request, response);
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

	public void getAll(HttpServletRequest request, HttpServletResponse response){
		
		List<TStaff> li = null;
		DAOTStaff daostaff = null;
		try{
			daostaff = new DAOTStaff();
			li = (List<TStaff>)daostaff.getAll();
			
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			JsonObject jobj = new JsonObject();
			
			if(li!=null&&li.size()>0){
				jobj.addProperty("success", true);
				JsonArray ja = (JsonArray) gson.toJsonTree(li);
				jobj.add("result", ja);
			}else{
				jobj.addProperty("success", false);
				jobj.addProperty("message", "数据不存在");
			}	
			
			response.setContentType("text/html;charset=utf-8");
			String responseResult = null;
			PrintWriter pw = response.getWriter();			
			responseResult = gson.toJson(jobj);		
			pw.print(responseResult);
			pw.close();			
		}catch(Exception e){
			HibernateUtility.exceptionOutput(e, request, response);
			e.printStackTrace();
		}	
		
	
	}
}
