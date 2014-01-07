/*******************************************************************************
 *  Copyright (c) 2003, 2009 IBM Corporation and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *   
 *  Source:
 *     https://github.com/eclipse/eclipse.pde.ui/blob/master/ui/org.eclipse.pde.ui/src/org/eclipse/pde/internal/ui/parts/ComboPart.java
 *******************************************************************************/
package si.gos.eclipse.parts;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import si.gos.eclipse.widgets.helper.IWidgetFactory;

public class ComboPart {

	protected Control combo;

	public ComboPart() {
	}

	public void addSelectionListener(SelectionListener listener) {
		if (combo instanceof Combo)
			((Combo) combo).addSelectionListener(listener);
		else
			((CCombo) combo).addSelectionListener(listener);
	}

	public int indexOf(String item) {
		if (combo instanceof Combo)
			return ((Combo) combo).indexOf(item);

		return ((CCombo) combo).indexOf(item);
	}

	public void addModifyListener(ModifyListener listener) {
		if (combo instanceof Combo)
			((Combo) combo).addModifyListener(listener);
		else
			((CCombo) combo).addModifyListener(listener);
	}

	public void createControl(Composite parent, IWidgetFactory factory, int style) {
		combo = factory.createCoolCombo(parent, style);
	}

	public Control getControl() {
		return combo;
	}

	public int getSelectionIndex() {
		if (combo instanceof Combo)
			return ((Combo) combo).getSelectionIndex();
		return ((CCombo) combo).getSelectionIndex();
	}

	public void add(String item, int index) {
		if (combo instanceof Combo)
			((Combo) combo).add(item, index);
		else
			((CCombo) combo).add(item, index);
	}

	public void add(String item) {
		if (combo instanceof Combo)
			((Combo) combo).add(item);
		else
			((CCombo) combo).add(item);
	}

	/**
	 * @param index
	 */
	public void remove(int index) {
		// Ensure the index is valid
		if ((index < 0) || (index >= getItemCount())) {
			return;
		}
		// Remove the item from the specified index
		if (combo instanceof Combo) {
			((Combo) combo).remove(index);
		} else {
			((CCombo) combo).remove(index);
		}
	}

	public void select(int index) {
		if (combo instanceof Combo)
			((Combo) combo).select(index);
		else
			((CCombo) combo).select(index);
	}

	public String getSelection() {
		if (combo instanceof Combo)
			return ((Combo) combo).getText().trim();
		return ((CCombo) combo).getText().trim();
	}

	public void setText(String text) {
		if (combo instanceof Combo)
			((Combo) combo).setText(text);
		else
			((CCombo) combo).setText(text);
	}

	public void setItems(String[] items) {
		if (combo instanceof Combo)
			((Combo) combo).setItems(items);
		else
			((CCombo) combo).setItems(items);
	}

	public void setEnabled(boolean enabled) {
		if (combo instanceof Combo)
			((Combo) combo).setEnabled(enabled);
		else
			((CCombo) combo).setEnabled(enabled);
	}

	public int getItemCount() {
		if (combo instanceof Combo)
			return ((Combo) combo).getItemCount();
		return ((CCombo) combo).getItemCount();
	}

	public String[] getItems() {
		if (combo instanceof Combo)
			return ((Combo) combo).getItems();
		return ((CCombo) combo).getItems();
	}

	public void setVisibleItemCount(int count) {
		if (combo instanceof Combo)
			((Combo) combo).setVisibleItemCount(count);
		else
			((CCombo) combo).setVisibleItemCount(count);
	}
}
