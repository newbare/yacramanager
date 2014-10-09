package fr.wati.yacramanager.web.elfinder;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import cn.bluejoe.elfinder.service.FsItem;
import cn.bluejoe.elfinder.service.FsVolume;
import cn.bluejoe.elfinder.util.MimeTypesUtils;

public class LocalFsVolume implements FsVolume {
	String _name;
	File _rootDir;

	private File asFile(FsItem fsi) {
		return ((LocalFsItem) fsi).getFile();
	}

	public void createFile(FsItem fsi) throws IOException {
		asFile(fsi).createNewFile();
	}

	public void createFolder(FsItem fsi) throws IOException {
		asFile(fsi).mkdirs();
	}

	public void deleteFile(FsItem fsi) throws IOException {
		File file = asFile(fsi);
		if (file.isDirectory())
			return;
		file.delete();
	}

	public void deleteFolder(FsItem fsi) throws IOException {
		File file = asFile(fsi);
		if (!(file.isDirectory()))
			return;
		FileUtils.deleteDirectory(file);
	}

	public boolean exists(FsItem newFile) {
		return asFile(newFile).exists();
	}

	private LocalFsItem fromFile(File file) {
		return new LocalFsItem(this, file);
	}

	public FsItem fromPath(String relativePath) {
		return fromFile(new File(this._rootDir, relativePath));
	}

	public String getDimensions(FsItem fsi) {
		return null;
	}

	public long getLastModified(FsItem fsi) {
		return asFile(fsi).lastModified();
	}

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

	public String getName() {
		return this._name;
	}

	public String getName(FsItem fsi) {
		return asFile(fsi).getName();
	}

	public FsItem getParent(FsItem fsi) {
		return fromFile(asFile(fsi).getParentFile());
	}

	public String getPath(FsItem fsi) throws IOException {
		String fullPath = asFile(fsi).getCanonicalPath();
		String rootPath = this._rootDir.getCanonicalPath();
		String relativePath = fullPath.substring(rootPath.length());
		return relativePath.replace('\\', '/');
	}

	public FsItem getRoot() {
		return fromFile(this._rootDir);
	}

	public File getRootDir() {
		return this._rootDir;
	}

	public long getSize(FsItem fsi) {
		return asFile(fsi).length();
	}

	public String getThumbnailFileName(FsItem fsi) {
		return null;
	}

	public boolean hasChildFolder(FsItem fsi) {
		return ((asFile(fsi).isDirectory()) && (asFile(fsi).listFiles(
				new FileFilter() {
					public boolean accept(File arg0) {
						return arg0.isDirectory();
					}
				}).length > 0));
	}

	public boolean isFolder(FsItem fsi) {
		return asFile(fsi).isDirectory();
	}

	public boolean isRoot(FsItem fsi) {
		return (this._rootDir == asFile(fsi));
	}

	public FsItem[] listChildren(FsItem fsi) {
		List list = new ArrayList();
		File[] cs = asFile(fsi).listFiles();
		if (cs == null) {
			return new FsItem[0];
		}

		for (File c : cs) {
			list.add(fromFile(c));
		}

		return ((FsItem[]) list.toArray(new FsItem[0]));
	}

	public InputStream openInputStream(FsItem fsi) throws IOException {
		return new FileInputStream(asFile(fsi));
	}

	public OutputStream openOutputStream(FsItem fsi) throws IOException {
		return new FileOutputStream(asFile(fsi));
	}

	public void rename(FsItem src, FsItem dst) throws IOException {
		asFile(src).renameTo(asFile(dst));
	}

	public void setName(String name) {
		this._name = name;
	}

	public void setRootDir(File rootDir) {
		if (!(rootDir.exists())) {
			rootDir.mkdirs();
		}

		this._rootDir = rootDir;
	}
}