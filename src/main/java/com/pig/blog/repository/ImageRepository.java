package com.pig.blog.repository;

import com.pig.blog.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ImageRepository extends JpaRepository<Image, Integer>{
	
}
