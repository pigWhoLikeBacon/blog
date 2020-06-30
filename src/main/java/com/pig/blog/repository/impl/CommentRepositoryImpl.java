package com.pig.blog.repository.impl;

import com.pig.blog.entity.Article;
import com.pig.blog.entity.Comment;
import com.pig.blog.repository.CommentRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class CommentRepositoryImpl implements CommentRepositoryCustom {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public void removeById(Integer id) {

		Comment comment = em.find(Comment.class, id);
		Article article = comment.getArticle();
		
		article.setComments(null);
		em.remove(comment);
	}
}
