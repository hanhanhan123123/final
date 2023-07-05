package org.edupoll.controller;

import java.util.List;

import org.edupoll.model.dto.FeedWrapper;
import org.edupoll.model.dto.request.CreateFeedRequest;
import org.edupoll.model.dto.response.FeedListResponse;
import org.edupoll.repository.FeedRepository;
import org.edupoll.service.FeedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
@CrossOrigin
public class FeedController {
	private final FeedService feedService;
	private final FeedRepository feedRepository;

	// =============================================================
	// 신규 글 등록해주는 API
	@PostMapping("/storage")
	public ResponseEntity<?> createNewFeedHandle(
			@AuthenticationPrincipal String principal, CreateFeedRequest request)
			throws Exception {
		boolean r = feedService.create(principal, request); //서비스로 넘겨서 업로드작업

		return new ResponseEntity<>(HttpStatus.CREATED);//보통 들어갈떈 create 한다
	}

	// =============================================================
	// 전체 글(or 특정조건의) 목록 제공해주는 API
		@GetMapping("/storage")
		public ResponseEntity<?> readAllFeedHandle(@RequestParam(defaultValue = "1")int page) {
			
			Long total=feedService.size();
			List<FeedWrapper> feeds = feedService.allItems(page);
			
			FeedListResponse response = new FeedListResponse(total, feeds);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	
	// =============================================================
	// 특정 글 제공해주는 API
	public ResponseEntity<?> readSpecificFeedHandleA(@RequestParam String id) {
		return null;
	}

	public ResponseEntity<?> readSpecificFeedHandleB(@PathVariable String id) {
		return null;
	}

	// =============================================================
	// 특정 글 삭제해주는 API
	public ResponseEntity<?> deleteSpecificFeedHandle() {
		return null;
	}

}
