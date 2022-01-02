package cc.page.study.week9.practice2.rpcfx.core.api;

import java.util.List;

public interface LoadBalancer {

    String select(List<String> urls);

}
