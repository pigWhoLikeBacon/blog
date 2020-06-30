package com.pig.blog.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import com.pig.blog.entity.Article;
import com.pig.blog.repository.ArticleRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.alibaba.fastjson.JSONObject;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Page<Article> searchNCTC(boolean isShow, List<String> words, Pageable pageable) {
		
		int i;
		Sort sort = pageable.getSort();
		String hql = "SELECT a FROM Article a";
		String countHql = "SELECT count(*) FROM Article a";
		
		/* where */
		String where = " WHERE a.isShow = :isShow";
		if (!words.isEmpty()) {
			where += " AND (";
			i = 0;
			for (String word : words) {
				i++;
				where += " a.title LIKE :word" + i + " OR a.introduce LIKE :word" + i + " OR a.content LIKE :word" + i + " OR";
			}
			if (where.substring(where.length()-3).equals(" OR")) {
				where = where.substring(0, where.length() - 3);
			}
			where += " )";
		}
		hql += where;
		countHql += where;
		/* where */
		
		/* order by */
		hql += getOrderBy(sort);
		/* order by */
		
		Query query = em.createQuery(hql, Article.class);
		query.setParameter("isShow", isShow);
		i = 0;
		for (String word : words) {
			i++;
			query.setParameter("word" + i, "%" + word + "%");
		}
		query = setPage(query, pageable);
		List<Article> articlesList = query.getResultList();
		
		Query countQuery = em.createQuery(countHql, Long.class);
		countQuery.setParameter("isShow", isShow);
		i = 0;
		for (String word : words) {
			i++;
			countQuery.setParameter("word" + i, "%" + word + "%");
		}
		long count = ((Long) countQuery.getSingleResult()).longValue();
		
		Page<Article> articlePage = new PageImpl<Article>(articlesList, pageable, count);
		return articlePage;
	}

	@Override
	public ArrayList<JSONObject> countGroupByMonths(boolean isShow) {
		String hql = "select DATE_FORMAT(date,'%Y'), DATE_FORMAT(date,'%m'), count(id) from Article group by DATE_FORMAT(date,'%Y%m')";
		TypedQuery<Tuple> query = em.createQuery(hql, Tuple.class);
		List<Tuple> resultList = query.getResultList();
		
		HashMap<String, Long> map = new HashMap<String, Long>();
		for (Tuple t : resultList) {
			map.put((String) t.get(0), (Long) t.get(2));
		}
		
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		for (Tuple t : resultList) {
			JSONObject j = new JSONObject();
			j.put("year", t.get(0));
			j.put("month", t.get(1));
			j.put("count", t.get(2));
			list.add(j);
		}
		
		return list;
	}

	@Override
	public Page<Article> findByTimeNCTC(boolean isShow, Date start, Date end, Pageable pageable) {
		Sort sort = pageable.getSort();
		String hql = "SELECT a FROM Article a";
		String countHql = "SELECT count(*) FROM Article a";
		
		/* where */
		String where = " WHERE a.isShow = :isShow AND a.date BETWEEN :start AND :end";
		hql += where;
		countHql += where;
		/* where */
		
		/* order by */
		hql += getOrderBy(sort);
		/* order by */
		
		Query query = em.createQuery(hql, Article.class);
		query.setParameter("isShow", isShow);
		query.setParameter("start", start, TemporalType.DATE);
		query.setParameter("end", end, TemporalType.DATE);
		query = setPage(query, pageable);
		List<Article> articlesList = query.getResultList();
		
		Query countQuery = em.createQuery(countHql, Long.class);
		countQuery.setParameter("isShow", isShow);
		countQuery.setParameter("start", start, TemporalType.DATE);
		countQuery.setParameter("end", end, TemporalType.DATE);
		long count = ((Long) countQuery.getSingleResult()).longValue();
		
		Page<Article> articlePage = new PageImpl<Article>(articlesList, pageable, count);
		return articlePage;
	}
	
	/* util */
	List<String> allowedOrders = Arrays.asList("id", "click", "date");
	
	private boolean legalOrder(Order order) {
		
		String dir = order.getDirection().toString();
		boolean cond1 = dir.equals("ASC");
		boolean cond2 = dir.equals("DESC");
		boolean cond3 = dir.equals("asc");
		boolean cond4 = dir.equals("desc");
		
		if (cond1 || cond2 || cond3 || cond4) {
			String pro = order.getProperty();
			for (String allowOrder : allowedOrders) {
				if (pro.equals(allowOrder)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private String getOrderBy(Sort sort) {
		String orderBy = "";
		if (!sort.isEmpty()) {
			orderBy += " ORDER BY";
			for (Order order : sort) {
				if (legalOrder(order)) {
					orderBy += " " + order.getProperty() + " " + order.getDirection() + ",";
				}
			}
			if (orderBy.substring(orderBy.length()-1).equals(",")) {
				orderBy = orderBy.substring(0, orderBy.length() - 1);
			}
		}
		return orderBy;
	}
	
	private Query setPage(Query query, Pageable pageable) {
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		return query;
	}
	/* util */
}
