package com.siyi.crawler.config;

import com.siyi.crawler.helper.CookieHelper;
import com.siyi.crawler.helper.CrawlerHelper;
import com.siyi.crawler.process.entity.CrawlerConfigProperty;
import com.siyi.crawler.process.scheduler.DbAndRedisScheduler;
import com.siyi.crawler.service.CrawlerIpPoolService;
import com.siyi.crawler.utils.SeleniumClient;
import com.siyi.model.crawler.core.callback.DataValidateCallBack;
import com.siyi.model.crawler.core.callback.ProxyProviderCallBack;
import com.siyi.model.crawler.core.parse.ParseRule;
import com.siyi.model.crawler.core.proxy.CrawlerProxy;
import com.siyi.model.crawler.core.proxy.CrawlerProxyProvider;
import com.siyi.model.crawler.enums.CrawlerEnum;
import com.siyi.model.crawler.pojos.ClIpPool;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@PropertySource("classpath:crawler.properties")
@ConfigurationProperties(prefix = "crawler.init.url")
public class CrawlerConfig {

    private String prefix;//https://www.csdn.net/nav/
    private String suffix;//java,web,arch,db,mobile,ops,sec

    @Value("${crux.cookie.name}")
    private String CRUX_COOKIE_NAME;
    @Value("${proxy.isUsedProxyIp}")
    private Boolean isUsedProxyIp;

    private String initCrawlerXpath="//ul[@class='feedlist_mod']/li[@class='clearfix']/div[@class='list_con']/dl[@class='list_userbar']/dd[@class='name']/a";

    private String helpCrawlerXpath="//div[@class='article-list']/div[@class='article-item-box']/h4/a";

    @Value("${crawler.help.nextPagingSize}")
    private Integer crawlerHelpNextPagingSize;

    @Autowired
    private CrawlerIpPoolService crawlerIpPoolService;

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int reidsPort;
    @Value("${redis.timeout}")
    private int reidstimeout;
//    @Value("${redis.password}")
//    private String reidsPassword;

    static {
        //jre??????????????????????????????SSL????????????????????????????????????????????????????????????????????????TLS??????????????????jdk1.7?????????TLS?????????1.0??????TLS????????????1.1??????1.2????????????
        System.setProperty("https.protocols", "TLSv1.2");
//        System.setProperty("https.protocols", "TLSv1");
    }

    public List<String> getInitCrawlerUrlList(){
        List<String> initCrawlerUrlList = new ArrayList<>();
        if(StringUtils.isNotEmpty(suffix)){
            String[] urlArray = suffix.split(",");
            if(urlArray!=null && urlArray.length>0){
                for(int i = 0;i<urlArray.length;i++){
                    String initUrl = urlArray[i];
                    if(StringUtils.isNotEmpty(initUrl)){
                        if(!initUrl.toLowerCase().startsWith("http")){
                            initCrawlerUrlList.add(prefix+initUrl);
                        }
                    }
                }
            }
        }
        return initCrawlerUrlList;
    }

    @Bean
    public SeleniumClient getSeleniumClient() {
        return new SeleniumClient();
    }

    /**
     * ??????Cookie?????????
     *
     * @return
     */
    @Bean
    public CookieHelper getCookieHelper() {
        return new CookieHelper(CRUX_COOKIE_NAME);
    }

    /**
     * ???????????????????????????
     * @param cookieHelper
     * @return
     */
    private DataValidateCallBack getDataValidateCallBack(CookieHelper cookieHelper) {
        return new DataValidateCallBack() {
            @Override
            public boolean validate(String content) {
                boolean flag = true;
                if (StringUtils.isEmpty(content)) {
                    flag = false;
                } else {
                    boolean isContains_acw_sc_v2 = content.contains("acw_sc__v2");
                    boolean isContains_location_reload = content.contains("document.location.reload()");
                    if (isContains_acw_sc_v2 && isContains_location_reload) {
                        flag = false;
                    }
                }
                return flag;
            }
        };
    }

    /**
     * CrawerHelper ?????????
     *
     * @return
     */
    @Bean
    public CrawlerHelper getCrawlerHelper() {
        CookieHelper cookieHelper = getCookieHelper();
        CrawlerHelper crawlerHelper = new CrawlerHelper();
        DataValidateCallBack dataValidateCallBack = getDataValidateCallBack(cookieHelper);
        crawlerHelper.setDataValidateCallBack(dataValidateCallBack);
        return crawlerHelper;
    }

    /**
     * CrawlerProxyProvider bean
     *
     * @return
     */
    @Bean
    public CrawlerProxyProvider getCrawlerProxyProvider() {
        CrawlerProxyProvider crawlerProxyProvider = new CrawlerProxyProvider();
        crawlerProxyProvider.setUsedProxyIp(isUsedProxyIp);
        //??????????????????
        crawlerProxyProvider.setProxyProviderCallBack(new ProxyProviderCallBack() {
            @Override
            public List<CrawlerProxy> getProxyList() {
                return getCrawlerProxyList();
            }

            @Override
            public void unavailable(CrawlerProxy crawlerProxy) {
                unavailableProxy(crawlerProxy);
            }
        });
        return crawlerProxyProvider;
    }

    /**
     * ??????????????????ip??????
     * @return
     */
    public List<CrawlerProxy> getCrawlerProxyList(){
        List<CrawlerProxy> crawlerProxyList = new ArrayList<>();
        ClIpPool clIpPool = new ClIpPool();
        clIpPool.setDuration(5);
        List<ClIpPool> clIpPools = crawlerIpPoolService.queryAvailableList(clIpPool);
        if(null != clIpPools && !clIpPools.isEmpty()){
            for (ClIpPool ipPool : clIpPools) {
                crawlerProxyList.add(new CrawlerProxy(ipPool.getIp(),ipPool.getPort()));
            }
        }

        return crawlerProxyList;
    }

    /**
     * ??????ip?????????????????????
     * @param crawlerProxy
     */
    public void unavailableProxy(CrawlerProxy crawlerProxy){
        if(crawlerProxy != null){
            crawlerIpPoolService.unAvailableProxy(crawlerProxy,"????????????");
        }
    }

    @Bean
    public CrawlerConfigProperty getCrawlerConfigProperty(){
        CrawlerConfigProperty property = new CrawlerConfigProperty();
        //?????????url??????
        property.setInitCrawlerUrlList(getInitCrawlerUrlList());
        //?????????url??????????????????
        property.setInitCrawlerXpath(initCrawlerXpath);
        //??????????????????????????????  url
        property.setHelpCrawlerXpath(helpCrawlerXpath);
        //?????????????????????????????????
        property.setCrawlerHelpNextPagingSize(crawlerHelpNextPagingSize);
        //????????????????????????
        property.setTargetParseRuleList(getTargetParseRuleList());

        return property;
    }

    /**
     * ????????????????????????
     * @return
     */
    private List<ParseRule> getTargetParseRuleList() {
        List<ParseRule> parseRules = new ArrayList<ParseRule>(){{
            //??????
            add(new ParseRule("title", CrawlerEnum.ParseRuleType.XPATH,"//h1[@class='title-article']/text()"));
            //??????
            add(new ParseRule("author",CrawlerEnum.ParseRuleType.XPATH,"//a[@class='follow-nickName']/text()"));
            //????????????
            add(new ParseRule("releaseDate",CrawlerEnum.ParseRuleType.XPATH,"//span[@class='time']/text()"));
            //??????
            add(new ParseRule("labels",CrawlerEnum.ParseRuleType.XPATH,"//span[@class='tags-box']/a/text()"));
            //????????????
            add(new ParseRule("personalSpace",CrawlerEnum.ParseRuleType.XPATH,"//a[@class='follow-nickName']/@href"));
            //?????????
            add(new ParseRule("readCount",CrawlerEnum.ParseRuleType.XPATH,"//span[@class='read-count']/text()"));
            //?????????
            add(new ParseRule("likes",CrawlerEnum.ParseRuleType.XPATH,"//ul[@class='toolbox-list']/li[@id='is-like']/a/span/text()"));
            //????????????
            add(new ParseRule("commentCount",CrawlerEnum.ParseRuleType.XPATH,"//ul[@class='toolbox-list']/li[@class='tool-item-comment']/a/span[@class='count']/text()"));
            //????????????
            add(new ParseRule("content",CrawlerEnum.ParseRuleType.XPATH,"//div[@id='content_views']/html()"));
        }};
        return parseRules;
    }

    @Bean
    public DbAndRedisScheduler getDbAndRedisScheduler() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        JedisPool jedisPool = new JedisPool(genericObjectPoolConfig, redisHost, reidsPort, reidstimeout, null, 0);
        return new DbAndRedisScheduler(jedisPool);
    }

    private Spider spider;

    public Spider getSpider() {
        return spider;
    }

    public void setSpider(Spider spider) {
        this.spider = spider;
    }


}
