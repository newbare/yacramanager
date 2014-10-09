package fr.wati.yacramanager.web.elfinder;

import cn.bluejoe.elfinder.service.FsItem;
import cn.bluejoe.elfinder.service.FsVolume;
import java.io.File;

public class LocalFsItem implements FsItem {
	File _file;
	FsVolume _volumn;

	public LocalFsItem(LocalFsVolume volumn, File file) {
		this._volumn = volumn;
		this._file = file;
	}

	public File getFile() {
		return this._file;
	}

	public FsVolume getVolume() {
		return this._volumn;
	}

	public void setFile(File file) {
		this._file = file;
	}

	public void setVolumn(FsVolume volumn) {
		this._volumn = volumn;
	}
}