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

import org.hisp.dhis.android.core.calls.Call;
import org.hisp.dhis.android.core.common.BaseCallShould;
import org.hisp.dhis.android.core.common.D2CallException;
import org.hisp.dhis.android.core.common.GenericHandler;
import org.hisp.dhis.android.core.data.api.Fields;
import org.hisp.dhis.android.core.resource.ResourceModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UserCallShould extends BaseCallShould {

    @Mock
    private UserService userService;

    @Mock
    private GenericHandler<User, UserModel> userHandler;

    @Mock
    private retrofit2.Call<User> userCall;

    @Mock
    private User user;

    private Call<User> userSyncCall;

    @Override
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        super.setUp();
        userSyncCall = new UserCall(genericCallData, userService, userHandler);
        when(userService.getUser(any(Fields.class))).thenReturn(userCall);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void not_invoke_stores_on_call_io_exception() throws IOException {
        when(userCall.execute()).thenThrow(IOException.class);

        try {
            userSyncCall.call();
            fail("Exception was not thrown");
        } catch (Exception ex) {

            // verify that handlers was not touched
            verify(databaseAdapter, never()).beginNewTransaction();
            verify(transaction, never()).setSuccessful();
            verify(transaction, never()).end();

            verify(userHandler, never()).handle(eq(user), any(UserModelBuilder.class));
        }
    }

    @Test
    public void not_invoke_handler_after_call_failure() throws Exception {
        // unauthorized
        when(userCall.execute()).thenReturn(Response.<User>error(HttpURLConnection.HTTP_UNAUTHORIZED,
                ResponseBody.create(MediaType.parse("application/json"), "{}")));

        try {
            userSyncCall.call();
            fail("Call should't succeed");
        } catch (D2CallException d2Exception) {
        }

        // verify that database was not touched
        verify(databaseAdapter, never()).beginNewTransaction();
        verify(transaction, never()).setSuccessful();
        verify(transaction, never()).end();
        verify(userHandler, never()).handle(eq(user), any(UserModelBuilder.class));
    }

    @Test
    public void mark_as_executed_on_success() throws Exception {
        when(userCall.execute()).thenReturn(Response.success(user));

        userSyncCall.call();

        assertThat(userSyncCall.isExecuted()).isEqualTo(true);

        try {
            userSyncCall.call();

            fail("Two calls to the userSyncCall should throw exception");
        } catch (Exception exception) {
            // ignore exception
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void mark_as_executed_on_failure() throws Exception {
        when(userCall.execute()).thenThrow(IOException.class);

        try {
            userSyncCall.call();
        } catch (D2CallException d2CallException) {
            // swallow exception
        }

        assertThat(userSyncCall.isExecuted()).isEqualTo(true);

        try {
            userSyncCall.call();

            fail("Two calls to the userSyncCall should throw exception");
        } catch (Exception exception) {
            // ignore exception
        }
    }

    @Test
    public void invoke_handlers_on_success() throws Exception {
        when(userCall.execute()).thenReturn(Response.success(user));
        userSyncCall.call();
        verify(userHandler).handle(eq(user), any(UserModelBuilder.class));
        verify(resourceHandler).handleResource(ResourceModel.Type.USER, serverDate);
    }
}
