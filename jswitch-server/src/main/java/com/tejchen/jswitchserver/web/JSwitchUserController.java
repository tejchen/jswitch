package com.tejchen.jswitchserver.web;

import com.tejchen.jswitchserver.helper.ResponseHelper;
import com.tejchen.jswitchserver.service.JSwitchUserFavoriteService;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jswitch/user")
public class JSwitchUserController {

    @Autowired
    private JSwitchUserFavoriteService favoriteService;

    @RequestMapping("/add")
    public JSwitchHttpResponse get(@PathVariable("appCode") String appCode){

        return ResponseHelper.success();
    }


    @RequestMapping("/remove")
    public JSwitchHttpResponse get(@RequestParam String keyword, @RequestParam Integer page, @RequestParam Integer pageSize){

        return  ResponseHelper.success();
    }
}
