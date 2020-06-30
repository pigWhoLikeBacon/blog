package com.pig.blog.repository;

import com.pig.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Integer>,
		CommentRepositoryCustom {
	
	long countByIsRead(Boolean IsRead);
	
	Page<Comment> findByArticleId(Integer articleId, Pageable pageable);
	
	@Query("SELECT COUNT(c) FROM Comment c WHERE c.article.id = ?1")
	long countByArticleId(Integer articleId);
	
	@Modifying
	@Query("UPDATE FROM Comment comment SET comment.isShow = ?1 WHERE comment.id = ?2")
	void updateIsShowById(boolean isShow, Integer id);
	
	@Modifying
	@Query("UPDATE FROM Comment comment SET comment.isRead = ?1 WHERE comment.id = ?2")
	void updateIsReadById(boolean isRead, Integer id);
	
	@Modifying
	@Query("UPDATE FROM Comment comment SET comment.isRead = ?1")
	void updateIsReadAll(boolean isRead);
}
