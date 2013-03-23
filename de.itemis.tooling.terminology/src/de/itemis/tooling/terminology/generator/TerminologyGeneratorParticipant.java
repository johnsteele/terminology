package de.itemis.tooling.terminology.generator;

import java.util.List;

import org.eclipse.xtext.xbase.lib.Pair;

import com.google.common.base.Objects;

import de.itemis.tooling.terminology.terminology.SubjectEntries;

public abstract class TerminologyGeneratorParticipant {

	private String folder;
	private boolean active;

	/**
	 * provide a list filename->content for the given catagory
	 * files are written to <Compiler-output-folder>/<TerminologyName>/<folder>/<file>
	 * where folder is the folder configured for this generator and file is the 
	 * key element of the return values
	 * */
	public abstract List<Pair<String, CharSequence>> getFileContents(SubjectEntries entries);

	/**
	 * default folder in which the files are placed
	 * */
	public abstract String getDefaultFolder();

	/**
	 * Display name of the generator (shown in the complier preference page)
	 * */
	public abstract String getDisplayName();

	/**
	 * id of this generator
	 * */
	public abstract String getId();

	public final void setFolder(String folder) {
		this.folder = folder;
	}

	public final String getFolder() {
		if(folder!=null&&folder.length()>0){
			//we do not want any navigation to parent folders
			int i=0;
			while(i<folder.length() &&!Character.isLetterOrDigit(folder.charAt(i))){
				i++;
			}
			folder=folder.substring(i);
		}
		return folder;
	}

	public final void setActive(boolean active) {
		this.active = active;
	}

	public final boolean isActive() {
		return active;
	}

	@Override
	public final String toString() {
		return Objects.toStringHelper(getClass()).add("id", getId()).add("active", isActive()).add("folder", getFolder()).toString();
	}
}
