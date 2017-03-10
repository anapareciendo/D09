/* StringToCardConverter.java
 *
 * Copyright (C) 2016 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Actor;

@Component
@Transactional
public class StringToActorConverter implements Converter<String, Actor> {


	@Autowired
	AdministratorRepository adminRepository;
	@Autowired
	CustomerRepository customerRepository;
	


	@Override
	public Actor convert(String text) {
		Actor result;
		int id;

		try {
			id = Integer.valueOf(text);
			
			result = adminRepository.findOne(id);
			
			if(result == null){
				result = customerRepository.findOne(id);
			}
			
			
		} catch (Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
