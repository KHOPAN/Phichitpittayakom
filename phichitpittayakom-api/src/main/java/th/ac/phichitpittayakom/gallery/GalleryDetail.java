package th.ac.phichitpittayakom.gallery;

import java.time.LocalDateTime;

public class GalleryDetail {
	private final String title;
	private final String content;
	private final String[] images;
	private final LocalDateTime postTime;
	private final int viewCount;

	public GalleryDetail() {
		this.title = "";
		this.content = "";
		this.images = new String[0];
		this.postTime = LocalDateTime.of(0, 1, 1, 0, 0, 0, 0);
		this.viewCount = 0;
	}

	public GalleryDetail(String title, String content, String[] images, LocalDateTime postTime, int viewCount) {
		this.title = title;
		this.content = content;
		this.images = images;
		this.postTime = postTime;
		this.viewCount = viewCount;
	}

	public String getTitle() {
		return this.title;
	}

	public String getContent() {
		return this.content;
	}

	public String[] getImages() {
		return this.images;
	}

	public LocalDateTime getPostTime() {
		return this.postTime;
	}

	public int getViewCount() {
		return this.viewCount;
	}
}
