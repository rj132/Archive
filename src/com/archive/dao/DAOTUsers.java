package com.archive.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.archive.jpa.TUsers;
import com.archive.utility.HibernateUtility;

public class DAOTUsers {
	Session session = null;
	Boolean commit = false;
	
	public DAOTUsers(){
		session = HibernateUtility.getSession();	
		commit = true;
		HibernateUtility.beginTransaction(session);
	}
	
	public DAOTUsers(Session session){
		this.session = session;
		commit = false;
	}
	
	public void closeSession(){
		if(commit){
			HibernateUtility.commitTransaction(session);
			session.close();
		}		
	}
	
	@SuppressWarnings("unchecked")
	public List<TUsers> getUsers(String conditions, int start , int limit) throws Exception{
		List<TUsers> li = null;
		
		try
		{
			Query query = session.createQuery("from TUsers where " + conditions);
					
			if (limit > 0)
			{
				query.setFirstResult((int)start);
				query.setMaxResults((int)limit);
			}
			li = (List<TUsers>)query.list();			
		}
		catch(Exception e) {
			HibernateUtility.rollbackTransaction(session);
			e.printStackTrace();
		}		
		return li; 
	}
	
}
