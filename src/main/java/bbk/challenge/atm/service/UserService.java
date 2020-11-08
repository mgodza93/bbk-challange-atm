package bbk.challenge.atm.service;

import bbk.challenge.atm.authentication.PasswordEncrypt;
import bbk.challenge.atm.model.User;
import bbk.challenge.atm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncrypt passwordEncrypt;

    private ConcurrentMap<Long, User> idToUser = new ConcurrentHashMap<>();

    @Scheduled(fixedDelay = 10_000)
    private void syncWithDb() {

        ConcurrentMap<Long, User> idToUserTemporary = new ConcurrentHashMap<>();

        userRepository.findAll().forEach(user -> idToUserTemporary.put(user.getId(), user.clone()));

        idToUser = idToUserTemporary;
    }

    public User saveUser(User user) throws NoSuchAlgorithmException {

        user.setPassword(passwordEncrypt.encryptPassword(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return idToUser.get(id);
    }

    public Optional<User> findUserByName(String userName) {

        return idToUser.values().stream().filter(user -> Objects.equals(user.getName(), userName)).findFirst();
    }

    public Collection<User> getAllUsers() {
        return idToUser.values();
    }
}