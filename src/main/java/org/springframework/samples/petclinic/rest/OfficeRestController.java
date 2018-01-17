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

package org.springframework.samples.petclinic.rest;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Office;
import org.springframework.samples.petclinic.repository.OfficeRepository;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api/office")
public class OfficeRestController {

	@Autowired
	private ClinicService clinicService;
	@Autowired
	private OfficeRepository officeRepository;
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Collection<Office>> getOffices() {
		Collection<Office> offices = officeRepository.findAll();
		if (offices.isEmpty()) {
			return new ResponseEntity<Collection<Office>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Office>>(offices, HttpStatus.OK);
	}

	@RequestMapping(value = "/{officeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Office> getOffice(@PathVariable("officeId") int officeId) {
		Office office = null;
		office = officeRepository.findById(officeId);
		if (office == null) {
			return new ResponseEntity<Office>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Office>(office, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Office> addOffice(@RequestBody @Valid Office office, BindingResult bindingResult,
			UriComponentsBuilder ucBuilder) {
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if (bindingResult.hasErrors() || (office == null)) {
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Office>(headers, HttpStatus.BAD_REQUEST);
		}
		officeRepository.save(office);
		headers.setLocation(ucBuilder.path("/api/offices/{id}").buildAndExpand(office.getId()).toUri());
		return new ResponseEntity<Office>(office, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{officeId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Office> updateOffice(@PathVariable("officeId") int officeId, @RequestBody @Valid Office office,
			BindingResult bindingResult, UriComponentsBuilder ucBuilder) {
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if (bindingResult.hasErrors() || (office == null)) {
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Office>(headers, HttpStatus.BAD_REQUEST);
		}
		Office currentOffice = officeRepository.findById(officeId);
		if (currentOffice == null) {
			return new ResponseEntity<Office>(HttpStatus.NOT_FOUND);
		}
		currentOffice.setTitle(office.getTitle());
		currentOffice.setAddress(office.getAddress());
		currentOffice.setVets(office.getVets());
		officeRepository.save(currentOffice);
		return new ResponseEntity<Office>(currentOffice, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{officeId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional
	public ResponseEntity<Void> deleteOffice(@PathVariable("officeId") int officeId) {
		Office office = officeRepository.findById(officeId);
		if (office == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		officeRepository.delete(office);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
