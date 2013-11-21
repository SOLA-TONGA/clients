/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.clients.swing.common.controls;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.UIManager;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.CompoundHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 * Customized SwingX JXTreeTable intended to look much like the SOLA
 * JTableWithDefaultStyles. There is currently an issue with row selection. A
 * bug seems to prevent the selection color style from being applied to the
 * whole row. Further investigation required.
 *
 * @author solaDev
 */
public class JTreeTableWithDefaultStyles extends JXTreeTable {

    private Color scrollPaneBackgroundColor;
    private Color defaultBackground;
    private Color oddRowColor;
    private Color selectBackground;
    private Color selectForeground;

    /**
     * Class constructor. Initializes default values
     */
    public JTreeTableWithDefaultStyles() {
        // Remove the default icons displayed when opening and closing parent 
        // nodes as well as the default icon for leaf nodes. 
        this.setOpenIcon(null);
        this.setClosedIcon(null);
        this.setLeafIcon(null);

        Object newFirstRow = "Table.alternateRowColor";
        Color newFRColor = UIManager.getColor(newFirstRow);
        defaultBackground = newFRColor;

        Object newSecondRow = "PasswordField.background";
        Color newSRColor = UIManager.getColor(newSecondRow);
        oddRowColor = newSRColor;

        Object newSelectedRow = "List.background";
        selectBackground = UIManager.getColor(newSelectedRow);
        this.setSelectionBackground(selectBackground);

        Object newSelForecolor = "List.foreground";
        selectForeground = UIManager.getColor(newSelForecolor);
        this.setSelectionForeground(selectForeground);
        this.setGridColor(selectForeground);


        scrollPaneBackgroundColor = Color.WHITE;
        super.setBackground(scrollPaneBackgroundColor);
        this.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        Object tableFont = "Table.font";
        Font newTableFont = UIManager.getFont(tableFont);
        this.setFont(newTableFont);
        this.setShowGrid(true);
        this.setRowSelectionAllowed(true);
        this.setColumnSelectionAllowed(false);
        this.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Create the alternating strip pattern using the HighligtherFactory
        CompoundHighlighter compHigh = (CompoundHighlighter) HighlighterFactory.createAlternateStriping(defaultBackground, oddRowColor);
        for (Highlighter high : compHigh.getHighlighters()) {
            // Dispite explicitly setting the colors for selection, the JXTreeTable
            // ignores them outside of the extend of the tree node text
            ((ColorHighlighter) high).setSelectedBackground(selectBackground);
            ((ColorHighlighter) high).setSelectedForeground(selectForeground);
        }
        this.addHighlighter(compHigh);
    }

    /**
     * Retrieves all selected rows from the TreeTable. If no rows are selected,
     * the size of the returned list will be 0.
     */
    public List<TreeTableRowData> getSelectedDataRows() {
        List<TreeTableRowData> result = new ArrayList<TreeTableRowData>();
        TreePath[] selectedNodes = this.getTreeSelectionModel().getSelectionPaths();
        for (TreePath tp : selectedNodes) {
            if (tp.getLastPathComponent() instanceof DefaultMutableTreeNode) {
                Object row = ((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject();
                result.add((TreeTableRowData) row);
            }
        }
        return result;
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        defaultBackground = color;
    }

    public Color getOddRowColor() {
        return oddRowColor;
    }

    public void setOddRowColor(Color oddRowColor) {
        this.oddRowColor = oddRowColor;
    }

    public void enableSorting() {
        this.setRowSorter(new TableRowSorter<TableModel>(getModel()));
    }

    public void disableSorting() {
        this.setRowSorter(null);
    }
}
