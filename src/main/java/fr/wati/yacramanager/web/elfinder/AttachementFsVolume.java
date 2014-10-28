package fr.wati.yacramanager.web.elfinder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;

import cn.bluejoe.elfinder.service.FsItem;
import cn.bluejoe.elfinder.service.FsVolume;
import fr.wati.yacramanager.beans.Attachement;

public class AttachementFsVolume implements FsVolume {

	private static final String ROOT_PATH = "attachements";
	private static final String PATH_SEPARATOR="_";
	private List<Attachement> attachements=new ArrayList<>();
	private Map<Long,AttachementFsItem> attachementFsItems=new HashMap<>();
	private AttachementFsItem root;
	
	public AttachementFsVolume(List<Attachement> attachements) {
		root=new AttachementFsItem(null, this,true);
		this.attachements=attachements;
		CollectionUtils.forAllDo(attachements, new Closure() {
			@Override
			public void execute(Object input) {
				attachementFsItems.put(((Attachement)input).getId(),new AttachementFsItem((Attachement)input, AttachementFsVolume.this));
			}
		});
	}

	
	@Override
	public void createFile(FsItem paramFsItem) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void createFolder(FsItem paramFsItem) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFile(FsItem paramFsItem) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFolder(FsItem paramFsItem) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean exists(FsItem paramFsItem) {
		return paramFsItem!=null && (((AttachementFsItem)paramFsItem).getAttachement()!=null|| ((AttachementFsItem)paramFsItem).isRoot());
	}

	@Override
	public FsItem fromPath(String paramString) {
		String[] strings = paramString.split(PATH_SEPARATOR);
		return strings.length>1? attachementFsItems.get(Long.valueOf(strings[1])): root;
	}

	@Override
	public String getDimensions(FsItem paramFsItem) {
		return null;
	}

	@Override
	public long getLastModified(FsItem paramFsItem) {
		return !((AttachementFsItem)paramFsItem).isRoot()? ((AttachementFsItem)paramFsItem).getAttachement().getLastModifiedDate().getMillis():0;
	}

	@Override
	public String getMimeType(FsItem paramFsItem) {
		return !((AttachementFsItem)paramFsItem).isRoot()? ((AttachementFsItem)paramFsItem).getAttachement().getContentType():"directory";
	}

	@Override
	public String getName() {
		return "Attachements";
	}

	@Override
	public String getName(FsItem paramFsItem) {
		return !((AttachementFsItem)paramFsItem).isRoot() ?((AttachementFsItem)paramFsItem).getAttachement().getName():getName();
	}

	@Override
	public FsItem getParent(FsItem paramFsItem) {
		if(((AttachementFsItem)paramFsItem).isRoot()){
			return null;
		}else {
			return root;
		}
	}

	@Override
	public String getPath(FsItem paramFsItem) throws IOException {
		return ((AttachementFsItem)paramFsItem).isRoot()?ROOT_PATH:ROOT_PATH+PATH_SEPARATOR+((AttachementFsItem)paramFsItem).getAttachement().getId();
	}

	@Override
	public FsItem getRoot() {
		return root;
	}

	@Override
	public long getSize(FsItem paramFsItem) {
		if(paramFsItem!=null){
			AttachementFsItem attachementFsItem= (AttachementFsItem) paramFsItem;
			if(!attachementFsItem.isRoot()){
				Attachement attachement = attachementFsItem.getAttachement();
				return attachement.getSize();
			}
		}
		return 0;
	}

	@Override
	public String getThumbnailFileName(FsItem paramFsItem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildFolder(FsItem paramFsItem) {
		return ((AttachementFsItem)paramFsItem).isRoot();
	}

	@Override
	public boolean isFolder(FsItem paramFsItem) {
		return ((AttachementFsItem)paramFsItem).isRoot();
	}

	@Override
	public boolean isRoot(FsItem paramFsItem) {
		return ((AttachementFsItem)paramFsItem).isRoot();
	}

	@Override
	public FsItem[] listChildren(FsItem paramFsItem) {
		if(((AttachementFsItem)paramFsItem).isRoot()){
			return attachementFsItems.values().toArray(new AttachementFsItem[attachementFsItems.size()]);
		}
		return new FsItem[0];
	}

	@Override
	public InputStream openInputStream(FsItem paramFsItem) throws IOException {
		if(paramFsItem!=null){
			AttachementFsItem attachementFsItem= (AttachementFsItem) paramFsItem;
			Attachement attachement = attachementFsItem.getAttachement();
			return new ByteArrayInputStream(attachement.getContent());
		}
		return null;
	}

	@Override
	public OutputStream openOutputStream(FsItem paramFsItem) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rename(FsItem paramFsItem1, FsItem paramFsItem2)
			throws IOException {
		// TODO Auto-generated method stub

	}

	public List<Attachement> getAttachements() {
		return attachements;
	}

	public void setAttachements(List<Attachement> attachements) {
		this.attachements = attachements;
	}

	public static class AttachementFsItem implements FsItem{
		private Attachement attachement;
		private AttachementFsVolume attachementFsVolume;
		private boolean root=false;
		
		
		public AttachementFsItem(Attachement attachement,
				AttachementFsVolume attachementFsVolume,boolean root) {
			super();
			this.attachement = attachement;
			this.attachementFsVolume = attachementFsVolume;
			this.root=root;
		}

		public AttachementFsItem(Attachement attachement,
				AttachementFsVolume attachementFsVolume) {
			this(attachement, attachementFsVolume, false);
		}

		/**
		 * @return the root
		 */
		public boolean isRoot() {
			return root;
		}


		/**
		 * @param root the root to set
		 */
		public void setRoot(boolean root) {
			this.root = root;
		}


		@Override
		public FsVolume getVolume() {
			return attachementFsVolume;
		}


		public Attachement getAttachement() {
			return attachement;
		}


		public void setAttachement(Attachement attachement) {
			this.attachement = attachement;
		}


		public AttachementFsVolume getAttachementFsVolume() {
			return attachementFsVolume;
		}


		public void setAttachementFsVolume(AttachementFsVolume attachementFsVolume) {
			this.attachementFsVolume = attachementFsVolume;
		}
		
	}
}
