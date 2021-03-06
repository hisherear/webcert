package se.inera.webcert.persistence.legacy.repository;

import org.springframework.data.repository.CrudRepository;

import se.inera.webcert.persistence.legacy.model.MigreratMedcertIntyg;

/**
 * Repository for migrated Medcert certificate entities.
 *
 * @author nikpet
 */
public interface MigreratMedcertIntygRepository extends CrudRepository<MigreratMedcertIntyg, String> {

}
