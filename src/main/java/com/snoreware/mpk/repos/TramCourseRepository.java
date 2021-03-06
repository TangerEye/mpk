package com.snoreware.mpk.repos;

import com.snoreware.mpk.entities.TramCourseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TramCourseRepository extends CrudRepository<TramCourseEntity, UUID> {
    List<TramCourseEntity> findAllByOrderByCourseIdDesc();

    TramCourseEntity findByCourseId(UUID courseId);
}
