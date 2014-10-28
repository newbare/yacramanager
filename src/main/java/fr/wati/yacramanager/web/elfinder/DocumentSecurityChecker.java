package fr.wati.yacramanager.web.elfinder;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.bluejoe.elfinder.service.FsItem;
import cn.bluejoe.elfinder.service.FsSecurityChecker;
import cn.bluejoe.elfinder.service.FsService;
import fr.wati.yacramanager.services.EmployeService;

@Component
public class DocumentSecurityChecker implements FsSecurityChecker {

	@Autowired
	private EmployeService employeService;
	
	@Override
	public boolean isLocked(FsService paramFsService, FsItem paramFsItem)
			throws IOException {
		return false;
	}

	@Override
	public boolean isReadable(FsService paramFsService, FsItem paramFsItem)
			throws IOException {
		return true;
	}

	@Override
	public boolean isWritable(FsService paramFsService, FsItem paramFsItem)
			throws IOException {
		return paramFsItem instanceof LocalFsItem;
	}

}
