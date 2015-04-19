package fr.wati.yacramanager.web.elfinder;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import cn.bluejoe.elfinder.service.FsService;
import cn.bluejoe.elfinder.service.FsServiceFactory;

@Component("fsServiceFactory")
public class DynamicFsServiceFactory implements FsServiceFactory {

	@Inject
	private UserFsVolumeProvider volumeProvider;

	@Inject
	private FileSystemService fileSystemService;

	@Override
	public FsService getFileService(HttpServletRequest paramHttpServletRequest,
			ServletContext paramServletContext) {
		fileSystemService.setVolumeProvider(volumeProvider);
		return fileSystemService;
	}

}
