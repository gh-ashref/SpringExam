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

package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Office;
import org.springframework.samples.petclinic.model.Vet;

@Profile("spring-data-jpa")
public class SpringDataOfficeRepositoryImpl implements OfficeRepositoryOverride {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public void delete(Office office) {

		List<Vet> vets = new ArrayList<Vet>();
		vets = this.em.createQuery("SELECT vet FROM Vet vet ").getResultList();
		for (Vet vet : vets) {
			this.em.createQuery("DELETE FROM Vet vet WHERE id=" + vet.getId().toString()).executeUpdate();
		}
		this.em.createQuery("DELETE FROM Office office WHERE id=" + office.getId()).executeUpdate();
	}

}
