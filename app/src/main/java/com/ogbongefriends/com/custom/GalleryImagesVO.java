package com.ogbongefriends.com.custom;

import java.io.Serializable;

/**
 * Created by Icreon on 14-10-2015.
 */
public class GalleryImagesVO implements Serializable {
    private final String name;
    private final String fileSize;
    private final String path;
    private boolean isSelected;
    private final String extension;

    public GalleryImagesVO(String name, String fileSize, String path, boolean isSelected, String extension) {
        this.name = name;
        this.fileSize = fileSize;
        this.path = path;
        this.isSelected = isSelected;
        this.extension = extension;
    }

    public String getfileSize() {
        return fileSize;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean getSelected() {
        return this.isSelected;
    }

    public void setisSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getExtension() {
        return this.extension;
    }
}
