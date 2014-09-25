package fr.wati.yacramanager.web.elfinder;

import java.util.List;

import cn.bluejoe.elfinder.service.FsVolume;

public interface VolumeProvider<T> {

	FsVolume getUserPrivateVolume(T users);
	
	FsVolume getUserAttachementsVolume(T users);
	
	List<FsVolume> getSharedVolumes(T users);
	
	List<FsVolume> getAllVolumesForUser(T users);
}
