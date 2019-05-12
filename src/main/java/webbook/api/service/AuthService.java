package webbook.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import webbook.api.config.AuthSingleton;
import webbook.api.dto.AuthLoginDTO;
import webbook.api.model.AuthToken;
import webbook.api.model.User;
import webbook.api.repository.AuthTokenRepository;
import webbook.api.util.UUIDGeneratorUtil;

@Service
public class AuthService {
    @Autowired
    private AuthTokenRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthToken getByToken(String token) {
        return repository.findByToken(token);
    }

    public void registerAuthToken(AuthToken authToken){
        AuthSingleton.setAuthToken(authToken);
    }

    public Boolean validatePassword (AuthLoginDTO authLoginDTO, User user) {
        return passwordEncoder.matches(authLoginDTO.password, user.getPassword());
    }

    public AuthToken getActiveAuthTokenOfUser(User user) {
        return repository.findByUserAndActive(user, true);
    }

    public void invalidateAuthToken(AuthToken authToken) {
        authToken.setActive(false);
        repository.save(authToken);
    }

    public AuthToken createAuthToken(User user){
        AuthToken authToken = new AuthToken();
        authToken.setUser(user);
        authToken.setActive(true);
        authToken.setToken(UUIDGeneratorUtil.get());

        return repository.save(authToken);
    }
}