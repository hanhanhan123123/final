package org.edupoll.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

//사진이 주가 될 것이기 때문에! FeedAttach 만든다

@Entity
@Setter
@Getter
@Table(name = "feedAttachs")
public class FeedAttach {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne // 한 게시글에 사진이 여러개 들어갈 수 있기 때문에 배열처럼 쓸 것이다.
	@JoinColumn(name = "feedId")
	private Feed feed;

	private String type; // 멀티파트에서 겟컨텐츠타입하게되면 사용자가 올렸던 파일이 뭔지 나오잖아 그걸 저장

	private String mediaUrl;// 사진의 주소 저장 (업로드 해야되기 때문에)



	}
