package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 设置店铺的营业状态
     */
    @ApiOperation("获取店铺的营业状态")
    @GetMapping("/status")
    public Result getShopStatus(){
        String status = stringRedisTemplate.opsForValue().get(StatusConstant.SHOP_STATUS_KEY);
        log.info("获取到店铺的营业状态为：{}",status);
        return Result.success(Integer.parseInt(status));
    }
}
