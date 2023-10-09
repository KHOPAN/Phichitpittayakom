package th.ac.phichitpittayakom.nationalid;

public class NationalID {
	private final int firstDigit;
	private final int secondDigit;
	private final int thirdDigit;
	private final int forthDigit;
	private final int fifthDigit;
	private final int sixthDigit;
	private final int seventhDigit;
	private final int eighthDigit;
	private final int ninthDigit;
	private final int tenthDigit;
	private final int eleventhDigit;
	private final int twelfthDigit;
	private final int checksum;
	private final boolean isValid;

	public NationalID() {
		this.firstDigit = 0;
		this.secondDigit = 0;
		this.thirdDigit = 0;
		this.forthDigit = 0;
		this.fifthDigit = 0;
		this.sixthDigit = 0;
		this.seventhDigit = 0;
		this.eighthDigit = 0;
		this.ninthDigit = 0;
		this.tenthDigit = 0;
		this.eleventhDigit = 0;
		this.twelfthDigit = 0;
		this.checksum = 0;
		this.isValid = false;
	}

	NationalID(int firstDigit, int secondDigit, int thirdDigit, int forthDigit, int fifthDigit, int sixthDigit, int seventhDigit, int eighthDigit, int ninthDigit, int tenthDigit, int eleventhDigit, int twelfthDigit, int checksum) {
		this.firstDigit = firstDigit;
		this.secondDigit = secondDigit;
		this.thirdDigit = thirdDigit;
		this.forthDigit = forthDigit;
		this.fifthDigit = fifthDigit;
		this.sixthDigit = sixthDigit;
		this.seventhDigit = seventhDigit;
		this.eighthDigit = eighthDigit;
		this.ninthDigit = ninthDigit;
		this.tenthDigit = tenthDigit;
		this.eleventhDigit = eleventhDigit;
		this.twelfthDigit = twelfthDigit;
		this.checksum = checksum;
		this.isValid = this.checksum == this.calculateChecksum();
	}

	public int getFirstDigit() {
		return this.firstDigit;
	}

	public int getSecondDigit() {
		return this.secondDigit;
	}

	public int getThirdDigit() {
		return this.thirdDigit;
	}

	public int getForthDigit() {
		return this.forthDigit;
	}

	public int getFifthDigit() {
		return this.fifthDigit;
	}

	public int getSixthDigit() {
		return this.sixthDigit;
	}

	public int getSeventhDigit() {
		return this.seventhDigit;
	}

	public int getEighthDigit() {
		return this.eighthDigit;
	}

	public int getNinthDigit() {
		return this.ninthDigit;
	}

	public int getTenthDigit() {
		return this.tenthDigit;
	}

	public int getEleventhDigit() {
		return this.eleventhDigit;
	}

	public int getTwelfthDigit() {
		return this.twelfthDigit;
	}

	public int getChecksumDigit() {
		return this.checksum;
	}

	public boolean isValid() {
		return this.isValid;
	}

	public int calculateChecksum() {
		int x = this.firstDigit * 13 + this.secondDigit * 12 + this.thirdDigit * 11 + this.forthDigit * 10 + this.fifthDigit * 9 + this.sixthDigit * 8 + this.seventhDigit * 7 + this.eighthDigit * 6 + this.ninthDigit * 5 + this.tenthDigit * 4 + this.eleventhDigit * 3 + this.twelfthDigit * 2;
		x %= 11;
		x = 11 - x;
		return x > 9 ? x - 10 : x;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		if(!this.isValid) {
			builder.append("INVALID ");
		}

		builder.append(this.firstDigit);
		builder.append(' ');
		builder.append(this.secondDigit);
		builder.append(this.thirdDigit);
		builder.append(this.forthDigit);
		builder.append(this.fifthDigit);
		builder.append(' ');
		builder.append(this.sixthDigit);
		builder.append(this.seventhDigit);
		builder.append(this.eighthDigit);
		builder.append(this.ninthDigit);
		builder.append(this.tenthDigit);
		builder.append(' ');
		builder.append(this.eleventhDigit);
		builder.append(this.twelfthDigit);
		builder.append(' ');
		builder.append(this.checksum);
		return builder.toString();
	}

	public String toStringNoSpace() {
		return String.valueOf(this.firstDigit) + this.secondDigit + this.thirdDigit + this.forthDigit + this.fifthDigit + this.sixthDigit + this.seventhDigit + this.eighthDigit + this.ninthDigit + this.tenthDigit + this.eleventhDigit + this.twelfthDigit + this.checksum;
	}

	public static int compare(NationalID x, NationalID y) {
		if(x == null && y == null) {
			return 0;
		} else if(x == null) {
			return -1;
		} else if(y == null) {
			return 1;
		}

		if(x.firstDigit == y.firstDigit) {
			if(x.secondDigit == y.secondDigit) {
				if(x.thirdDigit == y.thirdDigit) {
					if(x.forthDigit == y.forthDigit) {
						if(x.fifthDigit == y.fifthDigit) {
							if(x.sixthDigit == y.sixthDigit) {
								if(x.seventhDigit == y.seventhDigit) {
									if(x.eighthDigit == y.eighthDigit) {
										if(x.ninthDigit == y.ninthDigit) {
											if(x.tenthDigit == y.tenthDigit) {
												if(x.eleventhDigit == y.eleventhDigit) {
													if(x.twelfthDigit == y.twelfthDigit) {
														return Integer.compare(x.checksum, y.checksum);
													}

													return Integer.compare(x.twelfthDigit, y.twelfthDigit);
												}

												return Integer.compare(x.eleventhDigit, y.eleventhDigit);
											}

											return Integer.compare(x.tenthDigit, y.tenthDigit);
										}

										return Integer.compare(x.ninthDigit, y.ninthDigit);
									}

									return Integer.compare(x.eighthDigit, y.eighthDigit);
								}

								return Integer.compare(x.seventhDigit, y.seventhDigit);
							}

							return Integer.compare(x.sixthDigit, y.sixthDigit);
						}

						return Integer.compare(x.fifthDigit, y.fifthDigit);
					}

					return Integer.compare(x.forthDigit, y.forthDigit);
				}

				return Integer.compare(x.thirdDigit, y.thirdDigit);
			}

			return Integer.compare(x.secondDigit, y.secondDigit);
		}

		return Integer.compare(x.firstDigit, y.firstDigit);
	}
}
