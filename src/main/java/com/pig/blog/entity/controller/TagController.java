package com.pig.blog.entity.controller;

import java.io.IOException;
import java.util.List;

import com.pig.blog.entity.Tag;
import com.pig.blog.service.ArticleService;
import com.pig.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/tag")
public class TagController {
	
	@Autowired
	private ArticleService articleService;
	@Autowired
    private TagService tagService;
	
	@GetMapping("/findById/{id}")
	@ResponseBody
	public Object findById(@PathVariable Integer id) {
		return tagService.findById(id);
	}
	
	@GetMapping("/findAll")
	@ResponseBody
	public List<Tag> findAll() {
		return tagService.findAll();
	}
	
	@RequestMapping("/save")
	@ResponseBody
    public JSONObject save(@ModelAttribute Tag tag) throws IOException {
		//System.out.println(tag.getName());
		JSONObject json = new JSONObject();
		json.put("success", false);
		if (tag.getName().length() > 100) {
			json.put("msg", "Name beyond 100!");
		} else if (tag.getColor().length() > 100) {
			json.put("msg", "Color beyond 100!");
		} else {
			tag = tagService.save(tag);
			json.put("success", true);
			json.put("msg", tag.getId());
		}
        return json;
    }
	
	@GetMapping("/deleteById/{id}")
	@ResponseBody
	public String deleteById(@PathVariable Integer id) {
		return tagService.deleteById(id);
	}
	
	@GetMapping(value = {"/unUsedTags/{id}", "/unUsedTags/"})
	public String unUsedTags(@PathVariable(required = false) Integer id, Model model) {
		if (id == null) {
			model.addAttribute("tags", tagService.findAll());
		} else {
			model.addAttribute("tags", tagService.findByArticleIdNot(id));
		}
		return "/back/common/tags";
	}
	
	@GetMapping(value = {"/usedTags/{id}", "/usedTags/"})
	public String usedTags(@PathVariable(required = false) Integer id, Model model) {
		if (id == null) {
			model.addAttribute("tags", null);
		} else {
			model.addAttribute("tags", articleService.findById(id).getTags());
		}
		return "/back/common/tags";
	}
}
