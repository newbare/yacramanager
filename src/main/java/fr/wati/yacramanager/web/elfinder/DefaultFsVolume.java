package fr.wati.yacramanager.web.elfinder;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import cn.bluejoe.elfinder.service.FsItem;
import cn.bluejoe.elfinder.util.MimeTypesUtils;

public class DefaultFsVolume extends LocalFsVolume {

	private File asFile(FsItem fsi) {
		return ((LocalFsItem) fsi).getFile();
	}
	
	@Override
	public String getMimeType(FsItem fsi) {
		File file = asFile(fsi);
		if (file.isDirectory()) {
			return "directory";
		}
		String ext = FilenameUtils.getExtension(file.getName());
		if ((ext != null) && (!(ext.isEmpty()))) {
			String mimeType = MimeTypesUtils.getMimeType(ext);
			return ((mimeType == null) ? "application/oct-stream" : mimeType);
		}

		return "application/oct-stream";
	}

	
	
}
