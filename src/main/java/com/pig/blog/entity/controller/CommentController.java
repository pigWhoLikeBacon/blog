package com.pig.blog.entity.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pig.blog.BlogApplication;
import com.pig.blog.entity.Comment;
import com.pig.blog.service.CommentService;
import com.pig.blog.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/comment")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping("/findById/{id}")
	@ResponseBody
    public Comment find(@PathVariable Integer id) {
        return commentService.findById(id);
    }
	
	@GetMapping("/findAll")
	@ResponseBody
	public List<Comment> findAll() {
		return commentService.findAll();
	}
	
	@RequestMapping("/save")
	@ResponseBody
    public JSONObject save(HttpServletRequest request, @ModelAttribute Comment comment) throws IOException {
		JSONObject json = new JSONObject();
		json.put("success", false);
		if (comment.getName() == null || comment.getName().equals("") || comment.getName().length() > 30) {
			json.put("msg", "名字的字符不能为空或超出30个!");
		} else if (comment.getContent() == null || comment.getContent().equals("") || comment.getContent().length() > 1000) {
			json.put("msg", "内容的字符不能为空或超出1000个!");
		} else if (comment.getEmail().length() > 30) {
			json.put("msg", "Email的字符不能为空或超出30个!");
		} else if (comment.getWebsite().length() > 30) {
			json.put("msg", "网站的字符不能为空或超出30个!");
		} else {
			comment.setIp(IpUtil.getIpAddress(request));
			comment = commentService.save(comment);
			json.put("success", true);
			json.put("msg", "评论发表成功！");
		}
        return json;
    }
	
	@GetMapping("/deleteById/{id}")
	@ResponseBody
	public String deleteById(@PathVariable Integer id) {
		return commentService.deleteById(id);
	}
	
	@GetMapping("/show/{id}")
	@ResponseBody
    public String show(@PathVariable(name = "id")Integer id) {
		commentService.updateIsShowById(true, id);
		return "Success!";
    }
	
	@GetMapping("/hide/{id}")
	@ResponseBody
    public String hide(@PathVariable(name = "id")Integer id) {
		commentService.updateIsShowById(false, id);
		return "Success!";
    }
	
	@GetMapping("/read/{id}")
	@ResponseBody
    public String read(@PathVariable(name = "id")Integer id) {
		commentService.updateIsReadById(true, id);
		return "Success!";
    }
	
	@GetMapping("/readAll")
	@ResponseBody
    public String readAll() {
		commentService.updateIsReadAll(true);
		return "Success!";
    }
	
	@GetMapping("/page")
	public String page(Model model, @RequestParam Integer articleId,
			@PageableDefault(size = BlogApplication.commentSize, sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable) {
		model.addAttribute("comments", commentService.findByArticleIdAndIsShow(articleId, true, pageable));
		model.addAttribute("url", "/comment/page");
		model.addAttribute("articleId", articleId);
		return "/front/common/comments";
	}
}
