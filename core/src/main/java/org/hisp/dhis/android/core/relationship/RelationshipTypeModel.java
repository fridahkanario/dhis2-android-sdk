/*
 * Copyright (c) 2017, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.core.relationship;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gabrielittner.auto.value.cursor.ColumnName;
import com.google.auto.value.AutoValue;

import org.hisp.dhis.android.core.common.BaseIdentifiableObjectModel;
import org.hisp.dhis.android.core.common.CursorModelFactory;
import org.hisp.dhis.android.core.utils.Utils;

import static org.hisp.dhis.android.core.utils.StoreUtils.sqLiteBind;

@AutoValue
public abstract class RelationshipTypeModel extends BaseIdentifiableObjectModel {

    public static final String TABLE = "RelationshipType";

    public static class Columns extends BaseIdentifiableObjectModel.Columns {
        public static final String B_IS_TO_A = "bIsToA";
        public static final String A_IS_TO_B = "AIsToB";

        @Override
        public String[] all() {
            return Utils.appendInNewArray(super.all(), B_IS_TO_A, A_IS_TO_B);
        }
    }

    public static RelationshipTypeModel create(Cursor cursor) {
        return AutoValue_RelationshipTypeModel.createFromCursor(cursor);
    }

    public static Builder builder() {
        return new $$AutoValue_RelationshipTypeModel.Builder();
    }

    public static final CursorModelFactory<RelationshipTypeModel> factory
            = new CursorModelFactory<RelationshipTypeModel>() {
        @Override
        public RelationshipTypeModel fromCursor(Cursor cursor) {
            return create(cursor);
        }
    };

    @Nullable
    @ColumnName(Columns.B_IS_TO_A)
    public abstract String bIsToA();

    @Nullable
    @ColumnName(Columns.A_IS_TO_B)
    public abstract String aIsToB();

    @Override
    public void bindToStatement(@NonNull SQLiteStatement sqLiteStatement) {
        super.bindToStatement(sqLiteStatement);
        sqLiteBind(sqLiteStatement, 7, bIsToA());
        sqLiteBind(sqLiteStatement, 8, aIsToB());
    }

    @AutoValue.Builder
    public static abstract class Builder extends BaseIdentifiableObjectModel.Builder<Builder> {

        public abstract Builder bIsToA(@Nullable String bIsToA);

        public abstract Builder aIsToB(@Nullable String aIsToB);

        public abstract RelationshipTypeModel build();
    }
}
