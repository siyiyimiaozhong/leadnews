package com.siyi.images.service;

import com.siyi.common.kafka.messages.app.ApHotArticleMessage;

public interface HotArticleImageService {

    /**
     * 处理热文章消息
     * @param message
     */
    public void handleHotImage(ApHotArticleMessage message);
}