/*******************************************************************************
 *  Copyright (c) 2000, 2011 IBM Corporation and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *     Philipp Kursawe <phil.kursawe@gmail.com> - bug 272654
 *     
 *  Source:
 * 	   https://github.com/eclipse/eclipse.pde.ui/blob/master/ui/org.eclipse.pde.ui/src/org/eclipse/pde/internal/ui/parts/FormEntry.java
 *******************************************************************************/
package si.gos.eclipse.forms;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import si.gos.eclipse.widgets.utils.IWidgetFactory;
import si.gos.eclipse.widgets.utils.ToolkitFactory;

/**
 * The helper class for creating entry fields with label and text. Optionally,
 * a button can be added after the text. The attached listener reacts to all
 * the events. Entering new text makes the entry 'dirty', but only when 'commit'
 * is called is 'valueChanged' method called (and only if 'dirty' flag is set).
 * This allows delayed commit.
 */
public class FormEntry {
	private Control label;
	private Text text;
	private Button browse;
	boolean ignoreModify = false;

	private ArrayList<IFormEntryListener> listeners = new ArrayList<IFormEntryListener>();

	public static final int DEFAULT_TEXT_WIDTH_HINT = 100;

	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText) {
		this(parent, factory, labelText, null, false);
	}
	
	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param style
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, int style) {
		createControl(parent, factory, labelText, style, null, false, 0, 0);
	}
	
	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param style
	 * @param linkLabel
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, int style, boolean linkLabel) {
		createControl(parent, factory, labelText, style, null, linkLabel, 0, 0);
	}
	
	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param indent
	 * @param colspan
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, int indent, int colspan) {
		this(parent, factory, labelText, null, false, indent, colspan);
	}
	
	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param browseText
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, String browseText) {
		this(parent, factory, labelText, browseText, false, 0, 0);
	}

	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param browseText
	 * @param indent
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, String browseText, int indent) {
		this(parent, factory, labelText, browseText, false, indent, 0);
	}
	
	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param browseText
	 * @param indent
	 * @param colspan
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, String browseText, int indent, int colspan) {
		this(parent, factory, labelText, browseText, false, indent, colspan);
	}
	
	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param linkLabel
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, boolean linkLabel) {
		this(parent, factory, labelText, null, linkLabel, 0, 0);
	}

	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param linkLabel
	 * @param indent
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, boolean linkLabel, int indent) {
		this(parent, factory, labelText, null, linkLabel, indent, 0);
	}
	
	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param linkLabel
	 * @param indent
	 * @param colspan
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, boolean linkLabel, int indent, int colspan) {
		this(parent, factory, labelText, null, linkLabel, indent, colspan);
	}
	
	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param browseText
	 * @param linkLabel
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, String browseText, boolean linkLabel) {
		this(parent, factory, labelText, browseText, linkLabel, 0);
	}

	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param browseText
	 * @param linkLabel
	 * @param indent
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, String browseText, boolean linkLabel, int indent) {
		this(parent, factory, labelText, browseText, linkLabel, indent, 0);
	}
	
	/**
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param browseText
	 * @param linkLabel
	 * @param indent
	 * @param colspan
	 */
	public FormEntry(Composite parent, IWidgetFactory factory, String labelText, String browseText, boolean linkLabel, int indent, int colspan) {
		createControl(parent, factory, labelText, SWT.SINGLE, browseText, linkLabel, indent, colspan);
	}
	
	/**
	 * Create all the controls in the provided parent.
	 * 
	 * @param parent
	 * @param factory
	 * @param labelText
	 * @param span
	 * @param browseText
	 * @param linkLabel
	 */
	private void createControl(Composite parent, IWidgetFactory factory, String labelText, int style, String browseText, boolean linkLabel, int indent, int tcolspan) {
		if (linkLabel) {
			Hyperlink link = factory.createHyperlink(parent, labelText, SWT.NULL);
			label = link;
		} else {
			if (labelText != null) {
				label = factory.createLabel(parent, labelText);
				if (factory instanceof ToolkitFactory) {
					label.setForeground(((ToolkitFactory)factory).getToolkit().getColors().getColor(IFormColors.TITLE));
				}
			}
		}
		text = factory.createText(parent, "", style); //$NON-NLS-1$
		addListeners();
		if (browseText != null) {
			browse = factory.createButton(parent, browseText, SWT.PUSH);
			browse.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					for (IFormEntryListener listener : listeners) {
						listener.browseButtonSelected(FormEntry.this);
					}
				}
			});
		}
		fillIntoGrid(parent, indent, tcolspan);
		// Set the default text width hint and let clients modify accordingly
		// after the fact
		setTextWidthHint(DEFAULT_TEXT_WIDTH_HINT);
	}

	public void setEnabled(boolean enabled) {
		text.setEnabled(enabled);
		if (label instanceof Hyperlink)
			((Hyperlink) label).setUnderlined(enabled);

		if (browse != null)
			browse.setEnabled(enabled);
	}
	
	public void setEditable(boolean editable) {
		text.setEditable(editable);
		if (label instanceof Hyperlink)
			((Hyperlink) label).setUnderlined(editable);

		if (browse != null)
			browse.setEnabled(editable);
	}

	private void fillIntoGrid(Composite parent, int indent, int tcolspan) {
		Layout layout = parent.getLayout();
		int tspan;
		if (layout instanceof GridLayout) {
			int span = ((GridLayout) layout).numColumns;
			if (tcolspan > 0)
				tspan = tcolspan;
			else
				tspan = browse != null ? span - 2 : span - 1;
			GridData gd;
			if (label != null) {
				gd = new GridData(GridData.VERTICAL_ALIGN_CENTER);
				gd.horizontalIndent = indent;
				label.setLayoutData(gd);
			}
			gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
			gd.horizontalSpan = tspan;
			if (label != null) {
				gd.horizontalIndent = FormLayoutFactory.CONTROL_HORIZONTAL_INDENT;
			}
			gd.grabExcessHorizontalSpace = (tspan == 1);
			gd.widthHint = 10;
			text.setLayoutData(gd);
			if (browse != null) {
				gd = new GridData(GridData.VERTICAL_ALIGN_CENTER);
				browse.setLayoutData(gd);
			}
		} else if (layout instanceof TableWrapLayout) {
			int span = ((TableWrapLayout) layout).numColumns;
			if (tcolspan > 0)
				tspan = tcolspan;
			else
				tspan = browse != null ? span - 2 : span - 1;
			TableWrapData td;
			if (label != null) {
				td = new TableWrapData();
				td.valign = TableWrapData.MIDDLE;
				td.indent = indent;
				label.setLayoutData(td);
			}
			td = new TableWrapData(TableWrapData.FILL);
			td.colspan = tspan;
			if (label != null) {
				td.indent = FormLayoutFactory.CONTROL_HORIZONTAL_INDENT;
			}
			td.grabHorizontal = (tspan == 1);
			td.valign = TableWrapData.MIDDLE;
			text.setLayoutData(td);
			if (browse != null) {
				td = new TableWrapData(TableWrapData.FILL);
				td.valign = TableWrapData.MIDDLE;
				browse.setLayoutData(td);
			}
		}
	}

	/**
	 * Attaches the listener for the entry.
	 * 
	 * @param listener
	 */
	public void addFormEntryListener(IFormEntryListener listener) {
		if (label != null && label instanceof Hyperlink) {
			((Hyperlink) label).addHyperlinkListener(listener);
		}
		listeners.add(listener);
	}
	

	/**
	 * Detaches the listener for the entry.
	 * 
	 * @param listener
	 */
	public void removeFormEntryListener(IFormEntryListener listener) {
		if (label != null && label instanceof Hyperlink) {
			((Hyperlink) label).removeHyperlinkListener(listener);
		}
		listeners.remove(listener);
	}

	private void addListeners() {
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!ignoreModify) {
					for (IFormEntryListener listener : listeners) {
						listener.textValueChanged(FormEntry.this);
					}
				}
			}
		});
		text.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				text.selectAll();
				
				for (IFormEntryListener listener : listeners) {
					listener.focusGained(FormEntry.this);
				}
			}

			public void focusLost(FocusEvent e) {
				for (IFormEntryListener listener : listeners) {
					listener.focusLost(FormEntry.this);
				}
			}
		});
	}

	/**
	 * Returns the text control.
	 * 
	 */
	public Text getText() {
		return text;
	}

	public Control getLabel() {
		return label;
	}

	/**
	 * Returns the browse button control.
	 */
	public Button getButton() {
		return browse;
	}

	/**
	 * Returns the current entry value. If the entry is dirty and was not
	 * commited, the value may be different from the text in the widget.
	 * 
	 */
	public String getValue() {
		if (text != null && !text.isDisposed()) {
			return text.getText().trim();
		}
		
		return null;
	}

	/**
	 * Sets the value of this entry.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		if (value == null) {
			value = "";
		}

		if (text != null && !text.isDisposed() && value != null && !value.equalsIgnoreCase(getValue())) {
			text.setText(value);
		}
	}

	/**
	 * Sets the value of this entry with the possibility to turn the
	 * notification off.
	 * 
	 * @param value
	 * @param blockNotification
	 */
	public void setValue(String value, boolean blockNotification) {
		ignoreModify = blockNotification;
		setValue(value);
		ignoreModify = false;
	}

	public void setVisible(boolean visible) {
		if (label != null)
			label.setVisible(visible);
		if (text != null)
			text.setVisible(visible);
		if (browse != null)
			browse.setVisible(visible);
	}

	/**
	 * If GridData was used, set the width hint.  If TableWrapData was used
	 * set the max width.  If no layout data was specified, this method does
	 * nothing.
	 * @param width
	 */
	public void setTextWidthHint(int width) {
		Object data = getText().getLayoutData();
		if (data == null) {
			return;
		} else if (data instanceof GridData) {
			((GridData) data).widthHint = width;
		} else if (data instanceof TableWrapData) {
			((TableWrapData) data).maxWidth = width;
		}
	}
}
