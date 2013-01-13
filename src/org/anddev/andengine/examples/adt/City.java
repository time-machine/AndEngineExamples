package org.anddev.andengine.examples.adt;

public class City {
  private final String mName;
  private final double mLatitude;
  private final double mLongtitude;
  private double mDistanceToUser;
  private double mBearingToUser;

  public City(final String pName, final double pLatitude,
      final double pLongtitude) {
    mName = pName;
    mLatitude = pLatitude;
    mLongtitude = pLongtitude;
  }

  public final String getName() {
    return mName;
  }

  public final double getLatitude() {
    return mLatitude;
  }

  public final double getLongtitude() {
    return mLongtitude;
  }

  public double getDistanceToUser() {
    return mDistanceToUser;
  }

  public void setDistanceToUser(final double pDistanceToUser) {
    mDistanceToUser = pDistanceToUser;
  }

  public double getBearingToUser() {
    return mBearingToUser;
  }

  public void setBearingToUser(final double pBearingToUser) {
    mBearingToUser = pBearingToUser;
  }
}
