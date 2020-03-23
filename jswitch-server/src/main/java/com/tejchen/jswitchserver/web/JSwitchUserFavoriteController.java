package com.tejchen.jswitchserver.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.FavoriteType;
import com.tejchen.jswitchserver.base.JSwitchWebEvent;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.helper.ResponseHelper;
import com.tejchen.jswitchserver.mapper.JSwitchApp;
import com.tejchen.jswitchserver.mapper.JSwitchAppNode;
import com.tejchen.jswitchserver.mapper.JSwitchUserFavorite;
import com.tejchen.jswitchserver.model.JSwitchAppForm;
import com.tejchen.jswitchserver.model.JSwitchAppListVO;
import com.tejchen.jswitchserver.model.JSwitchAppVO;
import com.tejchen.jswitchserver.service.JSwitchAppNodeService;
import com.tejchen.jswitchserver.service.JSwitchAppService;
import com.tejchen.jswitchserver.service.JSwitchUserFavoriteService;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jswitch/user/favorite")
public class JSwitchUserFavoriteController {

    @Autowired
    private JSwitchAppService appService;

    @Autowired
    private JSwitchUserFavoriteService favoriteService;

    @RequestMapping("/add")
    public JSwitchHttpResponse add(HttpServletRequest request, String favoriteType, String favoriteObject){
        FavoriteType type = FavoriteType.find(favoriteType);
        if (type == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        existRequired(type, favoriteObject);
        favoriteNotExistRequired("admin", type, favoriteObject);
        // 新增关注
        JSwitchUserFavorite favorite = new JSwitchUserFavorite();
        favorite.setUserId("admin");
        favorite.setFavoriteType(favoriteType);
        favorite.setFavoriteObject(favoriteObject);
        favoriteService.save(favorite);
        return ResponseHelper.success();
    }

    private void existRequired(FavoriteType type, String object){
        if (type == FavoriteType.APP){
            JSwitchApp app = appService.get(object);
            if (app == null){
                ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
            }
            return;
        }
        ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
    }

    @RequestMapping("/remove")
    public JSwitchHttpResponse remove(HttpServletRequest request, String favoriteType, String favoriteObject){
        FavoriteType type = FavoriteType.find(favoriteType);
        if (type == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        // 检查是否存在
        favoriteExistRequired("admin", type, favoriteObject);
        // 新增关注
        favoriteService.remove(Wrappers.<JSwitchUserFavorite>lambdaQuery()
            .eq(JSwitchUserFavorite::getUserId, "admin")
            .eq(JSwitchUserFavorite::getFavoriteType, favoriteType)
            .eq(JSwitchUserFavorite::getFavoriteObject, favoriteObject)
        );
        return ResponseHelper.success();
    }

    private void favoriteExistRequired(String userId, FavoriteType type, String object){
        JSwitchUserFavorite favorite = getFavorite(userId, type, object);
        if (favorite == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
    }

    private void favoriteNotExistRequired(String userId, FavoriteType type, String object){
        JSwitchUserFavorite favorite = getFavorite(userId, type, object);
        if (favorite != null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
    }

    private JSwitchUserFavorite getFavorite(String userId, FavoriteType type, String object){
        JSwitchUserFavorite favorite = favoriteService.getOne(Wrappers.<JSwitchUserFavorite>lambdaQuery()
                .eq(JSwitchUserFavorite::getUserId, userId)
                .eq(JSwitchUserFavorite::getFavoriteType, type)
                .eq(JSwitchUserFavorite::getFavoriteObject, object));
        return favorite;
    }
}
