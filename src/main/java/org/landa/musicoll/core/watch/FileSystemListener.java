package org.landa.musicoll.core.watch;

import java.nio.file.WatchEvent;

public interface FileSystemListener {

	public void fileSystemChanged(WatchEvent<?> event);

}
