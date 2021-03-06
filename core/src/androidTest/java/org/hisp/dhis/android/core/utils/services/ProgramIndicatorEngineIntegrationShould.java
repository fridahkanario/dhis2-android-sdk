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

package org.hisp.dhis.android.core.utils.services;

import android.support.test.runner.AndroidJUnit4;

import org.hisp.dhis.android.core.common.FormType;
import org.hisp.dhis.android.core.common.IdentifiableObjectStore;
import org.hisp.dhis.android.core.common.ValueType;
import org.hisp.dhis.android.core.data.database.AbsStoreTestCase;
import org.hisp.dhis.android.core.dataelement.DataElementModel;
import org.hisp.dhis.android.core.dataelement.DataElementStore;
import org.hisp.dhis.android.core.enrollment.EnrollmentStoreImpl;
import org.hisp.dhis.android.core.event.EventStoreImpl;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitModel;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitStore;
import org.hisp.dhis.android.core.program.ProgramIndicatorModel;
import org.hisp.dhis.android.core.program.ProgramIndicatorStore;
import org.hisp.dhis.android.core.program.ProgramModel;
import org.hisp.dhis.android.core.program.ProgramStageModel;
import org.hisp.dhis.android.core.program.ProgramStageStore;
import org.hisp.dhis.android.core.program.ProgramStore;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeStoreImpl;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeValueStoreImpl;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValueStoreImpl;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceStore;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceStoreImpl;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityTypeModel;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityTypeStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class ProgramIndicatorEngineIntegrationShould extends AbsStoreTestCase {

    private String teiUid = "H87GEVeG3JH";
    private String enrollmentUid = "la16vwCoFM8";
    private String event1 = "gphKB0UjOrX";
    private String event2 = "EAZOUgr2Ksv";

    private String dataElement1 = "ddaBs9lgZyP";
    private String dataElement2 = "Kb9hZ428FyH";
    private String attribute1 = "Kmtdopp5GC1";
    private String programIndicatorUid = "rg3JkCv0skl";

    // Auxiliary variables
    private String orgunitUid = "orgunit_uid";
    private String teiTypeUid = "tei_type_uid";
    private String programUid = "program_uid";
    private String programStage1 = "iM4svLr2hlO";
    private String programStage2 = "RXFTSe1oefv";

    private ProgramIndicatorEngine programIndicatorEngine;

    @Override
    @Before
    public void setUp() throws IOException {
        super.setUp();

        programIndicatorEngine = ProgramIndicatorEngine.create(databaseAdapter());
        
        OrganisationUnitModel orgunit = OrganisationUnitModel.builder().uid(orgunitUid).build();
        OrganisationUnitStore.create(databaseAdapter()).insert(orgunit);

        TrackedEntityTypeModel trackedEntityTypeModel = TrackedEntityTypeModel.builder().uid(teiTypeUid).build();
        TrackedEntityTypeStore.create(databaseAdapter()).insert(trackedEntityTypeModel);

        TrackedEntityInstanceStore teiStore = new TrackedEntityInstanceStoreImpl(databaseAdapter());
        teiStore.insert(teiUid, new Date(), new Date(), null, null, orgunitUid, teiTypeUid, null, null,
                null);

        ProgramModel program = ProgramModel.builder().uid(programUid).build();
        ProgramStore.create(databaseAdapter()).insert(program);

        ProgramStageModel stage1 = ProgramStageModel.builder().uid(programStage1).program(programUid)
                .formType(FormType.CUSTOM).build();
        ProgramStageModel stage2 = ProgramStageModel.builder().uid(programStage2).program(programUid)
                .formType(FormType.CUSTOM).build();

        IdentifiableObjectStore<ProgramStageModel> programStageStore = ProgramStageStore.create(databaseAdapter());
        programStageStore.insert(stage1);
        programStageStore.insert(stage2);

        DataElementModel de1 = DataElementModel.builder().uid(dataElement1).valueType(ValueType.NUMBER).build();
        DataElementModel de2 = DataElementModel.builder().uid(dataElement2).valueType(ValueType.NUMBER).build();
        IdentifiableObjectStore<DataElementModel> dataElementStore = DataElementStore.create(databaseAdapter());
        dataElementStore.insert(de1);
        dataElementStore.insert(de2);

        new TrackedEntityAttributeStoreImpl(databaseAdapter()).insert(attribute1,null,null,null,null,null,null,
                null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);

    }

    @Test
    public void evaluate_single_dataelement() {
        createEnrollment(null,null);
        createEvent(event1, programStage1, new Date());
        insertTrackedEntityDataValue(event1, dataElement1, "4");

        setProgramIndicatorExpression(de(programStage1,dataElement1));

        String result = programIndicatorEngine.getProgramIndicatorValue(enrollmentUid, event1, programIndicatorUid);

        assertThat(result).isEqualTo("4");
    }

    @Test
    public void evaluate_addition_two_dataelement() {
        createEnrollment(null, null);
        createEvent(event1, programStage1, null);
        insertTrackedEntityDataValue(event1, dataElement1, "5");
        insertTrackedEntityDataValue(event1, dataElement2, "3");

        setProgramIndicatorExpression(de(programStage1,dataElement1) + " * " + de(programStage1,dataElement2));

        String result = programIndicatorEngine.getProgramIndicatorValue(enrollmentUid, event1, programIndicatorUid);

        assertThat(result).isEqualTo("15");
    }

    @Test
    public void evaluate_operation_several_stages() {
        createEnrollment(null, null);
        createEvent(event1, programStage1, null);
        createEvent(event2, programStage2, null);

        insertTrackedEntityDataValue(event1, dataElement1, "5");
        insertTrackedEntityDataValue(event2, dataElement2, "1.5");
        insertTrackedEntityAttributeValue(attribute1, "2");

        setProgramIndicatorExpression("(" + de(programStage1,dataElement1) + " + " + de(programStage2,dataElement2) +
                ") / " + att(attribute1));

        String resultWithoutEvent = programIndicatorEngine.getProgramIndicatorValue(enrollmentUid, null,
                programIndicatorUid);
        String resultWithEvent = programIndicatorEngine.getProgramIndicatorValue(enrollmentUid, event1,
                programIndicatorUid);

        assertThat(resultWithoutEvent).isEqualTo("3.25");
        assertThat(resultWithEvent).isEqualTo("3.25");
    }

    @Test
    public void evaluate_expression_with_d2_functions() {
        createEnrollment(null, null);
        createEvent(event1, programStage1, null);
        insertTrackedEntityDataValue(event1, dataElement1, "4.8");
        insertTrackedEntityDataValue(event1, dataElement2, "3");

        setProgramIndicatorExpression("d2:round(" + de(programStage1,dataElement1) + ") * " +
                de(programStage1, dataElement2));

        String result = programIndicatorEngine.getProgramIndicatorValue(enrollmentUid, event1, programIndicatorUid);

        assertThat(result).isEqualTo("15");
    }

    @Test
    public void evaluate_d2_functions_with_dates() throws ParseException {
        Date enrollmentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-05-05T00:00:00.000");
        Date eventDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-05-21T00:00:00.000");

        createEnrollment(enrollmentDate, null);
        createEvent(event1, programStage1, eventDate);

        setProgramIndicatorExpression("d2:daysBetween(V{enrollment_date}, V{event_date})");

        String result = programIndicatorEngine.getProgramIndicatorValue(enrollmentUid, event1, programIndicatorUid);

        assertThat(result).isEqualTo("16");
    }

    private void createEnrollment(Date enrollmentDate, Date incidentDate) {
        new EnrollmentStoreImpl(databaseAdapter()).insert(enrollmentUid,null,null,null,null,orgunitUid,
                programUid,enrollmentDate,incidentDate,null,null,teiUid,null,null,null);
    }

    private void createEvent(String eventUid, String programStageUid, Date eventDate) {
        new EventStoreImpl(databaseAdapter()).insert(eventUid,enrollmentUid,null,null,null,null,null,null,null,
                programUid,programStageUid,orgunitUid,eventDate,null,null,null,null,null,null);
    }

    private void setProgramIndicatorExpression(String expression) {
        ProgramIndicatorModel programIndicator = ProgramIndicatorModel.builder().uid(programIndicatorUid)
                .program(programUid).expression(expression).build();
        ProgramIndicatorStore.create(databaseAdapter()).insert(programIndicator);
    }

    private void insertTrackedEntityDataValue(String eventUid, String dataElementUid, String value) {
        new TrackedEntityDataValueStoreImpl(databaseAdapter()).insert(eventUid,null,null,dataElementUid,null,
                value,null);
    }

    private void insertTrackedEntityAttributeValue(String attributeUid, String value) {
        new TrackedEntityAttributeValueStoreImpl(databaseAdapter()).insert(value,null,null,attributeUid,teiUid);
    }

    private String de(String programStageUid, String dataElementUid) {
        return "#{" + programStageUid + "." + dataElementUid + "}";
    }

    private String att(String attributeUid) {
        return "A{" + attributeUid + "}";
    }
}
