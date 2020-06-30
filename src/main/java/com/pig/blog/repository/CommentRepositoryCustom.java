package com.pig.blog.repository;

public interface CommentRepositoryCustom {
	
	/**
	 * Because of "@ManyToOne", delete() is unuseful, so delete entity please use this
	 * method.
	 * 
	 * @param id
	 * @return
	 */
	void removeById(Integer id);
}
