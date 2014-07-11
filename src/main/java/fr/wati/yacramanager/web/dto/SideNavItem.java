
package fr.wati.yacramanager.web.dto;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SideNavItem {

	public String link;
	public boolean haslink;
    public String title;
    public String icon;
    @Valid
    public SideNavItem[] submenu;
    public boolean hassubmenu;
    public String badge;
    public String badge_class;
    public String tooltip;
    private String clazz;
    public String tooltip_class;
    public boolean level1;
    public boolean level2;
    
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    
    
    public SideNavItem(String title, String link, String icon,
			boolean hassubmenu, boolean haslink) {
		super();
		this.title = title;
		this.link = link;
		this.icon = icon;
		this.hassubmenu = hassubmenu;
		this.haslink = haslink;
	}

	public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public SideNavItem withLink(String link) {
        this.link = link;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SideNavItem withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public SideNavItem withIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public SideNavItem[] getSubmenu() {
        return submenu;
    }

    public void setSubmenu(SideNavItem[] submenu) {
        this.submenu = submenu;
    }

    public SideNavItem withSubmenu(SideNavItem[] submenu) {
        this.submenu = submenu;
        return this;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public SideNavItem withBadge(String badge) {
        this.badge = badge;
        return this;
    }

    public String getBadge_class() {
        return badge_class;
    }

    public void setBadge_class(String badge_class) {
        this.badge_class = badge_class;
    }

    public SideNavItem withBadge_class(String badge_class) {
        this.badge_class = badge_class;
        return this;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public SideNavItem withTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public String getTooltip_class() {
        return tooltip_class;
    }

    public void setTooltip_class(String tooltip_class) {
        this.tooltip_class = tooltip_class;
    }

    /**
	 * @return the clazz
	 */
	public String getClazz() {
		return clazz;
	}

	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public SideNavItem withTooltip_class(String tooltip_class) {
        this.tooltip_class = tooltip_class;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

	/**
	 * @return the level1
	 */
	public boolean isLevel1() {
		return level1;
	}

	/**
	 * @param level1 the level1 to set
	 */
	public void setLevel1(boolean level1) {
		this.level1 = level1;
	}

	/**
	 * @return the level2
	 */
	public boolean isLevel2() {
		return level2;
	}

	/**
	 * @param level2 the level2 to set
	 */
	public void setLevel2(boolean level2) {
		this.level2 = level2;
	}

	public boolean isHassubmenu() {
		return hassubmenu;
	}

	public void setHassubmenu(boolean hassubmenu) {
		this.hassubmenu = hassubmenu;
	}

	public boolean isHaslink() {
		return haslink;
	}

	public void setHaslink(boolean haslink) {
		this.haslink = haslink;
	}

}
