package bbk.challenge.atm.service;

import bbk.challenge.atm.model.User;
import bbk.challenge.atm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public Optional<User> findUserByName(String userName) {
        return userRepository.findUserByName(userName);
    }
}
