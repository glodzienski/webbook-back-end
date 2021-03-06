package webbook.api.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import webbook.api.model.entity.User;
import webbook.api.rest.repository.UserRepository;
import webbook.api.core.helper.CpfHelper;
import webbook.api.core.helper.UUIDGeneratorHelper;

@Service
public class UserService implements ApiCrudServiceContract<User> {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User store(User user) {
        user.setCode(UUIDGeneratorHelper.get());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }

    @Override
    public User update(User currentUser, User requestUser) {
        currentUser.setBirthdayDate(requestUser.getBirthdayDate());
        currentUser.setCpf(requestUser.getCpf());
        currentUser.setName(requestUser.getName());
        currentUser.setLastName(requestUser.getLastName());
        currentUser.setPhotoUrl(requestUser.getPhotoUrl());

        User userSaved = repository.save(currentUser);
        userSaved.setPassword("");

        return userSaved;
    }

    @Override
    public Iterable<User> list() {
        return repository.findAll();
    }

    @Override
    public void destroy(User user) {
        repository.delete(user);
    }

    @Override
    public User getById(int id) {
        return null;
    }

    @Override
    public User getByCode(String code) {
        User user = repository.findByCode(code);
        user.setPassword("");

        return user;
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User getByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    public void validateUserInfo(User user, Boolean isEditing){
        User currentUser = this.getByEmail(user.getEmail());
        if ((currentUser != null)
                && (isEditing && !(user.getEmail().equals(currentUser.getEmail())))) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Usuário com email " + user.getEmail() + " já possui conta no sistema.");
        }
        if (!CpfHelper.isValid(user.getCpf())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "CPF " + user.getCpf() + " inválido.");
        }
        currentUser = this.getByCpf(user.getCpf());
        if ((currentUser != null)
                && (isEditing && !(user.getCpf().equals(currentUser.getCpf())))) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Usuário com CPF " + user.getCpf() + " já possui conta no sistema.");
        }
    }
}
