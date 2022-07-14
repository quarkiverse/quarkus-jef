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

package io.quarkiverse.jef.java.embedded.framework.mcu.core.boards;

import java.io.IOException;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
public class BoardManager {
    private static Board instance = null;
    private static final AtomicBoolean init = new AtomicBoolean(false);

    public static Board getBoard() throws IOException {
        if (!init.get() && instance == null) {
            synchronized (BoardManager.class) {
                if (!init.get() && instance == null) {
                    instance = initBoard();
                    init.set(true);
                }
            }
        }
        return instance;
    }

    private static Board initBoard() throws IOException {
        ServiceLoader<BoardLoader> sl = ServiceLoader.load(BoardLoader.class, BoardLoader.class.getClassLoader());
        for (BoardLoader bl : sl) {
            if (bl.accept()) {
                return bl.create();
            }
        }
        throw new IOException("Can't identify board");
    }
}
