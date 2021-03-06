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

package org.hisp.dhis.android.core.program;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import org.hisp.dhis.android.core.category.CategoryCombo;
import org.hisp.dhis.android.core.category.CategoryComboModel;
import org.hisp.dhis.android.core.common.Access;
import org.hisp.dhis.android.core.common.BaseNameableObject;
import org.hisp.dhis.android.core.common.DataAccess;
import org.hisp.dhis.android.core.common.ObjectStyle;
import org.hisp.dhis.android.core.common.ObjectWithUid;
import org.hisp.dhis.android.core.data.api.Field;
import org.hisp.dhis.android.core.data.api.Fields;
import org.hisp.dhis.android.core.data.api.NestedField;
import org.hisp.dhis.android.core.period.PeriodType;
import org.hisp.dhis.android.core.relationship.RelationshipType;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityType;

import java.util.Date;
import java.util.List;

import static org.hisp.dhis.android.core.utils.Utils.safeUnmodifiableList;

@AutoValue
public abstract class Program extends BaseNameableObject {
    private static final String VERSION = "version";
    private static final String ONLY_ENROLL_ONCE = "onlyEnrollOnce";
    private static final String ENROLLMENT_DATE_LABEL = "enrollmentDateLabel";
    private static final String DISPLAY_INCIDENT_DATE = "displayIncidentDate";
    private static final String INCIDENT_DATE_LABEL = "incidentDateLabel";
    private static final String REGISTRATION = "registration";
    private static final String SELECT_ENROLLMENT_DATES_IN_FUTURE = "selectEnrollmentDatesInFuture";
    private static final String DATA_ENTRY_METHOD = "dataEntryMethod";
    private static final String IGNORE_OVERDUE_EVENTS = "ignoreOverdueEvents";
    private static final String RELATIONSHIP_FROM_A = "relationshipFromA";
    private static final String SELECT_INCIDENT_DATES_IN_FUTURE = "selectIncidentDatesInFuture";
    private static final String CAPTURE_COORDINATES = "captureCoordinates";
    private static final String USE_FIRST_STAGE_DURING_REGISTRATION = "useFirstStageDuringRegistration";
    private static final String DISPLAY_FRONT_PAGE_LIST = "displayFrontPageList";
    private static final String PROGRAM_TYPE = "programType";
    private static final String RELATIONSHIP_TYPE = "relationshipType";
    private static final String RELATIONSHIP_TEXT = "relationshipText";
    private static final String PROGRAM_TRACKED_ENTITY_ATTRIBUTES = "programTrackedEntityAttributes";
    private static final String RELATED_PROGRAM = "relatedProgram";
    private static final String TRACKED_ENTITY_TYPE = "trackedEntityType";
    private static final String CATEGORY_COMBO = "categoryCombo";
    private static final String PROGRAM_INDICATORS = "programIndicators";
    private static final String PROGRAM_STAGES = "programStages";
    private static final String PROGRAM_RULES = "programRules";
    private static final String PROGRAM_RULE_VARIABLES = "programRuleVariables";
    private final static String ACCESS = "access";
    private final static String STYLE = "style";
    private final static String EXPIRY_DAYS = "expiryDays";
    private final static String COMPLETE_EVENTS_EXPIRY_DAYS = "completeEventsExpiryDays";
    private final static String EXPIRY_PERIOD_TYPE = "expiryPeriodType";
    private final static String MIN_ATTRIBUTES_REQUIRED_TO_SEARCH = "minAttributesRequiredToSearch";
    private final static String MAX_TEI_COUNT_TO_RETURN = "maxTeiCountToReturn";
    private final static String PROGRAM_SECTIONS = "programSections";

    static final Field<Program, String> uid = Field.create(UID);
    private static final Field<Program, String> code = Field.create(CODE);
    private static final Field<Program, String> name = Field.create(NAME);
    private static final Field<Program, String> displayName = Field.create(DISPLAY_NAME);
    private static final Field<Program, String> created = Field.create(CREATED);
    static final Field<Program, String> lastUpdated = Field.create(LAST_UPDATED);
    private static final Field<Program, Boolean> deleted = Field.create(DELETED);
    private static final Field<Program, String> shortName = Field.create(SHORT_NAME);
    private static final Field<Program, String> displayShortName = Field.create(DISPLAY_SHORT_NAME);
    private static final Field<Program, String> description = Field.create(DESCRIPTION);
    private static final Field<Program, String> displayDescription = Field.create(DISPLAY_DESCRIPTION);
    private static final Field<Program, Integer> version = Field.create(VERSION);
    private static final Field<Program, Boolean> onlyEnrollOnce = Field.create(ONLY_ENROLL_ONCE);
    private static final Field<Program, String> enrollmentDateLabel = Field.create(ENROLLMENT_DATE_LABEL);
    private static final Field<Program, Boolean> displayIncidentDate = Field.create(DISPLAY_INCIDENT_DATE);
    private static final Field<Program, String> incidentDateLabel = Field.create(INCIDENT_DATE_LABEL);
    private static final Field<Program, Boolean> registration = Field.create(REGISTRATION);
    private static final Field<Program, Boolean> selectEnrollmentDatesInFuture
            = Field.create(SELECT_ENROLLMENT_DATES_IN_FUTURE);
    private static final Field<Program, Boolean> dataEntryMethod = Field.create(DATA_ENTRY_METHOD);
    private static final Field<Program, Boolean> ignoreOverdueEvents = Field.create(IGNORE_OVERDUE_EVENTS);
    private static final Field<Program, Boolean> relationshipFromA = Field.create(RELATIONSHIP_FROM_A);
    private static final Field<Program, Boolean> selectIncidentDatesInFuture
            = Field.create(SELECT_INCIDENT_DATES_IN_FUTURE);
    private static final Field<Program, Boolean> captureCoordinates = Field.create(CAPTURE_COORDINATES);
    private static final Field<Program, Boolean> useFirstStageDuringRegistration
            = Field.create(USE_FIRST_STAGE_DURING_REGISTRATION);
    private static final Field<Program, Boolean> displayFrontPageList = Field.create(DISPLAY_FRONT_PAGE_LIST);
    private static final Field<Program, ProgramType> programType = Field.create(PROGRAM_TYPE);
    private static final Field<Program, String> relationshipText = Field.create(RELATIONSHIP_TEXT);
    private static final Field<Program, String> expiryDays = Field.create(EXPIRY_DAYS);
    private static final Field<Program, String> completeEventsExpiryDays
            = Field.create(COMPLETE_EVENTS_EXPIRY_DAYS);
    private static final Field<Program, String> expiryPeriodType = Field.create(EXPIRY_PERIOD_TYPE);
    private static final Field<Program, Integer> minAttributesRequiredToSearch =
            Field.create(MIN_ATTRIBUTES_REQUIRED_TO_SEARCH);
    private static final Field<Program, Integer> maxTeiCountToReturn =
            Field.create(MAX_TEI_COUNT_TO_RETURN);

    private static final NestedField<Program, ObjectWithUid> relationshipType
            = NestedField.create(RELATIONSHIP_TYPE);
    private static final NestedField<Program, ProgramTrackedEntityAttribute> programTrackedEntityAttributes
            = NestedField.create(PROGRAM_TRACKED_ENTITY_ATTRIBUTES);
    private static final NestedField<Program, Program> relatedProgram
            = NestedField.create(RELATED_PROGRAM);
    private static final NestedField<Program, ObjectWithUid> trackedEntityType
            = NestedField.create(TRACKED_ENTITY_TYPE);
    private static final NestedField<Program, ObjectWithUid> categoryCombo
            = NestedField.create(CATEGORY_COMBO);
    static final NestedField<Program, Access> access = NestedField.create(ACCESS);
    private static final NestedField<Program, ProgramIndicator> programIndicators
            = NestedField.create(PROGRAM_INDICATORS);
    private static final NestedField<Program, ObjectWithUid> programStages
            = NestedField.create(PROGRAM_STAGES);
    private static final NestedField<Program, ProgramRule> programRules = NestedField.create(PROGRAM_RULES);
    private static final NestedField<Program, ProgramRuleVariable> programRuleVariables
            = NestedField.create(PROGRAM_RULE_VARIABLES);
    private static final NestedField<Program, ObjectStyle> style = NestedField.create(STYLE);
    private static final NestedField<Program, ProgramSection> programSections = NestedField.create(PROGRAM_SECTIONS);

    static final Fields<Program> allFields = Fields.<Program>builder().fields(
            uid, code, name, displayName, created, lastUpdated, shortName, displayShortName, description,
            displayDescription, version, captureCoordinates, dataEntryMethod, deleted, displayFrontPageList,
            displayIncidentDate, enrollmentDateLabel, ignoreOverdueEvents, incidentDateLabel, onlyEnrollOnce,
            programType, registration, relationshipFromA, relationshipText, selectEnrollmentDatesInFuture,
            selectIncidentDatesInFuture, useFirstStageDuringRegistration, expiryDays, completeEventsExpiryDays,
            expiryPeriodType, minAttributesRequiredToSearch, maxTeiCountToReturn,
            relatedProgram.with(Program.uid), programStages.with(ObjectWithUid.uid),
            programRules.with(ProgramRule.allFields), programRuleVariables.with(ProgramRuleVariable.allFields),
            programIndicators.with(ProgramIndicator.allFields),
            programTrackedEntityAttributes.with(ProgramTrackedEntityAttribute.allFields),
            trackedEntityType.with(ObjectWithUid.uid), categoryCombo.with(ObjectWithUid.uid),
            relationshipType.with(ObjectWithUid.uid), access.with(Access.data.with(DataAccess.write)),
            style.with(ObjectStyle.allFields), programSections.with(ProgramSection.allFields)).build();

    @Nullable
    @JsonProperty(VERSION)
    public abstract Integer version();

    @Nullable
    @JsonProperty(ONLY_ENROLL_ONCE)
    public abstract Boolean onlyEnrollOnce();

    @Nullable
    @JsonProperty(ENROLLMENT_DATE_LABEL)
    public abstract String enrollmentDateLabel();

    @Nullable
    @JsonProperty(DISPLAY_INCIDENT_DATE)
    public abstract Boolean displayIncidentDate();

    @Nullable
    @JsonProperty(INCIDENT_DATE_LABEL)
    public abstract String incidentDateLabel();

    @Nullable
    @JsonProperty(REGISTRATION)
    public abstract Boolean registration();

    @Nullable
    @JsonProperty(SELECT_ENROLLMENT_DATES_IN_FUTURE)
    public abstract Boolean selectEnrollmentDatesInFuture();

    @Nullable
    @JsonProperty(DATA_ENTRY_METHOD)
    public abstract Boolean dataEntryMethod();

    @Nullable
    @JsonProperty(IGNORE_OVERDUE_EVENTS)
    public abstract Boolean ignoreOverdueEvents();

    @Nullable
    @JsonProperty(RELATIONSHIP_FROM_A)
    public abstract Boolean relationshipFromA();

    @Nullable
    @JsonProperty(SELECT_INCIDENT_DATES_IN_FUTURE)
    public abstract Boolean selectIncidentDatesInFuture();

    @Nullable
    @JsonProperty(CAPTURE_COORDINATES)
    public abstract Boolean captureCoordinates();

    @Nullable
    @JsonProperty(USE_FIRST_STAGE_DURING_REGISTRATION)
    public abstract Boolean useFirstStageDuringRegistration();

    @Nullable
    @JsonProperty(DISPLAY_FRONT_PAGE_LIST)
    public abstract Boolean displayFrontPageList();

    @Nullable
    @JsonProperty(PROGRAM_TYPE)
    public abstract ProgramType programType();

    @Nullable
    @JsonProperty(RELATIONSHIP_TYPE)
    public abstract RelationshipType relationshipType();

    String relationshipTypeUid() {
        RelationshipType relationshipType = relationshipType();
        return relationshipType == null ? null : relationshipType.uid();
    }

    @Nullable
    @JsonProperty(RELATIONSHIP_TEXT)
    public abstract String relationshipText();

    @Nullable
    @JsonProperty(PROGRAM_TRACKED_ENTITY_ATTRIBUTES)
    public abstract List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes();

    @Nullable
    @JsonProperty(RELATED_PROGRAM)
    public abstract Program relatedProgram();

    String relatedProgramUid() {
        Program relatedProgram = relatedProgram();
        return relatedProgram == null ? null : relatedProgram.uid();
    }

    @Nullable
    @JsonProperty(TRACKED_ENTITY_TYPE)
    public abstract TrackedEntityType trackedEntityType();

    String trackedEntityTypeUid() {
        TrackedEntityType trackedEntityType = trackedEntityType();
        return trackedEntityType == null ? null : trackedEntityType.uid();
    }

    @Nullable
    @JsonProperty(CATEGORY_COMBO)
    public abstract CategoryCombo categoryCombo();

    String categoryComboUid() {
        CategoryCombo combo = categoryCombo();
        return combo == null ? CategoryComboModel.DEFAULT_UID : combo.uid();
    }

    @Nullable
    @JsonProperty(ACCESS)
    public abstract Access access();

    @Nullable
    @JsonProperty(PROGRAM_INDICATORS)
    public abstract List<ProgramIndicator> programIndicators();

    @Nullable
    @JsonProperty(PROGRAM_STAGES)
    public abstract List<ObjectWithUid> programStages();

    @Nullable
    @JsonProperty(PROGRAM_RULES)
    public abstract List<ProgramRule> programRules();

    @Nullable
    @JsonProperty(PROGRAM_RULE_VARIABLES)
    public abstract List<ProgramRuleVariable> programRuleVariables();

    @Nullable
    @JsonProperty(STYLE)
    public abstract ObjectStyle style();

    @Nullable
    @JsonProperty(EXPIRY_DAYS)
    public abstract Integer expiryDays();

    @Nullable
    @JsonProperty(COMPLETE_EVENTS_EXPIRY_DAYS)
    public abstract Integer completeEventsExpiryDays();

    @Nullable
    @JsonProperty(EXPIRY_PERIOD_TYPE)
    public abstract PeriodType expiryPeriodType();

    @Nullable
    @JsonProperty(MIN_ATTRIBUTES_REQUIRED_TO_SEARCH)
    public abstract Integer minAttributesRequiredToSearch();

    @Nullable
    @JsonProperty(MAX_TEI_COUNT_TO_RETURN)
    public abstract Integer maxTeiCountToReturn();

    @Nullable
    @JsonProperty(PROGRAM_SECTIONS)
    public abstract List<ProgramSection> programSections();

    @JsonCreator
    public static Program create(
            @JsonProperty(UID) String uid,
            @JsonProperty(CODE) String code,
            @JsonProperty(NAME) String name,
            @JsonProperty(DISPLAY_NAME) String displayName,
            @JsonProperty(CREATED) Date created,
            @JsonProperty(LAST_UPDATED) Date lastUpdated,
            @JsonProperty(SHORT_NAME) String shortName,
            @JsonProperty(DISPLAY_SHORT_NAME) String displayShortName,
            @JsonProperty(DESCRIPTION) String description,
            @JsonProperty(DISPLAY_DESCRIPTION) String displayDescription,
            @JsonProperty(VERSION) Integer version,
            @JsonProperty(ONLY_ENROLL_ONCE) Boolean onlyEnrollOnce,
            @JsonProperty(ENROLLMENT_DATE_LABEL) String enrollmentDateLabel,
            @JsonProperty(DISPLAY_INCIDENT_DATE) Boolean displayIncidentDate,
            @JsonProperty(INCIDENT_DATE_LABEL) String incidentDateLabel,
            @JsonProperty(REGISTRATION) Boolean registration,
            @JsonProperty(SELECT_ENROLLMENT_DATES_IN_FUTURE) Boolean selectEnrollmentDatesInFuture,
            @JsonProperty(DATA_ENTRY_METHOD) Boolean dataEntryMethod,
            @JsonProperty(IGNORE_OVERDUE_EVENTS) Boolean ignoreOverdueEvents,
            @JsonProperty(RELATIONSHIP_FROM_A) Boolean relationshipFromA,
            @JsonProperty(SELECT_INCIDENT_DATES_IN_FUTURE) Boolean selectIncidentDatesInFuture,
            @JsonProperty(CAPTURE_COORDINATES) Boolean captureCoordinates,
            @JsonProperty(USE_FIRST_STAGE_DURING_REGISTRATION) Boolean useFirstStageDuringRegistration,
            @JsonProperty(DISPLAY_FRONT_PAGE_LIST) Boolean displayFrontPageList,
            @JsonProperty(PROGRAM_TYPE) ProgramType programType,
            @JsonProperty(RELATIONSHIP_TYPE) RelationshipType relationshipType,
            @JsonProperty(RELATIONSHIP_TEXT) String relationshipText,
            @JsonProperty(PROGRAM_TRACKED_ENTITY_ATTRIBUTES) List<ProgramTrackedEntityAttribute> attributes,
            @JsonProperty(RELATED_PROGRAM) Program relatedProgram,
            @JsonProperty(TRACKED_ENTITY_TYPE) TrackedEntityType trackedEntityType,
            @JsonProperty(CATEGORY_COMBO) CategoryCombo categoryCombo,
            @JsonProperty(ACCESS) Access access,
            @JsonProperty(PROGRAM_INDICATORS) List<ProgramIndicator> programIndicators,
            @JsonProperty(PROGRAM_STAGES) List<ObjectWithUid> programStages,
            @JsonProperty(PROGRAM_RULES) List<ProgramRule> programRules,
            @JsonProperty(PROGRAM_RULE_VARIABLES) List<ProgramRuleVariable> programRuleVariables,
            @JsonProperty(STYLE) ObjectStyle style,
            @JsonProperty(EXPIRY_DAYS) Integer expiryDays,
            @JsonProperty(COMPLETE_EVENTS_EXPIRY_DAYS) Integer completeEventsExpiryDays,
            @JsonProperty(EXPIRY_PERIOD_TYPE) PeriodType expiryPeriodType,
            @JsonProperty(MIN_ATTRIBUTES_REQUIRED_TO_SEARCH) Integer minAttributesRequiredToSearch,
            @JsonProperty(MAX_TEI_COUNT_TO_RETURN) Integer maxTeiCountToReturn,
            @JsonProperty(PROGRAM_SECTIONS) List<ProgramSection> programSections,
            @JsonProperty(DELETED) Boolean deleted) {

        return new AutoValue_Program(
                uid,
                code,
                name,
                displayName,
                created,
                lastUpdated,
                deleted,
                shortName,
                displayShortName,
                description,
                displayDescription,
                version,
                onlyEnrollOnce,
                enrollmentDateLabel,
                displayIncidentDate,
                incidentDateLabel,
                registration,
                selectEnrollmentDatesInFuture,
                dataEntryMethod,
                ignoreOverdueEvents,
                relationshipFromA,
                selectIncidentDatesInFuture,
                captureCoordinates,
                useFirstStageDuringRegistration,
                displayFrontPageList,
                programType,
                relationshipType,
                relationshipText,
                safeUnmodifiableList(attributes),
                relatedProgram,
                trackedEntityType,
                categoryCombo,
                access,
                safeUnmodifiableList(programIndicators),
                safeUnmodifiableList(programStages),
                safeUnmodifiableList(programRules),
                safeUnmodifiableList(programRuleVariables),
                style,
                expiryDays,
                completeEventsExpiryDays,
                expiryPeriodType,
                minAttributesRequiredToSearch,
                maxTeiCountToReturn,
                programSections);
    }
}
