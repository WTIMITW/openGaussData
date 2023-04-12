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

package org.opengauss.mppdbide.view.ui.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import org.opengauss.mppdbide.bl.serverdatacache.ColumnMetaData;
import org.opengauss.mppdbide.bl.serverdatacache.Database;
import org.opengauss.mppdbide.bl.serverdatacache.Namespace;
import org.opengauss.mppdbide.bl.serverdatacache.TableMetaData;
import org.opengauss.mppdbide.bl.serverdatacache.TableOrientation;
import org.opengauss.mppdbide.bl.serverdatacache.TableValidatorRules;
import org.opengauss.mppdbide.bl.serverdatacache.TypeMetaData;
import org.opengauss.mppdbide.utils.IMessagesConstants;
import org.opengauss.mppdbide.utils.MPPDBIDEConstants;
import org.opengauss.mppdbide.utils.SystemObjectName;
import org.opengauss.mppdbide.utils.loader.MessageConfigLoader;
import org.opengauss.mppdbide.view.utils.FontAndColorUtility;
import org.opengauss.mppdbide.view.utils.UIMandatoryAttribute;
import org.opengauss.mppdbide.view.utils.UIVerifier;
import org.opengauss.mppdbide.view.utils.consts.TOOLTIPS;
import org.opengauss.mppdbide.view.utils.icon.IconUtility;
import org.opengauss.mppdbide.view.utils.icon.IiconPath;

/**
 * 
 * Title: class
 * 
 * Description: The Class ColumnUI.
 *
 * @since 3.0.0
 */
public class ColumnUI {
    private Text textColumnName;
    private StyledText txtUserValue;

    private Button chkColumnNameCase;

    /* column data type */
    private Combo cmbClmDataSchema;

    private Combo cmbClmDataType;

    private Spinner spinnerPreSize;
    private Spinner spinnerScale;
    private volatile Spinner spinnerColumnArray;

    private volatile StyledText textArrayDim;

    private StyledText textDefaultExpr;
    private Label lblDescriptionOfDatatype;

    /* not null */
    private Button chkNotNull;

    /* unique constraint */
    private Button chkUnique;

    private StyledText textCheckExpr;

    private Group grpColumns;
    private Group grpValues;

    private Database db;
    private TableMetaData selectedTable;
    private int editIndex = -1;
    private TableValidatorRules validator;
    private Table tableCloumnList;
    private Table tblValues;

    private boolean duplicateName;

    private Label lblArrayDiementions;
    private Label lblScale;
    private Label lblDatatypeSchema;
    private Label lblCheck;

    private Button chkFunction;
    private Button editValues;

    private Text textColumnDescription;

    private ArrayList<String> setOrEnumValues;
    private HashSet<String> setOrEnumList;

    /**
     * Sets the UI labels color gray.
     */
    public void setUILabelsColorGray() {
        lblArrayDiementions.setForeground(FontAndColorUtility.getColor(SWT.COLOR_DARK_GRAY));
        lblDatatypeSchema.setForeground(FontAndColorUtility.getColor(SWT.COLOR_DARK_GRAY));
        lblScale.setForeground(FontAndColorUtility.getColor(SWT.COLOR_DARK_GRAY));
        lblCheck.setForeground(FontAndColorUtility.getColor(SWT.COLOR_DARK_GRAY));
        // can add other labels if want
    }

    /**
     * Sets the UI labels color black.
     */
    public void setUILabelsColorBlack() {
        lblArrayDiementions.setForeground(FontAndColorUtility.getColor(SWT.COLOR_BLACK));
        lblDatatypeSchema.setForeground(FontAndColorUtility.getColor(SWT.COLOR_BLACK));
        lblScale.setForeground(FontAndColorUtility.getColor(SWT.COLOR_BLACK));
        lblCheck.setForeground(FontAndColorUtility.getColor(SWT.COLOR_BLACK));
        // can add other labels if want
    }

    /**
     * Checks if is duplicate name.
     *
     * @return true, if is duplicate name
     */
    public boolean isDuplicateName() {
        return duplicateName;
    }

    /**
     * Sets the duplicate name default value.
     */
    public void setDuplicateNameDefaultValue() {
        duplicateName = false;
    }

    /**
     * Sets the cmb clm data schema.
     *
     * @param cmbClmDataSchema the new cmb clm data schema
     */
    public void setCmbClmDataSchema(Combo cmbClmDataSchema) {
        this.cmbClmDataSchema = cmbClmDataSchema;

    }

    /**
     * Gets the cmb clm data type.
     *
     * @return the cmb clm data type
     */
    public Combo getCmbClmDataType() {
        return cmbClmDataType;
    }

    /**
     * Gets the cmb clm data schema.
     *
     * @return the cmb clm data schema
     */
    public Combo getCmbClmDataSchema() {
        return cmbClmDataSchema;
    }

    /**
     * Gets the chk unique.
     *
     * @return the chk unique
     */
    public Button getChkUnique() {
        return chkUnique;
    }

    /**
     * Gets the text array dim.
     *
     * @return the text array dim
     */
    public StyledText getTextArrayDim() {
        return textArrayDim;
    }

    /**
     * Gets the spinner column array.
     *
     * @return the spinner column array
     */
    public Spinner getSpinnerColumnArray() {
        return spinnerColumnArray;
    }

    /**
     * Gets the text check expr.
     *
     * @return the text check expr
     */
    public StyledText getTextCheckExpr() {
        return textCheckExpr;
    }

    /**
     * Instantiates a new column UI.
     *
     * @param db1 the db 1
     * @param selectedTable the selected table
     */
    public ColumnUI(Database db1, TableMetaData selectedTable) {
        this.db = db1;
        this.selectedTable = selectedTable;
        this.validator = new TableValidatorRules(selectedTable);
        this.duplicateName = false;
    }

    /**
     * Gets the column name.
     *
     * @return the column name
     */
    public Text getColumnName() {
        return textColumnName;
    }

    /**
     * Gets the column name txt.
     *
     * @return the column name txt
     */
    public String getColumnNameTxt() {
        return textColumnName.getText();
    }

    /**
     * Creates the column info gui.
     *
     * @param compositeColumns the composite columns
     * @param isCreateTable the is create table
     */
    public void createColumnInfoGui(Composite compositeColumns, boolean isCreateTable) {
        /**
         * STEP: 2 COLUMN CREATION
         */
        Composite columnDetails = new Composite(compositeColumns, SWT.FILL);
        columnDetails.setLayout(new GridLayout(4, false));
        GridData columnDetailsGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        columnDetails.setLayoutData(columnDetailsGD);

        addColumnNameGui(columnDetails);
        addArrayDimGui(columnDetails);

        Label seperator = new Label(columnDetails, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData seperatorGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        seperatorGD.horizontalSpan = 4;
        seperatorGD.heightHint = 5;
        seperator.setLayoutData(seperatorGD);

        addDataTypeSchemaGui(columnDetails);

        Composite typeDescriptionComposite = new Composite(compositeColumns, SWT.FILL);
        typeDescriptionComposite.setLayout(new GridLayout(2, false));
        GridData typeDescriptionCompositeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        typeDescriptionCompositeGD.heightHint = 50;
        typeDescriptionComposite.setLayoutData(typeDescriptionCompositeGD);
        addDatatypeDescpGui(typeDescriptionComposite);

        addDisplayEditValuesButton(compositeColumns);
        addDatatypeGui(columnDetails);
        addPrecisionSizeGUI(columnDetails);
        addScaleGui(columnDetails);

        createValues(compositeColumns);
        addColumnConstraintGui(compositeColumns);

        addColumnDescription(compositeColumns);

        if (isCreateTable) {
            grpColumns = new Group(compositeColumns, SWT.NONE);
            GridData grpColumnsGD = new GridData(SWT.FILL, SWT.FILL, true, true);
            grpColumns.setLayoutData(grpColumnsGD);
            grpColumns.setText(MessageConfigLoader.getProperty(IMessagesConstants.CREATE_TABLE_COLUMNS));
        }
        enableDisableSizeLen();
        enableDisableColumnComponents();
    }

    private void addColumnDescription(Composite compositeColumns) {
        Group grpDescriptions = new Group(compositeColumns, SWT.NONE);
        grpDescriptions.setLayout(new GridLayout(1, false));
        grpDescriptions.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        grpDescriptions.setText(MessageConfigLoader.getProperty(IMessagesConstants.ENTER_COL_DESCRIPTIONS_TEXT));

        textColumnDescription = new Text(grpDescriptions, SWT.BORDER | SWT.MULTI);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.heightHint = 25;
        textColumnDescription.setLayoutData(gridData);
        textColumnDescription.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_TXT_CREATE_TBL_COLDESC_001");
        textColumnDescription.addVerifyListener(new TextLengthVerifyListner());
    }

    private void addScaleGui(Composite compositeColumns) {
        Composite scaleComposite = new Composite(compositeColumns, SWT.NONE);
        scaleComposite.setLayout(new GridLayout(1, false));
        GridData scaleCompositeGD = new GridData(SWT.NONE, SWT.FILL, false, true);
        scaleCompositeGD.widthHint = 120;
        scaleComposite.setLayoutData(scaleCompositeGD);

        lblScale = new Label(scaleComposite, SWT.NONE);
        lblScale.setText(MessageConfigLoader.getProperty(IMessagesConstants.SCALE_MSG));
        lblScale.pack();

        spinnerScale = new Spinner(scaleComposite, SWT.BORDER);
        GridData spinnerScaleGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        spinnerScale.setLayoutData(spinnerScaleGD);
        spinnerScale.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_SPINNER_COLUMNUI_SCALE_001");
    }

    private void addPrecisionSizeGUI(Composite compositeColumns) {
        Composite precisionSizeComposite = new Composite(compositeColumns, SWT.NONE);
        precisionSizeComposite.setLayout(new GridLayout(1, false));
        GridData precisionSizeCompositeGD = new GridData(SWT.NONE, SWT.FILL, false, true);
        precisionSizeCompositeGD.widthHint = 160;
        precisionSizeComposite.setLayoutData(precisionSizeCompositeGD);

        Label lblPre = new Label(precisionSizeComposite, SWT.NONE);
        lblPre.setText(MessageConfigLoader.getProperty(IMessagesConstants.SIZE));
        lblPre.pack();

        spinnerPreSize = new Spinner(precisionSizeComposite, SWT.BORDER);
        GridData spinnerPreSizeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        spinnerPreSize.setLayoutData(spinnerPreSizeGD);
        spinnerPreSize.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_SPINNER_COLUMNUI_PRECI_SIZE_001");
        spinnerPreSize.setMaximum(Integer.MAX_VALUE);
    }

    /**
     * Adds the column constraint gui.
     *
     * @param compositeColumns the composite columns
     */
    private void addColumnConstraintGui(Composite compositeColumns) {
        Group grpConstrains = new Group(compositeColumns, SWT.NONE);
        grpConstrains.setLayout(new GridLayout(3, true));
        GridData grpConstrainsGD = new GridData(SWT.FILL, SWT.NONE, true, true);
        grpConstrains.setLayoutData(grpConstrainsGD);
        grpConstrains.setText(MessageConfigLoader.getProperty(IMessagesConstants.COL_CONS));

        addClmConstraintNullCheckui(grpConstrains);
        addClmDefaultExpression(grpConstrains);

        chkFunction = new Button(grpConstrains, SWT.CHECK);
        GridData chkFunctionGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        chkFunction.setLayoutData(chkFunctionGD);
        chkFunction.setText(MessageConfigLoader.getProperty(IMessagesConstants.COLUMN_DEFAULT_VALUE_EXPRESSION));

        addClmConstraintUniqueUi(grpConstrains);
        addCheckConstranitExpressionUi(grpConstrains);
    }

    /**
     * Adds the clm default expression.
     *
     * @param grpConstrains the grp constrains
     */
    private void addClmDefaultExpression(Composite comp) {
        Composite defaultExpressionComposite = new Composite(comp, SWT.NONE);
        defaultExpressionComposite.setLayout(new GridLayout(2, false));
        GridData defaultExpressionCompositeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        defaultExpressionCompositeGD.heightHint = 35;
        defaultExpressionComposite.setLayoutData(defaultExpressionCompositeGD);

        Label lblDefault = new Label(defaultExpressionComposite, SWT.NONE);
        GridData lblDefaultGD = new GridData(SWT.LEFT, SWT.CENTER, false, true);
        lblDefault.setLayoutData(lblDefaultGD);
        lblDefault.setText(MessageConfigLoader.getProperty(IMessagesConstants.DFLT_CLM_UI));

        textDefaultExpr = new StyledText(defaultExpressionComposite, SWT.BORDER);
        GridData textDefaultExprGD = new GridData(SWT.RIGHT, SWT.FILL, true, true);
        textDefaultExprGD.widthHint = 130;
        textDefaultExpr.setLayoutData(textDefaultExprGD);
        textDefaultExpr.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_TXT_COLUMNUI_DEFAULT_EXPR_001");

    }

    /**
     * Adds the check constranit expression ui.
     *
     * @param grpConstrains the grp constrains
     */
    private void addCheckConstranitExpressionUi(Composite comp) {
        Composite checkConstraintComposite = new Composite(comp, SWT.NONE);
        checkConstraintComposite.setLayout(new GridLayout(2, false));
        GridData checkConstraintCompositeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        checkConstraintCompositeGD.heightHint = 35;
        checkConstraintComposite.setLayoutData(checkConstraintCompositeGD);

        lblCheck = new Label(checkConstraintComposite, SWT.NONE);
        GridData lblCheckGD = new GridData(SWT.LEFT, SWT.CENTER, false, true);
        lblCheck.setLayoutData(lblCheckGD);
        lblCheck.setText(MessageConfigLoader.getProperty(IMessagesConstants.CHECK_COLUMN_UI));

        textCheckExpr = new StyledText(checkConstraintComposite, SWT.BORDER);
        GridData textCheckExprGD = new GridData(SWT.RIGHT, SWT.FILL, true, true);
        textCheckExprGD.widthHint = 130;
        textCheckExpr.setLayoutData(textCheckExprGD);
        textCheckExpr.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_TXT_COLUMNUI_CHECK_EXPR_001");
    }

    /**
     * Adds the clm constraint unique ui.
     *
     * @param grpConstrains the grp constrains
     */
    private void addClmConstraintUniqueUi(Group grpConstrains) {
        chkUnique = new Button(grpConstrains, SWT.CHECK);
        GridData chkUniqueGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        chkUnique.setLayoutData(chkUniqueGD);
        chkUnique.setText(MessageConfigLoader.getProperty(IMessagesConstants.UNIQUE_MSG));
    }

    /**
     * Adds the clm constraint null checkui.
     *
     * @param grpConstrains the grp constrains
     */
    private void addClmConstraintNullCheckui(Group grpConstrains) {
        chkNotNull = new Button(grpConstrains, SWT.CHECK);
        GridData chkNotNullGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        chkNotNull.setLayoutData(chkNotNullGD);
        chkNotNull.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_CHK_BTN_COLUMNUI_NOTNULL_001");
        chkNotNull.setText(MessageConfigLoader.getProperty(IMessagesConstants.NOT_NULL));
    }

    /**
     * Adds the datatype gui.
     *
     * @param compositeColumns the composite columns
     */
    private void addDatatypeGui(Composite compositeColumns) {
        Composite dataTypeComposite = new Composite(compositeColumns, SWT.FILL);
        dataTypeComposite.setLayout(new GridLayout(1, false));
        GridData dataTypeCompositeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        dataTypeComposite.setLayoutData(dataTypeCompositeGD);

        Label lblDataType = new Label(dataTypeComposite, SWT.NONE);
        lblDataType.setText(MessageConfigLoader.getProperty(IMessagesConstants.DATA_TYPE));
        lblDataType.pack();

        cmbClmDataType = new Combo(dataTypeComposite, SWT.READ_ONLY);
        GridData cmbClmDataTypeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        cmbClmDataType.setLayoutData(cmbClmDataTypeGD);
        cmbClmDataType.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_COMBO_COLUMNUI_DATATYPE_001");
        cmbClmDataType.addModifyListener(new ClmDatatypeModifyListener());
        UIUtils.displayDatatypeList(validator.getDataTypeList(db, true), null, cmbClmDataType);
    }

    /**
     * Adds the datatype descp gui.
     *
     * @param compositeColumns the composite columns
     */
    private void addDatatypeDescpGui(Composite comp) {
        Label lblTypeDescription = new Label(comp, SWT.NONE);
        lblTypeDescription.setLayoutData(new GridData(SWT.NONE, SWT.TOP, false, true));
        lblTypeDescription.setText(MessageConfigLoader.getProperty(IMessagesConstants.TYPE_DES));
        lblTypeDescription.pack();

        lblDescriptionOfDatatype = new Label(comp, SWT.NONE);
        GridData lblDescriptionOfDatatypeGD = new GridData(SWT.FILL, SWT.TOP, true, true);
        lblDescriptionOfDatatype.setLayoutData(lblDescriptionOfDatatypeGD);
        lblDescriptionOfDatatype.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_LBL_COLUMNUI_DATATYPE_DESC_001");
        lblDescriptionOfDatatype.setBackground(FontAndColorUtility.getColor(SWT.COLOR_WHITE));
        lblDescriptionOfDatatype.setText("Description Of Datatype");
    }

    /**
     * Adds the data type schema gui.
     *
     * @param compositeColumns the composite columns
     */
    private void addDataTypeSchemaGui(Composite compositeColumns) {
        Composite dataTypeSchemaComposite = new Composite(compositeColumns, SWT.NONE);
        dataTypeSchemaComposite.setLayout(new GridLayout(1, false));
        GridData dataTypeSchemaCompositeGD = new GridData(SWT.NONE, SWT.FILL, false, true);
        dataTypeSchemaCompositeGD.widthHint = 130;
        dataTypeSchemaComposite.setLayoutData(dataTypeSchemaCompositeGD);

        lblDatatypeSchema = new Label(dataTypeSchemaComposite, SWT.NONE);
        lblDatatypeSchema.setText(MessageConfigLoader.getProperty(IMessagesConstants.DTYPE_SCHEMA));
        lblDatatypeSchema.pack();

        cmbClmDataSchema = new Combo(dataTypeSchemaComposite, SWT.READ_ONLY);
        GridData cmbClmDataSchemaGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        cmbClmDataSchema.setLayoutData(cmbClmDataSchemaGD);
        cmbClmDataSchema.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_COMBO_COLUMNUI_DATATYPE_SCHEMA_001");
        cmbClmDataSchema.addSelectionListener(new ClmDataSchemaSelectionListener());
        // Need to get the list of schemas available from GAUSS DB
        UIUtils.displayDatatypeSchemaList(db, "DEFAULT", cmbClmDataSchema, true);
    }

    /**
     * Adds the array dim gui.
     *
     * @param compositeColumns the composite columns
     */
    private void addArrayDimGui(Composite compositeColumns) {
        Composite arrayComposite = new Composite(compositeColumns, SWT.NONE);
        arrayComposite.setLayout(new GridLayout(2, false));
        GridData arrayCompositeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        arrayCompositeGD.horizontalSpan = 2;
        arrayComposite.setLayoutData(arrayCompositeGD);

        lblArrayDiementions = new Label(arrayComposite, SWT.NONE);
        lblArrayDiementions.setText(MessageConfigLoader.getProperty(IMessagesConstants.ARRAY_DIMENSIONS));
        lblArrayDiementions.pack();
        GridData lblArrayDiementionsGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        lblArrayDiementionsGD.horizontalSpan = 2;
        lblArrayDiementions.setLayoutData(lblArrayDiementionsGD);

        spinnerColumnArray = new Spinner(arrayComposite, SWT.BORDER);
        GridData spinnerColumnArrayGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        spinnerColumnArray.setLayoutData(spinnerColumnArrayGD);
        spinnerColumnArray.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_SPINNER_COLUMNUI_COL_ARRAY_001");
        setListner();

        textArrayDim = new StyledText(arrayComposite, SWT.BORDER | SWT.READ_ONLY);
        GridData textArrayDimGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        textArrayDim.setLayoutData(textArrayDimGD);
        textArrayDim.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_TXT_COLUMNUI_DUMMY_001");
    }

    /**
     * Adds the column name gui.
     *
     * @param compositeColumns the composite columns
     */
    private void addColumnNameGui(Composite compositeColumns) {
        Composite columnNameComposite = new Composite(compositeColumns, SWT.NONE);
        columnNameComposite.setLayout(new GridLayout(2, false));
        GridData columnNameCompositeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        columnNameCompositeGD.horizontalSpan = 2;
        columnNameComposite.setLayoutData(columnNameCompositeGD);

        Label lblColumnName = new Label(columnNameComposite, SWT.NONE);
        lblColumnName.setFont(FontAndColorUtility.getFont("Arial", 9, SWT.BOLD, compositeColumns));
        lblColumnName.setText(MessageConfigLoader.getProperty(IMessagesConstants.COL_NAME));
        GridData lblColumnNameGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        lblColumnNameGD.horizontalSpan = 2;
        lblColumnName.setLayoutData(lblColumnNameGD);
        lblColumnName.pack();

        textColumnName = new Text(columnNameComposite, SWT.BORDER);
        GridData textColumnNameGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        textColumnNameGD.heightHint = 30;
        textColumnNameGD.widthHint = 200;
        textColumnName.setLayoutData(textColumnNameGD);
        textColumnName.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_TXT_COLUMNUI_COL_NAME_001");
        UIVerifier.verifyTextSize(textColumnName, 63);
        Image image = IconUtility.getIconImage(IiconPath.MANDATORY_FIELD, this.getClass());
        UIMandatoryAttribute.mandatoryField(textColumnName, image, TOOLTIPS.COLUMNNAME_TOOLTIPS);

        chkColumnNameCase = new Button(columnNameComposite, SWT.CHECK);
        GridData chkColumnNameCaseGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        chkColumnNameCase.setLayoutData(chkColumnNameCaseGD);
        chkColumnNameCase.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_CHK_BTN_COLUMNUI_QUOTED_COL_NAME_001");
        chkColumnNameCase.setText(MessageConfigLoader.getProperty(IMessagesConstants.CASE_MSG));
    }

    /**
     * The listener interface for receiving clmDatatypeModify events. The class
     * that is interested in processing a clmDatatypeModify event implements
     * this interface, and the object created with that class is registered with
     * a component using the component's
     * <code>addClmDatatypeModifyListener<code> method. When the
     * clmDatatypeModify event occurs, that object's appropriate method is
     * invoked.
     *
     * ClmDatatypeModifyEvent
     */
    private class ClmDatatypeModifyListener implements ModifyListener {

        /**
         * Modify text.
         *
         * @param e the e
         */
        public void modifyText(ModifyEvent event) {
            enableDisableSizeLen();
            enableDisableSetValues();
        }

    }

    /**
     * The listener interface for receiving clmDataSchemaSelection events. The
     * class that is interested in processing a clmDataSchemaSelection event
     * implements this interface, and the object created with that class is
     * registered with a component using the component's
     * <code>addClmDataSchemaSelectionListener<code> method. When the
     * clmDataSchemaSelection event occurs, that object's appropriate method is
     * invoked.
     *
     * ClmDataSchemaSelectionEvent
     */
    private class ClmDataSchemaSelectionListener implements SelectionListener {
        @Override
        public void widgetSelected(SelectionEvent event) {

            populateDataTypes();
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent event) {

        }

    }

    /**
     * Sets the listner.
     */
    public void setListner() {
        spinnerColumnArray.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent event) {
                try {
                    int dim = Integer.parseInt(spinnerColumnArray.getText());
                    if (dim > spinnerColumnArray.getMaximum()) {
                        return;
                    }
                    String txtArrayVal = getArrayDim(dim, textArrayDim.getText());
                    textArrayDim.setText(txtArrayVal);
                } catch (NumberFormatException formatExcept) {
                    textArrayDim.setText("");
                }
            }
        });
    }

    /**
     * If the array dimension code incase array dim spinner has a value.
     *
     * @param dimCount the dim count
     * @param arrayDesc the array desc
     * @return the array dim
     */
    private String getArrayDim(int dim, String arrayDesc) {
        StringBuilder dimension = new StringBuilder(MPPDBIDEConstants.STRING_BUILDER_CAPACITY);
        int targetCounter = 0;

        if (arrayDesc.matches("[\\[\\d*\\]]*")) {
            String[] parts = null;
            if (!arrayDesc.isEmpty()) {
                parts = arrayDesc.split("]");
            }

            if (null != parts) {
                if (parts.length >= dim) {
                    targetCounter = dim;
                } else {
                    targetCounter = parts.length;
                }

                for (int index = 0; index < targetCounter; index++) {
                    dimension.append(parts[index]).append("]");
                }
            }
        }

        for (int i = targetCounter; i < dim; i++) {
            dimension.append("[]");
        }

        return dimension.toString();
    }

    /**
     * Enable disable size len.
     */
    public void enableDisableSizeLen() {
        Namespace selectedNS = UIUtils.getNamespaceForDatatype(db, cmbClmDataSchema);
        TypeMetaData type = null;

        type = UIUtils.getDtypeFromCombo(selectedNS, db, cmbClmDataType);

        if (null == type || null == spinnerPreSize || null == spinnerScale) {
            lblDescriptionOfDatatype.setText("");
            return;
        }

        if (selectedNS == null || (!SystemObjectName.INFORMATION_SCHEMA.equals(selectedNS.getName()) &&
                !SystemObjectName.PG_CATALOG.equals(selectedNS.getName()))) {

            spinnerPreSize.setEnabled(UIUtils.enableDisablePrecisionFieldForDatatype(type.getName(), db.getDolphinTypes()));
            spinnerScale.setEnabled(UIUtils.enableDisableScaleFieldForDatatype(type.getName(), db.getDolphinTypes()));
        } else {
            spinnerPreSize.setEnabled(false);
            spinnerScale.setEnabled(false);
        }
        spinnerPreSize.setSelection(0);
        spinnerScale.setSelection(0);

        lblDescriptionOfDatatype.setText(type.getDescription() == null ? "" : type.getDescription());
        spinnerColumnArray.setSelection(0);
        textArrayDim.setText("");
        if (type.getTyparray() > 0 && validator.enableDisable()) {
            spinnerColumnArray.setEnabled(true);
            textArrayDim.setEnabled(true);
        } else {
            spinnerColumnArray.setEnabled(false);
            textArrayDim.setEnabled(false);
        }
    }

    /**
     * Enable disable set values tab.
     */
    public void enableDisableSetValues() {
        Namespace selectedNS = UIUtils.getNamespaceForDatatype(db, cmbClmDataSchema);
        TypeMetaData type = UIUtils.getDtypeFromCombo(selectedNS, db, cmbClmDataType);
        if (null == type || null == grpValues) {
            return;
        }
        if (SystemObjectName.SET.equals(type.getName()) || SystemObjectName.ENUM.equals(type.getName())) {
            editValues.setVisible(true);
        } else {
            grpValues.setVisible(false);
            grpValues.setSize(0,0);
            editValues.setVisible(false);
        }
    }

    /**
     * Gets the grp control.
     *
     * @return the grp control
     */
    public Group getGrpControl() {
        return this.grpColumns;
    }

    /**
     * Gets the DB column.
     *
     * @param tableColList the table col list
     * @param isUpdate the is update
     * @param orientation the orientation
     * @return the DB column
     */
    public ColumnMetaData getDBColumn(Table tableColList, boolean isUpdate, TableOrientation orientation) {
        String colname = null;
        TypeMetaData type = null;
        int numRows = 0;
        Namespace selectedNS = null;

        selectedNS = UIUtils.getNamespaceForDatatype(this.db, cmbClmDataSchema);

        type = UIUtils.getDtypeFromCombo(selectedNS, this.db, cmbClmDataType);

        colname = textColumnName.getText();

        // if quoted then changes the type.
        // Need to check for Double Quotes in String
        if (!chkColumnNameCase.getSelection()) {
            colname = colname.toLowerCase(Locale.ENGLISH);
        }

        if (!isUpdate) {
            if (tableColList != null) {
                numRows = tableColList.getItemCount();
                TableItem tableItem = null;
                while (numRows != 0) {
                    tableItem = tableColList.getItem(numRows - 1);
                    if (tableItem.getText(0).equals(colname) && chkColumnNameCase.getSelection()) {
                        duplicateName = true;
                        return null;
                    } else if (tableItem.getText(0).equalsIgnoreCase(colname) && !chkColumnNameCase.getSelection()) {
                        duplicateName = true;
                        return null;
                    }
                    numRows--;
                }
            }
        }

        ColumnMetaData newTempColumn = setNewClmData(colname, type);
        return newTempColumn;
    }

    private ColumnMetaData setNewClmData(String colname, TypeMetaData type) {
        ColumnMetaData newTempColumn = new ColumnMetaData(this.selectedTable, 0, colname, type);

        if (0 != cmbClmDataSchema.getSelectionIndex()) {
            newTempColumn.setDataTypeScheam(cmbClmDataSchema.getText());
        }

        newTempColumn.setPre(spinnerPreSize.getSelection(), spinnerScale.getSelection());

        newTempColumn.setNotNull(chkNotNull.getSelection());
        newTempColumn.setUnique(chkUnique.getSelection());
        newTempColumn.setColumnCase(chkColumnNameCase.getSelection());
        newTempColumn.setIsFunction(chkFunction.getSelection());

        newTempColumn.setArrayNDim(spinnerColumnArray.getSelection());
        if (!textCheckExpr.getText().trim().isEmpty()) {
            newTempColumn.setCheckConstraint(textCheckExpr.getText());
        }
        if (!textDefaultExpr.getText().trim().isEmpty()) {

            newTempColumn.setDefaultValue(textDefaultExpr.getText());

        }

        newTempColumn.setColDescription(textColumnDescription.getText());
        if (SystemObjectName.SET.equals(type.getName()) || SystemObjectName.ENUM.equals(type.getName())) {
            newTempColumn.setEnumOrSetValues(new ArrayList<String>(this.setOrEnumValues));
            newTempColumn.setEnumOrSetList(new HashSet<String>(this.setOrEnumList));
            setOrEnumValues = new ArrayList<String>();
            setOrEnumList = new HashSet<String>();
            repopulateValueCols();
            txtUserValue.setText("");
        }
        return newTempColumn;
    }

    /**
     * Sets the column details.
     *
     * @param columnMetaData the column meta data
     * @param editIndx the edit indx
     */
    public void setColumnDetails(final ColumnMetaData columnMetaData, int editIndx) {
        textColumnDescription.setText("");
        if (columnMetaData.getName().indexOf('"') == 0
                && (columnMetaData.getName().lastIndexOf('"') + 1) == columnMetaData.getName().length()) {
            textColumnName.setText(columnMetaData.getName().substring(1, columnMetaData.getName().length() - 1));
            chkColumnNameCase.setSelection(true);
        } else {
            textColumnName.setText(columnMetaData.getName());
            textColumnDescription.setText(columnMetaData.getColDescription());
        }
        Color color = new Color(null, 255, 229, 180);
        spinnerColumnArray.setBackground(color);
        textArrayDim.setBackground(color);
        spinnerColumnArray.setSelection(columnMetaData.getArrayNDim());

        setDefaultExprText(columnMetaData);
        /* not null */
        chkNotNull.setSelection(columnMetaData.isNotNull());

        /* unique constraint */
        chkUnique.setSelection(columnMetaData.isUnique());
        textCheckExpr.setText(
                columnMetaData.getCheckConstraintExpr() != null ? columnMetaData.getCheckConstraintExpr() : "");
        chkFunction.setSelection(columnMetaData.getIsFunction());

        int schemaSize = cmbClmDataSchema.getItemCount();
        int index = 0;
        cmbClmDataSchema.select(0);
        for (; index < schemaSize; index++) {
            if (cmbClmDataSchema.getItem(index).equalsIgnoreCase(columnMetaData.getColDataTypeSchema())) {
                cmbClmDataSchema.select(index);
                break;
            }
        }

        populateDataTypes();
        cmbClmDataType.setText(columnMetaData.getDataType().getName());

        spinnerPreSize.setSelection(columnMetaData.getLenOrPrecision());
        spinnerScale.setSelection(columnMetaData.getScale());
        chkColumnNameCase.setSelection(columnMetaData.getColumnCase());
        this.editIndex = editIndx;
        if (SystemObjectName.SET.equals(columnMetaData.getDataTypeName()) ||
                SystemObjectName.ENUM.equals(columnMetaData.getDataTypeName())) {
            this.setOrEnumValues = columnMetaData.getEnumOrSetValues() != null ? new ArrayList<String>(columnMetaData.getEnumOrSetValues()) :
                new ArrayList<String>();
            this.setOrEnumList = columnMetaData.getEnumOrSetList() != null ? new HashSet<String>(columnMetaData.getEnumOrSetList()) :
                new HashSet<String>();
            repopulateValueCols();
        }

        setSpinnerData(columnMetaData);
    }

    private void setSpinnerData(final ColumnMetaData columnMetaData) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                Color color = new Color(null, 255, 229, 180);
                spinnerColumnArray.setBackground(color);
                textArrayDim.setBackground(color);
                spinnerColumnArray.setSelection(columnMetaData.getArrayNDim());
            }
        });
    }

    private void setDefaultExprText(final ColumnMetaData columnMetaData) {
        if (null != columnMetaData.getDefaultValue() && columnMetaData.getDefaultValue().indexOf('\'') == 0
                && (columnMetaData.getDefaultValue().lastIndexOf('\'') + 1) == columnMetaData.getDefaultValue()
                        .length()) {
            textDefaultExpr.setText(
                    columnMetaData.getDefaultValue().substring(1, columnMetaData.getDefaultValue().length() - 1));
        } else {
            textDefaultExpr.setText(columnMetaData.getDefaultValue() != null ? columnMetaData.getDefaultValue() : "");
        }
    }

    /**
     * Clear column details.
     */
    public void clearColumnDetails() {
        Color systemColor = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
        textArrayDim.setBackground(systemColor);
        spinnerColumnArray.setBackground(systemColor);
        textColumnName.setText("");
        chkColumnNameCase.setSelection(false);

        spinnerPreSize.setSelection(0);
        spinnerScale.setSelection(0);
        spinnerColumnArray.setSelection(0);
        textArrayDim.setText("");
        textDefaultExpr.setText("");

        /* not null */
        chkNotNull.setSelection(false);

        /* Is function Uncheck */
        chkFunction.setSelection(false);

        /* unique constraint */
        chkUnique.setSelection(false);
        textCheckExpr.setText("");

        /* description */
        textColumnDescription.setText("");

        cmbClmDataSchema.select(0);
        this.setOrEnumList = new HashSet<>();
        this.setOrEnumValues = new ArrayList<>();
        tblValues.removeAll();
        populateDataTypes();
        this.editIndex = -1;
    }

    /**
     * Gets the edits the index.
     *
     * @return the edits the index
     */
    public int getEditIndex() {
        return editIndex;
    }

    /**
     * Populate data types.
     */
    private void populateDataTypes() {
        Namespace dtypeNamespace = UIUtils.getNamespaceForDatatype(db, cmbClmDataSchema);
        if (null != dtypeNamespace) {
            UIUtils.displayDatatypeList(dtypeNamespace.getTypes().getList(), null, cmbClmDataType);
        } else {
            if (this.selectedTable != null) {
                UIUtils.displayDatatypeList(validator.getDataTypeList(db, true), null, cmbClmDataType);
            }
        }
    }

    /**
     * Sets the focus on column name.
     */
    public void setFocusOnColumnName() {
        if (textColumnName != null) {
            textColumnName.forceFocus();
        }
    }

    /**
     * Checkspinner.
     */
    public void checkspinner() {
        if (this.spinnerColumnArray.isEnabled() || this.textCheckExpr.isEnabled()) {
            spinnerColumnArray.setEnabled(false);
            textArrayDim.setEnabled(false);
            textArrayDim.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            spinnerColumnArray.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        }

    }

    /**
     * Sets the column UI table.
     *
     * @param tableColList the new column UI table
     */
    public void setColumnUITable(Table tableColList) {
        this.tableCloumnList = tableColList;
    }

    /**
     * Gets the column UI table.
     *
     * @return the column UI table
     */
    public Table getColumnUITable() {
        return this.tableCloumnList;
    }

    /**
     * Enable disable column components. For Call through ADD handlers
     */
    private void enableDisableColumnComponents() {
        if (!(validator.enableDisable())) {
            cmbClmDataSchema.setEnabled(false);
            getSpinnerColumnArray().setEnabled(false);
            getTextArrayDim().setEnabled(false);
            getTextCheckExpr().setEnabled(false);
            getChkUnique().setEnabled(false);
            cmbClmDataType.select(0);
            getSpinnerColumnArray().setSelection(0);
            getChkUnique().setSelection(false);
        }
    }

    /**
     * 
     * Title: class
     * 
     * Description: The Class TextLengthVerifyListner.
     */
    private static final class TextLengthVerifyListner implements VerifyListener {
        @Override
        public void verifyText(VerifyEvent event) {
            String textStr = ((Text) event.widget).getText() + event.text;
            try {
                if (textStr.length() > 5000) {
                    event.doit = false;
                }
            } catch (NumberFormatException e) {
                event.doit = false;
            }
        }
    }

    /**
     * Creates the values list for enum or set.
     *
     * @param comp the comp
     */
    private void createValues(Composite comp) {
        grpValues = new Group(comp, SWT.FILL);
        grpValues.setLayout(new GridLayout(5, false));
        GridData groupGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        groupGD.heightHint = 10;
        groupGD.horizontalSpan = 4;
        grpValues.setLayoutData(groupGD);

        createUserDefComposite();
        createAddRemoveValueComposite();
        createValueTable(grpValues);
        createUpDownValueComposite();
        createFinishComposite();
        grpValues.setVisible(false);
        
    }

    private void createUserDefComposite() {
        Composite userValueComp = new Composite(this.grpValues, SWT.FILL);
        userValueComp.setLayout(new GridLayout(1, false));
        GridData avalColsOrUserExpCompGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        userValueComp.setLayoutData(avalColsOrUserExpCompGD);

        Composite userExprComposite = new Composite(userValueComp, SWT.FILL);
        
        userExprComposite.setLayout(new GridLayout(1, false));
        GridData userExprCompositeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        userExprCompositeGD.verticalAlignment = SWT.CENTER;
        userExprComposite.setLayoutData(userExprCompositeGD);
        userExprComposite.setSize(50, 30);

        Label lblUserDefinedValue = new Label(userExprComposite, SWT.FILL);
        lblUserDefinedValue.setText(MessageConfigLoader.getProperty(IMessagesConstants.SET_ENUM_UI_USER_VALUE));
        lblUserDefinedValue.pack();

        txtUserValue = new StyledText(userExprComposite, SWT.BORDER);
        txtUserValue.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_TXT_INDEXUI_USEREXPR_001");
        GridData txtUserExprGD = new GridData(SWT.FILL, SWT.NONE, true, true);
        txtUserExprGD.heightHint = 30;
        txtUserValue.setLayoutData(txtUserExprGD);

        setOrEnumList = new HashSet<String>();
        setOrEnumValues = new ArrayList<String>();
    }

    private void createAddRemoveValueComposite() {
        Composite addRemoveValueComposite = new Composite(this.grpValues, SWT.NONE);
        addRemoveValueComposite.setLayout(new GridLayout(1, false));
        GridData addRemoveIndexCompositeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        addRemoveIndexCompositeGD.verticalAlignment = SWT.CENTER;
        addRemoveValueComposite.setLayoutData(addRemoveIndexCompositeGD);

        createAddToValue(addRemoveValueComposite);
        createRemoveFromValues(addRemoveValueComposite);
    }

    /**
     * Creates the add to Add/Remove/MoveUp/MoveDown Buttons
     *
     * @param comp the comp
     */
    private void createAddToValue(Composite comp) {
        Button addToIndex = new Button(comp, SWT.ARROW | SWT.RIGHT);
        GridData addToIndexGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        addToIndexGD.heightHint = 20;
        addToIndex.setLayoutData(addToIndexGD);
        addToIndex.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_BTN_INDEXUI_ADD_TO_INDEX_001");
        addToIndex.setText(MessageConfigLoader.getProperty(IMessagesConstants.INDEX_UI_ADD_TO));
        addToIndex.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                String userExpr = txtUserValue.getText().trim();
                if (!"".equals(userExpr)) {
                    if (!setOrEnumList.contains(userExpr)) {
                        setOrEnumList.add(userExpr);
                        setOrEnumValues.add(userExpr);
                        repopulateValueCols();
                        txtUserValue.setText("");
                    }
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent event) {
                // Nothing to do
            }
        });
    }

    public void repopulateValueCols() {
        TableItem item = null;
        tblValues.removeAll();
        Iterator<String> valuesItr = this.setOrEnumValues.iterator();
        String value = null;
        while (valuesItr.hasNext()) {
            value = valuesItr.next();
            item = new TableItem(tblValues, SWT.NONE);
            item.setText(value);
        }
    }

    private void createRemoveFromValues(Composite comp) {
        Button removeFromValues = new Button(comp, SWT.ARROW | SWT.LEFT);
        GridData removeFromValuesGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        removeFromValuesGD.heightHint = 20;
        removeFromValues.setLayoutData(removeFromValuesGD);
        removeFromValues.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_BTN_INDEXUI_REMOVE_FROM_INDEX_001");
        removeFromValues.setText(MessageConfigLoader.getProperty(IMessagesConstants.INDEX_UI_REMOVE));
        removeFromValues.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                int selectedIdx = tblValues.getSelectionIndex();
                if (selectedIdx > -1) {
                    String expr = setOrEnumValues.get(selectedIdx);
                    setOrEnumValues.remove(selectedIdx);
                    tblValues.remove(selectedIdx);
                    setOrEnumList.remove(expr);
                    txtUserValue.setText(expr);
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent eevent) {
                // Nothing to do.
            }
        });
    }

    private void createValueTable(Composite comp) {
        tblValues = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION);
        tblValues.setLayout(new GridLayout(1, false));
        GridData tblIndexColsGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        tblIndexColsGD.horizontalIndent = 5;
        tblValues.setLayoutData(tblIndexColsGD);

        tblValues.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_TBL_INDEXUI_INDEX_COLS_001");
        tblValues.setLinesVisible(true);
        tblValues.setHeaderVisible(true);

        ControlDecoration decofk = new ControlDecoration(tblValues, SWT.TOP | SWT.LEFT);

        // use an existing image
        Image image = IconUtility.getIconImage(IiconPath.MANDATORY_FIELD, this.getClass());

        // set description and image
        decofk.setDescriptionText(MessageConfigLoader.getProperty(IMessagesConstants.INDEX_UI_MSG));
        decofk.setImage(image);

        TableColumn tblclmnValues = new TableColumn(tblValues, SWT.NONE);
        tblclmnValues.setWidth(185);
        tblclmnValues.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_TBL_COL_INDEXUI_INDEX_COLS_001");
        tblclmnValues.setText(MessageConfigLoader.getProperty(IMessagesConstants.SET_ENUM_UI_USER_VALUE));

    }

    private void createFinishComposite() {
        Composite finishValueComposite = new Composite(this.grpValues, SWT.NONE);
        finishValueComposite.setLayout(new GridLayout(1, false));
        GridData finishValueCompositeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        finishValueCompositeGD.verticalAlignment = SWT.CENTER;
        finishValueComposite.setLayoutData(finishValueCompositeGD);
        
        Button finish = new Button(finishValueComposite, SWT.NONE);
        GridData finishGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        finishGD.heightHint = 20;
        finish.setLayoutData(finishGD);
        finish.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_BTN_INDEXUI_MOVE_UP_INDEX_001");
        finish.setText(MessageConfigLoader.getProperty(IMessagesConstants.CREATE_TABLE_FINISH_BTN));
        finish.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                grpValues.setVisible(false);
                grpValues.setSize(0,0);
                editValues.setVisible(true);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent event) {
                // Nothing to do
            }
        });
    }

    private void createUpDownValueComposite() {
        Composite upDownValueComposite = new Composite(this.grpValues, SWT.NONE);
        upDownValueComposite.setLayout(new GridLayout(1, false));
        GridData upDownValueCompositeGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        upDownValueCompositeGD.verticalAlignment = SWT.CENTER;
        upDownValueComposite.setLayoutData(upDownValueCompositeGD);

        createValueMoveUp(upDownValueComposite);
        createValueMoveDown(upDownValueComposite);
    }

    /**
     * 
     * @param comp
     */
    private void createValueMoveUp(Composite comp) {
        Button moveUp = new Button(comp, SWT.ARROW | SWT.UP);
        GridData moveUpGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        moveUpGD.heightHint = 20;
        moveUp.setLayoutData(moveUpGD);
        moveUp.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_BTN_INDEXUI_MOVE_UP_INDEX_001");
        moveUp.setText(MessageConfigLoader.getProperty(IMessagesConstants.INDEX_UI_MOVE_UP));
        moveUp.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                int selectedIdx = tblValues.getSelectionIndex();
                if (selectedIdx > 0) {
                    String value = setOrEnumValues.get(selectedIdx);
                    setOrEnumValues.remove(selectedIdx);
                    setOrEnumValues.add(selectedIdx - 1, value);
                    repopulateValueCols();
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent event) {
                // Nothing to do
            }
        });
    }

    /**
     * 
     * @param comp
     */
    private void createValueMoveDown(Composite comp) {
        Button moveDown = new Button(comp, SWT.ARROW | SWT.DOWN);
        GridData moveDownGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        moveDownGD.heightHint = 20;
        moveDown.setLayoutData(moveDownGD);
        moveDown.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_BTN_INDEXUI_MOVE_DOWN_INDEX_001");
        moveDown.setText(MessageConfigLoader.getProperty(IMessagesConstants.INDEX_UI_MOVE_DOWN));
        moveDown.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                int selectedIdx = tblValues.getSelectionIndex();
                if (selectedIdx > -1 && selectedIdx < (tblValues.getItemCount() - 1)) {
                    String value = setOrEnumValues.get(selectedIdx);
                    setOrEnumValues.remove(selectedIdx);
                    setOrEnumValues.add(selectedIdx + 1, value);
                    repopulateValueCols();
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent event) {
                // Nothing to do

            }
        });
    }

    /**
     * 
     * @param compositeColumns
     */
    private void addDisplayEditValuesButton(Composite compositeColumns) {
        Composite editValuesComposite = new Composite(compositeColumns, SWT.FILL);
        editValuesComposite.setLayout(new GridLayout(1, false));
        editValues = new Button(editValuesComposite, SWT.NONE);
        GridData editValuesCompositeGD = new GridData(SWT.NONE, SWT.NONE, true, false);
        editValuesCompositeGD.horizontalAlignment = SWT.RIGHT;
        editValuesCompositeGD.heightHint = 30;
        editValuesCompositeGD.widthHint = 100;
        editValues.setLayoutData(editValuesCompositeGD);
        editValues.setSize(30, 100);
        editValues.setData(MPPDBIDEConstants.SWTBOT_KEY, "ID_BTN_INDEXUI_MOVE_UP_INDEX_001");
        editValues.setText(MessageConfigLoader.getProperty(IMessagesConstants.EDIT_SETTING_VALUE));
        editValues.setVisible(false);
        editValues.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                grpValues.setVisible(true);
                editValues.setVisible(false);
                grpValues.setSize(625, 200);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent event) {
                // Nothing to do
            }
        });
    }
}