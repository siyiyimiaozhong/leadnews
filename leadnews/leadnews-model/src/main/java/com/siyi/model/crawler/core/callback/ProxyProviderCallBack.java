package com.siyi.model.crawler.core.callback;

import com.siyi.model.crawler.core.proxy.CrawlerProxy;

import java.util.List;

/**
 * IP池更新回调
 */
public interface ProxyProviderCallBack {
    public List<CrawlerProxy> getProxyList();

    public void unavailable(CrawlerProxy crawlerProxy);
}