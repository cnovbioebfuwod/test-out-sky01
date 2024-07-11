package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String WECHAT_LOGIN_URL="https://api.weixin.qq.com/sns/jscode2session";

    @Resource
    private WeChatProperties weChatProperties;

    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private UserMapper userMapper;

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        String openId = getOpenId(userLoginDTO.getCode());

        if(ObjectUtils.isEmpty(openId)){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);

        }
        User user=userMapper.getByOpenid(openId);
        if(null==user){
            user=User.builder().openid(openId).createTime(LocalDateTime.now()).build();
            userMapper.insert(user);//生成主键Id
        }

        //为微信用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO vo = UserLoginVO.builder()
                .id(user.getId())
                .openid(openId)
                .token(token)
                .build();
        return vo;
    }


    private String getOpenId(String code){
        //调用微信接口服务，获得当前微信用户的openid

        Map<String, String> paramMap =new HashMap<>();
        HttpClientUtil.doGet(UserServiceImpl.WECHAT_LOGIN_URL,paramMap);
        paramMap.put("appid",weChatProperties.getAppid());
        paramMap.put("secret",weChatProperties.getSecret());
        paramMap.put("js_code",code);
        paramMap.put("grant_type","authorization_code");

        String resultString= Optional.ofNullable(HttpClientUtil
                        .doGet(UserServiceImpl.WECHAT_LOGIN_URL,paramMap))
                .orElseThrow(()->new LoginFailedException(MessageConstant.LOGIN_FAILED));


        JSONObject jsonObject= JSON.parseObject(resultString);
        String errcode=jsonObject.getString("errcode");
        if(null!=errcode&& !"0".equals(errcode)){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        String openid=jsonObject.getString("openid");
        return openid;



    }

}