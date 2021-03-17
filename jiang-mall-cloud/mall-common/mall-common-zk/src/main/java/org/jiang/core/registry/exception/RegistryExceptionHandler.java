package org.jiang.core.registry.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;

import java.security.PrivateKey;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegistryExceptionHandler {

    public static void handleException(final Exception cause) {
        if (cause == null) {
            return;
        }
        boolean flag = isIgnoredException(cause) || cause.getCause() != null && isIgnoredException(cause.getCause());
        if (flag) {
            log.debug("Elastic job: ignored exception for: {}", cause.getMessage());
        } else if (cause instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        } else {
            throw new RegistryException(cause);
        }
    }

    private static boolean isIgnoredException(final Throwable cause) {
        return cause instanceof NoNodeException || cause instanceof NodeExistsException;
    }
}
