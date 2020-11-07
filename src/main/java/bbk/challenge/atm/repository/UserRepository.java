package bbk.challenge.atm.repository;

import bbk.challenge.atm.model.Transaction;
import bbk.challenge.atm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByName(String userName);
}