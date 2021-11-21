package cc.page.study.week3.outbound.Client;

import java.util.function.Consumer;

/**
 * 统一客户端调用方式
 */
public interface ClientCall {

    /**
     * 设置请求头
     */
    void setHeader(String name, String value);

    /**
     * 获取响应头
     */
    String getHeader(String name);

    /**
     * 执行请求，返回字节数组
     */
    void execute(String url, Consumer<byte[]> consumer);
}
