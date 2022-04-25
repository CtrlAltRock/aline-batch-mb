package com.smoothstack.transactionbatch.context;

import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;

import com.smoothstack.transactionbatch.model.UserBase;

public class ContextHolder {
    private AbstractMap<Long, UserBase> users = new ConcurrentHashMap<>();

    public boolean doesUserExist(long id) {
        return users.containsKey(id);
    }

    public void putUser(long id, UserBase user) {
        users.put(id, user);
    }

    public UserBase getUser(long id) {
        return users.get(id);
    }
}
