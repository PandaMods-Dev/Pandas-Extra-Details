package me.pandamods.pandalib.utils.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import java.io.IOException;

public class Vector2fTypeAdapter extends TypeAdapter<Vector2fc> {
	@Override
	public void write(JsonWriter jsonWriter, Vector2fc vector2fc) throws IOException {
		jsonWriter.beginArray();
		jsonWriter.value(vector2fc.x());
		jsonWriter.value(vector2fc.y());
		jsonWriter.endArray();
	}

	@Override
	public Vector2fc read(JsonReader jsonReader) throws IOException {
		jsonReader.beginArray();
		float x = (float) jsonReader.nextDouble();
		float y = (float) jsonReader.nextDouble();
		jsonReader.endArray();
		return new Vector2f(x, y);
	}
}