package com.redhood.issuetracker.service.account.auth;

import com.redhood.issuetracker.repository.account.auth.entity.Auth;

import java.util.List;

public interface AuthService {

    public List<Auth> findAll();

    public Auth findById(int id);

    public void save(Auth auth);

    public void deleteById(int id);

}
