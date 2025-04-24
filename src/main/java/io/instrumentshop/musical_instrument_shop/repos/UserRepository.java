package io.instrumentshop.musical_instrument_shop.repos;

import io.instrumentshop.musical_instrument_shop.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, UUID> {

    User findByNameAndPassword(String userName, String password);
}
