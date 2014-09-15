package fr.wati.yacramanager.web.elfinder;

import java.util.List;

import cn.bluejoe.elfinder.service.FsVolume;
import fr.wati.yacramanager.beans.Users;

public interface VolumeProvider {

	FsVolume getUserPrivateVolume(Users users);
	
	List<FsVolume> getSharedVolumes(Users users);
	
	List<FsVolume> getAllVolumesForUser(Users users);
}
