package com.pig.blog.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pig.blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;

public interface ArticleRepositoryCustom {
	
	Page<Article> searchNCTC(boolean isShow, List<String> words, Pageable pageable);
	
	Page<Article> findByTimeNCTC(boolean isShow, Date start, Date end, Pageable pageable);
	
	ArrayList<JSONObject> countGroupByMonths(boolean isShow);
}
