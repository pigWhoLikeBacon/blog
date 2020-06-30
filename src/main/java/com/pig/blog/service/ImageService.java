package com.pig.blog.service;

import java.util.List;

import com.pig.blog.entity.Image;
import com.pig.blog.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ImageRepository imageRepository;
	
	/* Base */
	@Cacheable(value = "image", key = "'findById_' + #id")
	public Image findById(Integer id) {
		return imageRepository.findById(id).orElse(null);
	}
	
	@Cacheable(value = "images", key = "'findByAll'")
	public List<Image> findAll() {
		return imageRepository.findAll();
	}
	
	@Caching(evict = {@CacheEvict(value = "image", key = "'findById_' + #image.id"),
			@CacheEvict(value = "images", allEntries = true)})
	public Image save(Image image) {
		return imageRepository.save(image);
	}
	
	@Caching(evict = {@CacheEvict(value = "image", key = "'findById_' + #id"),
			@CacheEvict(value = "images", allEntries = true)})
	public String deleteById(Integer id) {
		if (imageRepository.existsById(id)) {
			imageRepository.deleteById(id);
			if (imageRepository.existsById(id)) {
				return "Fail!";
			} else {
				return "Success!";
			}
		} else {
			return "No image with id " + id + " exists!";
		}
    }
	
	@Cacheable(value = "images", key = "'count'")
	public long count() {
		return imageRepository.count();
	}
	/* Base */
	
	/* Advance */
	@Cacheable(value = "images", key = "'findAll_' + #pageable")
	public Page<Image> findAll(Pageable pageable) {
		return imageRepository.findAll(pageable);
	}
	/* Advance */
}
