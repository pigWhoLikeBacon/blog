package com.pig.blog.repository;

import java.util.List;
import java.util.Optional;

import com.pig.blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ArticleRepository extends JpaRepository<Article, Integer>,
		ArticleRepositoryCustom {
	
	long countByIsShow(boolean IsShow);
	
	Optional<Article> findByIdAndIsShow(Integer id, boolean IsShow);
	
	@Query("SELECT new Article(a.id, a.imageUrl, a.introduce, a.title, a.click, a.date, a.isShow) FROM Article a JOIN a.tags t WHERE a.isShow = ?1 AND t.id = ?2")
	Page<Article> findByIsShowAndTagsIdNCTC(boolean isShow, Integer id, Pageable pageable);
	
	@Query("SELECT new Article(a.id, a.imageUrl, a.introduce, a.title, a.click, a.date, a.isShow) FROM Article a WHERE a.isShow = ?1")
	public Page<Article> findByIsShowNCTC(boolean isShow, Pageable pageable);
	
	@Query("SELECT new Article(a.id, a.title, a.imageUrl) FROM Article a WHERE a.isShow = ?1")
	List<Article> findByIsShowOITI(boolean isShow, Pageable pageable);
	
	@Modifying
	@Query("UPDATE FROM Article article SET article.click = (article.click + 1) WHERE article.id = ?1")
	void clickAdd(Integer id);
	
	@Modifying
	@Query("UPDATE FROM Article article SET article.isShow = ?1 WHERE article.id = ?2")
	void updateIsShowById(boolean isShow, Integer id);
}
