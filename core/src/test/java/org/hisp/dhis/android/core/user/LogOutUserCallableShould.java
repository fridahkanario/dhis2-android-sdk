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

package org.hisp.dhis.android.core.user;

import org.hisp.dhis.android.core.common.DeletableStore;
import org.hisp.dhis.android.core.common.IdentifiableObjectStore;
import org.hisp.dhis.android.core.common.ObjectStore;
import org.hisp.dhis.android.core.common.Unit;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class LogOutUserCallableShould {

    @Mock
    private IdentifiableObjectStore<UserModel> userStore;

    @Mock
    private UserCredentialsStore userCredentialsStore;

    @Mock
    private ObjectStore<UserOrganisationUnitLinkModel> userOrganisationUnitLinkStore;

    @Mock
    private AuthenticatedUserStore authenticatedUserStore;

    @Mock
    private IdentifiableObjectStore<OrganisationUnitModel> organisationUnitStore;

    private Callable<Unit> logOutUserCallable;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        List<DeletableStore> deletableStoreList = new ArrayList<>();
        deletableStoreList.add(userStore);
        deletableStoreList.add(userCredentialsStore);
        deletableStoreList.add(userOrganisationUnitLinkStore);
        deletableStoreList.add(authenticatedUserStore);
        deletableStoreList.add(organisationUnitStore);
        logOutUserCallable = new LogOutUserCallable(deletableStoreList
        );
    }

    @Test
    public void clear_tables_on_log_out() throws Exception {
        logOutUserCallable.call();

        verify(userStore).delete();
        verify(userCredentialsStore).delete();
        verify(userOrganisationUnitLinkStore).delete();
        verify(authenticatedUserStore).delete();
        verify(organisationUnitStore).delete();
    }
}
