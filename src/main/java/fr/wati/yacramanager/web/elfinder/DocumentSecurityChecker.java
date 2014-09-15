package fr.wati.yacramanager.web.elfinder;

import java.io.IOException;

import org.springframework.stereotype.Component;

import cn.bluejoe.elfinder.service.FsItem;
import cn.bluejoe.elfinder.service.FsSecurityChecker;
import cn.bluejoe.elfinder.service.FsService;

@Component
public class DocumentSecurityChecker implements FsSecurityChecker {

	@Override
	public boolean isLocked(FsService paramFsService, FsItem paramFsItem)
			throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReadable(FsService paramFsService, FsItem paramFsItem)
			throws IOException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isWritable(FsService paramFsService, FsItem paramFsItem)
			throws IOException {
		// TODO Auto-generated method stub
		return true;
	}

}
