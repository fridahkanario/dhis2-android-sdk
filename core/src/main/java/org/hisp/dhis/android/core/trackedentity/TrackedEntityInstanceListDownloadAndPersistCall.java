package org.hisp.dhis.android.core.trackedentity;

import android.support.annotation.NonNull;

import org.hisp.dhis.android.core.calls.Call;
import org.hisp.dhis.android.core.common.D2CallException;
import org.hisp.dhis.android.core.common.D2CallExecutor;
import org.hisp.dhis.android.core.common.SyncCall;
import org.hisp.dhis.android.core.data.database.DatabaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Retrofit;

public final class TrackedEntityInstanceListDownloadAndPersistCall extends SyncCall<List<TrackedEntityInstance>> {

    private final DatabaseAdapter databaseAdapter;
    private final Retrofit retrofit;

    private final Collection<String> trackedEntityInstanceUids;

    private TrackedEntityInstanceListDownloadAndPersistCall(
            @NonNull DatabaseAdapter databaseAdapter,
            @NonNull Retrofit retrofit,
            @NonNull Collection<String> trackedEntityInstanceUids) {
        this.databaseAdapter = databaseAdapter;
        this.retrofit = retrofit;
        this.trackedEntityInstanceUids = trackedEntityInstanceUids;
    }

    @Override
    public List<TrackedEntityInstance> call() throws D2CallException {
        setExecuted();

        if (trackedEntityInstanceUids == null) {
            throw D2CallException.builder().isHttpError(false).errorDescription("UID list null").build();
        }

        List<TrackedEntityInstance> teis = new ArrayList<>();
        D2CallExecutor executor = new D2CallExecutor();
        for (String uid: trackedEntityInstanceUids) {
            Call<TrackedEntityInstance> teiCall =
                    TrackedEntityInstanceDownloadByUidEndPointCall.create(retrofit, uid);
            teis.add(executor.executeD2Call(teiCall));
        }

        executor.executeD2Call(TrackedEntityInstancePersistenceCall.create(databaseAdapter, retrofit, teis));

        return teis;
    }

    public static Call<List<TrackedEntityInstance>> create(DatabaseAdapter databaseAdapter,
                                                           Retrofit retrofit,
                                                           Collection<String> trackedEntityInstanceUids) {
        return new TrackedEntityInstanceListDownloadAndPersistCall(
                databaseAdapter,
                retrofit,
                trackedEntityInstanceUids
        );
    }
}
