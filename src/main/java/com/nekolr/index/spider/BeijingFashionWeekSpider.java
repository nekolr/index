package com.nekolr.index.spider;

import com.alibaba.fastjson.JSON;
import com.nekolr.index.dao.redis.IndexRedisRepository;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.io.Serializable;
import java.util.List;

@Component
public class BeijingFashionWeekSpider implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private IndexRedisRepository indexRedisRepository;

    public void setIndexRedisRepository(IndexRedisRepository indexRedisRepository) {
        this.indexRedisRepository = indexRedisRepository;
    }

    @Override
    public void process(Page page) {
        List<Selectable> liList = page.getHtml().css("body > div.container > div > ul > li").nodes();
        for (int i = 0; i < liList.size(); i++) {
            String title = liList.get(i).xpath("//a/div[2]/tidyText()").toString();
            String date = liList.get(i).xpath("//a/span/tidyText()").toString();
            indexRedisRepository.addSetElement("infoList", JSON.toJSON(new Element(title, date, "北京时装周官网")));
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}

class Element implements Serializable {
    private String title;
    private String date;
    private String source;

    public Element(String title, String date, String source) {
        this.title = title;
        this.date = date;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
