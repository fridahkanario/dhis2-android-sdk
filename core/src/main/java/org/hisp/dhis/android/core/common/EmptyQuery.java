package org.hisp.dhis.android.core.common;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class EmptyQuery extends BaseQuery {

    @Override
    boolean isValid() {
        return true;
    }

    public static EmptyQuery create() {
        return new AutoValue_EmptyQuery(1, BaseQuery.DEFAULT_PAGE_SIZE, false);
    }
}
