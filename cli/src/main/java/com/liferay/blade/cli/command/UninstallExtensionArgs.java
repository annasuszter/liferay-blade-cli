/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blade.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * @author Christopher Bryan Boyd
 */
@Parameters(commandDescription = "Uninstalls an extension from blade.", commandNames = "extension uninstall")
public class UninstallExtensionArgs extends BaseArgs {

	public String getName() {
		return _name;
	}

	@Parameter(description = "[name]")
	private String _name;

}