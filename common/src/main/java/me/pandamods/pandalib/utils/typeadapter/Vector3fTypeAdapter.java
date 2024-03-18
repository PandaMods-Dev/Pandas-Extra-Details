package me.pandamods.pandalib.utils.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.io.IOException;

public class Vector3fTypeAdapter extends TypeAdapter<Vector3fc> {
	@Override
	public void write(JsonWriter jsonWriter, Vector3fc vector3fc) throws IOException {
		jsonWriter.beginArray();
		jsonWriter.value(vector3fc.x());
		jsonWriter.value(vector3fc.y());
		jsonWriter.value(vector3fc.z());
		jsonWriter.endArray();
	}

	@Override
	public Vector3fc read(JsonReader jsonReader) throws IOException {
		jsonReader.beginArray();
		float x = (float) jsonReader.nextDouble();
		float y = (float) jsonReader.nextDouble();
		float z = (float) jsonReader.nextDouble();
		jsonReader.endArray();
		return new Vector3f(x, y, z);
	}
}