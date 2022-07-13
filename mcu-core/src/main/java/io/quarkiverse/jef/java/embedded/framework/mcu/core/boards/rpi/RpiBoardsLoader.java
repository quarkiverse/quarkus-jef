/*
 * Copyright (c) 2021, IOT-Hub.RU and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is dual-licensed: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License Version 3 as
 * published by the Free Software Foundation. For the terms of this
 * license, see <http://www.gnu.org/licenses/>.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public
 * License Version 3 for more details (a copy is included in the LICENSE
 * file that accompanied this code).
 *
 * You should have received a copy of the GNU Affero General Public License
 * version 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact support@iot-hub.ru or visit www.iot-hub.ru if you need
 * additional information or have any questions.
 *
 * You can be released from the requirements of the license by purchasing
 * a Java Embedded Framework Commercial License. Buying such a license is
 * mandatory as soon as you develop commercial activities involving the
 * Java Embedded Framework software without disclosing the source code of
 * your own applications.
 *
 * Please contact sales@iot-hub.ru if you have any question.
 */

package io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.rpi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.Board;
import io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.BoardLoader;

public class RpiBoardsLoader implements BoardLoader {
    private final static String CPU_INFO_PATH = "/proc/cpuinfo";
    private final static String REV_KEY = "Revision";

    private final static Map<String, RpiBoardInfo> mapping = new HashMap<String, RpiBoardInfo>() {
        {
            put(RpiBoardInfo.A_PLUS_1_1.getCode(), RpiBoardInfo.A_PLUS_1_1);
            put(RpiBoardInfo.B_PLUS_1_2.getCode(), RpiBoardInfo.B_PLUS_1_2);
            put(RpiBoardInfo.ZERO_1_2.getCode(), RpiBoardInfo.ZERO_1_2);
            put(RpiBoardInfo.ZERO_1_3.getCode(), RpiBoardInfo.ZERO_1_3);
            put(RpiBoardInfo.ZERO_W.getCode(), RpiBoardInfo.ZERO_W);
            put(RpiBoardInfo.THREE_A_PLUS_1_0.getCode(), RpiBoardInfo.THREE_A_PLUS_1_0);
            put(RpiBoardInfo.ZERO_1_2E.getCode(), RpiBoardInfo.ZERO_1_2E);
            put(RpiBoardInfo.ZERO_1_3E.getCode(), RpiBoardInfo.ZERO_1_3E);
            put(RpiBoardInfo.CM_1_1.getCode(), RpiBoardInfo.CM_1_1);
            put(RpiBoardInfo.TWO_B_1_0.getCode(), RpiBoardInfo.TWO_B_1_0);
            put(RpiBoardInfo.TWO_B_1_1.getCode(), RpiBoardInfo.TWO_B_1_1);
            put(RpiBoardInfo.THREE_B_1_2.getCode(), RpiBoardInfo.THREE_B_1_2);
            put(RpiBoardInfo.CM_3_1_0.getCode(), RpiBoardInfo.CM_3_1_0);
            put(RpiBoardInfo.THREE_B_PLUS_1_3.getCode(), RpiBoardInfo.THREE_B_PLUS_1_3);
            put(RpiBoardInfo.TWO_B_1_2.getCode(), RpiBoardInfo.TWO_B_1_2);
            put(RpiBoardInfo.TWO_B_1_1E.getCode(), RpiBoardInfo.TWO_B_1_1E);
            put(RpiBoardInfo.TWO_B_1_2E.getCode(), RpiBoardInfo.TWO_B_1_2E);
            put(RpiBoardInfo.THREE_B_1_2E.getCode(), RpiBoardInfo.THREE_B_1_2E);
            put(RpiBoardInfo.CM_3_1_0E.getCode(), RpiBoardInfo.CM_3_1_0E);
            put(RpiBoardInfo.THREE_B_1_2S.getCode(), RpiBoardInfo.THREE_B_1_2S);
            put(RpiBoardInfo.THREE_B_1_2ST.getCode(), RpiBoardInfo.THREE_B_1_2ST);
            put(RpiBoardInfo.THREE_B_1_3E.getCode(), RpiBoardInfo.THREE_B_1_3E);
            put(RpiBoardInfo.CM_3_1_0_PLUS.getCode(), RpiBoardInfo.CM_3_1_0_PLUS);
            put(RpiBoardInfo.FOUR_B_1_1_1G.getCode(), RpiBoardInfo.FOUR_B_1_1_1G);
            put(RpiBoardInfo.FOUR_B_1_1_2G.getCode(), RpiBoardInfo.FOUR_B_1_1_2G);
            put(RpiBoardInfo.FOUR_B_1_2_2G.getCode(), RpiBoardInfo.FOUR_B_1_2_2G);
            put(RpiBoardInfo.FOUR_B_1_4_2G.getCode(), RpiBoardInfo.FOUR_B_1_4_2G);
            put(RpiBoardInfo.FOUR_B_1_1_4G.getCode(), RpiBoardInfo.FOUR_B_1_1_4G);
            put(RpiBoardInfo.FOUR_B_1_2_4G.getCode(), RpiBoardInfo.FOUR_B_1_2_4G);
            put(RpiBoardInfo.FOUR_B_1_4_4G.getCode(), RpiBoardInfo.FOUR_B_1_4_4G);
            put(RpiBoardInfo.FOUR_B_1_4_8G.getCode(), RpiBoardInfo.FOUR_B_1_4_8G);
            put(RpiBoardInfo.PI_400_4G.getCode(), RpiBoardInfo.PI_400_4G);
        }
    };

    private final Properties props;

    public RpiBoardsLoader() throws IOException {
        props = new Properties();
        try (InputStreamReader is = new InputStreamReader(new FileInputStream(CPU_INFO_PATH))) {
            props.load(is);
        }
    }

    @Override
    public boolean accept() {
        String rev = props.getProperty(REV_KEY);
        RpiBoardInfo info = mapping.get(rev);
        return rev != null && info != null && info.getProvider() != null;
    }

    @Override
    public Board create() throws IOException {
        String rev = props.getProperty(REV_KEY);
        RpiBoardInfo info = mapping.get(rev);
        Class<? extends RaspberryPiAbstractBoard> provider = info.getProvider();
        try {
            RaspberryPiAbstractBoard board = provider.getDeclaredConstructor().newInstance();
            board.setBoardInfo(info);
            return board;
        } catch (Exception e) {
            throw new IOException("board provider not implemented for " + info.getModel());
        }
    }
}
