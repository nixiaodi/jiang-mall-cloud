package org.jiang.core.registry.base;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDto {
    private String app;

    private String host;

    private CoordinatorRegistryCenter coordinatorRegistryCenter;
}
