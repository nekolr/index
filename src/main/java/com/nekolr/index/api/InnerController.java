package com.nekolr.index.api;

import com.nekolr.index.common.BaseController;
import com.nekolr.index.common.ResultBean;
import com.nekolr.index.dao.redis.IndexRedisRepository;
import com.nekolr.index.selenium.BaiDuIndex;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1")
@Api(value = "inner", tags = "远程控制 API")
public class InnerController extends BaseController {
    @Autowired
    private IndexRedisRepository indexRedisRepository;

    @GetMapping("/start")
    @ApiOperation(value = "启动远程控制", notes = "启动远程控制")
    public ResultBean start() {
        // 启动模拟登陆以及搜索
        BaiDuIndex.mockSearch();
        // 执行查询人群画像
        Map<String, Object> crowd = BaiDuIndex.executeGetSocial("20180101|20180726");
        // 将结果持久化
        indexRedisRepository.addCrowd("crowd", crowd);

        return assembleResultOfSuccess("操作成功");
    }

    @GetMapping("/quit")
    @ApiOperation(value = "退出远程控制", notes = "退出远程控制")
    public ResultBean quit() {
        BaiDuIndex.quit();
        return assembleResultOfSuccess("操作成功");
    }
}
