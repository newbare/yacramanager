package fr.wati.yacramanager.web.elfinder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.bluejoe.elfinder.service.FsService;
import cn.bluejoe.elfinder.service.FsServiceFactory;

@Component("fsServiceFactory")
public class DynamicFsServiceFactory implements FsServiceFactory {

	@Autowired
	private UserFsVolumeProvider volumeProvider;

	@Autowired
	private FileSystemService fileSystemService;

	@Override
	public FsService getFileService(HttpServletRequest paramHttpServletRequest,
			ServletContext paramServletContext) {
		fileSystemService.setVolumeProvider(volumeProvider);
		return fileSystemService;
	}

}
