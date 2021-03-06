package org.hisp.dhis.android.core.trackedentity;

import android.support.annotation.NonNull;

import org.hisp.dhis.android.core.common.ObjectWithoutUidStore;
import org.hisp.dhis.android.core.common.State;
import org.hisp.dhis.android.core.data.database.DatabaseAdapter;
import org.hisp.dhis.android.core.enrollment.Enrollment;
import org.hisp.dhis.android.core.enrollment.EnrollmentHandler;
import org.hisp.dhis.android.core.relationship.Relationship;
import org.hisp.dhis.android.core.relationship.RelationshipModel;
import org.hisp.dhis.android.core.relationship.RelationshipModelBuilder;
import org.hisp.dhis.android.core.relationship.RelationshipStore;

import java.util.Collection;
import java.util.List;

import static org.hisp.dhis.android.core.utils.Utils.isDeleted;

public class TrackedEntityInstanceHandler {
    private final TrackedEntityInstanceStore trackedEntityInstanceStore;
    private final ObjectWithoutUidStore<RelationshipModel> relationshipStore;
    private final TrackedEntityAttributeValueHandler trackedEntityAttributeValueHandler;
    private final EnrollmentHandler enrollmentHandler;

    public TrackedEntityInstanceHandler(
            @NonNull TrackedEntityInstanceStore trackedEntityInstanceStore,
            @NonNull ObjectWithoutUidStore relationshipStore,
            @NonNull TrackedEntityAttributeValueHandler trackedEntityAttributeValueHandler,
            @NonNull EnrollmentHandler enrollmentHandler) {
        this.trackedEntityInstanceStore = trackedEntityInstanceStore;
        this.relationshipStore = relationshipStore;
        this.trackedEntityAttributeValueHandler = trackedEntityAttributeValueHandler;
        this.enrollmentHandler = enrollmentHandler;
    }

    public void handle(@NonNull TrackedEntityInstance trackedEntityInstance) {
        if (trackedEntityInstance == null) {
            return;
        }

        if (isDeleted(trackedEntityInstance)) {
            trackedEntityInstanceStore.delete(trackedEntityInstance.uid());
        } else {
            int updatedRow = trackedEntityInstanceStore.update(
                    trackedEntityInstance.uid(), trackedEntityInstance.created(), trackedEntityInstance.lastUpdated(),
                    trackedEntityInstance.createdAtClient(), trackedEntityInstance.lastUpdatedAtClient(),
                    trackedEntityInstance.organisationUnit(), trackedEntityInstance.trackedEntityType(),
                    trackedEntityInstance.coordinates(), trackedEntityInstance.featureType(),
                    State.SYNCED, trackedEntityInstance.uid());

            if (updatedRow <= 0) {
                trackedEntityInstanceStore.insert(
                        trackedEntityInstance.uid(), trackedEntityInstance.created(),
                        trackedEntityInstance.lastUpdated(), trackedEntityInstance.createdAtClient(),
                        trackedEntityInstance.lastUpdatedAtClient(), trackedEntityInstance.organisationUnit(),
                        trackedEntityInstance.trackedEntityType(), trackedEntityInstance.coordinates(),
                        trackedEntityInstance.featureType(), State.SYNCED);
            }

            trackedEntityAttributeValueHandler.handle(
                    trackedEntityInstance.uid(),
                    trackedEntityInstance.trackedEntityAttributeValues());

            List<Enrollment> enrollments = trackedEntityInstance.enrollments();

            enrollmentHandler.handle(enrollments);

            RelationshipModelBuilder relationshipModelBuilder = new RelationshipModelBuilder();
            for (Relationship relationship : trackedEntityInstance.relationships()) {
                this.handle(relationship.relative());
                this.relationshipStore.updateOrInsertWhere(relationshipModelBuilder.buildModel(relationship));
            }
        }
    }

    public void handleMany(@NonNull Collection<TrackedEntityInstance> trackedEntityInstances) {
        for (TrackedEntityInstance trackedEntityInstance : trackedEntityInstances) {
            handle(trackedEntityInstance);
        }
    }

    public static TrackedEntityInstanceHandler create(DatabaseAdapter databaseAdapter) {
        return new TrackedEntityInstanceHandler(
                new TrackedEntityInstanceStoreImpl(databaseAdapter),
                RelationshipStore.create(databaseAdapter),
                TrackedEntityAttributeValueHandler.create(databaseAdapter),
                EnrollmentHandler.create(databaseAdapter)
        );
    }
}
