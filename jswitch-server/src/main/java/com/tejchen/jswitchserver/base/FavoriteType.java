package com.tejchen.jswitchserver.base;

public enum  FavoriteType {

    APP;

    public static FavoriteType find(String favoriteType){
        return valueOf(favoriteType);
    }
}
