package th.ac.phichitpittayakom.gallery;

public class Gallery {
	private final byte[] thumbnail;
	private final String title;
	private final int imageCount;
	private final int viewCount;

	public Gallery() {
		this.thumbnail = new byte[0];
		this.title = "";
		this.imageCount = 0;
		this.viewCount = 0;
	}

	public Gallery(byte[] thumbnail, String title, int imageCount, int viewCount) {
		this.thumbnail = thumbnail;
		this.title = title;
		this.imageCount = imageCount;
		this.viewCount = viewCount;
	}

	public byte[] getThumbnail() {
		return this.thumbnail;
	}

	public String getTitle() {
		return this.title;
	}

	public int getImageCount() {
		return this.imageCount;
	}

	public int getViewCount() {
		return this.viewCount;
	}
}
