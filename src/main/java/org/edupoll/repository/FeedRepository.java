package org.edupoll.repository;

import org.edupoll.model.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long>, ListCrudRepository<Feed, Long> {
	
}
