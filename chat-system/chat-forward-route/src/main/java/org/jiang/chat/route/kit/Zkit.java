package org.jiang.chat.route.kit;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.jiang.chat.route.cache.ServerCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class Zkit {
    private static final String PATH_ROOT = "/route";

    @Autowired
    private ZkClient zkClient;

    @Autowired
    private ServerCache serverCache;


    /**
     * 监听事件
     * @param path
     */
    public void subscribeEvent(String path) {
        zkClient.subscribeChildChanges(path,(parentPath,currentChildren) -> {
            log.info("Clear and update local cache parentPath=[{}],currentChildren=[{}]", parentPath,currentChildren.toString());
            //update local cache, delete and save
            serverCache.updateCache(currentChildren);
        });
    }

    public List<String> getAllNode() {
        List<String> children = zkClient.getChildren(PATH_ROOT);
        log.info("Query all node =[{}] success.", JSON.toJSONString(children));
        return children;
    }
}
