package org.hisp.dhis.android.core.dataelement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.runner.AndroidJUnit4;

import org.hisp.dhis.android.core.common.BaseIdentifiableObject;
import org.hisp.dhis.android.core.common.ValueType;
import org.hisp.dhis.android.core.data.database.AbsStoreTestCase;
import org.hisp.dhis.android.core.data.database.DbOpenHelper.Tables;
import org.hisp.dhis.android.core.dataelement.DataElementModel.Columns;
import org.hisp.dhis.android.core.option.CreateOptionSetUtils;
import org.hisp.dhis.android.core.option.OptionSetModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import static com.google.common.truth.Truth.assertThat;
import static org.hisp.dhis.android.core.data.database.CursorAssert.assertThatCursor;

@RunWith(AndroidJUnit4.class)
public class DataElementStoreIntegrationTest extends AbsStoreTestCase {

    private static final long ID = 21L;

    private static final String UID = "test_uid";
    private static final String CODE = "test_code";
    private static final String NAME = "test_name";
    private static final String DISPLAY_NAME = "test_display_name";

    private static final String SHORT_NAME = "test_short_name";
    private static final String DISPLAY_SHORT_NAME = "test_display_short_name";
    private static final String DESCRIPTION = "test_description";
    private static final String DISPLAY_DESCRIPTION = "test_display_description";

    private static final ValueType VALUE_TYPE = ValueType.TEXT;
    private static final Boolean ZERO_IS_SIGNIFICANT = Boolean.FALSE;
    private static final String AGGREGATION_OPERATOR = "test_aggregationOperator";
    private static final String FORM_NAME = "test_formName";
    private static final String NUMBER_TYPE = "test_numberType";
    private static final String DOMAIN_TYPE = "test_domainType";
    private static final String DIMENSION = "test_dimension";
    private static final String DISPLAY_FORM_NAME = "test_displayFormName";
    private static final String OPTION_SET = "test_optionSet";

    // timestamp
    private static final String DATE = "2016-12-20T16:26:00.007";

    private static final String[] DATA_ELEMENT_PROJECTION = {
            Columns.UID,
            Columns.CODE,
            Columns.NAME,
            Columns.DISPLAY_NAME,
            Columns.CREATED,
            Columns.LAST_UPDATED,
            Columns.SHORT_NAME,
            Columns.DISPLAY_SHORT_NAME,
            Columns.DESCRIPTION,
            Columns.DISPLAY_DESCRIPTION,
            Columns.VALUE_TYPE,
            Columns.ZERO_IS_SIGNIFICANT,
            Columns.AGGREGATION_TYPE,
            Columns.FORM_NAME,
            Columns.NUMBER_TYPE,
            Columns.DOMAIN_TYPE,
            Columns.DIMENSION,
            Columns.DISPLAY_FORM_NAME,
            Columns.OPTION_SET
    };

    private DataElementStore dataElementStore;

    @Override
    @Before
    public void setUp() throws IOException {
        super.setUp();
        this.dataElementStore = new DataElementStoreImpl(database());
    }

    @Test
    public void insert_shouldPersistDataElementInDatabase() throws ParseException {
        ContentValues optionSet = CreateOptionSetUtils.create(ID, OPTION_SET);


        database().insert(Tables.OPTION_SET, null, optionSet);

        Date timeStamp = BaseIdentifiableObject.DATE_FORMAT.parse(DATE);
        long rowId = dataElementStore.insert(
                UID,
                CODE,
                NAME,
                DISPLAY_NAME,
                timeStamp,
                timeStamp,
                SHORT_NAME,
                DISPLAY_SHORT_NAME,
                DESCRIPTION,
                DISPLAY_DESCRIPTION,
                VALUE_TYPE,
                ZERO_IS_SIGNIFICANT,
                AGGREGATION_OPERATOR,
                FORM_NAME,
                NUMBER_TYPE,
                DOMAIN_TYPE,
                DIMENSION,
                DISPLAY_FORM_NAME,
                OPTION_SET
        );

        Cursor cursor = database().query(Tables.DATA_ELEMENT, DATA_ELEMENT_PROJECTION,
                null, null, null, null, null);

        // Checking if rowId == 1.
        // If it is 1, then it means it is first successful insert into db
        assertThat(rowId).isEqualTo(1L);

        assertThatCursor(cursor).hasRow(
                UID,
                CODE,
                NAME,
                DISPLAY_NAME,
                DATE,
                DATE,
                SHORT_NAME,
                DISPLAY_SHORT_NAME,
                DESCRIPTION,
                DISPLAY_DESCRIPTION,
                VALUE_TYPE,
                0, // ZERO_IS_SIGNIFICANT = Boolean.FALSE
                AGGREGATION_OPERATOR,
                FORM_NAME,
                NUMBER_TYPE,
                DOMAIN_TYPE,
                DIMENSION,
                DISPLAY_FORM_NAME,
                OPTION_SET
        ).isExhausted();
    }

    @Test
    public void insert_shouldPersistDataElementInDatabaseWithoutOptionSet() throws ParseException {
        Date timeStamp = BaseIdentifiableObject.DATE_FORMAT.parse(DATE);
        long rowId = dataElementStore.insert(
                UID,
                CODE,
                NAME,
                DISPLAY_NAME,
                timeStamp,
                timeStamp,
                SHORT_NAME,
                DISPLAY_SHORT_NAME,
                DESCRIPTION,
                DISPLAY_DESCRIPTION,
                VALUE_TYPE,
                ZERO_IS_SIGNIFICANT,
                AGGREGATION_OPERATOR,
                FORM_NAME,
                NUMBER_TYPE,
                DOMAIN_TYPE,
                DIMENSION,
                DISPLAY_FORM_NAME,
                null
        );

        Cursor cursor = database().query(Tables.DATA_ELEMENT, DATA_ELEMENT_PROJECTION,
                null, null, null, null, null);

        // Checking if rowId == 1.
        // If it is 1, then it means it is first successful insert into db
        assertThat(rowId).isEqualTo(1L);

        assertThatCursor(cursor).hasRow(
                UID,
                CODE,
                NAME,
                DISPLAY_NAME,
                DATE,
                DATE,
                SHORT_NAME,
                DISPLAY_SHORT_NAME,
                DESCRIPTION,
                DISPLAY_DESCRIPTION,
                VALUE_TYPE,
                0, // ZERO_IS_SIGNIFICANT = Boolean.FALSE
                AGGREGATION_OPERATOR,
                FORM_NAME,
                NUMBER_TYPE,
                DOMAIN_TYPE,
                DIMENSION,
                DISPLAY_FORM_NAME,
                null
        ).isExhausted();
    }

    @Test(expected = SQLiteConstraintException.class)
    public void exception_persistDataElementWithInvalidForeignKey() throws ParseException {
        Date timeStamp = BaseIdentifiableObject.DATE_FORMAT.parse(DATE);
        String fakeOptionSetUid = "fake_option_set_uid";
        dataElementStore.insert(
                UID,
                CODE,
                NAME,
                DISPLAY_NAME,
                timeStamp,
                timeStamp,
                SHORT_NAME,
                DISPLAY_SHORT_NAME,
                DESCRIPTION,
                DISPLAY_DESCRIPTION,
                VALUE_TYPE,
                ZERO_IS_SIGNIFICANT,
                AGGREGATION_OPERATOR,
                FORM_NAME,
                NUMBER_TYPE,
                DOMAIN_TYPE,
                DIMENSION,
                DISPLAY_FORM_NAME,
                fakeOptionSetUid
        );
    }

    @Test
    public void delete_shouldDeleteDataElementWhenDeletingOptionSetForeignKey() throws Exception {
        ContentValues optionSet = CreateOptionSetUtils.create(ID, OPTION_SET);
        database().insert(Tables.OPTION_SET, null, optionSet);

        ContentValues dataElement = new ContentValues();
        dataElement.put(Columns.ID, ID);
        dataElement.put(Columns.UID, UID);
        dataElement.put(Columns.OPTION_SET, OPTION_SET);

        database().insert(Tables.DATA_ELEMENT, null, dataElement);

        String[] PROJECTION = {Columns.ID, Columns.UID, Columns.OPTION_SET};

        Cursor cursor = database().query(Tables.DATA_ELEMENT, PROJECTION, null, null, null, null, null);

        // checking that dataElement was successfully inserted
        assertThatCursor(cursor).hasRow(ID, UID, OPTION_SET);

        // deleting option set
        database().delete(Tables.OPTION_SET, OptionSetModel.Columns.UID + "=?", new String[]{OPTION_SET});

        cursor = database().query(Tables.DATA_ELEMENT, PROJECTION, null, null, null, null, null);

        // checking that dataElement was deleted by option set on delete cascade
        assertThatCursor(cursor).isExhausted();
    }

    @Test
    public void close_shouldNotCloseDatabase() {
        dataElementStore.close();

        assertThat(database().isOpen()).isTrue();
    }

}
