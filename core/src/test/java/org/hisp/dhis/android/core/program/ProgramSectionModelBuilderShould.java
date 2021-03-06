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

import org.hisp.dhis.android.core.common.IdentifiableModelBuilderAbstractShould;
import org.hisp.dhis.android.core.common.ModelBuilder;
import org.hisp.dhis.android.core.common.ObjectStyle;
import org.hisp.dhis.android.core.common.ObjectWithUid;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.CODE;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.COLOR;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.CREATED;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.DELETED;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.DESCRIPTION;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.DISPLAY_NAME;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.ICON;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.LAST_UPDATED;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.NAME;
import static org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils.UID;

@RunWith(JUnit4.class)
public class ProgramSectionModelBuilderShould extends
        IdentifiableModelBuilderAbstractShould<ProgramSection, ProgramSectionModel> {

    @Override
    @Before
    public void setUp() throws IOException {
        super.setUp();
    }

    @Override
    protected ProgramSection buildPojo() {
        return ProgramSection.create(
                UID,
                CODE,
                NAME,
                DISPLAY_NAME,
                CREATED,
                LAST_UPDATED,
                DESCRIPTION,
                ObjectWithUid.create("program_uid"),
                null,
                2,
                ObjectStyle.create(COLOR, ICON),
                "form_name",
                DELETED);
    }

    @Override
    protected ModelBuilder<ProgramSection, ProgramSectionModel> modelBuilder() {
        return new ProgramSectionModelBuilder();
    }

    @Test
    public void copy_pojo_program_section_properties() {
        assertThat(model.description()).isEqualTo(pojo.description());
        assertThat(model.program()).isEqualTo(pojo.program().uid());
        assertThat(model.sortOrder()).isEqualTo(pojo.sortOrder());
        assertThat(model.formName()).isEqualTo(pojo.formName());
    }
}
