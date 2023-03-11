package com.redhood.issuetracker.service.account.auth;

import com.redhood.issuetracker.repository.account.auth.repository.AuthRepository;
import com.redhood.issuetracker.repository.account.auth.entity.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    private AuthRepository authRepository;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------------------------------
    @Autowired
    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // AuthService interface
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public List<Auth> findAll() {
        return authRepository.findAll();
    }

    @Override
    public Auth findById(int id) {
        Optional<Auth> result = authRepository.findById(id);
        Auth auth = null;
        if (result.isPresent()) { auth = result.get(); }
        return auth;
    }

    @Override
    public void save(Auth auth) {
        authRepository.save(auth);
    }

    @Override
    public void deleteById(int id) {
        authRepository.deleteById(id);
    }
    //------------------------------------------------------------------------------------------------------------------
}
