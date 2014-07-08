/**
 * 
 */
package fr.wati.yacramanager.web.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.wati.yacramanager.beans.Attachement;
import fr.wati.yacramanager.services.AttachementService;

/**
 * @author Rachid Ouattara
 * 
 */
@RequestMapping("/rest/attachements")
@Controller
public class AttachementController {

	private static final Log LOG = LogFactory
			.getLog(AttachementController.class);
	@Autowired
	private AttachementService attachementService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Long> upload(@RequestParam("file") Part file) {
		try {
			if (file != null) {
				Attachement attachement = new Attachement();
				attachement.setDate(new Date());
				attachement.setName("attachements"+System.currentTimeMillis());
				attachement.setContent(IOUtils.toByteArray(file.getInputStream()));
				Long attachementId = attachementService
						.addAttachement(attachement);
				return new ResponseEntity<>(attachementId, HttpStatus.CREATED);
			}
			return new ResponseEntity<Long>(HttpStatus.NOT_MODIFIED);

		} catch (Exception exception) {
			LOG.error(exception.getMessage(), exception);
			return new ResponseEntity<Long>(-1L,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> getFile(@PathVariable("id") Long id,
			HttpServletResponse response) {
		try {

			List<Attachement> attachementsByIds = attachementService
					.findAttachementsByIds(id);
			if (!attachementsByIds.isEmpty()) {
				InputStream inputStream = new ByteArrayInputStream(attachementService.getAttachementContent(id));
				IOUtils.copy(inputStream, response.getOutputStream());
				response.setHeader("Content-Disposition",
	                       "attachment; filename=\"" + attachementsByIds.get(0).getName() + "\"");
				response.flushBuffer();
				return new ResponseEntity<String>(HttpStatus.FOUND);
			}
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		} catch (IOException ex) {
			LOG.info(ex.getMessage(),ex);
			throw new RuntimeException("IOError writing file to output stream");
		}

	}
}