package com.archive.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import com.archive.jpa.TStaff;
import com.archive.utility.HibernateUtility;

/*
 * 工作人员类CUID操作
 */

public class DAOTStaff {
	Session session = null;
	boolean commit = false;
	
	public DAOTStaff(){
		session = HibernateUtility.getSession();
		commit = true;
		session.beginTransaction();
	}
	
	public DAOTStaff(Session session){
		this.session = session;
		commit = false;
		session.beginTransaction();
	}
	
	public void closeSession(){
		if(commit){
			HibernateUtility.commitTransaction(session);
			session.close();
		}		
	}
	
	
	/* 根据指定条件查询
	 * @param: condtion 查询条件,start 结果集首序号,limit 结果集长度
	 * @return : 得到结果集
	 */	
	@SuppressWarnings("unchecked")
	public List<TStaff> getByCond(String condition,int start, int limit){
		List<TStaff> li = null;
		if("all".equals(condition)){
			condition = "1=1";
		}
		try{
			Query query = session.createQuery("from TStaff where "+condition);
			if(limit>0){
				query.setFirstResult(start);
				query.setMaxResults(limit);
			}
			li = (List<TStaff>)query.list();			
		}catch(Exception e){
			HibernateUtility.rollbackTransaction(session);
			e.printStackTrace();
		}
		return li;
	}
	
	/* 查询所有工作人员信息	  
	 * @return : 得到结果集
	 */	
	@SuppressWarnings("unchecked")
	public List<TStaff> getAll(){
		List<TStaff> li = null;		
		try{
			Query query = session.createQuery("from TStaff");			
			li = (List<TStaff>)query.list();			
		}catch(Exception e){
			HibernateUtility.rollbackTransaction(session);
			e.printStackTrace();
		}
		return li;
	}
	
}
