/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Fedoriv
 *
 */

@Repository
@Profile("jpa")
public class JpaSpecialtyRepositoryImpl implements SpecialtyRepository {
	
    private EntityManager em;

	@Override
	public Specialty findById(int id){
//		if(this.em.find(Specialty.class, id) == null){
//			return new Specialty();
//		}
		return em.find(Specialty.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Specialty> findAll() throws DataAccessException {
		return this.em.createQuery("SELECT Specialty FROM Specialty specialty").getResultList();
	}

	@Override
	public void save(Specialty specialty) throws DataAccessException {
		if (specialty.getId() == null) {
            this.em.persist(specialty);
        } else {
            this.em.merge(specialty);
        }
	}

	@Override
	public void delete(Specialty specialty) throws DataAccessException {
		this.em.remove(this.em.contains(specialty) ? specialty : this.em.merge(specialty));
		String specId = specialty.getId().toString();
		this.em.createNativeQuery("DELETE FROM vet_specialties WHERE specialty_id=" + specId).executeUpdate();
		this.em.createQuery("DELETE FROM Specialty specialty WHERE id=" + specId).executeUpdate();
	}

}