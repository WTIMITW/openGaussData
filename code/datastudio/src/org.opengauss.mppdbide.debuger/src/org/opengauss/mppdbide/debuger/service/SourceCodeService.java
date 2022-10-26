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

import org.opengauss.mppdbide.debuger.exception.DebugPositionNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Title: the SourceCodeService class
 *
 * @since 3.0.0
 */
public class SourceCodeService implements IService {
    /**
     *  the offset of show code and base code
     */
    public static final int CODE_BASE_OFFSET = 1;
    private static final String BEGIN = "BEGIN";
    private static final String END = "END;";

    private CodeDescription baseCodeDesc = null;
    private CodeDescription totalCodeDesc = null;

    /**
     * close service
     *
     * @return void
     */
    @Override
    public void closeService() {
    }

    /**
     * set base code
     *
     * @param the base code
     * @return void
     */
    public void setBaseCode(String code) {
        this.baseCodeDesc = new CodeDescription(code);
    }

    /**
     * set total code
     *
     * @param the total code
     * @return void
     */
    public void setTotalCode(String code) {
        this.totalCodeDesc = new CodeDescription(code);
    }

    /**
     * showline convert to codeline
     *
     * @param showLine the show line
     * @return int return converted result line
     * @throws DebugPositionNotFoundException debug position exp
     */
    public int showLine2CodeLine(int showLine) throws DebugPositionNotFoundException {
        return showLine - getBeginTotalAndBaseDiff() + CODE_BASE_OFFSET;
    }

    /**
     * codeline convert to showline
     *
     * @param codeLine the code line
     * @return int return converted result line
     * @throws DebugPositionNotFoundException debug position exp
     */
    public int codeLine2ShowLine(int codeLine) throws DebugPositionNotFoundException {
        return codeLine + getBeginTotalAndBaseDiff() - CODE_BASE_OFFSET;
    }

    /**
     * get begin debug line number
     *
     * @return int return begin debug line number in code line
     * @throws DebugPositionNotFoundException debug position exp
     */
    public int getBeginDebugCodeLine() throws DebugPositionNotFoundException {
        return getFirstValidDebugPos() + getBeginTotalAndBaseDiff();
    }

    /**
     * get base code begin line position
     *
     * @return int return begin debug line number in code line
     * @throws DebugPositionNotFoundException debug position exp
     */
    public int getFirstValidDebugPos() throws DebugPositionNotFoundException {
        // BEGIN line is not a valid debug pos, so add 1
        // out code line base on 0, but breakpoint base on 1, so add 1
        // next BEGIN line is default attach breakpoint, so add 1, total pos need add 3!
        return getBeignOfBaseCode() + CODE_BASE_OFFSET;
    }

    /**
     * get max code line pos
     *
     * @return int return max debug line number in code line
     */
    public int getMaxValidDebugPos() {
        return this.baseCodeDesc.getMaxPostion();
    }

    /**
     * get base code desc
     *
     * @return CodeDescription code desc
     */
    public CodeDescription getBaseCodeDesc() {
        return this.baseCodeDesc;
    }

    /**
     * get total code desc
     *
     * @return CodeDescription code desc
     */
    public CodeDescription getTotalCodeDesc() {
        return this.totalCodeDesc;
    }

    /**
     * get total and base diff line
     *
     * @return int the diff line
     * @throws DebugPositionNotFoundException not found exp
     */
    public int getBeginTotalAndBaseDiff() throws DebugPositionNotFoundException {
        return getBeignfTotalCode() - getBeignOfBaseCode();
    }

    /**
     * get base code being pos
     *
     * @return int the begin line
     * @throws DebugPositionNotFoundException not found exp
     */
    public int getBeignOfBaseCode() throws DebugPositionNotFoundException {
        return this.baseCodeDesc.getBeginPosition();
    }

    /**
     * get total code being pos
     *
     * @return int the begin line
     * @throws DebugPositionNotFoundException not found exp
     */
    public int getBeignfTotalCode() throws DebugPositionNotFoundException {
        return this.totalCodeDesc.getBeginPosition();
    }

    /**
     * Title: CodeDescription class
     */
    public static class CodeDescription {
        /**
         *  invalid position
         */
        public static final int INVALID_POSITION = -1;
        private String code;
        private List<String> codeList;
        private int beginPosition = INVALID_POSITION;

        public CodeDescription(String code) {
            this.code = code;
            this.codeList = getLines(this.code);
            this.beginPosition = getBeginFromCode(this.codeList);
        }

        /**
         * get the begin line
         *
         * @return int the begin line
         * @throws DebugPositionNotFoundException the not found exp
         */
        public int getBeginPosition() throws DebugPositionNotFoundException {
            if (beginPosition == INVALID_POSITION) {
                throw new DebugPositionNotFoundException();
            }
            return beginPosition;
        }

        /**
         * get max code line
         *
         * @return int the max line
         */
        public int getMaxPostion() {
            return this.codeList.size();
        }

        /**
         * get special line code
         *
         * @param idx code line
         * @return String the code
         */
        public String getCodeByIndex(int idx) {
            return this.codeList.get(idx);
        }

        /**
         * code to mutil line
         *
         * @param srcCode source code
         * @return List<String> the list of code
         */
        public static List<String> getLines(String srcCode) {
            return Arrays.stream(srcCode.split("[\\n]")).collect(Collectors.toList());
        }

        /**
         * get code to run lines
         *
         * @param srcCode the code
         * @return the run line num
         */
        public static List<String> getRunLines(String srcCode) {
            List<String> totalLines = getLines(srcCode);
            Boolean isStart = false;
            Boolean isEnd = false;
            Boolean isBegin = false;
            List<String> runLines = new ArrayList();
            for (String line : totalLines) {
                if (!isStart) {
                    isStart = line.toUpperCase(Locale.ENGLISH).contains(BEGIN);
                }
                isEnd = line.toUpperCase(Locale.ENGLISH).contains(END);
                if (isEnd) {
                    break;
                }
                if (isStart) {
                    isBegin = line.toUpperCase(Locale.ENGLISH).contains(BEGIN);
                    if (!isBegin) {
                        runLines.add(line);
                    }
                }
            }
            return runLines;
        }

        /**
         * get line num by code
         *
         * @param srcCode the code
         * @return the line num
         */
        public static List<String> getRunLinesNums(String srcCode) {
            List<String> totalLines = getLines(srcCode);
            Boolean isStart = false;
            Boolean isEnd = false;
            Boolean isBegin = false;
            List<String> runLines = new ArrayList();
            Integer count = 0;
            for (String line : totalLines) {
                count++;
                if (!isStart) {
                    isStart = line.toUpperCase(Locale.ENGLISH).contains(BEGIN);
                }
                isEnd = line.toUpperCase(Locale.ENGLISH).contains(END);
                if (isEnd) {
                    break;
                }
                if (isStart) {
                    isBegin = line.toUpperCase(Locale.ENGLISH).contains(BEGIN);
                    if (!isBegin) {
                        runLines.add(count.toString());
                    }
                }
            }
            return runLines;
        }

        private int getBeginFromCode(List<String> lines) {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).toUpperCase(Locale.ENGLISH).trim().startsWith("BEGIN")) {
                    return i;
                }
            }
            return INVALID_POSITION;
        }
    }
}
