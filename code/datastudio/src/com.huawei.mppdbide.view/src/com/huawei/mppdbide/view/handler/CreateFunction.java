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

package com.huawei.mppdbide.view.handler;

import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.huawei.mppdbide.bl.serverdatacache.Database;
import com.huawei.mppdbide.bl.serverdatacache.DebugObjects;
import com.huawei.mppdbide.bl.serverdatacache.IDebugObject;
import com.huawei.mppdbide.bl.serverdatacache.INamespace;
import com.huawei.mppdbide.bl.serverdatacache.OBJECTTYPE;
import com.huawei.mppdbide.bl.serverdatacache.SourceCode;
import com.huawei.mppdbide.bl.serverdatacache.groups.DebugObjectGroup;
import com.huawei.mppdbide.bl.serverdatacache.groups.OLAPObjectGroup;
import com.huawei.mppdbide.bl.serverdatacache.groups.ObjectGroup;
import com.huawei.mppdbide.utils.IMessagesConstants;
import com.huawei.mppdbide.utils.loader.MessageConfigLoader;
import com.huawei.mppdbide.utils.logger.MPPDBIDELoggerUtility;
import com.huawei.mppdbide.view.createfunction.CreateFunctionMainDlg;
import com.huawei.mppdbide.view.createfunction.CreateFunctionRelyInfo;
import com.huawei.mppdbide.view.createfunction.DsCreateFunctionRelyInfo;
import com.huawei.mppdbide.view.search.SearchWindow;
import com.huawei.mppdbide.view.ui.PLSourceEditor;
import com.huawei.mppdbide.view.utils.UIElement;

/**
 * 
 * Title: class
 * 
 * Description: The Class CreateFunction.
 *
 * @since 3.0.0
 */
public class CreateFunction extends CreateFunctionBase {
    @Inject
    private ECommandService commandService;

    @Inject
    private EHandlerService handlerService;
    /**
     * Execute.
     */
    @Execute
    public void execute() {
        MPPDBIDELoggerUtility.info(MessageConfigLoader.getProperty(IMessagesConstants.GUI_CREATE_FUNCTION));
        baseExecute(CreateFunctionRelyInfo.LANGUAGE_PLP);
    }

    /**
     * Can execute.
     *
     * @return true, if successful
     */
    @CanExecute
    public boolean canExecute() {
        return baseCanExecute();
    }

}
