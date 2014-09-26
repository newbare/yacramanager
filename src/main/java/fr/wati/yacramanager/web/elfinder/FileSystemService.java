package fr.wati.yacramanager.web.elfinder;

import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.bluejoe.elfinder.service.FsItem;
import cn.bluejoe.elfinder.service.FsSecurityChecker;
import cn.bluejoe.elfinder.service.FsService;
import cn.bluejoe.elfinder.service.FsServiceConfig;
import cn.bluejoe.elfinder.service.FsVolume;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.web.elfinder.AttachementFsVolume.AttachementFsItem;

@Component
public class FileSystemService implements FsService, InitializingBean {

	private VolumeProvider volumeProvider;
	
	@Autowired
	private FsSecurityChecker securityChecker;
	
	@Autowired
	private FsServiceConfig fsServiceConfig;
	
	String[][] escapes = { { "+", "_P" }, { "-", "_M" }, { "/", "_S" }, { ".", "_D" }, { "=", "_E" } };
	
	@Override
	public FsItem fromHash(String hash) throws IOException {
		for (FsVolume v : getVolumes())
		{
			if(v instanceof AttachementFsVolume){
				return v.fromPath(hash);
			}else {
				String prefix = getVolumeId(v) + "_";

				if (hash.equals(prefix))
				{
					return v.getRoot();
				}

				if (hash.startsWith(prefix))
				{
					String localHash = hash.substring(prefix.length());

					for (String[] pair : escapes)
					{
						localHash = localHash.replace(pair[1], pair[0]);
					}

					String relativePath = new String(Base64.decodeBase64(localHash));
					return v.fromPath(relativePath);
				}
			}
			
		}

		return null;
	}

	@Override
	public String getHash(FsItem item) throws IOException {
		if(item instanceof AttachementFsItem){
			if(((AttachementFsItem)item).getAttachement()!=null){
				return ((AttachementFsItem)item).getVolume().getPath(((AttachementFsItem)item));
			}else {
				return ((AttachementFsItem)item).getVolume().getPath(((AttachementFsItem)item));
			}
		}else {
			String relativePath = item.getVolume().getPath(item);
			String base = new String(Base64.encodeBase64(relativePath.getBytes()));

			for (String[] pair : escapes)
			{
				base = base.replace(pair[0], pair[1]);
			}

			return getVolumeId(item.getVolume()) + "_" + base;
		}
		
	}

	@Override
	public FsSecurityChecker getSecurityChecker() {
		return securityChecker;
	}

	@Override
	public FsServiceConfig getServiceConfig() {
		return fsServiceConfig;
	}

	@Override
	public String getVolumeId(FsVolume paramFsVolume) {
		return paramFsVolume.getName()+"_"+getConnectedUser().getId();
	}

	@Override
	public FsVolume[] getVolumes() {
		List<FsVolume> fsVolumes=volumeProvider.getAllVolumesForUser(getConnectedUser());
		return fsVolumes.toArray(new FsVolume[fsVolumes.size()]);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//Assert.notNull(volumeProvider);
	}

	public VolumeProvider getVolumeProvider() {
		return volumeProvider;
	}

	public void setVolumeProvider(VolumeProvider volumeProvider) {
		this.volumeProvider = volumeProvider;
	}

	public Users getConnectedUser() {
		return SecurityUtils.getConnectedUser();
	}

	
}
