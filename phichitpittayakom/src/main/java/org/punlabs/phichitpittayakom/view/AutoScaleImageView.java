package org.punlabs.phichitpittayakom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class AutoScaleImageView extends View {
	private final Paint paint;

	private Bitmap image;
	private double aspectRatio;
	private Bitmap scaledImage;

	public AutoScaleImageView(Context context) {
		super(context);
		this.paint = new Paint();
		this.aspectRatio = 1.0d;
	}

	public AutoScaleImageView(Context context, Bitmap image) {
		this(context);
		this.setImage(image);
	}

	public void setImage(Bitmap image) {
		this.image = image;
		this.aspectRatio = ((double) this.image.getHeight()) / ((double) this.image.getWidth());
		this.scaledImage = this.image;
		int width = this.getWidth();
		int height = this.getHeight();

		if(width < 1 || height < 1) {
			return;
		}

		this.updateScaledImage(width, height);
	}

	@Override
	protected void onMeasure(int widthMeasure, int heightMeasure) {
		int width = View.MeasureSpec.getSize(widthMeasure);
		int height = (int) Math.round(((double) width) * this.aspectRatio);
		this.setMeasuredDimension(width, height);
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		super.onSizeChanged(width, height, oldWidth, oldHeight);
		this.updateScaledImage(width, height);
	}

	private void updateScaledImage(int width, int height) {
		this.scaledImage = Bitmap.createScaledBitmap(this.image, width, height, true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Bitmap image = this.image;

		if(this.scaledImage != null) {
			image = this.scaledImage;
		}

		canvas.drawBitmap(image, 0.0f, 0.0f, this.paint);
	}
}
