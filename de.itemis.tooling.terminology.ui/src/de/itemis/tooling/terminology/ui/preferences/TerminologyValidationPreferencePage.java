/*******************************************************************************
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.terminology.ui.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.google.inject.Inject;

public class TerminologyValidationPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage{

	@Inject
	public TerminologyValidationPreferencePage(IPreferenceStore preferenceStore) {
		super();
		setPreferenceStore(preferenceStore);
//		setDescription("Choose the error levels for ");
	}

	private static final String[][] otherErrors=new String[][]{
		{"no validation","null"},{"info","info"},{"warning","warn"},{"error","error"}
	};

	@Override
	protected void createFieldEditors() {
		addField(new ComboFieldEditor(TerminologyPreferenceConstants.VALIDATION_PREFTERM_PERLANGUAGE_KEY, "one preferred term per language",otherErrors, getFieldEditorParent()));
		addField(new ComboFieldEditor(TerminologyPreferenceConstants.VALIDATION_MISSING_PREFTERM_KEY, "missing preferred term for language",otherErrors, getFieldEditorParent()));
		addField(new ComboFieldEditor(TerminologyPreferenceConstants.VALIDATION_UNIQUE_ENTRY_KEY, "unique entry id per terminology",otherErrors, getFieldEditorParent()));
		addField(new ComboFieldEditor(TerminologyPreferenceConstants.VALIDATION_UNIQUE_TERM_KEY, "duplicate term per terminology",otherErrors, getFieldEditorParent()));
		addField(new ComboFieldEditor(TerminologyPreferenceConstants.VALIDATION_MISSING_DEFINITION_KEY, "missing definition",otherErrors, getFieldEditorParent()));
		addField(new ComboFieldEditor(TerminologyPreferenceConstants.VALIDATION_ENTRY_REF_SYMMETRIC_KEY, "missing inverse entry reference",otherErrors, getFieldEditorParent()));
	}
	public void init(IWorkbench workbench) {}
}
