package com.twilio.earthday.earth.pieceone.repository;

import com.twilio.earthday.earth.pieceone.model.EcoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcoTaskRepository extends JpaRepository<EcoTask, Long> {

}
