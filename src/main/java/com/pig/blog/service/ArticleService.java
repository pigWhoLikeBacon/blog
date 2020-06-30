package com.pig.blog.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import com.pig.blog.entity.Article;
import com.pig.blog.entity.Tag;
import com.pig.blog.repository.ArticleRepository;
import com.pig.blog.repository.CommentRepository;
import com.pig.blog.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class ArticleService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/* Base */
	@Cacheable(value = "article", key = "'findById_' + #id")
	public Article findById(Integer id) {
        return articleRepository.findById(id).orElse(null);
    }
	
	@Cacheable(value = "articles", key = "'findAll'")
	public List<Article> findAll() {
        return articleRepository.findAll();
    }
	
	@Caching(evict = {@CacheEvict(value = "article", key = "'findById_' + #article.id"),
			@CacheEvict(value = "article", key = "'findByIdAndIsShow_' + #article.id + '_true'"),
			@CacheEvict(value = "article", key = "'findByIdAndIsShow_' + #article.id + '_false'"),
			@CacheEvict(value = "articles", allEntries = true),
			@CacheEvict(value = "tags", allEntries = true)})
	public Article save(Article article) {
		Article oldArticle = null;
		if (article.getId() != null) {
			oldArticle = articleRepository.findById(article.getId()).orElse(null);
		}
		
		if (oldArticle != null) {
			article.setDate(oldArticle.getDate());
			article.setClick(oldArticle.getClick());
		} else {
			Date date = new Date();
			article.setDate(date);
			article.setClick(0);
		}
		
        return articleRepository.save(article);
    }
	
	@Caching(evict = {@CacheEvict(value = "article", key = "'findById_' + #id"),
			@CacheEvict(value = "article", key = "'findByIdAndIsShow_' + #id + '_true'"),
			@CacheEvict(value = "article", key = "'findByIdAndIsShow_' + #id + '_false'"),
			@CacheEvict(value = "articles", allEntries = true)})
	public String deleteById(Integer id) {
		if (articleRepository.existsById(id)) {
			articleRepository.deleteById(id);
			if (articleRepository.existsById(id)) {
				return "Fail!";
			} else {
				return "Success!";
			}
		} else {
			return "No article with id " + id + " exists!";
		}
    }
	
	@Cacheable(value = "articles", key = "'count'")
	public long count() {
		return articleRepository.count();
	}
	/* Base */
	
	/* Advance */
	/* find */
	@Cacheable(value = "articles", key = "'countByIsShow_' + #isShow")
	public long countByIsShow(Boolean isShow) {
		System.out.println(isShow);
		return articleRepository.countByIsShow(isShow);
	}
	
	@Cacheable(value = "articles", key = "'findAllByIsShowNoContentNoComment_' + #isShow + '_' + #pageable")
	public Page<Article> findAllByIsShowNCC(Boolean isShow, Pageable pageable) {
        return setCommentNumber(setTags(articleRepository.findByIsShowNCTC(isShow, pageable)));
    }
	
	@Cacheable(value = "article", key = "'findByIdAndIsShow_' + #id + '_' + #isShow")
	public Article findByIdAndIsShow(Integer id, Boolean isShow) {
        Article a = articleRepository.findByIdAndIsShow(id, isShow).orElse(null);
        if (a != null) {
        	a = setCommentNumber(a);
        }
        return a;
    }
	
	@Cacheable(value = "articles", key = "'searchNCC_' + #isShow + '_' + #words + '_' + #pageable")
	public Page<Article> searchNCC(Boolean isShow, String words, Pageable pageable) {
		Page<Article> articlePage = null;
		if (words != null) {
			List<String> wordsList = Arrays.asList(words.split("\\s+"));
			System.out.println(wordsList.toString());
			for (String word : wordsList) {
				System.out.println("." + word + ".");
			}
			articlePage = articleRepository.searchNCTC(isShow, wordsList, pageable);
		} else {
			articlePage =  articleRepository.findByIsShowNCTC(isShow, pageable);
		}
		
		return setCommentNumber(setTags(articlePage));
    }
	
	@Cacheable(value = "articles", key = "'findByIsShowAndTagsIdNCC_' + #isShow + '_' + #id + '_' + #pageable")
	public Page<Article> findByIsShowAndTagsIdNCC(Boolean isShow, Integer id, Pageable pageable) {
        return setCommentNumber(setTags(articleRepository.findByIsShowAndTagsIdNCTC(isShow, id, pageable)));
    }
	
	@Cacheable(value = "articles", key = "'countGroupByMonths_' + #isShow")
	public ArrayList<JSONObject> countGroupByMonths(boolean isShow) {
		return articleRepository.countGroupByMonths(isShow);
	}
	
	@Cacheable(value = "articles", key = "'findByMonthNCC_' + #isShow + '_' + #year + '_' + #month + '_' + #pageable")
	public Page<Article> findByMonthNCC(boolean isShow, int year, int month, Pageable pageable) throws ParseException {
		String start = year + "-" + month + "-01 0:0:0";
		String end = year + "-" + (month + 1) + "-01 0:0:0";
		System.out.println(end);
		return setCommentNumber(articleRepository.findByTimeNCTC(isShow, sdf.parse(start), sdf.parse(end), pageable));
	}
	
	@Cacheable(value = "articles", key = "'findTop3ByIsShowOrderByIdDescOITI_' + #isShow")
	public List<Article> findTop3ByIsShowOrderByIdDescOITI(boolean isShow) {
		Pageable pageable = PageRequest.of(0, 3, Sort.Direction.DESC, "id");
		return articleRepository.findByIsShowOITI(isShow, pageable);
	}
	
	@Cacheable(value = "articles", key = "'getFrontTitle_' + #id")
	public JSONObject getFrontTitle(Integer id) {
		JSONObject j = new JSONObject();
		j.put("id", null);
		j.put("title", null);
		for (int i = id - 1; i > 0; i--) {
			Article a = articleRepository.findByIdAndIsShow(i, true).orElse(null);
			if (a != null) {
				j.put("id", i);
				j.put("title", a.getTitle());
				break;
			}
		}
		return j;
	}
	
	@Cacheable(value = "articles", key = "'getBackTitle_' + #id")
	public JSONObject getBackTitle(Integer id) {
		JSONObject j = new JSONObject();
		j.put("id", null);
		j.put("title", null);
		long count = articleRepository.count();
		for (int i = id + 1; i < count + 1; i++) {
			Article a = articleRepository.findByIdAndIsShow(i, true).orElse(null);
			if (a != null) {
				j.put("id", i);
				j.put("title", a.getTitle());
				break;
			}
		}
		return j;
	}
	/* find */
	
	/* change */
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = {@CacheEvict(value = "article", key = "'findById_' + #id"),
			@CacheEvict(value = "article", key = "'findByIdAndIsShow_' + #id + '_true'"),
			@CacheEvict(value = "article", key = "'findByIdAndIsShow_' + #id + '_false'"),
			@CacheEvict(value = "articles", allEntries = true)})
	public void updateIsShowById(boolean isShow, Integer id) {
		articleRepository.updateIsShowById(isShow, id);
	}
	
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = {@CacheEvict(value = "article", key = "'findById_' + #id"),
			@CacheEvict(value = "article", key = "'findByIdAndIsShow_' + #id + '_true'"),
			@CacheEvict(value = "article", key = "'findByIdAndIsShow_' + #id + '_false'"),
			@CacheEvict(value = "articles", allEntries = true)})
	public void clickAdd(Integer id) {
		articleRepository.clickAdd(id);
	}
	/* change */
	/* Advance */
	
	/* Util */
	private Page<Article> setTags(Page<Article> articlePage) {
		for (Article a : articlePage.getContent()) {
			Set<Tag> tags = new HashSet(tagRepository.findByArticlesId(a.getId()));
			a.setTags(tags);
		}
		return articlePage;
	}
	
	private Page<Article> setCommentNumber(Page<Article> articlePage) {
		for (Article a : articlePage.getContent()) {
			Integer commentNumber = (int) commentRepository.countByArticleId(a.getId());
			a.setCommentNumber(commentNumber);
		}
		return articlePage;
	}
	
	private Article setCommentNumber(Article a) {
		Integer commentNumber = (int) commentRepository.countByArticleId(a.getId());
		a.setCommentNumber(commentNumber);
		return a;
	}
	/* Util */
}
