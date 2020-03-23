package com.tejchen.jswitchserver.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tejchen.jswitchserver.base.FavoriteType;
import com.tejchen.jswitchserver.mapper.JSwitchUser;
import com.tejchen.jswitchserver.mapper.JSwitchUserFavorite;
import com.tejchen.jswitchserver.mapper.JSwitchUserFavoriteMapper;
import com.tejchen.jswitchserver.mapper.JSwitchUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JSwitchUserFavoriteServiceImpl extends ServiceImpl<JSwitchUserFavoriteMapper, JSwitchUserFavorite> implements JSwitchUserFavoriteService {
    private static final Logger logger = LoggerFactory.getLogger(JSwitchUserFavoriteServiceImpl.class);

    @Override
    public List<JSwitchUserFavorite> list(String userId, FavoriteType favoriteType, List<String> objects) {
        LambdaQueryWrapper<JSwitchUserFavorite> wrapper = Wrappers.<JSwitchUserFavorite>lambdaQuery()
                .eq(JSwitchUserFavorite::getUserId, userId)
                .eq(JSwitchUserFavorite::getFavoriteType, favoriteType);
        if (objects != null && !objects.isEmpty()){
            wrapper.in(JSwitchUserFavorite::getFavoriteObject, objects);
        }
        return list(wrapper);
    }
}
