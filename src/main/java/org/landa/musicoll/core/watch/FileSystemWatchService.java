package org.landa.musicoll.core.watch;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * 
 * @author lzsolt
 * 
 */
public class FileSystemWatchService {

	private static final Logger LOGGER = Logger.getLogger(FileSystemWatchService.class);

	private final File basePath;
	private FileSystemListener listener;

	@Inject
	public FileSystemWatchService(@Named("basePath") File basePath) {
		this.basePath = basePath;
	}

	public void watch() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				watchThread();
			}
		}).start();
	}

	private void watchThread() {

		// define a folder root
		Path myDir = Paths.get(basePath.getAbsolutePath());

		try {

			WatchService watchService = FileSystems.getDefault().newWatchService();
			myDir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

			for (;;) {
				WatchKey key = watchService.take();

				// Poll all the events queued for the key
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind kind = event.kind();

					Path path = (Path) event.context();

					File absoluteFile = path.toFile().getAbsoluteFile();

					LOGGER.info(event.toString() + " " + event.kind().name() + " " + event.context() + " " + absoluteFile);

					dispatcheEvent(event);

				}
				// reset is invoked to put the key back to ready state
				boolean valid = key.reset();

				// If the key is invalid, just exit.
				if (!valid) {
					break;
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error while watching dir", e);
		}
	}

	private void dispatcheEvent(WatchEvent<?> event) {
		if (this.listener != null) {

			listener.fileSystemChanged(event);
		}
	}

	public void setListener(FileSystemListener listener) {
		this.listener = listener;
	}

}
