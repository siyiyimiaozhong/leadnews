package com.siyi.article.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siyi.common.kafka.KafkaTopicConfig;
import com.siyi.common.kafka.messages.UpdateArticleMessage;
import com.siyi.common.kafka.messages.app.ArticleVisitStreamMessage;
import com.siyi.common.kafkastream.KafkaStreamListener;
import com.siyi.model.mess.app.ArticleVisitStreamDto;
import com.siyi.model.mess.app.UpdateArticle;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Log4j2
public class HotArticleStreamHandler implements KafkaStreamListener<KStream<?, String>> {

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public String listenerTopic() {
        return kafkaTopicConfig.getArticleUpdateBus();
    }

    @Override
    public String sendTopic() {
        return kafkaTopicConfig.getArticleIncrHandle();
    }

    @Override
    public KStream<?, String> getService(KStream<?, String> stream) {
        return stream.map((key, val) -> {
            UpdateArticleMessage value = format(val);
            System.out.println(value);
            //likes:1
            return new KeyValue<>(value.getData().getArticleId().toString(), value.getData().getType().name() + ":" + value.getData().getAdd());
        }).groupByKey().windowedBy(TimeWindows.of(10_000)).aggregate(new Initializer<String>() { //设置10秒聚合一次
            @Override
            public String apply() {
                return "COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0";
            }
        }, new Aggregator<Object, String, String>() {
            @Override
            public String apply(Object aggKey, String value, String aggValue) {
                //类似于  likes:1
                value = value.replace("UpdateArticle(", "").replace(")", "");
                String valArr[] = value.split(":");
                if ("null".equals(valArr[1])) {
                    return aggValue;
                }
//                "COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0";
                String[] aggArr = aggValue.split(",");
                int col = 0, com = 0, lik = 0, vie = 0;
                if("LIKES".equalsIgnoreCase(valArr[0])){
                    lik+=Integer.valueOf(valArr[1]);
                }
                if("COLLECTION".equalsIgnoreCase(valArr[0])){
                    col+=Integer.valueOf(valArr[1]);
                }
                if("COMMENT".equalsIgnoreCase(valArr[0])){
                    com+=Integer.valueOf(valArr[1]);
                }
                if("VIEWS".equalsIgnoreCase(valArr[0])){
                    vie+=Integer.valueOf(valArr[1]);
                }
                return String.format("COLLECTION:%d,COMMENT:%d,LIKES:%d,VIEWS:%d", col, com, lik, vie);
            }
        }, Materialized.as("count-article-num-miukoo-1")).toStream().map((key, value) -> {
            return new KeyValue<>(key.key().toString(), formatObj(key.key().toString(), value));
        });
    }

    private String formatObj(String articleId, String value) {
        String ret = "";
        ArticleVisitStreamMessage temp = new ArticleVisitStreamMessage();
        ArticleVisitStreamDto dto = new ArticleVisitStreamDto();
        String regEx = "COLLECTION:(\\d+),COMMENT:(\\d+),LIKES:(\\d+),VIEWS:(\\d+)";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(value);
        if (mat.find()) {
            dto.setCollect(Long.valueOf(mat.group(1)));
            dto.setCommont(Long.valueOf(mat.group(2)));
            dto.setLike(Long.valueOf(mat.group(3)));
            dto.setView(Long.valueOf(mat.group(4)));
        } else {
            dto.setCollect(0);
            dto.setCommont(0);
            dto.setLike(0);
            dto.setView(0);
        }
        dto.setArticleId(Integer.valueOf(articleId));
        temp.setData(dto);
        try {
            ret = mapper.writeValueAsString(temp);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private UpdateArticleMessage format(String val) {
        UpdateArticleMessage msg = null;
        try {
            msg = mapper.readValue(val, UpdateArticleMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}