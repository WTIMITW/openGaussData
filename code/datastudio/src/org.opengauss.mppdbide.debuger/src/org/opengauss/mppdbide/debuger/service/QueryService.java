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

package org.opengauss.mppdbide.debuger.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.opengauss.mppdbide.common.IConnection;
import org.opengauss.mppdbide.common.QueryResVoConvertHelper;
import org.opengauss.mppdbide.common.VersionHelper;
import org.opengauss.mppdbide.debuger.dao.FunctionDao;
import org.opengauss.mppdbide.debuger.debug.DebugConstants;
import org.opengauss.mppdbide.debuger.debug.DebugConstants.DebugOpt;
import org.opengauss.mppdbide.debuger.vo.FunctionVo;
import org.opengauss.mppdbide.debuger.vo.SourceCodeVo;
import org.opengauss.mppdbide.debuger.vo.TotalSourceCodeVo;
import org.opengauss.mppdbide.utils.logger.MPPDBIDELoggerUtility;

/**
 * Title: the QueryService class
 *
 * @since 3.0.0
 */
public class QueryService implements IService {
    private IConnection conn = null;
    private FunctionDao functionDao = null;

    /**
     * query function vo
     *
     * @param proname the debug function name
     * @return FunctionVo the function vo
     * @throws SQLException sql exp
     */
    public FunctionVo queryFunction(String proname) throws SQLException {
        try (PreparedStatement ps = conn.getStatement(functionDao.getSql(proname))) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return functionDao.parse(rs);
                }
                throw new SQLException("proname:" + proname + " not found!");
            }
        }
    }

    /**
     * get base source code
     *
     * @param oid function oid
     * @return Optional<SourceCodeVo> the source code
     * @throws SQLException sql exp
     */
    public Optional<SourceCodeVo> getSourceCode(Long oid) throws SQLException {
        DebugOpt opt = VersionHelper.getDebugOptByDebuggerVersion(conn, DebugConstants.DebugOpt.GET_SOURCE_CODE);
        Optional<SourceCodeVo> sourceCodeVo = getTempSourceCode(oid, opt, SourceCodeVo.class);
        String sourceCode = sourceCodeVo.get().getSourceCode();
        return sourceCodeVo;
    }

    /**
     * get sourceCode source code
     *
     * @param oid function oid
     * @return Optional<TotalSourceCode> the source code
     * @throws SQLException sql exp
     */
    public Optional<TotalSourceCodeVo> getTotalSourceCode(Long oid) throws SQLException {
        return getTempSourceCode(oid,
                DebugConstants.DebugOpt.GET_TOTAL_SOURCE_CODE,
                TotalSourceCodeVo.class);
    }

    private <T> Optional<T> getTempSourceCode(
            Long oid,
            DebugConstants.DebugOpt debugOpt,
            Class<T> clazz) throws SQLException {
        List<Object> inputParams = Arrays.asList(oid);
        try (PreparedStatement ps = conn.getDebugOptPrepareStatement(
                debugOpt, inputParams)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(QueryResVoConvertHelper.parse(rs, clazz, conn));
                }
                return Optional.empty();
            }
        }
    }

    /**
     * set conn
     *
     * @param conn connection
     * @return void
     */
    public void setConn(IConnection conn) {
        this.conn = conn;
    }

    /**
     * get conn
     *
     * @return IConnection the connection
     */
    public IConnection getConn() {
        return this.conn;
    }

    /**
     * set function dao
     *
     * @param dao set function dao
     * @return void
     */
    public void setFunctionDao(FunctionDao dao) {
        this.functionDao = dao;
    }

    /**
     * get function dao
     *
     * @return FunctionDao get function dao
     */
    public FunctionDao getFunctionDao() {
        return this.functionDao;
    }

    /**
     * close service
     *
     * @return void
     */
    @Override
    public void closeService() {
        try {
            if (this.conn != null) {
                this.conn.close();
                this.conn = null;
            }
        } catch (SQLException e) {
            MPPDBIDELoggerUtility.warn("close conn with err:" + e.toString());
        }
    }
}
