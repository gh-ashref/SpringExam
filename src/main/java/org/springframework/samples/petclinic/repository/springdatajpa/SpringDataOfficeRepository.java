
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Office;
import org.springframework.samples.petclinic.repository.OfficeRepository;

@Profile("spring-data-jpa")
public interface SpringDataOfficeRepository extends OfficeRepository, Repository<Office, Integer>, OfficeRepositoryOverride {

}
