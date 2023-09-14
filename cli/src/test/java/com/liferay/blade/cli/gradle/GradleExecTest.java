/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blade.cli.gradle;

import com.liferay.blade.cli.BladeCLI;
import com.liferay.blade.cli.BladeTest;
import com.liferay.blade.cli.TestUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author David Truong
 */
public class GradleExecTest {

	@Before
	public void setUp() throws Exception {
		File rootDir = temporaryFolder.getRoot();

		_rootPath = rootDir.toPath();

		File extensionsDir = temporaryFolder.newFolder(".blade", "extensions");

		_extensionsPath = extensionsDir.toPath();
	}

	@Test
	public void testGradleWrapper() throws Exception {
		File workspace70 = new File(_rootPath.toFile(), "workspace70");

		_makeWorkspaceVersion(workspace70, BladeTest.PRODUCT_VERSION_DXP_70);

		String[] args = {"--base", workspace70.toString(), "create", "-t", "api", "foo"};

		_getBladeTest().run(args);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PrintStream ps = new PrintStream(baos);

		BladeTest.BladeTestBuilder bladeTestBuilder = BladeTest.builder();

		bladeTestBuilder.setExtensionsDir(_extensionsPath);
		bladeTestBuilder.setSettingsDir(_rootPath);
		bladeTestBuilder.setStdOut(ps);

		BladeCLI bladeCLI = bladeTestBuilder.build();

		GradleExec gradleExec = new GradleExec(bladeCLI);

		ProcessResult result = gradleExec.executeTask("tasks");

		int resultCode = result.getResultCode();

		String output = result.get();

		if (resultCode > 0) {
			Assert.assertEquals(
				"Gradle command returned error code " + resultCode + System.lineSeparator() + output, 0, resultCode);
		}
		else {
			Assert.assertFalse(
				"Gradle build failed " + System.lineSeparator() + output, output.contains("BUILD FAILED"));
		}
	}

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private BladeTest _getBladeTest() {
		BladeTest.BladeTestBuilder bladeTestBuilder = BladeTest.builder();

		bladeTestBuilder.setExtensionsDir(_extensionsPath);
		bladeTestBuilder.setSettingsDir(_rootPath);

		return bladeTestBuilder.build();
	}

	private void _makeWorkspaceVersion(File workspace, String version) throws Exception {
		File parentFile = workspace.getParentFile();

		String[] args = {"--base", parentFile.getPath(), "init", workspace.getName(), "-v", version};

		TestUtil.runBlade(workspace, _extensionsPath.toFile(), args);
	}

	private Path _extensionsPath = null;
	private Path _rootPath = null;

}