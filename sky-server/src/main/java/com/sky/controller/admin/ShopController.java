package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.print.DocFlavor;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 设置店铺的营业状态
     */
    @ApiOperation("设置店铺的营业状态")
    @PutMapping("/{status}")
    public Result setShopStatus(@PathVariable Integer status){
        log.info("设置店铺的营业状态为：{}",status == 1 ? "营业中" : "打烊中");
        stringRedisTemplate.opsForValue().set(StatusConstant.SHOP_STATUS_KEY,status.toString());
        return Result.success();
    }

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
