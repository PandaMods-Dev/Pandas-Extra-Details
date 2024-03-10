package me.pandamods.pandalib.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

public class QuaternionfTypeAdapter extends TypeAdapter<Quaternionfc> {
    public void write(JsonWriter jsonWriter, Quaternionfc quaternion) throws IOException {
		writeQuaternion(jsonWriter, quaternion);
    }

	public static void writeQuaternion(JsonWriter jsonWriter, Quaternionfc quaternion) throws IOException {
		jsonWriter.beginArray();
		jsonWriter.value(quaternion.x());
		jsonWriter.value(quaternion.y());
		jsonWriter.value(quaternion.z());
		jsonWriter.value(quaternion.w());
		jsonWriter.endArray();
	}

    public Quaternionf read(JsonReader jsonReader) throws IOException {
        return readQuaternion(jsonReader);
    }

	public static Quaternionf readQuaternion(JsonReader jsonReader) throws IOException {
		jsonReader.beginArray();
		double x = jsonReader.nextDouble();
		double y = jsonReader.nextDouble();
		double z = jsonReader.nextDouble();
		double w = jsonReader.nextDouble();
		jsonReader.endArray();
		return new Quaternionf(x, y, z, w);
	}
}
