package me.vihara.core.storage.mysql;

/**
 * Database response handling
 */
public interface ResponseHandler<H, R> {

    /**
     * Async database response handling
     */
    R handleResponse(H handle) throws Exception;

    default void handleException(Throwable throwable) {
        throwable.printStackTrace();
    }
}
