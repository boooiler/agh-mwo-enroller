package com.company.enroller.persistence;

import java.util.Collection;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

//	public Collection<Meeting> getAll() {
//		String hql = "FROM Meeting";
//		Query query = connector.getSession().createQuery(hql);
//		return query.list();
//	}
	public Collection<Meeting> getAll(String sortBy, String sortOrder, String key) {
		String hql = "FROM Meeting WHERE title LIKE :titleValue";
		if(sortBy.equals("title") || sortBy.equals("date")) {
			hql += " ORDER BY " + sortBy;
			if(sortOrder.equals("ASC") || sortOrder.equals("DESC")){
				hql += " " + sortOrder;
			}
		}
		Query query = connector.getSession().createQuery(hql);
		query.setParameter("titleValue", "%" + key + "%");
		return query.list();
	}

	public Meeting findById(long id) {
		String hql = "FROM Meeting WHERE id = :id";
		Query<Meeting> query = connector.getSession().createQuery(hql, Meeting.class);
		query.setParameter("id", id);
		return query.uniqueResult();
	}

	public void add(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void delete(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().remove(meeting);
		transaction.commit();
	}

	public void update(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(meeting);
		transaction.commit();
	}

}
