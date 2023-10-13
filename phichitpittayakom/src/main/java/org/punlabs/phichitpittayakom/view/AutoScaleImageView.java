package org.punlabs.phichitpittayakom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class AutoScaleImageView extends View {
	private final Bitmap image;
	private final double aspectRatio;
	private final Paint paint;

	private Bitmap scaledImage;

	public AutoScaleImageView(Context context) {
		super(context);
		throw new IllegalArgumentException("Image missing!");
	}

	public AutoScaleImageView(Context context, Bitmap image) {
		super(context);
		this.image = image;
		this.aspectRatio = ((double) this.image.getHeight()) / ((double) this.image.getWidth());
		this.paint = new Paint();
		this.scaledImage = this.image;
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
