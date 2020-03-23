package com.tejchen.jswitchserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tejchen.jswitchserver.base.FavoriteType;
import com.tejchen.jswitchserver.mapper.JSwitchUser;
import com.tejchen.jswitchserver.mapper.JSwitchUserFavorite;

import java.util.List;

public interface JSwitchUserFavoriteService extends IService<JSwitchUserFavorite> {

    List<JSwitchUserFavorite> list(String userId, FavoriteType type, List<String> objects);
}
