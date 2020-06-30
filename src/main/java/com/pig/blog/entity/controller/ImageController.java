package com.pig.blog.entity.controller;

import java.util.List;

import com.pig.blog.BlogApplication;
import com.pig.blog.entity.Image;
import com.pig.blog.service.ImageService;
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

@Controller
@RequestMapping("/image")
public class ImageController {
	
	@Autowired
    private ImageService imageService;
	
	@GetMapping("/findById/{id}")
	@ResponseBody
	public Object findById(@PathVariable Integer id) {
		return imageService.findById(id);
	}
	
	@GetMapping("/findAll")
	@ResponseBody
	public List<Image> findAll() {
		return imageService.findAll();
	}
	
	@GetMapping("/save")
	@ResponseBody
    public Image save(@ModelAttribute Image image) {
        return imageService.save(image);
    }
	
	@GetMapping("/deleteById/{id}")
	@ResponseBody
	public String deleteById(@PathVariable Integer id) {
		return imageService.deleteById(id);
	}
	
	@GetMapping("/page")
	public String page(@PageableDefault(size = BlogApplication.imageSize) Pageable pageable, Model model) {
		model.addAttribute("images", imageService.findAll(pageable));
		model.addAttribute("url", "/image/page");
		return "/back/common/images";
	}
}
