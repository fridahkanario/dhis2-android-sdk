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

package org.hisp.dhis.android.core.common;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public final class APICallExecutor {

    private final D2CallException.Builder exceptionBuilder = D2CallException
            .builder()
            .isHttpError(true);

    public <P> List<P> executePayloadCall(Call<Payload<P>> call) throws D2CallException {
        try {
            Response<Payload<P>> response = call.execute();
            if (response.isSuccessful()) {
                if (response.body() == null) {
                    throw responseException(response);
                } else {
                    return response.body().items();
                }
            } else {
                throw responseException(response);
            }
        } catch (IOException e) {
            throw ioException(e);

        }
    }

    public <P> P executeObjectCall(Call<P> call) throws D2CallException {
        try {
            Response<P> response = call.execute();
            if (response.isSuccessful()) {
                if (response.body() == null) {
                    throw responseException(response);
                } else {
                    return response.body();
                }
            } else {
                throw responseException(response);
            }
        } catch (IOException e) {
            throw ioException(e);
        }
    }

    private D2CallException responseException(Response<?> response) {
        return exceptionBuilder
                .errorCode(D2ErrorCode.API_UNSUCCESSFUL_RESPONSE)
                .httpErrorCode(response.code())
                .errorDescription("API call failed, response: " + response.toString())
                .build();
    }

    private D2CallException ioException(IOException e) {
        Log.e(this.getClass().getSimpleName(), e.toString());
        return exceptionBuilder
                .errorCode(D2ErrorCode.API_RESPONSE_PROCESS_ERROR)
                .errorDescription("API call threw IOException")
                .originalException(e)
                .build();
    }
}