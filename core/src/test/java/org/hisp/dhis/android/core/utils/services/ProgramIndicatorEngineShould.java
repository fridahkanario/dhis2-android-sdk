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

import org.hisp.dhis.android.core.common.IdentifiableObjectStore;
import org.hisp.dhis.android.core.common.ValueType;
import org.hisp.dhis.android.core.constant.ConstantModel;
import org.hisp.dhis.android.core.dataelement.DataElementModel;
import org.hisp.dhis.android.core.enrollment.EnrollmentModel;
import org.hisp.dhis.android.core.enrollment.EnrollmentStore;
import org.hisp.dhis.android.core.event.EventModel;
import org.hisp.dhis.android.core.event.EventStore;
import org.hisp.dhis.android.core.program.ProgramIndicatorModel;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeValue;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeValueStore;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValue;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValueStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProgramIndicatorEngineShould {

    private String enrollmentUid = "enrollment-uid";
    private String eventUid1 = "qCUMGmMZAhz";
    private String eventUid2 = "QApcv9Je3bp";
    private String programIndicatorUid = "program-indicator-uid";
    private String trackedEntityInstanceUid = "tei-uid";

    @Mock
    private EventModel event1;

    @Mock
    private EventModel event2;

    @Mock
    private EnrollmentModel enrollmentModel;

    @Mock
    private ProgramIndicatorModel programIndicator;

    @Mock
    private TrackedEntityDataValue value1;

    @Mock
    private TrackedEntityDataValue value2;

    @Mock
    private TrackedEntityDataValue value3;

    @Mock
    private TrackedEntityDataValue value4;

    @Mock
    private TrackedEntityAttributeValue attributeValue;

    @Mock
    private DataElementModel dataElementModel;

    private String dataElementUid1 = "HhyfnvrrKpN";
    private String dataElementUid2 = "nM4RZkpgMcP";
    private String dataElementUid3 = "vJeQc8NlWu6";
    private String dataElementUid4 = "jnyUQYDwj7K";

    private String attributeUid = "UQ0qSNHEpLt";

    private String programStageUid1 = "un3rUMhluNu";
    private String programStageUid2 = "hr7jRePpYMD";

    @Mock
    private ConstantModel constantModel;

    private String constantUid1 = "gzlRs2HEGAf";

    @Mock
    private IdentifiableObjectStore<ProgramIndicatorModel> programIndicatorStore;
    @Mock
    private TrackedEntityDataValueStore trackedEntityDataValueStore;
    @Mock
    private EnrollmentStore enrollmentStore;
    @Mock
    private EventStore eventStore;
    @Mock
    private IdentifiableObjectStore<DataElementModel> dataElementStore;
    @Mock
    private IdentifiableObjectStore<ConstantModel> constantStore;
    @Mock
    private TrackedEntityAttributeValueStore trackedEntityAttributeValueStore;

    // Object to test
    private ProgramIndicatorEngine programIndicatorEngine;

    @Before
    public void setUp() throws Exception {

        programIndicatorEngine = new ProgramIndicatorEngine(programIndicatorStore, trackedEntityDataValueStore,
                enrollmentStore, eventStore, dataElementStore, constantStore, trackedEntityAttributeValueStore);

        when(value1.dataElement()).thenReturn(dataElementUid1);
        when(value2.dataElement()).thenReturn(dataElementUid2);
        when(value3.dataElement()).thenReturn(dataElementUid3);
        when(value4.dataElement()).thenReturn(dataElementUid4);
        when(attributeValue.trackedEntityAttribute()).thenReturn(attributeUid);

        when(trackedEntityDataValueStore.queryTrackedEntityDataValues(eventUid1))
                .thenReturn(Arrays.asList(value1, value2, value3));
        when(trackedEntityDataValueStore.queryTrackedEntityDataValues(eventUid2))
                .thenReturn(Collections.singletonList(value4));

        when(event1.uid()).thenReturn(eventUid1);
        when(event1.programStage()).thenReturn(programStageUid1);
        when(eventStore.queryByUid(eventUid1)).thenReturn(event1);
        when(eventStore.queryByEnrollmentAndProgramStage(enrollmentUid, programStageUid1)).thenReturn(event1);

        when(event2.uid()).thenReturn(eventUid2);
        when(event2.programStage()).thenReturn(programStageUid2);
        when(eventStore.queryByUid(eventUid2)).thenReturn(event2);
        when(eventStore.queryByEnrollmentAndProgramStage(enrollmentUid, programStageUid2)).thenReturn(event2);

        when(enrollmentModel.uid()).thenReturn(enrollmentUid);
        when(enrollmentModel.trackedEntityInstance()).thenReturn(trackedEntityInstanceUid);
        when(enrollmentStore.queryByUid(enrollmentUid)).thenReturn(enrollmentModel);

        when(programIndicatorStore.selectByUid(programIndicatorUid, ProgramIndicatorModel.factory)).thenReturn
                (programIndicator);

        when(dataElementModel.valueType()).thenReturn(ValueType.NUMBER);
        when(dataElementStore.selectByUid(dataElementUid1, DataElementModel.factory)).thenReturn(dataElementModel);
        when(dataElementStore.selectByUid(dataElementUid2, DataElementModel.factory)).thenReturn(dataElementModel);
        when(dataElementStore.selectByUid(dataElementUid3, DataElementModel.factory)).thenReturn(dataElementModel);
        when(dataElementStore.selectByUid(dataElementUid4, DataElementModel.factory)).thenReturn(dataElementModel);

        when(trackedEntityAttributeValueStore.queryByTrackedEntityInstance(trackedEntityInstanceUid))
                .thenReturn(Collections.singletonList(attributeValue));

        when(constantModel.uid()).thenReturn(constantUid1);
        when(constantStore.selectByUid(constantUid1, ConstantModel.factory)).thenReturn(constantModel);
    }

    @Test
    public void parse_static_value() throws Exception {
        when(programIndicator.expression()).thenReturn("5 * 10");

        String result = programIndicatorEngine.parseIndicatorExpression(enrollmentUid, null,
                programIndicatorUid);

        assertThat(result).isEqualTo("5 * 10");
    }

    @Test
    public void parse_one_dataelement() throws Exception {
        when(programIndicator.expression()).thenReturn(de(programStageUid1, dataElementUid1));

        when(value1.value()).thenReturn("3.5");

        String result = programIndicatorEngine.parseIndicatorExpression(null, eventUid1, programIndicatorUid);

        assertThat(result).isEqualTo("3.5");
    }

    @Test
    public void parse_operation_two_dataelements() throws Exception {
        when(programIndicator.expression()).thenReturn(
                de(programStageUid1, dataElementUid1) + " + " + de(programStageUid1, dataElementUid2));

        when(value1.value()).thenReturn("3.5");
        when(value2.value()).thenReturn("2");

        String result = programIndicatorEngine.parseIndicatorExpression(null, eventUid1, programIndicatorUid);

        assertThat(result).isEqualTo("3.5 + 2.0");
    }

    @Test
    public void parse_operation_with_parenthesis() throws Exception {
        when(programIndicator.expression()).thenReturn(
                "(" + de(programStageUid1, dataElementUid1) + " + " + de(programStageUid1, dataElementUid2) +
                ") / " + de(programStageUid1, dataElementUid3));

        when(value1.value()).thenReturn("2.5");
        when(value2.value()).thenReturn("2");
        when(value3.value()).thenReturn("1.5");

        String result = programIndicatorEngine.parseIndicatorExpression(null, eventUid1, programIndicatorUid);

        assertThat(result).isEqualTo("(2.5 + 2.0) / 1.5");
    }

    @Test
    public void parse_dataelement_and_constant() throws Exception {
        when(programIndicator.expression()).thenReturn(
                de(programStageUid1, dataElementUid1) + " + " + cons(constantUid1));

        when(value1.value()).thenReturn("3.5");
        when(constantModel.value()).thenReturn("2");

        String result = programIndicatorEngine.parseIndicatorExpression(null, eventUid1, programIndicatorUid);

        assertThat(result).isEqualTo("3.5 + 2");
    }

    @Test
    public void parse_enrollment_date_variable() throws Exception {
        when(programIndicator.expression()).thenReturn(var("enrollment_date"));

        Date enrollmentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-05-05T00:00:00.000");
        when(enrollmentModel.dateOfEnrollment()).thenReturn(enrollmentDate);

        String result = programIndicatorEngine.parseIndicatorExpression(enrollmentUid, null, programIndicatorUid);

        assertThat(result).isEqualTo("\"2018-05-05\"");
    }

    @Test
    public void parse_incident_date_variable() throws Exception {
        when(programIndicator.expression()).thenReturn(var("incident_date"));

        Date incidentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-05-05T00:00:00.000");
        when(enrollmentModel.dateOfIncident()).thenReturn(incidentDate);

        String result = programIndicatorEngine.parseIndicatorExpression(enrollmentUid, null, programIndicatorUid);

        assertThat(result).isEqualTo("\"2018-05-05\"");
    }

    @Test
    public void parse_event_date_variable() throws Exception {
        when(programIndicator.expression()).thenReturn(var("event_date"));

        Date eventDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-05-05T00:00:00.000");
        when(event1.eventDate()).thenReturn(eventDate);

        String result = programIndicatorEngine.parseIndicatorExpression(enrollmentUid, eventUid1, programIndicatorUid);

        assertThat(result).isEqualTo("\"2018-05-05\"");
    }

    @Test
    public void parse_value_count_variable() throws Exception {
        when(programIndicator.expression()).thenReturn(
                "(" + de(programStageUid1, dataElementUid1) + " + " + de(programStageUid1, dataElementUid2) +
                        ") / " + var("value_count"));

        when(value1.value()).thenReturn("3.5");
        when(value2.value()).thenReturn("2");

        String result = programIndicatorEngine.parseIndicatorExpression(null, eventUid1, programIndicatorUid);

        assertThat(result).isEqualTo("(3.5 + 2.0) / 2");
    }

    @Test
    public void parse_tracked_entity_attribute() {
        when(programIndicator.expression()).thenReturn(att(attributeUid));

        when(attributeValue.value()).thenReturn("1989");

        String result = programIndicatorEngine.parseIndicatorExpression(enrollmentUid, null, programIndicatorUid);

        assertThat(result).isEqualTo("1989");
    }

    @Test
    public void do_not_parse_d2_functions() {
        when(programIndicator.expression()).thenReturn("d2:floor(" + de(programStageUid1, dataElementUid1) + ")");

        when(value1.value()).thenReturn("3.5");

        String result = programIndicatorEngine.parseIndicatorExpression(null, eventUid1, programIndicatorUid);

        assertThat(result).isEqualTo("d2:floor(3.5)");
    }

    @Test
    public void parse_values_from_different_program_stage_instances() {
        when(programIndicator.expression()).thenReturn(
                de(programStageUid1, dataElementUid1) + " + " + de(programStageUid2, dataElementUid4));

        when(value1.value()).thenReturn("3.5");
        when(value4.value()).thenReturn("2");

        String resultWithoutEvent = programIndicatorEngine.parseIndicatorExpression(enrollmentUid, null,
                programIndicatorUid);
        String resultWithEvent1 = programIndicatorEngine.parseIndicatorExpression(enrollmentUid, eventUid1,
                programIndicatorUid);
        String resultWithEvent2 = programIndicatorEngine.parseIndicatorExpression(enrollmentUid, eventUid2,
                programIndicatorUid);

        assertThat(resultWithoutEvent).isEqualTo("3.5 + 2.0");
        assertThat(resultWithEvent1).isEqualTo("3.5 + 2.0");
        assertThat(resultWithEvent2).isEqualTo("3.5 + 2.0");
    }

    @Test
    public void ignore_values_from_non_existing_events() {
        when(programIndicator.expression()).thenReturn(
                de(programStageUid1, dataElementUid1) + " + " + de(programStageUid2, dataElementUid4));

        when(value1.value()).thenReturn("3.5");

        // Event2 does not exist
        when(eventStore.queryByUid(eventUid2)).thenReturn(null);
        when(eventStore.queryByEnrollmentAndProgramStage(enrollmentUid, programStageUid2)).thenReturn(null);

        String resultWithoutEvent = programIndicatorEngine.parseIndicatorExpression(enrollmentUid, null,
                programIndicatorUid);
        String resultWithEvent1 = programIndicatorEngine.parseIndicatorExpression(enrollmentUid, eventUid1,
                programIndicatorUid);

        assertThat(resultWithoutEvent).isEqualTo("3.5 + 0");
        assertThat(resultWithEvent1).isEqualTo("3.5 + 0");
    }

    @Test
    public void parse_operation_with_zero_values() throws Exception {
        when(programIndicator.expression()).thenReturn(
                de(programStageUid2, dataElementUid2) + " * 10");

        // Event2 does not exist
        when(eventStore.queryByUid(eventUid2)).thenReturn(null);
        when(eventStore.queryByEnrollmentAndProgramStage(enrollmentUid, programStageUid2)).thenReturn(null);

        String result = programIndicatorEngine.parseIndicatorExpression(enrollmentUid, null,
                programIndicatorUid);

        assertThat(result).isEqualTo("0 * 10");
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private String de(String programStageUid, String dataElementUid) {
        return "#{" + programStageUid + "." + dataElementUid + "}";
    }

    private String cons(String constantUid) {
        return "C{" + constantUid + "}";
    }

    private String var(String variable) {
        return "V{" + variable + "}";
    }

    private String att(String attributeUid) {
        return "A{" + attributeUid + "}";
    }
}