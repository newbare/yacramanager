package fr.wati.yacramanager.web.elfinder;

import java.util.ArrayList;
import java.util.List;

import cn.bluejoe.elfinder.service.FsVolume;
import fr.wati.yacramanager.beans.Users;

public abstract class SharedFsVolumeProvider implements VolumeProvider {

	@Override
	public abstract FsVolume getUserPrivateVolume(Users users);

	@Override
	public List<FsVolume> getSharedVolumes(Users users) {
		List<FsVolume> fsVolumes=new ArrayList<>();
		
		
		return fsVolumes;
	}

	@Override
	public List<FsVolume> getAllVolumesForUser(Users users) {
		List<FsVolume> fsVolumes=new ArrayList<>();
		fsVolumes.addAll(getSharedVolumes(users));
		fsVolumes.add(getUserPrivateVolume(users));
		return fsVolumes;
	}

}
