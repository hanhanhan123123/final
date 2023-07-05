package org.edupoll.model.dto;

import java.util.List;

import org.edupoll.model.entity.Feed;

import lombok.Data;

@Data
public class FeedWrapper {

	private Long id;
	
	private UserWrapper writer;
	
	private String description;
	private Long viewCount;
	
	private List<FeedAttachWrapper> attaches;
	
	public FeedWrapper(Feed e) {
		this.id = e.getId();
		this.description = e.getDescription();
		this.viewCount = e.getViewCount();
		this.writer = new UserWrapper(e.getWriter());
		
		this.attaches = e.getAttaches()
					.stream().map(item->new FeedAttachWrapper(item)).toList();
		
	}
}
