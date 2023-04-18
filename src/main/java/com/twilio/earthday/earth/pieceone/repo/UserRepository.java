package com.twilio.earthday.earth.pieceone.repo;

import com.twilio.earthday.earth.pieceone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByPhoneNumber(String phoneNumber);

}
