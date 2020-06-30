package com.pig.blog.repository;

import java.util.List;

import com.pig.blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

public interface TagRepository extends JpaRepository<Tag, Integer> {
	
	List<Tag> findByArticlesId(Integer articlesId);
	
	@Query("SELECT tag FROM Tag tag WHERE tag.id NOT IN"
			+ " (SELECT tag2.id FROM Tag tag2 LEFT JOIN tag2.articles article WHERE article.id = ?1)")
	List<Tag> findByArticleIdNot(Integer articleId);
}
