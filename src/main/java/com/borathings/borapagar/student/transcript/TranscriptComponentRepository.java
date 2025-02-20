package com.borathings.borapagar.student.transcript;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscriptComponentRepository extends JpaRepository<TranscriptComponentEntity, Long> {}
