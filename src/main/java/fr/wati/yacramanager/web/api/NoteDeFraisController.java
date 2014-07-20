/**
 * 
 */
package fr.wati.yacramanager.web.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.wati.yacramanager.beans.Attachement;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.services.AttachementService;
import fr.wati.yacramanager.services.NoteDeFraisService;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsenceDTO;
import fr.wati.yacramanager.web.dto.NoteDeFraisDTO;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

/**
 * @author Rachid Ouattara
 *
 */
@RestController()
@RequestMapping("/app/api/frais")
public class NoteDeFraisController extends RestCrudControllerAdapter<NoteDeFraisDTO> {

	private static final Log LOG=LogFactory.getLog(NoteDeFraisController.class);
	
	@Autowired
	private NoteDeFraisService noteDeFraisService;
	
	@Autowired
	private AttachementService attachementService;
	
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody NoteDeFraisDTO read(@PathVariable("id") Long id) {
		return noteDeFraisService.map(noteDeFraisService.findOne(id));
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable("id") Long id, @RequestBody NoteDeFraisDTO dto) {
		NoteDeFrais findOne = noteDeFraisService.findOne(id);
		noteDeFraisService.save(dto.toNoteDeFrais(findOne));
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public ResponseWrapper<List<NoteDeFraisDTO>> getAll(@RequestParam(required=false) Integer page,@RequestParam(required=false) Integer size,@RequestParam(value="sort", required=false) Map<String, String> sort,@RequestParam(value="filter", required=false) Map<String, String> filter) {
		if(page==null){
			page=0;
		}
		if(size==null){
			size=100;
		}
		PageRequest pageable=null;
		if(sort!=null){
			List<Order> orders=new ArrayList<>();
			for(Entry<String, String> entry:sort.entrySet()){
				Order order=new Order("asc".equals(entry.getValue())?Direction.ASC:Direction.DESC, entry.getKey());
				orders.add(order);
			}
			if(!orders.isEmpty()){
				pageable=new PageRequest(page, size, new Sort(orders));
			}else {
				pageable=new PageRequest(page, size);
			}
		}else {
			pageable=new PageRequest(page, size);
		}
		Page<NoteDeFrais> findByEmploye = noteDeFraisService.findByEmploye(SecurityUtils.getConnectedUser(), pageable);
		 ResponseWrapper<List<NoteDeFraisDTO>> responseWrapper = new ResponseWrapper<List<NoteDeFraisDTO>>(noteDeFraisService.mapNoteDeFrais(findByEmploye),findByEmploye.getTotalElements());
		long startIndex=findByEmploye.getNumber()*size+1;
		long endIndex=startIndex+findByEmploye.getNumberOfElements()-1;
		responseWrapper.setStartIndex(startIndex);
		responseWrapper.setEndIndex(endIndex);
		return responseWrapper;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> create(@RequestBody NoteDeFraisDTO dto) {
		try {
			NoteDeFrais noteDeFrais = dto.toNoteDeFrais();
			noteDeFrais.setDate(new Date());
			noteDeFrais.setEmploye(SecurityUtils.getConnectedUser());
			if(!dto.getAttachementsIds().isEmpty()){
				List<Attachement> findAttachementsByIds = attachementService.findAttachementsByIds(dto.getAttachementsIds().toArray(new Long[dto.getAttachementsIds().size()]));
				for(Attachement attachement:findAttachementsByIds){
					noteDeFrais.addAttachement(attachement);
					attachement.setNoteDeFrais(noteDeFrais);
					noteDeFraisService.save(noteDeFrais);
					attachementService.update(attachement);
				}
			}else {
				noteDeFraisService.save(noteDeFrais);
			}
			
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception exception) {
			LOG.error(exception.getMessage(), exception);
			return new ResponseEntity<String>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@Override
	@RequestMapping(value = "/{id}",method=RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		noteDeFraisService.delete(id);
	}

	@RequestMapping(value = "/types", method = RequestMethod.GET)
	public @ResponseBody List<TypeAbsenceDTO> getTypeAbsences() {
		List<TypeAbsenceDTO> absenceDTOs=new ArrayList<>();
		for(TypeAbsence absence:TypeAbsence.values()){
			absenceDTOs.add(TypeAbsenceDTO.fromEnum(absence));
		}
		return absenceDTOs;
	}
}
