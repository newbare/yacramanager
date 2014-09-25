package fr.wati.yacramanager.web.elfinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;

import cn.bluejoe.elfinder.service.FsItem;
import cn.bluejoe.elfinder.service.FsVolume;
import fr.wati.yacramanager.beans.Attachement;

public class AttachementFsVolume implements FsVolume {

	private List<Attachement> attachements=new ArrayList<>();
	private List<AttachementFsItem> attachementFsItems=new ArrayList<>();
	private AttachementFsItem root;
	
	public AttachementFsVolume(List<Attachement> attachements) {
		root=new AttachementFsItem(null, this);
		this.attachements=attachements;
		CollectionUtils.forAllDo(attachements, new Closure() {
			@Override
			public void execute(Object input) {
				attachementFsItems.add(new AttachementFsItem((Attachement)input, AttachementFsVolume.this));
				
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
		return paramFsItem!=null && ((AttachementFsItem)paramFsItem).getAttachement()!=null;
	}

	@Override
	public FsItem fromPath(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDimensions(FsItem paramFsItem) {
		return null;
	}

	@Override
	public long getLastModified(FsItem paramFsItem) {
		return paramFsItem!=root? ((AttachementFsItem)paramFsItem).getAttachement().getLastModifiedDate().getMillis():0;
	}

	@Override
	public String getMimeType(FsItem paramFsItem) {
		return paramFsItem!=root? ((AttachementFsItem)paramFsItem).getAttachement().getContentType():"";
	}

	@Override
	public String getName() {
		return "Attachements";
	}

	@Override
	public String getName(FsItem paramFsItem) {
		return paramFsItem!=root ?((AttachementFsItem)paramFsItem).getAttachement().getName():getName();
	}

	@Override
	public FsItem getParent(FsItem paramFsItem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath(FsItem paramFsItem) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FsItem getRoot() {
		return root;
	}

	@Override
	public long getSize(FsItem paramFsItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getThumbnailFileName(FsItem paramFsItem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildFolder(FsItem paramFsItem) {
		return paramFsItem.equals(root);
	}

	@Override
	public boolean isFolder(FsItem paramFsItem) {
		return paramFsItem.equals(root);
	}

	@Override
	public boolean isRoot(FsItem paramFsItem) {
		return paramFsItem.equals(root);
	}

	@Override
	public FsItem[] listChildren(FsItem paramFsItem) {
		if(paramFsItem.equals(root)){
			return attachementFsItems.toArray(new AttachementFsItem[attachementFsItems.size()]);
		}
		return new FsItem[0];
	}

	@Override
	public InputStream openInputStream(FsItem paramFsItem) throws IOException {
		
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
		
		
		public AttachementFsItem(Attachement attachement,
				AttachementFsVolume attachementFsVolume) {
			super();
			this.attachement = attachement;
			this.attachementFsVolume = attachementFsVolume;
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
