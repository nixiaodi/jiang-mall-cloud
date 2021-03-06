package org.jiang.core.generator;

import org.apache.curator.retry.RetryNTimes;
import org.jiang.constant.GlobalConstant;
import org.jiang.core.registry.base.CoordinatorRegistryCenter;
import org.jiang.core.registry.base.RegisterDto;

/**
 * FrameworkId 存储器
 */
public class IncrementIdGenerator implements IdGenerator{
    private static Long serviceId = null;
    private final RegisterDto registerDto;

    /**
     * Instantiates a new Increment id generator
     */
    public IncrementIdGenerator(RegisterDto registerDto) {
        this.registerDto = registerDto;
    }

    @Override
    public Long nextId() {
        String app = this.registerDto.getApp();
        String host = this.registerDto.getHost();
        CoordinatorRegistryCenter registryCenter = this.registerDto.getCoordinatorRegistryCenter();
        String path = GlobalConstant.ZK_REGISTRY_ID_ROOT_PATH + GlobalConstant.Symbol.SLASH + app + GlobalConstant.Symbol.SLASH + host;
        if (registryCenter.isExisted(path)) {
            // 如果已经有该节点，表示已经为当前的host上部署的该app分配的编号(应对某个服务重启之后编号不变的问题),直接获取该id，而无需生成
            return Long.valueOf(registryCenter.getDirectly(GlobalConstant.ZK_REGISTRY_ID_ROOT_PATH + GlobalConstant.Symbol.SLASH + app + GlobalConstant.Symbol.SLASH + host));
        } else {
            // 节点不存在，那么需要生成id，利用zk节点的版本号每写一次就自增的机制来实现
            registryCenter.increment(GlobalConstant.ZK_REGISTRY_SEQ + GlobalConstant.Symbol.SLASH + app + GlobalConstant.Symbol.SLASH + host,new RetryNTimes(2000,3));
            // 生成ID
            Integer id = registryCenter.getAtomicValue(GlobalConstant.ZK_REGISTRY_SEQ,new RetryNTimes(2000, 3)).postValue();
            // 将数据写入节点
            registryCenter.persist(path);
            registryCenter.persist(path,String.valueOf(id));
            return Long.valueOf(id);
        }

    }

    /**
     * Gets service id.
     */
    public static Long getServiceId() {
        return serviceId;
    }

    /**
     * Sets service id.
     *
     * @param serviceId the service id
     */
    public static void setServiceId(Long serviceId) {
        IncrementIdGenerator.serviceId = serviceId;
    }
}
