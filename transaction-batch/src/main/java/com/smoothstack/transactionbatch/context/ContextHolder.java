package com.smoothstack.transactionbatch.context;

import java.util.HashMap;
import java.util.Map;

import com.smoothstack.transactionbatch.model.UserBase;

public class ContextHolder {
    private Map<Long, UserBase> users = new HashMap<>();


    public boolean doesUserExist(long id) {
        return users.containsKey(id);
    }

    public void putUser(long id, UserBase user) {
        users.put(id, user);
    }
}
