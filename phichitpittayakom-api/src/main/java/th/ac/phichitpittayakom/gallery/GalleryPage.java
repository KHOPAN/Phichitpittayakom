package th.ac.phichitpittayakom.gallery;

import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;

public class GalleryPage {
	private final int pageNumber;
	private final boolean hasNextPage;
	private final Gallery[] galleryList;

	public GalleryPage() {
		this.pageNumber = 0;
		this.hasNextPage = false;
		this.galleryList = null;
	}

	public GalleryPage(int pageNumber, boolean hasNextPage, Gallery[] galleryList) {
		this.pageNumber = pageNumber;
		this.hasNextPage = hasNextPage;
		this.galleryList = galleryList;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public boolean hasNextPage() {
		return this.hasNextPage;
	}

	public Gallery[] getGalleryList() {
		return this.galleryList;
	}

	public Optional<GalleryPage> getNextPage() {
		if(!this.hasNextPage) {
			return Optional.empty();
		}

		return Phichitpittayakom.gallery.findGalleryPageByNumber(this.pageNumber + 1);
	}
}
