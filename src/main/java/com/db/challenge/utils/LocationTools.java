package com.db.challenge.utils;

public class LocationTools {

	/**
	 * Return the distances in meters between to coordinates.
	 * 
	 * @param lat_a
	 * @param lng_a
	 * @param lat_b
	 * @param lng_b
	 * @return
	 */
	public static double distanceBetween(double lat_a, double lng_a, double lat_b, double lng_b) {

		float pk = (float) (180.f / Math.PI);
		double a1 = lat_a / pk;
		double a2 = lng_a / pk;
		double b1 = lat_b / pk;
		double b2 = lng_b / pk;

		double t1 = (Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2));
		double t2 = (Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2));
		double t3 = (Math.sin(a1) * Math.sin(b1));

		double tt = Math.acos(t1 + t2 + t3);
		return 6366000 * tt;
	}
}
