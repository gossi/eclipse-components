/*******************************************************************************
 *  Copyright (c) 2000, 2008 IBM Corporation and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *  Source:
 *     https://github.com/eclipse/eclipse.pde.ui/blob/master/ui/org.eclipse.pde.ui/src/org/eclipse/pde/internal/ui/parts/TablePart.java
 *******************************************************************************/
package si.gos.eclipse.parts;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

import si.gos.eclipse.widgets.helper.IWidgetFactory;

public class TablePart extends StructuredViewerPart {

	/**
	 * Constructor for TablePart.
	 * @param buttonLabels
	 */
	public TablePart(String[] buttonLabels) {
		super(buttonLabels);
	}
	
	/**
	 * Creates are TablePart with sensitive buttons. Sensitive buttons will 
	 * be enabled once a selection is made in the viewer. 
	 *  
	 * @param buttonLabels
	 * @param sensitiveButtons indices of sensitive buttons
	 */
	public TablePart(String[] buttonLabels, int[] sensitiveButtons) {
		super(buttonLabels, sensitiveButtons);
	}

	/*
	 * @see StructuredViewerPart#createStructuredViewer(Composite, IWidgetFactory)
	 */
	protected StructuredViewer createStructuredViewer(Composite parent, int style, IWidgetFactory factory) {
		return factory.createTableViewer(parent, true);
	}

	public TableViewer getTableViewer() {
		return (TableViewer) getViewer();
	}

}