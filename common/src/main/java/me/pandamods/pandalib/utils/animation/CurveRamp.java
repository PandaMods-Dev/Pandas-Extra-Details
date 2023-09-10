package me.pandamods.pandalib.utils.animation;

import org.joml.Math;

import java.util.Arrays;
import java.util.List;

public class CurveRamp {
	private List<KeyPoint> keys;

	public CurveRamp(KeyPoint... keys) {
		this.keys = Arrays.asList(keys);
	}

	public float getValue(float time) {
		int p0, p1, p2, p3;

		// Find the points that surround the current time
		if (time <= keys.get(0).position.x) {
			p0 = p1 = p2 = p3 = 0;
		} else if (time >= keys.get(keys.size() - 1).position.x) {
			p0 = p1 = p2 = p3 = keys.size() - 1;
		} else {
			for (p1 = 0; p1 < keys.size() - 1 && time > keys.get(p1 + 1).position.x; p1++) {
				// Find the index of the key to the right of the current time
			}
			p0 = Math.max(0, p1 - 1);
			p2 = Math.min(keys.size() - 1, p1 + 1);
			p3 = Math.min(keys.size() - 1, p1 + 2);
		}

		// Calculate the t parameter within the segment
		float dt = keys.get(p2).position.x - keys.get(p1).position.x;
		if (dt <= 0) {
			dt = 1;
		}
		float t1 = (time - keys.get(p1).position.x) / dt;

		// Interpolate using the appropriate method based on key type
		if (keys.get(p1).type == KeyType.LINEAR && keys.get(p2).type == KeyType.LINEAR) {
			// Use linear interpolation
			return Math.lerp(keys.get(p1).position.y, keys.get(p2).position.y, t1);
		} else {
			// Use Catmull-Rom interpolation
			float tt = t1 * t1;
			float ttt = t1 * tt;
			float q1 = -ttt + 2.0f * tt - t1;
			float q2 = 3.0f * ttt - 5.0f * tt + 2.0f;
			float q3 = -3.0f * ttt + 4.0f * tt + t1;
			float q4 = ttt - tt;
			return 0.5f * (q1 * keys.get(p0).position.y + q2 * keys.get(p1).position.y + q3 * keys.get(p2).position.y + q4 * keys.get(p3).position.y);
		}
	}
}
