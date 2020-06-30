package com.pig.blog.service;

import java.util.List;

import com.pig.blog.entity.Tag;
import com.pig.blog.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public class TagService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TagRepository tagRepository;
	
	/* Base */
	@Cacheable(value = "tag", key = "'findById_' + #id")
	public Tag findById(Integer id) {
		return tagRepository.findById(id).orElse(null);
	}
	
	@Cacheable(value = "tags", key = "'findAll'")
	public List<Tag> findAll() {
		return tagRepository.findAll();
	}
	
	@Caching(evict = {@CacheEvict(value = "articles", allEntries = true),
			@CacheEvict(value = "article", allEntries = true),
			@CacheEvict(value = "tag", key = "'findById_' + #tag.id"),
			@CacheEvict(value = "tags", allEntries = true)})
	public Tag save(Tag tag) {
		System.out.println("hhd");
		return tagRepository.save(tag);
	}
	
	@Caching(evict = {@CacheEvict(value = "tag", key = "'findById_' + #id"),
			@CacheEvict(value = "tags", allEntries = true)})
	public String deleteById(Integer id) {
		if (tagRepository.existsById(id)) {
			tagRepository.deleteById(id);
			if (tagRepository.existsById(id)) {
				return "Fail!";
			} else {
				return "Success!";
			}
		} else {
			return "No tag with id " + id + " exists!";
		}
    }
	
	@Cacheable(value = "tags", key = "'count'")
	public long count() {
		return tagRepository.count();
	}
	/* Base */
	
	/* Advance */
	@Cacheable(value = "tags", key = "'findByArticleIdNot_' + #articleId")
	public List<Tag> findByArticleIdNot(Integer articleId) {
		return tagRepository.findByArticleIdNot(articleId);
	}
	/* Advance */
}
