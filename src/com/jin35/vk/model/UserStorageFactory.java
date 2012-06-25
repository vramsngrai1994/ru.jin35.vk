package com.jin35.vk.model;

public class UserStorageFactory {

    private static UserStorageFactory instance;

    private UserStorageFactory() {
    }

    public static UserStorageFactory getInstance() {
        if (instance == null) {
            instance = new UserStorageFactory();
        }
        return instance;
    }

    public IUserStorage getUserStorage() {
        return UserStorage.getInstance();
    }

}
