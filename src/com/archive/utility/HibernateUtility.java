package com.archive.utility;

import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class HibernateUtility {
	private static SessionFactory sessionFactory = null;
	private static Logger logger = Logger.getLogger(HibernateUtility.class.getName());
		
	static{
		try{
			Configuration conf = new Configuration().configure();//configuration and load hibernate
			sessionFactory = conf.buildSessionFactory();  //create SessionFactory
		}catch(Throwable ta){
			logger.log(Level.SEVERE, null, ta);
			//静态初始化程序中发生意外异常的信号
			throw new ExceptionInInitializerError(ta);
		}				
	}
	
	//private attribute to public 
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static Session getSession(){
		return sessionFactory.openSession();
	}
	
	public static void shutDown(){
		sessionFactory.close();		
	}
	
	public static void beginTransaction(Session session){
		session.getTransaction().begin();
	}
	
	public static void commitTransaction(Session session){
		session.getTransaction().commit();		
	}
	
	public static void rollbackTransaction(Session session){
		session.getTransaction().rollback();
	}
	
	
	public static void exceptionOutput(Exception e,HttpServletRequest request, HttpServletResponse response){
		String result = null;
		JsonObject jobj = new JsonObject();
		jobj.addProperty("success", false);
		jobj.addProperty("message", e.toString());
		Gson gson = new Gson();
		result = gson.toJson(jobj);
		
		response.setContentType("type=text/html;charset=utf-8");		
		PrintWriter pw = null;
		try{
		    pw = response.getWriter();
			pw.print(result); 
			pw.close();
		}catch(Exception e1){
			logger.log(Level.SEVERE, e1.toString());
			e1.printStackTrace();
		}
		
	}
}
