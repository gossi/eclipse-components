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
 *     https://raw.github.com/eclipse/eclipse.pde.ui/master/ui/org.eclipse.pde.ui/src/org/eclipse/pde/internal/ui/editor/TableSection.java
 *******************************************************************************/
package si.gos.eclipse.editor;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;

import si.gos.eclipse.parts.StructuredViewerPart;
import si.gos.eclipse.parts.TablePart;
import si.gos.eclipse.widgets.helper.ToolkitFactory;

public abstract class TableSection extends StructuredViewerSection {
	protected boolean handleDefaultButton = true;

	class PartAdapter extends TablePart {
		private Label count;

		public PartAdapter(String[] buttonLabels) {
			super(buttonLabels);
		}

		public void entryModified(Object entry, String value) {
			TableSection.this.entryModified(entry, value);
		}

		public void selectionChanged(IStructuredSelection selection) {
			getManagedForm().fireSelectionChanged(TableSection.this, selection);
			TableSection.this.selectionChanged(selection);
		}

		public void handleDoubleClick(IStructuredSelection selection) {
			TableSection.this.handleDoubleClick(selection);
		}

		public void buttonSelected(Button button, int index) {
			TableSection.this.buttonSelected(index);
			if (handleDefaultButton)
				button.getShell().setDefaultButton(null);
		}

		protected void createButtons(Composite parent, FormToolkit toolkit) {
			super.createButtons(parent, new ToolkitFactory(toolkit));
			enableButtons();
			if (createCount()) {
				Composite comp = toolkit.createComposite(fButtonContainer);
				comp.setLayout(createButtonsLayout());
				comp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_END | GridData.FILL_BOTH));
				count = toolkit.createLabel(comp, ""); //$NON-NLS-1$
				count.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
				count.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				getTablePart().getTableViewer().getTable().addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						updateLabel();
					}
				});
			}
		}

		protected void updateLabel() {
			if (count != null && !count.isDisposed())
				count.setText("Total: " + Integer.toString(getTableViewer().getTable().getItemCount()));
		}
	}
	
	/**
	 * Constructor for TableSection.
	 * 
	 * @param formPage
	 */
	public TableSection(SharedFormPage formPage, Composite parent, int style, String[] buttonLabels) {
		this(formPage, parent, style, true, buttonLabels);
	}

	/**
	 * Constructor for TableSection.
	 * 
	 * @param formPage
	 */
	public TableSection(SharedFormPage formPage, Composite parent, int style, boolean titleBar, String[] buttonLabels) {
		super(formPage, parent, style, titleBar, buttonLabels);
	}

	protected StructuredViewerPart createViewerPart(String[] buttonLabels) {
		return new PartAdapter(buttonLabels);
	}

	protected TablePart getTablePart() {
		return (TablePart) viewerPart;
	}

	protected void entryModified(Object entry, String value) {
	}

	protected void selectionChanged(IStructuredSelection selection) {
	}

	protected void handleDoubleClick(IStructuredSelection selection) {
	}

	protected void enableButtons() {
	}

	protected boolean createCount() {
		return false;
	}
}