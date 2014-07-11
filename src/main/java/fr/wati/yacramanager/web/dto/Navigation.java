/**
 * 
 */
package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rachid Ouattara
 *
 */
public class Navigation {

	private SideNavItem[] sidenav_navList;
	private static SideNavItem biblioNavItem;
	private static SideNavItem documentNavItem;
	private static SideNavItem messagerieNavItem;
	private static SideNavItem calendarNavItem;
	private static SideNavItem sallesEditionItem;
	private static SideNavItem classesEditionItem;
	private static SideNavItem matiereEditionItem;
	private static SideNavItem etablissementEditionItem;
	private static SideNavItem userEditionItem;
	private static SideNavItem adminEditionNavItem;
	private static SideNavItem adminNavItem;
	private static SideNavItem dashboardNavItem;
	private static SideNavItem batchNavItem;
	private static SideNavItem appSettingsNavItem;
	private static SideNavItem mailNavItem;
	private static SideNavItem chatNavItem;
	private static SideNavItem publicDocumentsNavItem;
	private static SideNavItem scolariteNavItem;
	private static SideNavItem evaluationsNavItem;
	private static SideNavItem absencesNavItem;

	/**
	 * @return the sidenav_navList
	 */
	public SideNavItem[] getSidenav_navList() {
		return sidenav_navList;
	}

	/**
	 * @param sidenav_navList the sidenav_navList to set
	 */
	public void setSidenav_navList(SideNavItem[] sidenav_navList) {
		this.sidenav_navList = sidenav_navList;
	}
	
	public static Navigation buildNavigationDefault(UserInfoDTO userInfoDTO, String contextPath){
		Navigation layout=new Navigation();
		List<SideNavItem> superNavItems=new ArrayList<>();
		String hrefPreffix=contextPath+"/app/"+userInfoDTO.getUsername();
		dashboardNavItem = new SideNavItem("DashBoard", "", "menu-icon fa fa-home", false, true);
		dashboardNavItem.setLevel1(true);
		dashboardNavItem.setLevel2(false);
		superNavItems.add(dashboardNavItem);
		adminNavItem = new SideNavItem("Admin", "", "menu-icon fa fa-star", true, false);
		adminNavItem.setLevel1(true);
		adminNavItem.setLevel2(false);
		superNavItems.add(adminNavItem);
		List<SideNavItem> adminSubItems=new ArrayList<>();
		//Admin Edition
		adminEditionNavItem = new SideNavItem("Edition", "", "menu-icon fa fa-star-half-o", true, false);
		adminEditionNavItem.setLevel1(false);
		adminEditionNavItem.setLevel2(true);
		//Admin edition submenu
		List<SideNavItem> adminEditionSubItems=new ArrayList<>();
		userEditionItem = new SideNavItem("Users edition", hrefPreffix+"/users", "menu-icon fa fa-star-o", false, true);
		userEditionItem.setLevel1(false);
		userEditionItem.setLevel2(true);
		adminEditionSubItems.add(userEditionItem);
		
		etablissementEditionItem = new SideNavItem("CRA", "cra", "menu-icon fa fa-star-o", false, true);
		etablissementEditionItem.setLevel1(false);
		etablissementEditionItem.setLevel2(true);
		adminEditionSubItems.add(etablissementEditionItem);
		
		matiereEditionItem = new SideNavItem("Matieres", "/matieres", "menu-icon fa fa-star-o", false, true);
		matiereEditionItem.setLevel1(false);
		matiereEditionItem.setLevel2(true);
		adminEditionSubItems.add(matiereEditionItem);
		
		classesEditionItem = new SideNavItem("Classes", "/classes", "menu-icon fa fa-star-o", false, true);
		classesEditionItem.setLevel1(false);
		classesEditionItem.setLevel2(true);
		adminEditionSubItems.add(classesEditionItem);
		
		sallesEditionItem = new SideNavItem("Salles", "/salles", "menu-icon fa fa-star-o", false, true);
		sallesEditionItem.setLevel1(false);
		sallesEditionItem.setLevel2(true);
		adminEditionSubItems.add(sallesEditionItem);
		
		
		adminEditionNavItem.setSubmenu(adminEditionSubItems.toArray(new SideNavItem[adminEditionSubItems.size()]));
		
		adminSubItems.add(adminEditionNavItem);
		//Batch
		batchNavItem = new SideNavItem("Batch", "/batch", "menu-icon fa fa-star-half-o",  false, true);
		adminSubItems.add(batchNavItem);
		
		//Global settings
		appSettingsNavItem = new SideNavItem("Global settings", "/app-settings", "menu-icon fa fa-wrench", false, true);
		adminSubItems.add(appSettingsNavItem);
				
		//Add Admin sub menus
		adminNavItem.setSubmenu(adminSubItems.toArray(new SideNavItem[adminSubItems.size()]));
		//scolarite
		scolariteNavItem = new SideNavItem("Scolarite","cra", "menu-icon fa fa-suitcase", true, false);
		scolariteNavItem.setLevel1(true);
		scolariteNavItem.setLevel2(false);
		List<SideNavItem> scolariteSubmenus=new ArrayList<>();
		evaluationsNavItem = new SideNavItem("Evaluation", "", null, false, true);
		evaluationsNavItem.setLevel1(false);
		evaluationsNavItem.setLevel2(true);
		absencesNavItem = new SideNavItem("Absences", "", null, false, true);
		absencesNavItem.setLevel1(false);
		absencesNavItem.setLevel2(true);
		scolariteSubmenus.add(evaluationsNavItem);
		scolariteSubmenus.add(absencesNavItem);
		scolariteNavItem.setSubmenu(scolariteSubmenus.toArray(new SideNavItem[scolariteSubmenus.size()]));
		
		superNavItems.add(scolariteNavItem);
		//emploie de temps
		calendarNavItem = new SideNavItem("Emploie de temps", "/calendar", "menu-icon fa fa-calendar", false, true);
		calendarNavItem.setLevel1(true);
		calendarNavItem.setLevel2(false);
		superNavItems.add(calendarNavItem);
		//Messagerie
		messagerieNavItem = new SideNavItem("Messagerie", "", "menu-icon fa fa-envelope", true, false);
		messagerieNavItem.setLevel1(true);
		messagerieNavItem.setLevel2(false);
		//Mail menu
		mailNavItem = new SideNavItem("Mail", "/mails", "menu-icon fa fa-envelope", false, true);
		mailNavItem.setLevel1(false);
		mailNavItem.setLevel2(true);
		//chat menu
		chatNavItem = new SideNavItem("Chat", "/chat", "menu-icon fa fa-comments", false, true);
		chatNavItem.setLevel1(false);
		chatNavItem.setLevel2(true);
		messagerieNavItem.setSubmenu(new SideNavItem[]{mailNavItem,chatNavItem});
		superNavItems.add(messagerieNavItem);
		//Documents
		documentNavItem = new SideNavItem("Documents", "", "menu-icon fa fa-folder", false, true);
		documentNavItem.setLevel1(true);
		documentNavItem.setLevel2(false);
		//myDocuments
//		myDocumentsNavItem= new SideNavItem("My documents", "/mydocuments", null, false, true);
//		myDocumentsNavItem.setLevel1(false);
//		myDocumentsNavItem.setLevel2(true);
		//public documents
//		publicDocumentsNavItem= new SideNavItem("Public documents", "/publicdocuments", null, false, true);
//		publicDocumentsNavItem.setLevel1(false);
//		publicDocumentsNavItem.setLevel2(true);
		
		//documentNavItem.setSubmenu(new SideNavItem[]{myDocumentsNavItem,publicDocumentsNavItem});
		superNavItems.add(documentNavItem);
		
		biblioNavItem = new SideNavItem("Bibliotheque", "", "menu-icon fa fa-star", false, true);
		biblioNavItem.setLevel1(true);
		biblioNavItem.setLevel2(false);
		superNavItems.add(biblioNavItem);
		layout.setSidenav_navList(superNavItems.toArray(new SideNavItem[superNavItems.size()]));
		return layout;
	}
	
	
	public static void active(String activeItem){
		switch (activeItem) {
		case "/":
			dashboardNavItem.setClazz("active");
			break;
		case "/users":
			userEditionItem.setClazz("active");
			adminEditionNavItem.setClazz("active open");
			adminNavItem.setClazz("active open");
			break;
		case "/etablissements":
			etablissementEditionItem.setClazz("active");
			adminEditionNavItem.setClazz("active open");
			adminNavItem.setClazz("active open");
			break;
		case "/classes":
			classesEditionItem.setClazz("active");
			adminEditionNavItem.setClazz("active open");
			adminNavItem.setClazz("active open");
			break;
		case "/salles":
			sallesEditionItem.setClazz("active");
			adminEditionNavItem.setClazz("active open");
			adminNavItem.setClazz("active open");
			break;
		case "/matieres":
			matiereEditionItem.setClazz("active");
			adminEditionNavItem.setClazz("active open");
			adminNavItem.setClazz("active open");
			break;
		case "/calendar":
			calendarNavItem.setClazz("active");
			break;
		case "/batch":
			batchNavItem.setClazz("active");
			adminNavItem.setClazz("active open");
			break;
		case "/app-settings":
			appSettingsNavItem.setClazz("active");
			adminNavItem.setClazz("active open");
			break;
		case "/mails":
			mailNavItem.setClazz("active");
			messagerieNavItem.setClazz("active open");
			break;
		case "/chat":
			chatNavItem.setClazz("active");
			messagerieNavItem.setClazz("active open");
			break;
		case "/mydocuments":
			documentNavItem.setClazz("active");
//			documentNavItem.setClazz("active open");
			break;
		case "/publicdocuments":
			publicDocumentsNavItem.setClazz("active");
			documentNavItem.setClazz("active open");
			break;
		case "/absences":
			absencesNavItem.setClazz("active");
			scolariteNavItem.setClazz("active open");
			break;
		case "/evaluation":
			evaluationsNavItem.setClazz("active");
			scolariteNavItem.setClazz("active open");
			break;
		case "/bibliotheque":
			biblioNavItem.setClazz("active");
		default:
			break;
		}
		
	}
}
