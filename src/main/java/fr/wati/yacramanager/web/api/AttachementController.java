/**
 * 
 */
package fr.wati.yacramanager.web.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Attachement;
import fr.wati.yacramanager.services.AttachementService;

/**
 * @author Rachid Ouattara
 * 
 */
@RequestMapping("/app/api/attachements")
@Controller
public class AttachementController {

	private static final Log LOG = LogFactory
			.getLog(AttachementController.class);
	@Inject
	private AttachementService attachementService;

	@RequestMapping(method = RequestMethod.POST)
	@Timed
	public ResponseEntity<Long> upload(@RequestParam("file") MultipartFile file) {
		try {
			if (file != null) {
				Attachement attachement = new Attachement();
				attachement.setDate(new DateTime());
				attachement.setName(file.getOriginalFilename());
				attachement.setContentType(file.getContentType());
				attachement.setSize(file.getSize());
				attachement.setContent(IOUtils.toByteArray(file
						.getInputStream()));
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
	@Timed
	public void getFile(@PathVariable("id") Long id,HttpServletResponse response) throws RestServiceException {
		try {
			List<Attachement> attachementsByIds = attachementService
					.findAttachementsByIds(id);
			if (!attachementsByIds.isEmpty()) {
				InputStream inputStream = new ByteArrayInputStream(attachementService.getAttachementContent(id));
				
				response.setContentType(attachementsByIds.get(0).getContentType());
				String fileName=attachementsByIds.get(0).getName();
				if (fileName != null) {
                    response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
                }
				IOUtils.copy(inputStream, response.getOutputStream());
				//response.flushBuffer();
			}
		} catch (IOException ex) {
			LOG.info(ex.getMessage(),ex);
			throw new RestServiceException("IOError writing file to output stream");
		}
	}
}
