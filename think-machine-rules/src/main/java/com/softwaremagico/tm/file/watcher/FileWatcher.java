package com.softwaremagico.tm.file.watcher;

import java.io.File;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *  
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *  
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.log.MachineLog;

public class FileWatcher {
	private WatchQueueReader fileWatcher = null;
	public String directoryToWatch = null;
	private Set<FileModifiedListener> fileModifiedListeners;
	private Set<FileAddedListener> fileAddedListeners;
	private Set<FileRemovedListener> fileRemovedListeners;
	private Thread thread;
	private Path pathToWatch = null;
	private WatchService watcher = null;

	public interface FileModifiedListener {
		void changeDetected(Path pathToFile);
	}

	public interface FileAddedListener {
		void fileCreated(Path pathToFile);
	}

	public interface FileRemovedListener {
		void fileDeleted(Path pathToFile);
	}

	/**
	 * Check some files in a specific path
	 * 
	 * @param directoryToWatch directory where the files are stored.
	 * @param filesNames       the files to check
	 * @throws IOException
	 */
	public FileWatcher(String directoryToWatch, Set<String> filesNames) throws IOException {
		if (directoryToWatch != null) {
			setDirectoryToWatch(directoryToWatch);
		} else {
			setDirectoryToWatch(FileReader.class.getClassLoader().getResource(".").toString());
		}
		fileModifiedListeners = new HashSet<>();
		fileAddedListeners = new HashSet<>();
		fileRemovedListeners = new HashSet<>();
		// Remove unexisting files.
		for (final String fileName : new ArrayList<>(filesNames)) {
			final File file = new File(directoryToWatch + File.pathSeparator + fileName);
			if (!file.exists()) {
				filesNames.remove(fileName);
			}
		}

		if (!filesNames.isEmpty()) {
			startWatcher(filesNames);
		}
	}

	/**
	 * Check some files in the resource directory.
	 * 
	 * @param filesNames
	 * @throws IOException
	 */
	public FileWatcher(Set<String> filesNames) throws IOException {
		this(null, filesNames);
	}

	/**
	 * Only check for a directory. Watch if any file is added, updated or deleted.
	 * 
	 * @param directoryToWatch
	 * @throws IOException
	 */
	public FileWatcher(String directoryToWatch) throws IOException {
		this(directoryToWatch, null);
	}

	public void addFileModifiedListener(FileModifiedListener listener) {
		fileModifiedListeners.add(listener);
	}

	public void addFileAddedListener(FileAddedListener listener) {
		fileAddedListeners.add(listener);
	}

	public void addFileRemovedListener(FileRemovedListener listener) {
		fileRemovedListeners.add(listener);
	}

	private Path getDirectoryToWatch() {
		if (pathToWatch == null) {
			pathToWatch = Paths.get(directoryToWatch);
		}
		return pathToWatch;
	}

	private WatchService getWatchService() throws IOException {
		if (watcher == null) {
			if (getDirectoryToWatch() == null) {
				throw new UnsupportedOperationException("Directory not found");
			}
			watcher = getDirectoryToWatch().getFileSystem().newWatchService();
		}
		return watcher;
	}

	private void startWatcher(final Set<String> filesNames) {
		try {
			fileWatcher = new WatchQueueReader(getWatchService(), getDirectoryToWatch());
			fileWatcher.setFilesNames(filesNames);
			stopThread();
			thread = new Thread(fileWatcher, "FileWatcher");
			thread.start();
			pathToWatch.register(getWatchService(), StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE);
			// Ensure to close the watcher.
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						MachineLog.debug(this.getClass().getName(),
								"Closing filewatcher for directory '" + directoryToWatch + "' and files '" + filesNames + "'.");
						if (watcher != null) {
							watcher.close();
						}
					} catch (Exception e) {
						MachineLog.errorMessage(this.getClass().getName(), e);
					}
				}
			});
		} catch (NoSuchFileException e) {
			MachineLog.warning(this.getClass().getName(), "Folder '" + getDirectoryToWatch() + "' not found!");
		} catch (IOException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		}
	}

	private class WatchQueueReader implements Runnable {
		private WatchService watcher;
		private Path pathToWatch;
		private Set<String> filesNames;

		public WatchQueueReader(WatchService watcher, Path pathToWatch) {
			this.watcher = watcher;
			this.pathToWatch = pathToWatch;
		}

		@Override
		public void run() {
			try {
				// Get the first event before looping
				WatchKey key = watcher.take();
				while (key != null) {
					// We have a polled event, now we traverse it and receive
					// all the states from it
					for (final WatchEvent<?> event : key.pollEvents()) {
						// Event on a directory or a set of files.
						if (filesNames == null || (filesNames.contains(event.context().toString()))) {
							if (event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
								for (final FileModifiedListener fileModifiedListener : new HashSet<>(fileModifiedListeners)) {
									fileModifiedListener.changeDetected(combine(pathToWatch, (Path) event.context()));
								}
							} else if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)) {
								for (final FileAddedListener fileCreationListener : new HashSet<>(fileAddedListeners)) {
									fileCreationListener.fileCreated(combine(pathToWatch, (Path) event.context()));
								}
							} else if (event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
								for (final FileRemovedListener fileDeletionListener : new HashSet<>(fileRemovedListeners)) {
									fileDeletionListener.fileDeleted(combine(pathToWatch, (Path) event.context()));
								}
							} else if (event.kind().equals(StandardWatchEventKinds.OVERFLOW)) {
								MachineLog.severe(this.getClass().getName(), "File Watcher events vents may have been lost or discarded.");
							}
						}
					}
					key.reset();
					key = watcher.take();
				}
			} catch (InterruptedException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			} catch (ClosedWatchServiceException e) {
				// watcher closed. Do nothing.
				return;
			}
		}

		public void setFilesNames(Set<String> filesNames) {
			this.filesNames = filesNames;
		}
	}

	protected static Path combine(Path path1, Path path2) {
		return Paths.get(path1.toString(), path2.toString());
	}

	public void closeFileWatcher() {
		pathToWatch = null;
		if (watcher != null) {
			try {
				watcher.close();
				stopThread();
			} catch (IOException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
		watcher = null;
	}

	public void setDirectoryToWatch(String directoryToWatch) {
		if (!directoryToWatch.equals(this.directoryToWatch)) {
			this.directoryToWatch = directoryToWatch;
			closeFileWatcher();
		}
	}

	public void stopThread() {
		if (thread != null) {
			thread.interrupt();
		}
	}

}
