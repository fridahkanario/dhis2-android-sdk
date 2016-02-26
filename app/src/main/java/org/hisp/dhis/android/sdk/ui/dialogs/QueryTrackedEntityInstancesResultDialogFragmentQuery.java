/*
 * Copyright (c) 2015, University of Oslo
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

package org.hisp.dhis.android.sdk.ui.dialogs;

import android.content.Context;
import android.util.Log;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.loaders.Query;
import org.hisp.dhis.android.sdk.persistence.models.DataElement;
import org.hisp.dhis.android.sdk.persistence.models.OptionSet;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.AutoCompleteRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.CheckBoxRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowTypes;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DatePickerRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.EditTextRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.RadioButtonsRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simen S. Russnes on 7/9/15.
 */
public class QueryTrackedEntityInstancesResultDialogFragmentQuery implements Query<QueryTrackedEntityInstancesResultDialogFragmentForm>
{
    public static final String TAG = QueryTrackedEntityInstancesResultDialogFragmentQuery.class.getSimpleName();
    private String orgUnit;
    private String programId;

    public QueryTrackedEntityInstancesResultDialogFragmentQuery(String orgUnit, String programId)
    {
        this.programId = programId;
        this.orgUnit = orgUnit;
    }

//    @Override
//    public QueryTrackedEntityInstancesResultDialogFragmentForm query(Context context)
//    {
//        QueryTrackedEntityInstancesResultDialogFragmentForm form = new QueryTrackedEntityInstancesResultDialogFragmentForm();
//        form.setOrganisationUnit(orgUnit);
//        form.setProgram(programId);
//
//        Log.d(TAG, orgUnit + programId);
//
//        Program program = MetaDataController.getProgram(programId);
//        if(program == null || orgUnit == null) {
//            return form;
//        }
//        List<ProgramTrackedEntityAttribute> programAttrs = program.getProgramTrackedEntityAttributes();
//        List<TrackedEntityAttributeValue> values = new ArrayList<>();
//        List<TrackedEntityAttribute> listAttributes = new ArrayList<>();
//        for(ProgramTrackedEntityAttribute ptea: programAttrs) {
//                listAttributes.add(ptea.getTrackedEntityAttribute());
//
//        }
//        Log.d(TAG, "rows1: " + listAttributes.size());
//        if(listAttributes == null)
//            return form;
//
//        List<DataEntryRow> dataEntryRows = new ArrayList<>();
//        for(int i=0;i<listAttributes.size();i++)
//        {
//            TrackedEntityAttributeValue value = new TrackedEntityAttributeValue();
//            value.setTrackedEntityAttributeId(listAttributes.get(i).getUid());
//            values.add(value);
//
//            DataEntryRow row = createDataEntryView(listAttributes.get(i), value);
//            dataEntryRows.add(row);
//        }
//        Log.d(TAG, "rows: " + dataEntryRows.size());
//        form.setTrackedEntityAttributeValues(values);
//        form.setDataEntryRows(dataEntryRows);
//        return form;
//    }
@Override
public QueryTrackedEntityInstancesResultDialogFragmentForm query(Context context)
{
    QueryTrackedEntityInstancesResultDialogFragmentForm form = new QueryTrackedEntityInstancesResultDialogFragmentForm();
    form.setOrganisationUnit(orgUnit);
    form.setProgram(programId);

    Log.d(TAG, orgUnit + programId);

    Program program = MetaDataController.getProgram(programId);
    if(program == null || orgUnit == null) {
        return form;
    }
    List<ProgramTrackedEntityAttribute> programAttrs = program.getProgramTrackedEntityAttributes();
    List<TrackedEntityAttributeValue> values = new ArrayList<>();
    List<DataEntryRow> dataEntryRows = new ArrayList<>();
    for(ProgramTrackedEntityAttribute ptea: programAttrs) {
        TrackedEntityAttribute trackedEntityAttribute = ptea.getTrackedEntityAttribute();
        TrackedEntityAttributeValue value = new TrackedEntityAttributeValue();
        value.setTrackedEntityAttributeId(trackedEntityAttribute.getUid());
        values.add(value);

        DataEntryRow row = createDataEntryView(ptea, trackedEntityAttribute, value);
        dataEntryRows.add(row);
    }
    form.setTrackedEntityAttributeValues(values);
    form.setDataEntryRows(dataEntryRows);
    return form;
}

    public DataEntryRow createDataEntryView(ProgramTrackedEntityAttribute programTrackedEntityAttribute,
                                            TrackedEntityAttribute trackedEntityAttribute, TrackedEntityAttributeValue dataValue) {
        DataEntryRow row;
        String trackedEntityAttributeName = trackedEntityAttribute.getName();
        if (trackedEntityAttribute.getOptionSet() != null) {
            OptionSet optionSet = MetaDataController.getOptionSet(trackedEntityAttribute.getOptionSet());
            if (optionSet == null) {
                row = new EditTextRow(trackedEntityAttributeName, dataValue, DataEntryRowTypes.TEXT);
            } else {
                row = new AutoCompleteRow(trackedEntityAttributeName, dataValue, optionSet);
            }
        } else if (trackedEntityAttribute.getValueType().equalsIgnoreCase(DataElement.VALUE_TYPE_TEXT)) {
            row = new EditTextRow(trackedEntityAttributeName, dataValue, DataEntryRowTypes.TEXT);
        } else if (trackedEntityAttribute.getValueType().equalsIgnoreCase(DataElement.VALUE_TYPE_LONG_TEXT)) {
            row = new EditTextRow(trackedEntityAttributeName, dataValue, DataEntryRowTypes.LONG_TEXT);
        } else if (trackedEntityAttribute.getValueType().equalsIgnoreCase(DataElement.VALUE_TYPE_NUMBER)) {
            row = new EditTextRow(trackedEntityAttributeName, dataValue, DataEntryRowTypes.NUMBER);
        } else if (trackedEntityAttribute.getValueType().equalsIgnoreCase(DataElement.VALUE_TYPE_INT)) {
            row = new EditTextRow(trackedEntityAttributeName, dataValue, DataEntryRowTypes.INTEGER);
        } else if (trackedEntityAttribute.getValueType().equalsIgnoreCase(DataElement.VALUE_TYPE_ZERO_OR_POSITIVE_INT)) {
            row = new EditTextRow(trackedEntityAttributeName, dataValue, DataEntryRowTypes.INTEGER_ZERO_OR_POSITIVE);
        } else if (trackedEntityAttribute.getValueType().equalsIgnoreCase(DataElement.VALUE_TYPE_POSITIVE_INT)) {
            row = new EditTextRow(trackedEntityAttributeName, dataValue, DataEntryRowTypes.INTEGER_POSITIVE);
        } else if (trackedEntityAttribute.getValueType().equalsIgnoreCase(DataElement.VALUE_TYPE_NEGATIVE_INT)) {
            row = new EditTextRow(trackedEntityAttributeName, dataValue, DataEntryRowTypes.INTEGER_NEGATIVE);
        } else if (trackedEntityAttribute.getValueType().equalsIgnoreCase(DataElement.VALUE_TYPE_BOOL)) {
            row = new RadioButtonsRow(trackedEntityAttributeName, dataValue, DataEntryRowTypes.BOOLEAN);
        } else if (trackedEntityAttribute.getValueType().equalsIgnoreCase(DataElement.VALUE_TYPE_TRUE_ONLY)) {
            row = new CheckBoxRow(trackedEntityAttributeName, dataValue);
        } else if (trackedEntityAttribute.getValueType().equalsIgnoreCase(DataElement.VALUE_TYPE_DATE)) {
            row = new DatePickerRow(trackedEntityAttributeName, dataValue, programTrackedEntityAttribute.getAllowFutureDate());
        } else if (trackedEntityAttribute.getValueType().equalsIgnoreCase(DataElement.VALUE_TYPE_STRING)) {
            row = new EditTextRow(trackedEntityAttributeName, dataValue, DataEntryRowTypes.LONG_TEXT);
        } else {
            row = new EditTextRow(trackedEntityAttributeName, dataValue, DataEntryRowTypes.LONG_TEXT);
        }
        return row;
    }
}
