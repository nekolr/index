package com.nekolr.index.api;

import com.nekolr.index.common.BaseController;
import com.nekolr.index.common.ResultBean;
import com.nekolr.index.dao.redis.IndexRedisRepository;
import com.nekolr.index.model.*;
import com.nekolr.index.util.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/v1")
@Api(value = "index", tags = "时尚指数 API")
public class IndexController extends BaseController {

    @Autowired
    private IndexRedisRepository indexRedisRepository;

    @GetMapping("/chinaMap")
    @ApiOperation(value = "中国地图数据", notes = "中国地图数据")
    public ResultBean chinaMap() throws FileNotFoundException {
        return assembleResultOfSuccess(JsonUtils.readObject("classpath:static/china_provinces.json", ChinaProvince.class));
    }

    @GetMapping("/hotBrand")
    @ApiOperation(value = "热门品牌", notes = "热门品牌")
    public ResultBean hotBrand() throws FileNotFoundException {
        return assembleResultOfSuccess(
                JsonUtils.readArray("classpath:static/hot_brand.json", HotBrand.class)
        );
    }

    @GetMapping("/hotWord")
    @ApiOperation(value = "热词", notes = "热词")
    public ResultBean hotWord() throws FileNotFoundException {
        return assembleResultOfSuccess(
                JsonUtils.readArray("classpath:static/hot_word.json", HotWord.class)
        );
    }

    @GetMapping("/gender")
    @ApiOperation(value = "性别分布", notes = "人群画像之性别分布")
    public ResultBean gender() throws FileNotFoundException {
        return assembleResultOfSuccess(
                JsonUtils.readObject("classpath:static/gender.json", Gender.class)
        );
    }

    @GetMapping("/age")
    @ApiOperation(value = "年龄分布", notes = "人群画像之年龄分布")
    public ResultBean age() throws FileNotFoundException {
        return assembleResultOfSuccess(
                JsonUtils.readObject("classpath:static/age.json", Age.class)
        );
    }

    @GetMapping("/crowd")
    @ApiOperation(value = "人群画像之年龄和性别分布", notes = "人群画像之年龄和性别分布")
    public ResultBean crowd() {
        return assembleResultOfSuccess(indexRedisRepository.getCrowd("crowd"));
    }

    @GetMapping("/mediaIndex")
    @ApiOperation(value = "媒体指数", notes = "媒体指数")
    public ResultBean mediaIndex() throws FileNotFoundException {
        return assembleResultOfSuccess(
                JsonUtils.readArray("classpath:static/media_index.json", Media.class)
        );
    }

    @GetMapping("/index")
    @ApiOperation(value = "搜索指数", notes = "搜索指数")
    public ResultBean index() throws FileNotFoundException {
        return assembleResultOfSuccess(
                JsonUtils.readObject("classpath:static/index.json", Index.class)
        );
    }

    @GetMapping("/distribution")
    @ApiOperation(value = "地域指数", notes = "地域搜索指数")
    public ResultBean distribution() throws FileNotFoundException {
        return assembleResultOfSuccess(
                JsonUtils.readObject("classpath:static/distribution.json", Distribution.class)
        );
    }
}
