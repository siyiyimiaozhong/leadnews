package com.siyi.media.controller.v1;

import com.siyi.common.media.constans.WmMediaConstans;
import com.siyi.media.apis.NewsControllerApi;
import com.siyi.media.service.NewsService;
import com.siyi.model.common.dtos.ResponseResult;
import com.siyi.model.media.dtos.WmNewsDto;
import com.siyi.model.media.dtos.WmNewsPageReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/media/news")
public class NewsController implements NewsControllerApi {

	@Autowired
	private NewsService newsService;

	@PostMapping("/submit")
	@Override  
	public ResponseResult summitNews(@RequestBody WmNewsDto wmNews) {
		return newsService.saveNews(wmNews, WmMediaConstans.WM_NEWS_SUMMIT_STATUS);
	}  


	@PostMapping("/save_draft")  
	@Override  
	public ResponseResult saveDraftNews(@RequestBody WmNewsDto wmNews) {  
		return newsService.saveNews(wmNews, WmMediaConstans.WM_NEWS_DRAFT_STATUS);  
	}

	@PostMapping("/list")
	@Override
	public ResponseResult listByUser(@RequestBody WmNewsPageReqDto dto) {
		return newsService.listByUser(dto);
	}

	@PostMapping("/news")
	@Override
	public ResponseResult wmNews(@RequestBody WmNewsDto dto) {
		return newsService.findWmNewsById(dto);
	}

	@PostMapping("/del_news")
	@Override
	public ResponseResult delNews(@RequestBody WmNewsDto dto) {
		return newsService.delNews(dto);
	}
}