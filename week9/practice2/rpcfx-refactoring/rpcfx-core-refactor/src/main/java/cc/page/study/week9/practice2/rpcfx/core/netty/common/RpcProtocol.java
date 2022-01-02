package cc.page.study.week9.practice2.rpcfx.core.netty.common;

import lombok.Data;

@Data
public class RpcProtocol {

    /**
     * 数据大小
     */
    private int len;

    /**
     * 数据内容
     */
    private byte[] content;
}
