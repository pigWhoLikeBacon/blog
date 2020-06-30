package com.pig.blog.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import com.pig.blog.entity.Comment;
import com.pig.blog.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CommentRepository commentRepository;

	/* Base */
	@Cacheable(value = "comment", key = "'findById_' + #id")
	public Comment findById(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }
	
	@Cacheable(value = "comments", key = "'findByAll'")
	public List<Comment> findAll() {
        return commentRepository.findAll();
    }
	
	@Caching(evict = {@CacheEvict(value = "comment", key = "'findById_' + #comment.id"),
			@CacheEvict(value = "comments", allEntries = true)})
	public Comment save(Comment comment) {
		comment.setIsRead(false);
		comment.setIsShow(true);
		Date date = new Date();
		comment.setDate(date);
        return commentRepository.save(comment);
    }
	
	@Caching(evict = {@CacheEvict(value = "comment", key = "'findById_' + #id"),
			@CacheEvict(value = "comments", allEntries = true)})
	public String deleteById(Integer id) {
		if (commentRepository.existsById(id)) {
			commentRepository.removeById(id);
			if (commentRepository.existsById(id)) {
				return "Success!";
			} else {
				return "Fail!";
			}
		} else {
			return "No comment with id " + id + " exists!";
		}
    }
	
	@Cacheable(value = "comments", key = "'count'")
	public long count() {
		return commentRepository.count();
	}
	/* Base */
	
	/* Advance */
	/* find */
	@Cacheable(value = "comments", key = "'findByIsRead_' + #isRead + '_' + #pageable")
	public Page<Comment> findByIsRead(Boolean isRead, Pageable pageable) {
		Comment comment = new Comment();
		comment.setIsRead(isRead);
		return commentRepository.findAll(Example.of(comment), pageable);
	}
	
	@Cacheable(value = "comments", key = "'countByIsShow_' + #isRead")
	public long countByIsShow(Boolean isRead) {
		return commentRepository.countByIsRead(isRead);
	}
	
	@Cacheable(value = "comments", key = "'findByArticleIdAndIsShow_' + #articleId + '_' + #isShow + '_' + #pageable")
	public Page<Comment> findByArticleIdAndIsShow(Integer articleId, Boolean isShow, Pageable pageable) {
		Page<Comment> commentPage = commentRepository.findByArticleId(articleId, pageable);
		if (isShow == true) {
			commentPage = hideComments(commentPage);
		}
		return commentPage;
	}
	
	@Cacheable(value = "comments", key = "'countByArticleId_' + #articleId")
	public long countByArticleId(Integer articleId) {
		return commentRepository.countByArticleId(articleId);
	}
	/* find */
	
	/* change */
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = {@CacheEvict(value = "comment", key = "'findById_' + #id"),
			@CacheEvict(value = "comments", allEntries = true)})
	public void updateIsShowById(Boolean isShow, Integer id) {
		commentRepository.updateIsShowById(isShow, id);
	}
	
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = {@CacheEvict(value = "comment", key = "'findById_' + #id"),
			@CacheEvict(value = "comments", allEntries = true)})
	public void updateIsReadById(Boolean isRead, Integer id) {
		commentRepository.updateIsReadById(isRead, id);
	}
	
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = {@CacheEvict(value = "comment", allEntries = true),
			@CacheEvict(value = "comments", allEntries = true)})
	public void updateIsReadAll(Boolean isRead) {
		commentRepository.updateIsReadAll(isRead);
	}
	/* change */
	/* Advance */
	
	/* Util */
	private Page<Comment> hideComments(Page<Comment> commentPage) {
		for (Comment c : commentPage.getContent()) {
			if (c.getIsShow() == false) {
				c.setName("NULL");
				c.setEmail(null);
				c.setWebsite(null);
				c.setContent("此评论已被删除！");
			}
		}
		return commentPage;
	}
	/* Util */
}
