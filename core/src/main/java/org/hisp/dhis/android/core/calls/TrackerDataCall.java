package org.hisp.dhis.android.core.calls;

import android.support.annotation.NonNull;

import org.hisp.dhis.android.core.common.D2CallException;
import org.hisp.dhis.android.core.common.D2CallExecutor;
import org.hisp.dhis.android.core.common.SyncCall;
import org.hisp.dhis.android.core.data.database.DatabaseAdapter;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceListDownloadAndPersistCall;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceStore;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceStoreImpl;

import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;

public final class TrackerDataCall extends SyncCall<List<TrackedEntityInstance>> {

    private final DatabaseAdapter databaseAdapter;
    private final Retrofit retrofit;
    private final TrackedEntityInstanceStore trackedEntityInstanceStore;

    private TrackerDataCall(@NonNull DatabaseAdapter databaseAdapter,
                            @NonNull Retrofit retrofit,
                            @NonNull TrackedEntityInstanceStore trackedEntityInstanceStore) {
        this.databaseAdapter = databaseAdapter;
        this.retrofit = retrofit;
        this.trackedEntityInstanceStore = trackedEntityInstanceStore;
    }

    @Override
    public List<TrackedEntityInstance> call() throws D2CallException {
        setExecuted();
        Map<String, TrackedEntityInstance> trackedEntityInstances = trackedEntityInstanceStore.querySynced();
        Call<List<TrackedEntityInstance>> call = TrackedEntityInstanceListDownloadAndPersistCall
                .create(databaseAdapter, retrofit, trackedEntityInstances.keySet());
        return new D2CallExecutor().executeD2Call(call);
    }

    public static TrackerDataCall create(DatabaseAdapter databaseAdapter, Retrofit retrofit) {
        return new TrackerDataCall(
                databaseAdapter,
                retrofit,
                new TrackedEntityInstanceStoreImpl(databaseAdapter)
        );
    }
}
