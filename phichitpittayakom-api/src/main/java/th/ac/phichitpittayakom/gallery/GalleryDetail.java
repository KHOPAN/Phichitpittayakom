package th.ac.phichitpittayakom.gallery;

import java.time.LocalDateTime;

public class GalleryDetail {
	private final String title;
	private final String content;
	private final byte[][] images;
	private final LocalDateTime postDate;
	private final int viewCount;

	public GalleryDetail() {
		this.title = "";
		this.content = "";
		this.images = new byte[0][];
		this.postDate = null;
		this.viewCount = 0;
	}

	public GalleryDetail(String title, String content, byte[][] images, LocalDateTime postDate, int viewCount) {
		this.title = title;
		this.content = content;
		this.images = images;
		this.postDate = postDate;
		this.viewCount = viewCount;
	}

	public String getTitle() {
		return this.title;
	}

	public String getContent() {
		return this.content;
	}

	public byte[][] getImages() {
		return this.images;
	}

	public LocalDateTime getPostDate() {
		return this.postDate;
	}

	public int getViewCount() {
		return this.viewCount;
	}
}
