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

import java.io.File;
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
            {
                for(RpiBoardInfo i : RpiBoardInfo.values()) {
                    mapping.put(i.getCode(), i);
                }
            }
        }
    };

    private final Properties props;

    public RpiBoardsLoader() throws IOException {
        props = new Properties();
        File f = new File(CPU_INFO_PATH);
        if (!f.exists())
            return;
        try (InputStreamReader is = new InputStreamReader(new FileInputStream(f))) {
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
