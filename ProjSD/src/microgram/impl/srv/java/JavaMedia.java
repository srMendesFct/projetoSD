package microgram.impl.srv.java;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import static microgram.api.java.Result.error;
import static microgram.api.java.Result.ok;
import static microgram.api.java.Result.ErrorCode.CONFLICT;
import static microgram.api.java.Result.ErrorCode.NOT_FOUND;

import microgram.api.java.Result;
import microgram.impl.srv.rest.RestResource;
import utils.Hash;

public class JavaMedia extends RestResource implements microgram.api.java.Media, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	protected Map<String, byte[]> photos = new HashMap<>();

	@Override
	public Result<String> upload(byte[] bytes) {
		String id = Hash.of(bytes);
		if (!photos.containsKey(id)) {
			photos.put(id, bytes);
			return ok(id);
		} else
			return error(CONFLICT);
	}

	@Override
	public Result<byte[]> download(String id) {
		byte[] res = photos.get(id);
		if (res != null)
			return ok(res);
		else
			return error(NOT_FOUND);
	}
	
	public void save() {
		try {
			FileOutputStream fileOut = new FileOutputStream("/tmp/JavaMedia.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this.getClass());
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in /tmp/JavaMedia.ser");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
