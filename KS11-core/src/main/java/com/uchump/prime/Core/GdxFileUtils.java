package com.uchump.prime.Core;

import static com.uchump.prime.Core.uAppUtils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class GdxFileUtils {

	// listDirectory
	public static String listDirectory(String dir) {
		//

		String log = "";
		log += "\n" + "DirectoryList";
		log += "\n (((";
		log += "FileSys: -GDXFileUtils ";
		log += "\n";
		log += "Directory: " + dir;

		FileHandle input = Gdx.files.internal("." + dir); // tmp directory for created assets?
		log += "\n";
		log += "FilesFound: " + input.list().length;
		log += "\n";
		log += "\n" + input.type();
		log += "\n";

		for (FileHandle f : input.list()) {

			log += "^>";
			log += "\n" + f.type();
			log += "><" + f.path();
			log += "\n";
			log += ">><<" + f.list();
			log += "\n";
		}

		log += "\n::___::";
		log += "\n";
		return log;
	}

	public static ArrayList<FileHandle> getFilesFrom(String dir) {
		FileHandle input = Gdx.files.internal("." + dir);
		ArrayList<FileHandle> result = new ArrayList<FileHandle>();
		for (FileHandle f : input.list()) {
			result.add(f);
			if (f.isDirectory()) {
				// ArrayList<FileHandle> F = getFilesFrom("."+f.name().substring(2)); //removes
				// preceding ...s
				ArrayList<FileHandle> F = getFilesFrom("." + f.name().substring(1));
				result.addAll(F);
			}
		}

		return result;
	}

	public static HashMap<String, FileHandle> mapFiles(ArrayList<FileHandle> files) {

		HashMap<String, FileHandle> result = new HashMap<String, FileHandle>();
		for (FileHandle F : files) {
			result.put(F.nameWithoutExtension(), F);
		}
		return result;
	}

	public static FileHandle getFile(String dir) {
		FileHandle input = Gdx.files.internal("." + dir);

		return input;
	}

	public static ArrayList<FileHandle> fillFilesFrom(ArrayList<FileHandle> to, String dir) {
		FileHandle input = Gdx.files.internal("." + dir);
		ArrayList<FileHandle> result = new ArrayList<FileHandle>();
		for (FileHandle f : input.list()) {
			result.add(f);
		}

		for (FileHandle h : result) {
			to.add(h);
		}

		return to;
	}

	public static HashMap<String, ArrayList<String>> mapFolder(File folder) {
		HashMap<String, ArrayList<String>> dirFiles = new HashMap<String, ArrayList<String>>();
		if (folder.isDirectory()) {

			ArrayList<String> fileNames = new ArrayList<String>();

			for (final File fileEntry : folder.listFiles()) {
				// System.out.println(fileEntry.toString());
				if (fileEntry.isDirectory()) {
					// System.out.println(fileEntry.toString());
					mapFolder(fileEntry);
				} else {
					String fileName = (fileEntry.getPath()).toString();
					fileNames.add(fileEntry.getPath());
				}
			}
			dirFiles.put(folder.getName(), fileNames);
		}
		return dirFiles;
	}

}
