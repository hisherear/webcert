package se.inera.webcert.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.webcert.persistence.intyg.model.Utkast;
import se.inera.webcert.service.intyg.dto.IntygItem;
import se.inera.webcert.service.intyg.dto.IntygStatus;
import se.inera.webcert.service.intyg.dto.StatusType;
import se.inera.webcert.web.controller.api.dto.IntygSource;
import se.inera.webcert.web.controller.api.dto.ListIntygEntry;

public final class IntygDraftsConverter {

    private static final Logger LOG = LoggerFactory.getLogger(IntygDraftsConverter.class);

    private static Comparator<ListIntygEntry> intygEntryDateComparator = new Comparator<ListIntygEntry>() {

        @Override
        public int compare(ListIntygEntry ie1, ListIntygEntry ie2) {
            return ie1.getLastUpdatedSigned().compareTo(ie2.getLastUpdatedSigned());
        }

    };

    private static Comparator<IntygStatus> intygStatusComparator = new Comparator<IntygStatus>() {

        @Override
        public int compare(IntygStatus c1, IntygStatus c2) {
            return c1.getTimestamp().compareTo(c2.getTimestamp());
        }

    };

    private static Predicate removeArchivedIntygStatusesPredicate = new Predicate() {

        private final List<StatusType> archivedStatuses = Arrays.asList(StatusType.DELETED, StatusType.RESTORED);

        @Override
        public boolean evaluate(Object obj) {
            if (obj instanceof IntygStatus) {
                IntygStatus intygStatus = (IntygStatus) obj;
                return !archivedStatuses.contains(intygStatus.getType());
            }
            return false;
        }
    };

    private IntygDraftsConverter() {

    }

    public static List<ListIntygEntry> merge(List<IntygItem> intygList, List<Utkast> utkastList) {

        LOG.debug("Merging intyg, signed {}, drafts {}", intygList.size(), utkastList.size());

        List<ListIntygEntry> listIntygEntries = new ArrayList<ListIntygEntry>();

        ListIntygEntry intygEntry;

        // add all signed intyg
        for (IntygItem cert : intygList) {
            intygEntry = convertIntygItemToListIntygEntry(cert);
            listIntygEntries.add(intygEntry);
        }

        // add alldrafts
        for (Utkast intyg : utkastList) {
            intygEntry = convertUtkastToListIntygEntry(intyg);
            listIntygEntries.add(intygEntry);
        }

        // sort according to signedUpdate date and then reverse so that last is on top.
        Collections.sort(listIntygEntries, intygEntryDateComparator);
        Collections.reverse(listIntygEntries);

        return listIntygEntries;
    }

    public static List<ListIntygEntry> convertUtkastsToListIntygEntries(List<Utkast> utkastList) {

        List<ListIntygEntry> listIntygEntries = new ArrayList<ListIntygEntry>();

        ListIntygEntry intygEntry;

        for (Utkast cert : utkastList) {
            intygEntry = convertUtkastToListIntygEntry(cert);
            listIntygEntries.add(intygEntry);
        }

        Collections.sort(listIntygEntries, intygEntryDateComparator);
        Collections.reverse(listIntygEntries);

        return listIntygEntries;
    }

    public static ListIntygEntry convertUtkastToListIntygEntry(Utkast utkast) {

        ListIntygEntry entry = new ListIntygEntry();

        entry.setIntygId(utkast.getIntygsId());
        entry.setIntygType(utkast.getIntygsTyp());
        entry.setSource(IntygSource.WC);
        entry.setUpdatedSignedBy(utkast.getSenastSparadAv().getNamn());
        entry.setLastUpdatedSigned(utkast.getSenastSparadDatum());
        entry.setPatientId(utkast.getPatientPersonnummer());
        entry.setVidarebefordrad(utkast.getVidarebefordrad());
        entry.setStatus(utkast.getStatus().toString());

        return entry;
    }

    public static ListIntygEntry convertIntygItemToListIntygEntry(IntygItem intygItem) {

        ListIntygEntry entry = new ListIntygEntry();

        entry.setIntygId(intygItem.getId());
        entry.setIntygType(intygItem.getType());
        entry.setStatus(findLatestStatus(intygItem.getStatuses()).toString());
        entry.setSource(IntygSource.IT);
        entry.setLastUpdatedSigned(intygItem.getSignedDate());
        entry.setUpdatedSignedBy(intygItem.getSignedBy());

        return entry;
    }

    public static StatusType findLatestStatus(List<IntygStatus> intygStatuses) {

        if (intygStatuses == null || intygStatuses.isEmpty()) {
            return StatusType.UNKNOWN;
        }

        CollectionUtils.filter(intygStatuses, removeArchivedIntygStatusesPredicate);

        if (intygStatuses.isEmpty()) {
            return StatusType.UNKNOWN;
        }

        IntygStatus latestStatus = Collections.max(intygStatuses, intygStatusComparator);
        return latestStatus.getType();
    }
}
