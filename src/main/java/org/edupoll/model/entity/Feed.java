package org.edupoll.model.entity;

import java.util.Collection;
import java.util.List;

import org.edupoll.model.dto.FeedAttachWrapper;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

//피드는 본문 어태치는 파일 주소들

@Entity
@Table(name = "feeds")
@Setter
@Getter
public class Feed {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id; // 기본 키

	@ManyToOne
	// @JoinColumn(name = "writerId",referencedColumnName = "email") 참고삼아 보고 패스
	// 이 값을 이용해서 애를 찾아야 하는데 기본적으로 아이디가 같은애를 찾는단 말이야
	// 이메일을 저장했는데 못찾으니 수정. 반대쪽에서 참조해야될 컬럼네임을(User) 유저객체의 email 이 같은애를 찾는다.
	@JoinColumn(name = "writerId")
	private User writer; // 작성자

	private String description; // 본문

	private Long viewCount;

	// 여러개의 FeedAttach 가질 수 있잖아
	@OneToMany(mappedBy = "feed")
	private List<FeedAttach> attaches;
	// 피트 어태치에서 피드가 나로 되어있는 애들 가져오겠다.

}
