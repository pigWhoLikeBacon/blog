package com.pig.blog.entity.controller;

import java.text.ParseException;
import java.util.List;

import com.pig.blog.BlogApplication;
import com.pig.blog.entity.Article;
import com.pig.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/findById/{id}")
	@ResponseBody
	public Object findById(@PathVariable Integer id) {
		Article article = articleService.findById(id);
		article.setComments(null);
		return article;
	}
	
	@GetMapping("/findAll")
	@ResponseBody
	public List<Article> findAll() {
		return articleService.findAll();
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public JSONObject save(@ModelAttribute Article article) {
		JSONObject json = new JSONObject();
		json.put("success", false);
		if (article.getTitle().length() > 100) {
			json.put("msg", "Title beyond 100!");
		} else if (article.getContent().length() > 10000) {
			json.put("msg", "Content beyond 10000!");
		} else if (article.getImageUrl().length() > 100) {
			json.put("msg", "ImageUrl beyond 100!");
		} else if (article.getIntroduce().length() > 200) {
			json.put("msg", "Introduce beyond 200!");
		} else {
			article = articleService.save(article);
			json.put("success", true);
			json.put("msg", article.getId());
		}
		return json;
	}
	
	@GetMapping("/deleteById/{id}")
	@ResponseBody
	public String deleteById(@PathVariable Integer id) {
		return articleService.deleteById(id);
	}
	
	@GetMapping("/show/{id}")
	@ResponseBody
    public void show(@PathVariable Integer id) {
        articleService.updateIsShowById(true, id);
    }
	
	@GetMapping("/hide/{id}")
	@ResponseBody
    public void hide(@PathVariable Integer id) {
        articleService.updateIsShowById(false, id);
    }
	
	@GetMapping("/mainPage")
	public String mainPage(Model model, @RequestParam(required=false) String sort,
			@PageableDefault(size = BlogApplication.articleSize)Pageable pageable) {
		model.addAttribute("articles", articleService.findAllByIsShowNCC(true, pageable));
		model.addAttribute("url", "/article/mainPage");
		model.addAttribute("sort", sort);
		model.addAttribute("otherUrl", "");
		return "/front/common/articles";
	}
	
	@GetMapping("/searchPage")
	public String searchPage(Model model, @RequestParam(required=false) String words,
			@RequestParam(required=false) String sort, @PageableDefault(size = BlogApplication.articleSize)Pageable pageable) {
		model.addAttribute("articles", articleService.searchNCC(true, words, pageable));
		model.addAttribute("url", "/article/searchPage");
		model.addAttribute("sort", sort);
		model.addAttribute("otherUrl", "&words=" + words);
		return "/front/common/articles";
	}
	
	@GetMapping("/groupByTagPage")
	public String groupByTagPage(Model model, @RequestParam Integer tagId,
			@RequestParam(required=false) String sort, @PageableDefault(size = BlogApplication.articleSize)Pageable pageable) {
		model.addAttribute("articles", articleService.findByIsShowAndTagsIdNCC(true, tagId, pageable));
		model.addAttribute("url", "/article/groupByTagPage");
		model.addAttribute("sort", sort);
		model.addAttribute("otherUrl", "&tagId=" + tagId);
		return "/front/common/articles";
	}
	
	@GetMapping("/groupByTimePage")
	public String groupByTimePage(Model model, @RequestParam Integer year, @RequestParam Integer month,
			@RequestParam(required=false) String sort, @PageableDefault(size = BlogApplication.articleSize)Pageable pageable) throws ParseException {
		model.addAttribute("articles", articleService.findByMonthNCC(true, year, month, pageable));
		model.addAttribute("url", "/article/groupByTimePage");
		model.addAttribute("sort", sort);
		model.addAttribute("otherUrl", "&year=" + year + "&month=" + month);
		return "/front/common/articles";
	}
}
