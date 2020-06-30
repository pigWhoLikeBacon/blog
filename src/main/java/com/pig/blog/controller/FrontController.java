package com.pig.blog.controller;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import com.pig.blog.BlogApplication;
import com.pig.blog.entity.Article;
import com.pig.blog.service.ArticleService;
import com.pig.blog.service.CommentService;
import com.pig.blog.service.ImageService;
import com.pig.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FrontController {
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private TagService tagService;
	
	@Value("${front.article-size}")
    private Integer articleSize;
	
	@GetMapping("/robots.txt")
	public void robotsTxt(HttpServletResponse response) throws IOException {
		Writer writer = response.getWriter();
		String lineSeparator = System.getProperty("line.separator", "\n");
		writer.append("User-agent: *").append(lineSeparator);
		writer.append("Allow: ").append("/").append(lineSeparator);
	}

	@GetMapping({"/main", ""})
	public String main(Model model,
			@PageableDefault(page = 0, size = BlogApplication.articleSize, sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable) {
		model.addAttribute("module", "main");
		
		/* public */
		modelSetPublic(model);
		/* public */
		
		model.addAttribute("url", "/article/mainPage");
		model.addAttribute("articles", articleService.findAllByIsShowNCC(true, pageable));
		model.addAttribute("sort", "id,DESC");
		model.addAttribute("otherUrl", "");
		return "/front/list";
	}
	
	@GetMapping("/search")
	public String search(Model model, @RequestParam(required=false) String words,
			@PageableDefault(page = 0, size =BlogApplication.articleSize, sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable) {
		model.addAttribute("title", " - 搜索： " + words);
		model.addAttribute("listTop", "搜索： " + words);
		
		/* public */
		modelSetPublic(model);
		/* public */
		
		model.addAttribute("url", "/article/searchPage");
		model.addAttribute("articles", articleService.searchNCC(true, words, pageable));
		model.addAttribute("sort", "id,DESC");
		model.addAttribute("otherUrl", "&words=" + words);
		return "/front/list";
	}
	
	@GetMapping("/groupByTag")
	public String groupByTag(Model model, @RequestParam Integer tagId,
			@PageableDefault(page = 0, size = BlogApplication.articleSize, sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable) {
		if (tagId == 1) {
			model.addAttribute("module", "skill");
		} else if (tagId == 2) {
			model.addAttribute("module", "tools");
		} else if (tagId == 3) {
			model.addAttribute("module", "take");
		}
		model.addAttribute("title", " - 标签： " + tagService.findById(tagId).getName());
		model.addAttribute("listTop", "标签： " + tagService.findById(tagId).getName());
		
		/* public */
		modelSetPublic(model);
		/* public */
		
		model.addAttribute("url", "/article/groupByTagPage");
		model.addAttribute("articles", articleService.findByIsShowAndTagsIdNCC(true, tagId, pageable));
		model.addAttribute("sort", "id,DESC");
		model.addAttribute("otherUrl", "&tagId=" + tagId);
		return "/front/list";
	}
	
	@GetMapping("/groupByTime")
	public String groupByTime(Model model, @RequestParam Integer year, @RequestParam Integer month,
			@PageableDefault(page = 0, size = BlogApplication.articleSize, sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable) throws ParseException {
		model.addAttribute("title", " - 归档： " + year + "年 " + month + "月");
		model.addAttribute("listTop", year + "年 " + month + "月");
		
		/* public */
		modelSetPublic(model);
		/* public */
		
		model.addAttribute("url", "/article/groupByTimePage");
		model.addAttribute("articles", articleService.findByMonthNCC(true, year, month, pageable));
		model.addAttribute("sort", "id,DESC");
		model.addAttribute("otherUrl", "&year=" + year + "&month=" + month);
		return "/front/list";
	}
	
	@GetMapping("/entity/{id}")
	public String entity(Model model, @PathVariable Integer id,
			@PageableDefault(page = 0, size = BlogApplication.commentSize, sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable) {
		
		Article article = articleService.findByIdAndIsShow(id, true);
		
		articleService.clickAdd(id);
		
		model.addAttribute("title", " - 文章： " + article.getTitle());
		
		if (id == 1) {
			model.addAttribute("module", "notice");
		} else if (id == 2) {
			model.addAttribute("module", "about");
		}
		
		/* public */
		modelSetPublic(model);
		/* public */
		
		model.addAttribute("front", articleService.getFrontTitle(id));
		model.addAttribute("back", articleService.getBackTitle(id));
		model.addAttribute("url", "/comment/page");
		model.addAttribute("articleId", id);
		model.addAttribute("article", article);
		model.addAttribute("comments", commentService.findByArticleIdAndIsShow(id, true, pageable));
		return "/front/entity";
	}
	
	private Model modelSetPublic(Model model) {
		model.addAttribute("tags", tagService.findAll());
		model.addAttribute("noticeArticle", articleService.findById(1));
		model.addAttribute("newArticles", articleService.findTop3ByIsShowOrderByIdDescOITI(true));
		model.addAttribute("countGroupByMonths", articleService.countGroupByMonths(true));
		return model;
	}
}
