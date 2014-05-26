package org.landa.musicoll.core;

import java.io.File;

import org.landa.musicoll.model.Resource;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class FilePlaceResolver {

	private final File basePath;
	private final String absoluteBasePath;

	@Inject
	public FilePlaceResolver(@Named("basePath") final File basePath) {
		this.basePath = basePath;
		this.absoluteBasePath = canonize(basePath.getAbsoluteFile()
				.getAbsolutePath());
	}

	public String getRelativePath(File file) {

		String absolutePath = file.getAbsoluteFile().getAbsolutePath();

		String replace = canonize(absolutePath).replace(absoluteBasePath, "");

		return replace;

	}

	private String canonize(String path) {
		return path.replaceAll("\\\\", "/");
	}

	public File getFile(Resource resource) {

		return new File(absoluteBasePath + resource.getRelativePath());

	}

}
