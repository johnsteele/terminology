/*******************************************************************************
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.terminology.ui.folding;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.IRegion;
import org.eclipse.xtext.ui.editor.folding.DefaultFoldedPosition;
import org.eclipse.xtext.ui.editor.folding.DefaultFoldingRegionAcceptor;
import org.eclipse.xtext.ui.editor.folding.DefaultFoldingRegionProvider;
import org.eclipse.xtext.ui.editor.folding.FoldedPosition;
import org.eclipse.xtext.ui.editor.folding.IFoldingRegionAcceptor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.ITextRegion;

import de.itemis.tooling.terminology.terminology.TerminologyPackage;

public class TerminologyFoldingRegionProvider extends DefaultFoldingRegionProvider {

	//typed region for custom folding actions/initial folding of particular elements
	public class TypedFoldedRegion extends DefaultFoldedPosition{
		EClass type;
		public TypedFoldedRegion(int offset, int length, int contentStart,
				int contentLength, EClass type) {
			super(offset, length, contentStart, contentLength);
			this.type=type;
		}

		public EClass getType() {
			return type;
		}
	}

	//hack for marking typed regions, the acceptor is informed beforehand about the type
	//of the foldable element
	//the corresponding marker region is created in case the
	//offset (start of line) does not correspond with that of the previous region
	//this could happen e.g. for a triple (string in first line of the triple)
	//in which case there would be two foldable regions starting on the same line
	//this leads to unexpected behaviour as you cannot choose which region to expand/collapse
	private class TerminologyFoldingRegionAcceptor extends DefaultFoldingRegionAcceptor{
		private EClass type;
		private int lastOffset=-1;
		public TerminologyFoldingRegionAcceptor(IXtextDocument document,
				Collection<FoldedPosition> result) {
			super(document, result);
		}

		@Override
		protected FoldedPosition newFoldedPosition(IRegion region,
				ITextRegion significantRegion) {
			FoldedPosition result=null;
			if(region!=null && lastOffset!=region.getOffset()){
				if(type!=null && significantRegion!=null){
					//additional -1 introduced as it seemed to help for folding 2-line regions that where otherwise not folded
					result= new TypedFoldedRegion(region.getOffset(), region.getLength(), significantRegion.getOffset() - region.getOffset()-1, significantRegion.getLength(),type);
				}else{
					result= super.newFoldedPosition(region, significantRegion);
				}
			}
			if(result!=null){
				lastOffset=region.getOffset();
			}
			return result;
		}

		public void setRegionType(EClass type) {
			this.type = type;
		}
	}

	@Override
	protected IFoldingRegionAcceptor<ITextRegion> createAcceptor(
			IXtextDocument xtextDocument,
			Collection<FoldedPosition> foldedPositions) {
		return new TerminologyFoldingRegionAcceptor(xtextDocument, foldedPositions);
	}

	@Override
	protected void computeObjectFolding(EObject eObject,
			IFoldingRegionAcceptor<ITextRegion> foldingRegionAcceptor) {
		((TerminologyFoldingRegionAcceptor)foldingRegionAcceptor).setRegionType(eObject.eClass());
		super.computeObjectFolding(eObject, foldingRegionAcceptor);
	}

	private static final Set<EClass>foldableElements=new HashSet<EClass>(Arrays.asList(TerminologyPackage.Literals.META_DATA, TerminologyPackage.Literals.TERM, TerminologyPackage.Literals.FEEDBACK));

	@Override
	protected boolean isHandled(EObject eObject) {
		return foldableElements.contains(eObject.eClass());
	}
}
