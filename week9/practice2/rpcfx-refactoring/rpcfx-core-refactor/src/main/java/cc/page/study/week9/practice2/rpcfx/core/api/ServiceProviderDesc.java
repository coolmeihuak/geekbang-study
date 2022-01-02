package cc.page.study.week9.practice2.rpcfx.core.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceProviderDesc {

    private String host;
    private Integer port;
    private String serviceClass;

    // group
    // version
}
