package fr.wati.yacramanager.web.elfinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import cn.bluejoe.elfinder.service.FsVolume;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.services.AttachementService;

@Component
public abstract class SharedFsVolumeProvider implements VolumeProvider<Employe> {

	@Autowired
	private Environment environment;
	
	@Autowired
	private AttachementService attachementService;
	
	@Override
	public abstract FsVolume getUserPrivateVolume(Employe users);

	@Override
	public List<FsVolume> getSharedVolumes(Employe users) {
		List<FsVolume> fsVolumes=new ArrayList<>();
		DefaultFsVolume fsVolume = new DefaultFsVolume();
		String companyPublicPath = null;
		Company company = ((Employe) users).getCompany();
		fsVolume.setName(company.getName());
		companyPublicPath = company.getName() + File.separatorChar + "public";
		String companyPublicRootFilePath = environment.getProperty("document.root.folder",environment.getProperty("user.home")+ File.separator+".yacra") + File.separator
				+ companyPublicPath;
		fsVolume.setRootDir(new File(companyPublicRootFilePath));
		fsVolumes.add(fsVolume);
		return fsVolumes;
	}

	@Override
	public FsVolume getUserAttachementsVolume(Employe users) {
		AttachementFsVolume attachementFsVolume=new AttachementFsVolume(attachementService.findAllAttachementForEmploye(users.getId()));
		return attachementFsVolume;
	}

	@Override
	public List<FsVolume> getAllVolumesForUser(Employe users) {
		List<FsVolume> fsVolumes=new ArrayList<>();
		fsVolumes.addAll(getSharedVolumes(users));
		fsVolumes.add(getUserPrivateVolume(users));
		fsVolumes.add(getUserAttachementsVolume(users));
		return fsVolumes;
	}

}
