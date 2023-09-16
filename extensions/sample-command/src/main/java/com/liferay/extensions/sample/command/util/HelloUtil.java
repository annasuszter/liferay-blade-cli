/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.extensions.sample.command.util;

import com.liferay.extensions.sample.command.HelloArgs;

/**
 * @author Liferay
 */
public class HelloUtil {

	public static String getHello(HelloArgs helloArgs) {
		return "Hello " + helloArgs.getName() + "!";
	}

}