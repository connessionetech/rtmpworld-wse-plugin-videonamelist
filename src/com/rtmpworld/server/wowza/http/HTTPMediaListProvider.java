package com.rtmpworld.server.wowza.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.wowza.util.HTTPUtils;
import com.wowza.wms.http.HTTProvider2Base;
import com.wowza.wms.http.IHTTPRequest;
import com.wowza.wms.http.IHTTPResponse;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.vhost.IVHost;

public class HTTPMediaListProvider extends HTTProvider2Base {

	public static final String WOWZAHOMEDIR = "WMSAPP_HOME";

	private static String PROP_NAME_PREFIX = "medialist";
	protected static String MODULE_NAME = "ModuleVideoNamesList";

	private static final Class<HTTPMediaListProvider> CLASS = HTTPMediaListProvider.class;
	private static final String CLASSNAME = "HTTPMediaListProvider";

	private static String[] ALLOWED_TYPES = { "mp4", "flv", "f4v", "mp3", "m4a", "3g2", "3gp" };

	public HTTPMediaListProvider() {

	}

	@Override
	public void onHTTPRequest(IVHost vhost, IHTTPRequest req, IHTTPResponse res) {

		String queryStr = req.getQueryString();
		OutputStream out = res.getOutputStream();
		res.setHeader("Content-Type", "application/json; ; charset=UTF-8");

		try {

			if (queryStr == null) {
				WMSLoggerFactory.getLogger(CLASS).warn(CLASSNAME + ".onHTTPRequest: Query string missing");
				queryStr = "";
			}

			Map<String, String> queryMap = HTTPUtils.splitQueryStr(queryStr);

			// gather valid requested types
			List<String> requested_types = new ArrayList<String>();
			String[] fileTypes = req.getParameterValues("types");
			if (fileTypes != null && fileTypes.length > 0) {
				for (String type : fileTypes) {
					// Do your stuff here
					if (Arrays.asList(ALLOWED_TYPES).contains(type)) {
						requested_types.add(type);
					}
				}
			} else {
				requested_types = Arrays.asList(ALLOWED_TYPES);
			}

			// include hls fragments or not
			boolean include_hls_fragments = (queryMap.containsKey("include_hls_fragments"))
					? Boolean.valueOf(queryMap.get("include_hls_fragments"))
					: false;
			Resource[] resources = getResources(requested_types, include_hls_fragments);

			SuccessResponse success = new SuccessResponse();
			success.data = resources;

			byte[] outBytes = new Gson().toJson(success).toString().getBytes();
			out.write(outBytes);

		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse();
			error.code = 400;
			error.data = e.getMessage();

			byte[] outBytes = new Gson().toJson(error).toString().getBytes();

			try {
				out.write(outBytes);
			} catch (IOException e1) {
				WMSLoggerFactory.getLogger(CLASS).error(CLASSNAME + ".onHTTPRequest: " + e1.getMessage());
			}
		}
	}

	class SuccessResponse extends Response {
		String type = "data";

		public SuccessResponse() {
			this.code = 200;
		}
	}

	class ErrorResponse extends Response {
		String type = "error";
	}

	class Response {
		int code;
		Object data;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}
	}

	class Resource {
		String name;
		String created;
		String modified;
		long size;

		public Resource(File f) throws IOException {

			Path file = Paths.get(f.getAbsolutePath());
			BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);

			name = f.getName();
			size = Files.size(file);
			created = attr.creationTime().toString();
			modified = attr.lastModifiedTime().toString();
		}
	}
	
	
	
	

	public Resource[] getResources(List<String> types, boolean include_hls_fragments) {
		String wowzaHomeDir = System.getenv(WOWZAHOMEDIR);
		String storageDir = "content";

		if (wowzaHomeDir == null) {
			throw new RuntimeException("Environment variable WMSAPP_HOME is empty");
		} else if (!wowzaHomeDir.endsWith("/") && !wowzaHomeDir.endsWith("\\")) {
			storageDir = wowzaHomeDir + File.separatorChar + storageDir;
		} else {
			storageDir = wowzaHomeDir + storageDir;
		}
		File fStorageDir = new File(storageDir);

		ArrayList<Resource> res = new ArrayList<Resource>();

		for (File file : fStorageDir.listFiles()) {
			if (file.isFile()) {
				String extension = getExtensionByStringHandling(file.getName()).get();
				if (types.contains(extension)) {
					try {
						res.add(new Resource(file));
					} catch (IOException e) {
						WMSLoggerFactory.getLogger(CLASS).error(CLASSNAME + ".getResources: Unable to gather info for file " + file.getAbsolutePath());
					}
				}
			}
		}

		Resource[] resources = res.toArray(new Resource[res.size()]);
		return resources;

	}

	public Optional<String> getExtensionByStringHandling(String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}

}
