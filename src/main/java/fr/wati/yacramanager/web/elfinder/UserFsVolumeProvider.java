package fr.wati.yacramanager.web.elfinder;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import cn.bluejoe.elfinder.service.FsVolume;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Personne;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.services.CompanyService;

@Component("UserFsVolumeProvider")
public class UserFsVolumeProvider extends SharedFsVolumeProvider {

	@Autowired
	private Environment environment;

	@Autowired
	private CompanyService companyService;

	@Override
	public FsVolume getUserPrivateVolume(Users users) {
		DefaultFsVolume fsVolume = new DefaultFsVolume();
		fsVolume.setName(((Personne) users).getUsername());
		String userPath = null;
		Company company = ((Employe) users).getCompany();
		userPath = company.getName() + File.separatorChar + users.getUsername()
				+ "_" + users.getId();
		String userRootFilePath = environment.getProperty("document.root.folder",environment.getProperty("user.home")+ File.separator+".yacra") + File.separator
				+ userPath;
		fsVolume.setRootDir(new File(userRootFilePath));
		return fsVolume;
	}

}
