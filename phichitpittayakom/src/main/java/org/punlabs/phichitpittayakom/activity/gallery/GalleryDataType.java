package org.punlabs.phichitpittayakom.activity.gallery;

public enum GalleryDataType {
	EMPTY_SEPARATOR(0),
	TEXT_SEPARATOR(1),
	LOADING(2),
	CARD_VIEW(3),
	IMAGE_VIEW(4);

	private final int viewType;

	GalleryDataType(int viewType) {
		this.viewType = viewType;
	}

	public int getViewType() {
		return this.viewType;
	}

	public static GalleryDataType parse(int viewType) {
		GalleryDataType[] types = GalleryDataType.values();

		for(GalleryDataType type : types) {
			if(type.getViewType() == viewType) {
				return type;
			}
		}

		return null;
	}
}
