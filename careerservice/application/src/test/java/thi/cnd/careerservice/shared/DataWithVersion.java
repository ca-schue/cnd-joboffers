package thi.cnd.careerservice.shared;

import thi.cnd.careerservice.shared.model.DataWithETag;

public class DataWithVersion<T> {

    private final DataWithETag<T> dataWithETag;

    public static <T> DataWithVersion<T> of(DataWithETag<T> dataWithETag) {
        return new DataWithVersion<>(dataWithETag);
    }

    public DataWithVersion(DataWithETag<T> dataWithETag) {
        this.dataWithETag = dataWithETag;
    }

    public long getVersion() {
        return dataWithETag.getETag().getValue();
    }

    public String getVersionAsString() {
        return dataWithETag.getETag().getText();
    }

    public T get() {
        return dataWithETag.getData();
    }
}
