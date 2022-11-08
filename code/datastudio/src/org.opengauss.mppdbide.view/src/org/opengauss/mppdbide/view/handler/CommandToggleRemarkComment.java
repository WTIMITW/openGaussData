/*
 * Copyright (c) 2022 Huawei Technologies Co.,Ltd.
 *
 * openGauss is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *
 *           http://license.coscl.org.cn/MulanPSL2
 *
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.opengauss.mppdbide.view.handler;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.opengauss.mppdbide.view.ui.PLSourceEditor;
import org.opengauss.mppdbide.view.ui.terminal.SQLTerminal;

/**
 * Title: class
 * <p>
 * Description: The Class CommandToggleLineComment.
 *
 * @since 3.0.0
 */
public class CommandToggleRemarkComment {

    /**
     * Execute.
     *
     * @param activePart the active part
     */
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_PART) @Optional MPart activePart) {
        if (null == activePart) {
            return;
        }

        Object partObject = activePart.getObject();
        if (partObject instanceof PLSourceEditor) {
            ((PLSourceEditor) partObject).getSourceEditorCore().remarkLineComment();
        } else if (partObject instanceof SQLTerminal) {
            ((SQLTerminal) partObject).getTerminalCore().remarkLineComment();
        }
    }

    /**
     * Can execute.
     *
     * @param activePart the active part
     * @return true, if successful
     */
    @CanExecute
    public boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) @Optional MPart activePart) {
        if (null == activePart) {
            return false;
        }

        Object partObject = activePart.getObject();
        if (partObject instanceof SQLTerminal) {
            SQLTerminal terminal = (SQLTerminal) partObject;
            return terminal.getTerminalCore().getSourceViewer().isEditable();
        } else if (partObject instanceof PLSourceEditor) {
            PLSourceEditor sourceEditor = (PLSourceEditor) partObject;
            return sourceEditor.getSourceEditorCore() != null
                    && sourceEditor.getSourceEditorCore().getSourceViewer().isEditable();
        }
        return false;
    }
}
