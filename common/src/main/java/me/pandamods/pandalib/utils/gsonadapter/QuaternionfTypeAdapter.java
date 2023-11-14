package me.pandamods.pandalib.utils.gsonadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.IOException;

public class QuaternionfTypeAdapter extends TypeAdapter<Quaternionf> {
	@Override
	public void write(JsonWriter out, Quaternionf quaternionf) throws IOException {
		out.beginArray();
		out.value(quaternionf.w);
		out.value(quaternionf.x);
		out.value(quaternionf.y);
		out.value(quaternionf.z);
		out.endArray();
	}

	@Override
	public Quaternionf read(JsonReader in) throws IOException {
		in.beginArray();
		float w = (float) in.nextDouble();
		float x = (float) in.nextDouble();
		float y = (float) in.nextDouble();
		float z = (float) in.nextDouble();
		in.endArray();
		return new Quaternionf(x, y, z, w);
	}
}
