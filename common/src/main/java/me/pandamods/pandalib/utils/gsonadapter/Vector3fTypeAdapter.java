package me.pandamods.pandalib.utils.gsonadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.joml.Vector3f;

import java.io.IOException;

public class Vector3fTypeAdapter extends TypeAdapter<Vector3f> {
	@Override
	public void write(JsonWriter out, Vector3f vector) throws IOException {
		out.beginArray();
		out.value(vector.x);
		out.value(vector.y);
		out.value(vector.z);
		out.endArray();
	}

	@Override
	public Vector3f read(JsonReader in) throws IOException {
		in.beginArray();
		float x = (float) in.nextDouble();
		float y = (float) in.nextDouble();
		float z = (float) in.nextDouble();
		in.endArray();
		return new Vector3f(x, y, z);
	}
}
