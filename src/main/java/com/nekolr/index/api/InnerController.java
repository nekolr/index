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

        return assembleResultOfSuccess("操作成功");
    }

    @GetMapping("/executeGetSocial")
    @ApiOperation(value = "执行人群画像爬取", notes = "执行人群画像爬取")
    public ResultBean executeGetSocial(String time) {
        if (time == null || time.length() == 0) {
            assembleResultOfFail("请填写参数");
        }
        if (BaiDuIndex.isRunning()) {
            // 执行查询人群画像
            Map<String, Object> crowd = BaiDuIndex.executeGetSocial(time);
            // 将结果持久化
            indexRedisRepository.addAll("crowd", crowd);
        } else {
            return assembleResultOfFail("请先调用 start 方法启动远程控制");
        }
        return assembleResultOfSuccess("操作成功");
    }

    @GetMapping("/executeGetRegion")
    @ApiOperation(value = "执行地域访问量爬取", notes = "执行地域访问量爬取")
    public ResultBean executeGetRegion(String time) {
        if (time == null || time.length() == 0) {
            assembleResultOfFail("请填写参数");
        }
        if (BaiDuIndex.isRunning()) {
            // 执行查询地域访问量
            Map<String, Object> distribution = BaiDuIndex.executeGetRegion(time);
            // 将结果持久化
            indexRedisRepository.addAll("distribution", distribution);
        } else {
            return assembleResultOfFail("请先调用 start 方法启动远程控制");
        }

        return assembleResultOfSuccess("操作成功");
    }

    @GetMapping("/quit")
    @ApiOperation(value = "退出远程控制", notes = "退出远程控制")
    public ResultBean quit() {
        BaiDuIndex.quit();
        return assembleResultOfSuccess("操作成功");
    }
}
