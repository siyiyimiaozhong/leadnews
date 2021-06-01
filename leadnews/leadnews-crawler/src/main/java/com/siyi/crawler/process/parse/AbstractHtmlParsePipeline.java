package com.siyi.crawler.process.parse;

import com.siyi.crawler.helper.CrawlerHelper;
import com.siyi.crawler.process.AbstractProcessFlow;
import com.siyi.crawler.process.entity.ProcessFlowData;
import com.siyi.model.crawler.core.label.HtmlStyle;
import com.siyi.model.crawler.core.parse.ParseItem;
import com.siyi.model.crawler.enums.CrawlerEnum;
import com.siyi.utils.common.ReflectUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.HashMap;
import java.util.Map;

/**
 * Html 解析抽象类，定义了公用的方法以及抽象模板
 * <p>
 * <p>
 * Pipeline负责抽取结果的处理，包括计算、持久化到文件、数据库等。WebMagic默认提供了“输出到控制台”和“保存到文件”两种结果处理方案。
 * <p>
 * Pipeline定义了结果保存的方式，如果你要保存到指定数据库，则需要编写对应的Pipeline。对于一类需求一般只需编写一个Pipeline。
 *
 * @param <T>
 */
@Log4j2
public abstract class AbstractHtmlParsePipeline<T> extends AbstractProcessFlow implements Pipeline {


    @Autowired
    private CrawlerHelper crawlerHelper;

    @Override
    public void handel(ProcessFlowData processFlowData) {

    }

    @Override
    public CrawlerEnum.ComponentType getComponentType() {
        return CrawlerEnum.ComponentType.PIPELINE;
    }

    /**
     * 这里是传入的处理好的对象，对结构数据进行清洗和存储 是一个入口
     * @param resultItems  保存了抽取的结果，它是一个map结构
     * @param task
     */
    @Override
    public void process(ResultItems resultItems, Task task) {
        long currentTimeMillis = System.currentTimeMillis();
        String url = resultItems.getRequest().getUrl();
        String documentType = crawlerHelper.getDocumentType(resultItems.getRequest());
        String handelType = crawlerHelper.getHandelType(resultItems.getRequest());
        log.info("开始解析抽取后的数据，url:{},handelType:{}",url,handelType);
        if(!CrawlerEnum.DocumentType.PAGE.name().equals(documentType)){
            log.info("不符合文档类型,url:{},documentType:{},handelType:{}",url,documentType,handelType);
            return;
        }
        ParseItem parseItem = crawlerHelper.getParseItem(resultItems.getRequest());
        if(null!=parseItem && StringUtils.isNotEmpty(url)){
            Map<String, Object> itemsAll = resultItems.getAll();
            //前置参数处理  评论处理
            preParameterHandel(itemsAll);
            if(url.equals(parseItem.getInitialUrl())){
                //通过反射进行设置属性
                ReflectUtils.setPropertie(parseItem,itemsAll,true);
                parseItem.setHandelType(crawlerHelper.getHandelType(resultItems.getRequest()));
                handelHtmlData((T)parseItem);
            }
        }
        log.info("解析结束，url:{},handelType:{},耗时:{}",url,handelType,System.currentTimeMillis()-currentTimeMillis);

    }

    /**
     * 前置参数处理
     * @param parseItem
     */
    protected abstract void handelHtmlData(T parseItem);

    /**
     * 处理清洗及保存
     * @param itemsAll
     */
    protected abstract void preParameterHandel(Map<String, Object> itemsAll);

    /**
     * 获取解析表达式
     *
     * @return
     */
    public String getParseExpression() {
        return "p,pre,h1,h2,h3,h4,h5";
    }

    /**
     *  Html 样式
     *
     */
    public Map<String, HtmlStyle> getDefHtmlStyleMap() {
        Map<String, HtmlStyle> styleMap = new HashMap<String, HtmlStyle>();
        //h1 数据添加
        HtmlStyle h1Style = new HtmlStyle();
        h1Style.addStyle("font-size", "22px");
        h1Style.addStyle("line-height", "24px");
        styleMap.put("h1", h1Style);
        //h2 数据添加
        HtmlStyle h2Style = new HtmlStyle();
        h2Style.addStyle("font-size", "18px");
        h2Style.addStyle("line-height", "20px");
        styleMap.put("h2", h2Style);
        //h3 数据添加
        HtmlStyle h3Style = new HtmlStyle();
        h3Style.addStyle("font-size", "16px");
        h3Style.addStyle("line-height", "18px");
        styleMap.put("h3", h3Style);
        //h4 数据添加
        HtmlStyle h4Style = new HtmlStyle();
        h4Style.addStyle("font-size", "14px");
        h4Style.addStyle("line-height", "16px");
        styleMap.put("h4", h4Style);
        //h5 数据添加
        HtmlStyle h5Style = new HtmlStyle();
        h5Style.addStyle("font-size", "12px");
        h5Style.addStyle("line-height", "14px");
        styleMap.put("h5", h5Style);
        //h6 数据添加
        HtmlStyle h6Style = new HtmlStyle();
        h6Style.addStyle("font-size", "10px");
        h6Style.addStyle("line-height", "12px");
        styleMap.put("h6", h6Style);
        return styleMap;
    }
}
