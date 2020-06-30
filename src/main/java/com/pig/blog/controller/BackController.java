package com.pig.blog.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pig.blog.BlogApplication;
import com.pig.blog.WebMvcConfig;
import com.pig.blog.entity.Article;
import com.pig.blog.service.ArticleService;
import com.pig.blog.service.CommentService;
import com.pig.blog.service.ImageService;
import com.pig.blog.service.TagService;
import com.pig.blog.util.IpUtil;
import com.pig.blog.util.LoginLockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/back")
public class BackController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${back.admin.name}")
	private String adminName;
	@Value("${back.admin.password}")
	private String adminPassword;

	@Autowired
	private ArticleService articleService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private TagService tagService;

	@GetMapping("/loginPage")
	public String loginPage(HttpSession session) {
		session.setAttribute("isMaster", false);
		return "/back/loginPage";
	}

	@PostMapping("/login")
	public String login(HttpServletRequest request, @RequestParam("name") String name, @RequestParam("password") String password, HttpSession session, Model model) {
		session.setAttribute("isMaster", false);
		if (LoginLockUtil.isUnlock()) {
			boolean cond1 = adminName.equals(name);
			boolean cond2 = adminPassword.equals(password);
			if (cond1 && cond2) {
				LoginLockUtil.reSet();
				session.setAttribute("isMaster", true);
				logger.warn("Admin login. IP: " + IpUtil.getIpAddress(request));
				
				String url = (String) session.getAttribute("url");
				if (url != null) {
					return "redirect:" + url;
				} else {
					return "redirect:/back/main";
				}

			} else {
				LoginLockUtil.fail();
				model.addAttribute("msg", "Username and password error!The remaining number of attempts : " + LoginLockUtil.getTimes() + ".");
				logger.warn("Fail login. IP: " + IpUtil.getIpAddress(request));
				return "/back/loginPage";
			}
		} else {
			LoginLockUtil.lock();
			model.addAttribute("msg", "Has been locked!Remaining unlocking time : " + LoginLockUtil.timeToString(LoginLockUtil.getTime()));
			return "/back/loginPage";
		}
	}

	@GetMapping("/main")
	public String main(Model model, @PageableDefault(page = 0, size = 3, sort = { "id" }, direction = Sort.Direction.DESC)Pageable pageable) {
		model.addAttribute("module", "main");
		model.addAttribute("articleSum", articleService.count());
		model.addAttribute("commentSum", commentService.count());
		model.addAttribute("imageSum", imageService.count());
		model.addAttribute("tagSum", tagService.count());
		model.addAttribute("articleShowSum", articleService.countByIsShow(true));
		model.addAttribute("articleHideSum", articleService.countByIsShow(false));
		model.addAttribute("commentIsreadSum", commentService.countByIsShow(true));
		model.addAttribute("commentUnreadSum", commentService.countByIsShow(false));
		model.addAttribute("comments", commentService.findByIsRead(false, pageable));
		model.addAttribute("article", articleService.findById((int) articleService.count()));
		System.out.println(commentService.findByIsRead(false, pageable));
		System.out.println(articleService.findById((int) articleService.count()));
		return "/back/index";
	}
	
	@GetMapping("/writing")
	public String writing(Model model,
			@PageableDefault(size = BlogApplication.imageSize)Pageable pageable) {
		model.addAttribute("module", "writing");
		model.addAttribute("article", new Article());
		model.addAttribute("unUsedTags", tagService.findAll());
		model.addAttribute("images", imageService.findAll(pageable));
		return "/back/index";
	}
	
	@GetMapping("/writing/{id}")
	public String writing(@PathVariable(name = "id")Integer id, Model model,
			@PageableDefault(size = BlogApplication.imageSize)Pageable pageable) {
		model.addAttribute("module", "writing");
		model.addAttribute("article", articleService.findById(id));
		model.addAttribute("unUsedTags", tagService.findByArticleIdNot(id));
		model.addAttribute("images", imageService.findAll(pageable));
		return "/back/index";
	}

	@GetMapping("/articleManager")
	public String articleManager(Model model) {
		model.addAttribute("module", "articleManager");
		model.addAttribute("articleShowSum", articleService.countByIsShow(true));
		model.addAttribute("articleHideSum", articleService.countByIsShow(false));
		model.addAttribute("articles", articleService.findAll());
		return "/back/index";
	}

	@GetMapping("/tagManager")
	public String tagManager(Model model) {
		model.addAttribute("module", "tagManager");
		model.addAttribute("tags", tagService.findAll());
		return "/back/index";
	}

	@GetMapping("/commentManager")
	public String commentManager(Model model) {
		model.addAttribute("module", "commentManager");
		model.addAttribute("commentIsreadSum", commentService.countByIsShow(true));
		model.addAttribute("commentUnreadSum", commentService.countByIsShow(false));
		model.addAttribute("comments", commentService.findAll());
		return "/back/index";
	}

	@GetMapping("/imageManager")
	public String imageManager(Model model) {
		model.addAttribute("module", "imageManager");
		model.addAttribute("images", imageService.findAll());
		return "/back/index";
	}
	
	@GetMapping("/logs")
	public String logs(Model model) {
		String[] visits = new File(WebMvcConfig.FILE_DIR + "logs/visit-logs").list();
		String[] debugs = new File(WebMvcConfig.FILE_DIR + "logs/debug").list();
		String[] errors = new File(WebMvcConfig.FILE_DIR + "logs/error").list();
		String[] infos = new File(WebMvcConfig.FILE_DIR + "logs/info").list();
		String[] warns = new File(WebMvcConfig.FILE_DIR + "logs/warn").list();
		
		model.addAttribute("visits", visits);
		model.addAttribute("debugs", debugs);
		model.addAttribute("errors", errors);
		model.addAttribute("infos", infos);
		model.addAttribute("warns", warns);
		model.addAttribute("module", "logs");
		return "/back/index";
	}
}
