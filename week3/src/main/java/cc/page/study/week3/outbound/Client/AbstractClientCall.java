package cc.page.study.week3.outbound.Client;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractClientCall implements ClientCall {

    protected Map<String, String> header = new HashMap<>();

    @Override
    public String getHeader(String name) {
        return header.get(name);
    }
}
