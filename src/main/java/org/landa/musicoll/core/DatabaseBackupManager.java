package org.landa.musicoll.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.landa.musicoll.App;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * 
 * @author lzsolt
 * 
 */
public class DatabaseBackupManager {

	private static final String MUSICOLL_BACKUPS = "musicoll-backups";
	private final File basePath;

	@Inject
	public DatabaseBackupManager(@Named("basePath") final File basePath) {
		this.basePath = basePath;

	}

	public void createBackup(File dbFile) {

		File backupDir = new File(basePath.getAbsoluteFile().getAbsolutePath() + File.separator + MUSICOLL_BACKUPS);

		if (!backupDir.exists()) {
			backupDir.mkdir();
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		String date = dateFormat.format(new Date());
		File destFile = new File(backupDir, dbFile.getName() + "-" + date);

		try {
			Files.copy(dbFile.toPath(), destFile.toPath());
		} catch (IOException e) {
			App.LOGGER.error("Cannot create backup", e);
		}

	}
}
