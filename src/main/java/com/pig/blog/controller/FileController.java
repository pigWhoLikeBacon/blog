package com.pig.blog.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.pig.blog.WebMvcConfig;
import com.pig.blog.entity.Image;
import com.pig.blog.service.ImageService;
import com.pig.blog.util.FileUtil;
import com.pig.blog.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/file")
public class FileController {
	
	@Autowired
    private ImageService imageService;

	@RequestMapping("/upload")
	@ResponseBody
	@Transactional(rollbackOn = Exception.class)
	public Object upload(@RequestParam("file") MultipartFile uploadFile, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 获取文件后缀
		String fileSuffix = FileUtil.getSuffix(uploadFile.getOriginalFilename());

		boolean cond1 = fileSuffix.equals(".jpg");
		boolean cond2 = fileSuffix.equals(".png");
		boolean cond3 = fileSuffix.equals(".gif");
		
		if (cond1 || cond2 || cond3) {
			byte[] content = uploadFile.getBytes();
			// 保存文件到具体目录，此处为root/violet/upload
			String path = WebMvcConfig.FILE_DIR + "upload/";
			File folder = new File(path);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			// 设置文件名
			File file = new File(folder.getAbsolutePath() + File.separator + UUID.randomUUID().toString() + fileSuffix);
			file.createNewFile();
			// 写到服务器文件
			FileUtil.writeFile(file, content);

			ImageUtil.thumd(file);
			
			Image image = new Image();
			image.setName(uploadFile.getOriginalFilename());
			image.setUrl("/upload/" + file.getName());
			image = imageService.save(image);
			
			JSONObject json = new JSONObject();
			json.put("append", false);
			//String img = "<img src=" + "'/upload/" + file.getName() + "'"
			//		+ " class='file-preview-image kv-preview-data' style='width:auto;height:auto;max-width:100%;max-height:100%;'>";
			//json.put("initialPreview", img);
			//JSONObject json2 = new JSONObject();
			//json2.put("url", "/image/delete/" + image.getId());
			//json.put("initialPreviewConfig", json2);
			//JSONObject obj = JSON.parseObject("{initialPreviewConfig:[{url: 'http://localhost/avatar/delete',key: fileid}],append: true}");

			// response.getWriter().write("/upload/" + file.getName());
			return json;
		} else {
			return "Not image! plase upload image.";
		}
	}
}
