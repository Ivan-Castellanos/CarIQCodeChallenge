package org.cariq;

public class APIStore extends AbstractAPIPets<APIStore> {
    public APIStore(String directory) {
        super("store/" + directory);
    }

    public APIStore() {
        super("store");
    }
}
