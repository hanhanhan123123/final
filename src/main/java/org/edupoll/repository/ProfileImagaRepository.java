package org.edupoll.repository;

import java.util.Optional;

import org.edupoll.model.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImagaRepository extends JpaRepository<ProfileImage, Long>{
	Optional<ProfileImage> findTop1ByUrl(String url);
}
