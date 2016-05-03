package org.hisp.dhis.client.sdk.ui.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class FormEntityCharSequence extends FormEntity {
    private static final String EMPTY_STRING = "";

    @Nullable
    private OnFormEntityChangeListener onFormEntityChangeListener;

    @NonNull
    private CharSequence value;

    public FormEntityCharSequence(String id, String label) {
        super(id, label);

        this.value = EMPTY_STRING;
    }

    @Nullable
    public OnFormEntityChangeListener getOnFormEntityChangeListener() {
        return onFormEntityChangeListener;
    }

    public void setOnFormEntityChangeListener(@Nullable OnFormEntityChangeListener listener) {
        this.onFormEntityChangeListener = listener;
    }

    @NonNull
    public CharSequence getValue() {
        return value;
    }

    public void setValue(@Nullable CharSequence value) {
        CharSequence newValue = value;

        // we need to make sure that we never nullify value
        if (newValue == null) {
            newValue = EMPTY_STRING;
        }

        if (!this.value.equals(newValue)) {
            this.value = newValue;

            if (onFormEntityChangeListener != null) {
                this.onFormEntityChangeListener.onFormEntityChanged(this);
            }
        }
    }
}
